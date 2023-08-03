
package org.opencastproject.search.impl.persistence;
import static org.opencastproject.security.api.Permissions.Action.CONTRIBUTE;
import static org.opencastproject.security.api.Permissions.Action.READ;
import static org.opencastproject.security.api.Permissions.Action.WRITE;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.mediapackage.MediaPackageParser;
import org.opencastproject.security.api.AccessControlList;
import org.opencastproject.security.api.AccessControlParser;
import org.opencastproject.security.api.AccessControlUtil;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.UnauthorizedException;
import org.opencastproject.security.api.User;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.util.data.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
public class SearchServiceDatabaseImpl implements SearchServiceDatabase {
  public static final String PERSISTENCE_UNIT = "org.opencastproject.search.impl.persistence";
  private static final Logger logger = LoggerFactory.getLogger(SearchServiceDatabaseImpl.class);
  protected EntityManagerFactory emf;
  protected SecurityService securityService;
  public void setEntityManagerFactory(EntityManagerFactory emf) {
    this.emf = emf;
  }
  public void activate(ComponentContext cc) throws SearchServiceDatabaseException {
    logger.info("Activating persistence manager for search service");
    this.populateSeriesData();
  }
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }
  private void populateSeriesData() throws SearchServiceDatabaseException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      TypedQuery<SearchEntity> q = (TypedQuery<SearchEntity>) em.createNamedQuery("Search.getNoSeries");
      List<SearchEntity> seriesList = q.getResultList();
      for (SearchEntity series : seriesList) {
        String mpSeriesId = MediaPackageParser.getFromXml(series.getMediaPackageXML()).getSeries();
        if (StringUtils.isNotBlank(mpSeriesId) && !mpSeriesId.equals(series.getSeriesId())) {
          logger.info("Fixing missing series ID for episode {}, series is {}", series.getMediaPackageId(), mpSeriesId);
          series.setSeriesId(mpSeriesId);
          em.merge(series);
        }
      }
      tx.commit();
    } catch (Exception e) {
      logger.error("Could not update media package: {}", e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public void deleteMediaPackage(String mediaPackageId, Date deletionDate) throws SearchServiceDatabaseException,
  NotFoundException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity searchEntity = getSearchEntity(mediaPackageId, em);
      if (searchEntity == null)
        throw new NotFoundException("No media package with id=" + mediaPackageId + " exists");
      String accessControlXml = searchEntity.getAccessControl();
      if (accessControlXml != null) {
        AccessControlList acl = AccessControlParser.parseAcl(accessControlXml);
        User currentUser = securityService.getUser();
        Organization currentOrg = securityService.getOrganization();
        if (!AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, WRITE.toString()))
          throw new UnauthorizedException(currentUser + " is not authorized to delete media package " + mediaPackageId);
        searchEntity.setDeletionDate(deletionDate);
        em.merge(searchEntity);
      }
      tx.commit();
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not delete episode {}: {}", mediaPackageId, e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public int countMediaPackages() throws SearchServiceDatabaseException {
    EntityManager em = emf.createEntityManager();
    Query query = em.createNamedQuery("Search.getCount");
    try {
      Long total = (Long) query.getSingleResult();
      return total.intValue();
    } catch (Exception e) {
      logger.error("Could not find number of mediapackages", e);
      throw new SearchServiceDatabaseException(e);
    } finally {
      em.close();
    }
  }
  @Override
  @SuppressWarnings("unchecked")
  public Iterator<Tuple<MediaPackage, String>> getAllMediaPackages() throws SearchServiceDatabaseException {
    List<SearchEntity> searchEntities = null;
    EntityManager em = null;
    try {
      em = emf.createEntityManager();
      Query query = em.createNamedQuery("Search.findAll");
      searchEntities = (List<SearchEntity>) query.getResultList();
    } catch (Exception e) {
      logger.error("Could not retrieve all episodes: {}", e.getMessage());
      throw new SearchServiceDatabaseException(e);
    } finally {
      em.close();
    }
    List<Tuple<MediaPackage, String>> mediaPackageList = new LinkedList<Tuple<MediaPackage, String>>();
    try {
      for (SearchEntity entity : searchEntities) {
        MediaPackage mediaPackage = MediaPackageParser.getFromXml(entity.getMediaPackageXML());
        mediaPackageList.add(Tuple.tuple(mediaPackage, entity.getOrganization().getId()));
      }
    } catch (Exception e) {
      logger.error("Could not parse series entity: {}", e.getMessage());
      throw new SearchServiceDatabaseException(e);
    }
    return mediaPackageList.iterator();
  }
  @Override
  public AccessControlList getAccessControlList(String mediaPackageId) throws NotFoundException,
  SearchServiceDatabaseException {
    EntityManager em = null;
    try {
      em = emf.createEntityManager();
      SearchEntity entity = getSearchEntity(mediaPackageId, em);
      if (entity == null) {
        throw new NotFoundException("Could not found media package with ID " + mediaPackageId);
      }
      if (entity.getAccessControl() == null) {
        return null;
      } else {
        return AccessControlParser.parseAcl(entity.getAccessControl());
      }
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not retrieve ACL {}: {}", mediaPackageId, e.getMessage());
      throw new SearchServiceDatabaseException(e);
    } finally {
      em.close();
    }
  }
  @Override
  public void storeMediaPackage(MediaPackage mediaPackage, AccessControlList acl, Date now)
          throws SearchServiceDatabaseException, UnauthorizedException {
    String mediaPackageXML = MediaPackageParser.getAsXml(mediaPackage);
    String mediaPackageId = mediaPackage.getIdentifier().toString();
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity entity = getSearchEntity(mediaPackageId, em);
      if (entity == null) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setOrganization(securityService.getOrganization());
        searchEntity.setMediaPackageId(mediaPackageId);
        searchEntity.setMediaPackageXML(mediaPackageXML);
        searchEntity.setAccessControl(AccessControlParser.toXml(acl));
        searchEntity.setModificationDate(now);
        searchEntity.setSeriesId(mediaPackage.getSeries());
        em.persist(searchEntity);
      } else {
        String accessControlXml = entity.getAccessControl();
        if (accessControlXml != null) {
          AccessControlList accessList = AccessControlParser.parseAcl(accessControlXml);
          User currentUser = securityService.getUser();
          Organization currentOrg = securityService.getOrganization();
          if (!AccessControlUtil.isAuthorized(accessList, currentUser, currentOrg, WRITE.toString())) {
            throw new UnauthorizedException(currentUser + " is not authorized to update media package "
                    + mediaPackageId);
          }
        }
        entity.setOrganization(securityService.getOrganization());
        entity.setMediaPackageId(mediaPackageId);
        entity.setMediaPackageXML(mediaPackageXML);
        entity.setAccessControl(AccessControlParser.toXml(acl));
        entity.setModificationDate(now);
        entity.setDeletionDate(null);
        entity.setSeriesId(mediaPackage.getSeries());
        em.merge(entity);
      }
      tx.commit();
    } catch (Exception e) {
      logger.error("Could not update media package: {}", e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public MediaPackage getMediaPackage(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity episodeEntity = getSearchEntity(mediaPackageId, em);
      if (episodeEntity == null)
        throw new NotFoundException("No episode with id=" + mediaPackageId + " exists");
      String accessControlXml = episodeEntity.getAccessControl();
      if (accessControlXml != null) {
        AccessControlList acl = AccessControlParser.parseAcl(accessControlXml);
        User currentUser = securityService.getUser();
        Organization currentOrg = securityService.getOrganization();
        if (!AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, READ.toString())
                && !AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, CONTRIBUTE.toString())
                && !AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, WRITE.toString())) {
          throw new UnauthorizedException(currentUser + " is not authorized to see episode " + mediaPackageId);
        }
      }
      return MediaPackageParser.getFromXml(episodeEntity.getMediaPackageXML());
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not get episode {} from database: {} ", mediaPackageId, e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public Date getModificationDate(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity searchEntity = getSearchEntity(mediaPackageId, em);
      if (searchEntity == null)
        throw new NotFoundException("No media package with id=" + mediaPackageId + " exists");
      String accessControlXml = searchEntity.getAccessControl();
      if (accessControlXml != null) {
        AccessControlList acl = AccessControlParser.parseAcl(accessControlXml);
        User currentUser = securityService.getUser();
        Organization currentOrg = securityService.getOrganization();
        if (!AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, READ.toString()))
          throw new UnauthorizedException(currentUser + " is not authorized to read media package " + mediaPackageId);
      }
      return searchEntity.getModificationDate();
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not get modification date {}: {}", mediaPackageId, e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public Date getDeletionDate(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity searchEntity = getSearchEntity(mediaPackageId, em);
      if (searchEntity == null) {
        throw new NotFoundException("No media package with id=" + mediaPackageId + " exists");
      }
      String accessControlXml = searchEntity.getAccessControl();
      if (accessControlXml != null) {
        AccessControlList acl = AccessControlParser.parseAcl(accessControlXml);
        User currentUser = securityService.getUser();
        Organization currentOrg = securityService.getOrganization();
        if (!AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, READ.toString()))
          throw new UnauthorizedException(currentUser + " is not authorized to read media package " + mediaPackageId);
      }
      return searchEntity.getDeletionDate();
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not get deletion date {}: {}", mediaPackageId, e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  @Override
  public String getOrganizationId(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException {
    EntityManager em = null;
    EntityTransaction tx = null;
    try {
      em = emf.createEntityManager();
      tx = em.getTransaction();
      tx.begin();
      SearchEntity searchEntity = getSearchEntity(mediaPackageId, em);
      if (searchEntity == null)
        throw new NotFoundException("No media package with id=" + mediaPackageId + " exists");
      String accessControlXml = searchEntity.getAccessControl();
      if (accessControlXml != null) {
        AccessControlList acl = AccessControlParser.parseAcl(accessControlXml);
        User currentUser = securityService.getUser();
        Organization currentOrg = securityService.getOrganization();
        if (!AccessControlUtil.isAuthorized(acl, currentUser, currentOrg, READ.toString()))
          throw new UnauthorizedException(currentUser + " is not authorized to read media package " + mediaPackageId);
      }
      return searchEntity.getOrganization().getId();
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Could not get deletion date {}: {}", mediaPackageId, e.getMessage());
      if (tx.isActive()) {
        tx.rollback();
      }
      throw new SearchServiceDatabaseException(e);
    } finally {
      if (em != null)
        em.close();
    }
  }
  private SearchEntity getSearchEntity(String id, EntityManager em) {
    Query q = em.createNamedQuery("Search.findById").setParameter("mediaPackageId", id);
    try {
      return (SearchEntity) q.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
