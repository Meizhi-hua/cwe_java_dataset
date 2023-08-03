
package org.opencastproject.search.impl;
import static org.opencastproject.security.api.SecurityConstants.GLOBAL_ADMIN_ROLE;
import org.opencastproject.job.api.AbstractJobProducer;
import org.opencastproject.job.api.Job;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.mediapackage.MediaPackageException;
import org.opencastproject.mediapackage.MediaPackageParser;
import org.opencastproject.mediapackage.MediaPackageSerializer;
import org.opencastproject.metadata.api.StaticMetadataService;
import org.opencastproject.metadata.mpeg7.Mpeg7CatalogService;
import org.opencastproject.search.api.SearchException;
import org.opencastproject.search.api.SearchQuery;
import org.opencastproject.search.api.SearchResult;
import org.opencastproject.search.api.SearchService;
import org.opencastproject.search.impl.persistence.SearchServiceDatabase;
import org.opencastproject.search.impl.persistence.SearchServiceDatabaseException;
import org.opencastproject.search.impl.solr.SolrIndexManager;
import org.opencastproject.search.impl.solr.SolrRequester;
import org.opencastproject.security.api.AccessControlList;
import org.opencastproject.security.api.AuthorizationService;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.OrganizationDirectoryService;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.StaticFileAuthorization;
import org.opencastproject.security.api.UnauthorizedException;
import org.opencastproject.security.api.User;
import org.opencastproject.security.api.UserDirectoryService;
import org.opencastproject.security.util.SecurityUtil;
import org.opencastproject.series.api.SeriesService;
import org.opencastproject.serviceregistry.api.ServiceRegistry;
import org.opencastproject.serviceregistry.api.ServiceRegistryException;
import org.opencastproject.solr.SolrServerFactory;
import org.opencastproject.util.LoadUtil;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.util.data.Tuple;
import org.opencastproject.workspace.api.Workspace;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.osgi.framework.ServiceException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public final class SearchServiceImpl extends AbstractJobProducer implements SearchService, ManagedService,
    StaticFileAuthorization {
  private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
  public static final String CONFIG_SOLR_URL = "org.opencastproject.search.solr.url";
  public static final String CONFIG_SOLR_ROOT = "org.opencastproject.search.solr.dir";
  public static final String JOB_TYPE = "org.opencastproject.search";
  public static final float DEFAULT_ADD_JOB_LOAD = 0.1f;
  public static final float DEFAULT_DELETE_JOB_LOAD = 0.1f;
  public static final String ADD_JOB_LOAD_KEY = "job.load.add";
  public static final String DELETE_JOB_LOAD_KEY = "job.load.delete";
  private float addJobLoad = DEFAULT_ADD_JOB_LOAD;
  private float deleteJobLoad = DEFAULT_DELETE_JOB_LOAD;
  private int retriesToPopulateIndex = 0;
  private enum Operation {
    Add, Delete
  };
  private SolrServer solrServer;
  private SolrRequester solrRequester;
  private SolrIndexManager indexManager;
  private List<StaticMetadataService> mdServices = new ArrayList<StaticMetadataService>();
  private Mpeg7CatalogService mpeg7CatalogService;
  private SeriesService seriesService;
  private Workspace workspace;
  private SecurityService securityService;
  private AuthorizationService authorizationService;
  private ServiceRegistry serviceRegistry;
  private SearchServiceDatabase persistence;
  protected UserDirectoryService userDirectoryService = null;
  protected OrganizationDirectoryService organizationDirectory = null;
  protected MediaPackageSerializer serializer = null;
  private LoadingCache<Tuple<User, String>, Boolean> cache = null;
  private static final Pattern staticFilePattern = Pattern.compile("^/([^/]+)/engage-player/([^/]+)/.*$");
  public SearchServiceImpl() {
    super(JOB_TYPE);
    cache = CacheBuilder.newBuilder()
        .maximumSize(2048)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build(new CacheLoader<Tuple<User, String>, Boolean>() {
          @Override
          public Boolean load(Tuple<User, String> key) {
            return loadUrlAccess(key.getB());
          }
        });
  }
  public SolrIndexManager getSolrIndexManager() {
    return indexManager;
  }
  @Override
  public void activate(final ComponentContext cc) throws IllegalStateException {
    super.activate(cc);
    final String solrServerUrlConfig = StringUtils.trimToNull(cc.getBundleContext().getProperty(CONFIG_SOLR_URL));
    logger.info("Setting up solr server");
    solrServer = new Object() {
      SolrServer create() {
        if (solrServerUrlConfig != null) {
          try {
            logger.info("Setting up solr server at {}", solrServerUrlConfig);
            URL solrServerUrl = new URL(solrServerUrlConfig);
            return setupSolr(solrServerUrl);
          } catch (MalformedURLException e) {
            throw connectError(solrServerUrlConfig, e);
          }
        } else {
          String solrRoot = SolrServerFactory.getEmbeddedDir(cc, CONFIG_SOLR_ROOT, "search");
          try {
            logger.debug("Setting up solr server at {}", solrRoot);
            return setupSolr(new File(solrRoot));
          } catch (IOException e) {
            throw connectError(solrServerUrlConfig, e);
          } catch (SolrServerException e) {
            throw connectError(solrServerUrlConfig, e);
          }
        }
      }
      IllegalStateException connectError(String target, Exception e) {
        logger.error("Unable to connect to solr at {}: {}", target, e.getMessage());
        return new IllegalStateException("Unable to connect to solr at " + target, e);
      }
    }.create();
    solrRequester = new SolrRequester(solrServer, securityService, serializer);
    indexManager = new SolrIndexManager(solrServer, workspace, mdServices, seriesService, mpeg7CatalogService,
            securityService);
    String systemUserName = cc.getBundleContext().getProperty(SecurityUtil.PROPERTY_KEY_SYS_USER);
    populateIndex(systemUserName);
  }
  public void deactivate() {
    SolrServerFactory.shutdown(solrServer);
  }
  static SolrServer setupSolr(File solrRoot) throws IOException, SolrServerException {
    logger.info("Setting up solr search index at {}", solrRoot);
    File solrConfigDir = new File(solrRoot, "conf");
    if (solrConfigDir.exists()) {
      logger.info("solr search index found at {}", solrConfigDir);
    } else {
      logger.info("solr config directory doesn't exist.  Creating {}", solrConfigDir);
      FileUtils.forceMkdir(solrConfigDir);
    }
    copyClasspathResourceToFile("/solr/conf/protwords.txt", solrConfigDir);
    copyClasspathResourceToFile("/solr/conf/schema.xml", solrConfigDir);
    copyClasspathResourceToFile("/solr/conf/scripts.conf", solrConfigDir);
    copyClasspathResourceToFile("/solr/conf/solrconfig.xml", solrConfigDir);
    copyClasspathResourceToFile("/solr/conf/stopwords.txt", solrConfigDir);
    copyClasspathResourceToFile("/solr/conf/synonyms.txt", solrConfigDir);
    File solrDataDir = new File(solrRoot, "data");
    if (!solrDataDir.exists()) {
      FileUtils.forceMkdir(solrDataDir);
    }
    File solrIndexDir = new File(solrDataDir, "index");
    if (solrIndexDir.isDirectory() && solrIndexDir.list().length == 0) {
      FileUtils.deleteDirectory(solrIndexDir);
    }
    return SolrServerFactory.newEmbeddedInstance(solrRoot, solrDataDir);
  }
  static SolrServer setupSolr(URL url) {
    logger.info("Connecting to solr search index at {}", url);
    return SolrServerFactory.newRemoteInstance(url);
  }
  static void copyClasspathResourceToFile(String classpath, File dir) {
    InputStream in = null;
    FileOutputStream fos = null;
    try {
      in = SearchServiceImpl.class.getResourceAsStream(classpath);
      File file = new File(dir, FilenameUtils.getName(classpath));
      logger.debug("copying " + classpath + " to " + file);
      fos = new FileOutputStream(file);
      IOUtils.copy(in, fos);
    } catch (IOException e) {
      throw new RuntimeException("Error copying solr classpath resource to the filesystem", e);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(fos);
    }
  }
  public SearchResult getByQuery(String query, int limit, int offset) throws SearchException {
    try {
      logger.debug("Searching index using custom query '" + query + "'");
      return solrRequester.getByQuery(query, limit, offset);
    } catch (SolrServerException e) {
      throw new SearchException(e);
    }
  }
  public Job add(MediaPackage mediaPackage) throws SearchException, MediaPackageException, IllegalArgumentException,
          UnauthorizedException, ServiceRegistryException {
    try {
      return serviceRegistry.createJob(JOB_TYPE, Operation.Add.toString(),
              Arrays.asList(MediaPackageParser.getAsXml(mediaPackage)), addJobLoad);
    } catch (ServiceRegistryException e) {
      throw new SearchException(e);
    }
  }
  public void addSynchronously(MediaPackage mediaPackage)
      throws SearchException, IllegalArgumentException, UnauthorizedException, NotFoundException,
      SearchServiceDatabaseException {
    if (mediaPackage == null) {
      throw new IllegalArgumentException("Unable to add a null mediapackage");
    }
    final String mediaPackageId = mediaPackage.getIdentifier().toString();
    logger.debug("Attempting to add media package {} to search index", mediaPackageId);
    AccessControlList acl = authorizationService.getActiveAcl(mediaPackage).getA();
    AccessControlList seriesAcl = persistence.getAccessControlLists(mediaPackage.getSeries(), mediaPackageId).stream()
        .reduce(new AccessControlList(acl.getEntries()), AccessControlList::mergeActions);
    logger.debug("Updating series with merged access control list: {}", seriesAcl);
    Date now = new Date();
    try {
      if (indexManager.add(mediaPackage, acl, seriesAcl, now)) {
        logger.info("Added media package `{}` to the search index, using ACL `{}`", mediaPackageId, acl);
      } else {
        logger.warn("Failed to add media package {} to the search index", mediaPackageId);
      }
    } catch (SolrServerException e) {
      throw new SearchException(e);
    }
    try {
      persistence.storeMediaPackage(mediaPackage, acl, now);
    } catch (SearchServiceDatabaseException e) {
      throw new SearchException(
          String.format("Could not store media package to search database %s", mediaPackageId), e);
    }
  }
  public Job delete(String mediaPackageId) throws SearchException, UnauthorizedException, NotFoundException {
    try {
      return serviceRegistry.createJob(JOB_TYPE, Operation.Delete.toString(), Arrays.asList(mediaPackageId), deleteJobLoad);
    } catch (ServiceRegistryException e) {
      throw new SearchException(e);
    }
  }
  public boolean deleteSynchronously(final String mediaPackageId) throws SearchException {
    SearchResult result;
    try {
      result = solrRequester.getForWrite(new SearchQuery().withId(mediaPackageId));
      if (result.getItems().length == 0) {
        logger.warn(
                "Can not delete mediapackage {}, which is not available for the current user to delete from the search index.",
                mediaPackageId);
        return false;
      }
      final String seriesId = result.getItems()[0].getDcIsPartOf();
      logger.info("Removing media package {} from search index", mediaPackageId);
      Date now = new Date();
      try {
        persistence.deleteMediaPackage(mediaPackageId, now);
        logger.info("Removed mediapackage {} from search persistence", mediaPackageId);
      } catch (NotFoundException e) {
        logger.info("Could not find mediapackage with id {} in persistence, but will try remove it from index, anyway.",
                mediaPackageId);
      } catch (SearchServiceDatabaseException e) {
        logger.error("Could not delete media package with id {} from persistence storage", mediaPackageId);
        throw new SearchException(e);
      }
      final boolean success = indexManager.delete(mediaPackageId, now);
      if (seriesId != null) {
        if (persistence.getMediaPackages(seriesId).size() > 0) {
          final AccessControlList seriesAcl = persistence.getAccessControlLists(seriesId).stream()
              .reduce(new AccessControlList(), AccessControlList::mergeActions);
          indexManager.addSeries(seriesId, seriesAcl);
        } else {
          indexManager.delete(seriesId, now);
        }
      }
      return success;
    } catch (SolrServerException | SearchServiceDatabaseException e) {
      logger.info("Could not delete media package with id {} from search index", mediaPackageId);
      throw new SearchException(e);
    }
  }
  public void clear() throws SearchException {
    try {
      logger.info("Clearing the search index");
      indexManager.clear();
    } catch (SolrServerException e) {
      throw new SearchException(e);
    }
  }
  public SearchResult getByQuery(SearchQuery q) throws SearchException {
    try {
      logger.debug("Searching index using query object '" + q + "'");
      return solrRequester.getForRead(q);
    } catch (SolrServerException e) {
      throw new SearchException(e);
    }
  }
  @Override
  public SearchResult getForAdministrativeRead(SearchQuery q) throws SearchException, UnauthorizedException {
    User user = securityService.getUser();
    if (!user.hasRole(GLOBAL_ADMIN_ROLE) && !user.hasRole(user.getOrganization().getAdminRole()))
      throw new UnauthorizedException(user, getClass().getName() + ".getForAdministrativeRead");
    try {
      return solrRequester.getForAdministrativeRead(q);
    } catch (SolrServerException e) {
      throw new SearchException(e);
    }
  }
  protected void populateIndex(String systemUserName) {
    long instancesInSolr = 0L;
    try {
      instancesInSolr = indexManager.count();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    if (instancesInSolr > 0) {
      logger.debug("Search index found");
      return;
    }
    if (instancesInSolr == 0L) {
      logger.info("No search index found");
      Iterator<Tuple<MediaPackage, String>> mediaPackages;
      int total = 0;
      try {
        total = persistence.countMediaPackages();
        logger.info("Starting population of search index from {} items in database", total);
        mediaPackages = persistence.getAllMediaPackages();
      } catch (SearchServiceDatabaseException e) {
        logger.error("Unable to load the search entries: {}", e.getMessage());
        throw new ServiceException(e.getMessage());
      }
      int errors = 0;
      int current = 0;
      while (mediaPackages.hasNext()) {
        current++;
        try {
          Tuple<MediaPackage, String> mediaPackage = mediaPackages.next();
          String mediaPackageId = mediaPackage.getA().getIdentifier().toString();
          Organization organization = organizationDirectory.getOrganization(mediaPackage.getB());
          securityService.setOrganization(organization);
          securityService.setUser(SecurityUtil.createSystemUser(systemUserName, organization));
          AccessControlList acl = persistence.getAccessControlList(mediaPackageId);
          Date modificationDate = persistence.getModificationDate(mediaPackageId);
          Date deletionDate = persistence.getDeletionDate(mediaPackageId);
          indexManager.add(mediaPackage.getA(), acl, deletionDate, modificationDate);
        } catch (Exception e) {
          logger.error("Unable to index search instances:", e);
          if (retryToPopulateIndex(systemUserName)) {
            logger.warn("Trying to re-index search index later. Aborting for now.");
            return;
          }
          errors++;
        } finally {
          securityService.setOrganization(null);
          securityService.setUser(null);
        }
        if (current % 100 == 0) {
          logger.info("Indexing search {}/{} ({} percent done)", current, total, current * 100 / total);
        }
      }
      if (errors > 0)
        logger.error("Skipped {} erroneous search entries while populating the search index", errors);
      logger.info("Finished populating search index");
    }
  }
  private boolean retryToPopulateIndex(final String systemUserName) {
    if (retriesToPopulateIndex > 0) {
      return false;
    }
    long instancesInSolr = 0L;
    try {
      instancesInSolr = indexManager.count();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    if (instancesInSolr > 0) {
      logger.debug("Search index found, other files could be indexed. No retry needed.");
      return false;
    }
    retriesToPopulateIndex++;
      new Thread() {
        public void run() {
          try {
            Thread.sleep(30000);
          } catch (InterruptedException ex) {
          }
          populateIndex(systemUserName);
        }
      }.start();
    return true;
  }
  @Override
  protected String process(Job job) throws Exception {
    Operation op = null;
    String operation = job.getOperation();
    List<String> arguments = job.getArguments();
    try {
      op = Operation.valueOf(operation);
      switch (op) {
        case Add:
          MediaPackage mediaPackage = MediaPackageParser.getFromXml(arguments.get(0));
          addSynchronously(mediaPackage);
          return null;
        case Delete:
          String mediapackageId = arguments.get(0);
          boolean deleted = deleteSynchronously(mediapackageId);
          return Boolean.toString(deleted);
        default:
          throw new IllegalStateException("Don't know how to handle operation '" + operation + "'");
      }
    } catch (IllegalArgumentException e) {
      throw new ServiceRegistryException("This service can't handle operations of type '" + op + "'", e);
    } catch (IndexOutOfBoundsException e) {
      throw new ServiceRegistryException("This argument list for operation '" + op + "' does not meet expectations", e);
    } catch (Exception e) {
      throw new ServiceRegistryException("Error handling operation '" + op + "'", e);
    }
  }
  void testSetup(SolrServer server, SolrRequester requester, SolrIndexManager manager) {
    this.solrServer = server;
    this.solrRequester = requester;
    this.indexManager = manager;
  }
  public void setStaticMetadataService(StaticMetadataService mdService) {
    this.mdServices.add(mdService);
    if (indexManager != null)
      indexManager.setStaticMetadataServices(mdServices);
  }
  public void unsetStaticMetadataService(StaticMetadataService mdService) {
    this.mdServices.remove(mdService);
    if (indexManager != null)
      indexManager.setStaticMetadataServices(mdServices);
  }
  public void setMpeg7CatalogService(Mpeg7CatalogService mpeg7CatalogService) {
    this.mpeg7CatalogService = mpeg7CatalogService;
  }
  public void setPersistence(SearchServiceDatabase persistence) {
    this.persistence = persistence;
  }
  public void setSeriesService(SeriesService seriesService) {
    this.seriesService = seriesService;
  }
  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }
  public void setAuthorizationService(AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }
  public void setServiceRegistry(ServiceRegistry serviceRegistry) {
    this.serviceRegistry = serviceRegistry;
  }
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }
  public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
    this.userDirectoryService = userDirectoryService;
  }
  public void setOrganizationDirectoryService(OrganizationDirectoryService organizationDirectory) {
    this.organizationDirectory = organizationDirectory;
  }
  @Override
  protected OrganizationDirectoryService getOrganizationDirectoryService() {
    return organizationDirectory;
  }
  @Override
  protected SecurityService getSecurityService() {
    return securityService;
  }
  @Override
  protected ServiceRegistry getServiceRegistry() {
    return serviceRegistry;
  }
  @Override
  protected UserDirectoryService getUserDirectoryService() {
    return userDirectoryService;
  }
  protected void setMediaPackageSerializer(MediaPackageSerializer serializer) {
    this.serializer = serializer;
    if (solrRequester != null)
      solrRequester.setMediaPackageSerializer(serializer);
  }
  @Override
  public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
    addJobLoad = LoadUtil.getConfiguredLoadValue(properties, ADD_JOB_LOAD_KEY, DEFAULT_ADD_JOB_LOAD, serviceRegistry);
    deleteJobLoad = LoadUtil.getConfiguredLoadValue(properties, DELETE_JOB_LOAD_KEY, DEFAULT_DELETE_JOB_LOAD, serviceRegistry);
  }
  @Override
  public List<Pattern> getProtectedUrlPattern() {
    return Collections.singletonList(staticFilePattern);
  }
  private boolean loadUrlAccess(final String mediaPackageId) {
    logger.debug("Check if user `{}` has access to media package `{}`", securityService.getUser(), mediaPackageId);
    final SearchQuery query = new SearchQuery()
        .withId(mediaPackageId)
        .includeEpisodes(true)
        .includeSeries(false);
    return getByQuery(query).size() > 0;
  }
  @Override
  public boolean verifyUrlAccess(final String path) {
    final User user = securityService.getUser();
    if (user.hasRole(GLOBAL_ADMIN_ROLE)) {
      logger.debug("Allow access for admin `{}`", user);
      return true;
    }
    final Matcher m = staticFilePattern.matcher(path);
    if (!m.matches()) {
      logger.debug("Path does not match pattern. Preventing access.");
      return false;
    }
    final String organizationId = m.group(1);
    if (!securityService.getOrganization().getId().equals(organizationId)) {
      logger.debug("The user's organization does not match. Preventing access.");
      return false;
    }
    final String mediaPackageId = m.group(2);
    final boolean access = cache.getUnchecked(Tuple.tuple(user, mediaPackageId));
    logger.debug("Check if user `{}` has access to media package `{}` using cache: {}", user, mediaPackageId, access);
    return access;
  }
}
