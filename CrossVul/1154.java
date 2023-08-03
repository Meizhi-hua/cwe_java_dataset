
package org.opencastproject.search.impl.persistence;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.security.api.AccessControlList;
import org.opencastproject.security.api.UnauthorizedException;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.util.data.Tuple;
import java.util.Date;
import java.util.Iterator;
public interface SearchServiceDatabase {
  Iterator<Tuple<MediaPackage, String>> getAllMediaPackages() throws SearchServiceDatabaseException;
  String getOrganizationId(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException;
  int countMediaPackages() throws SearchServiceDatabaseException;
  MediaPackage getMediaPackage(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException;
  AccessControlList getAccessControlList(String mediaPackageId) throws NotFoundException,
          SearchServiceDatabaseException;
  Date getModificationDate(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException;
  Date getDeletionDate(String mediaPackageId) throws NotFoundException, SearchServiceDatabaseException;
  void deleteMediaPackage(String mediaPackageId, Date deletionDate) throws SearchServiceDatabaseException,
          NotFoundException;
  void storeMediaPackage(MediaPackage mediaPackage, AccessControlList acl, Date now)
          throws SearchServiceDatabaseException, UnauthorizedException;
}
