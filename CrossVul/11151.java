
package org.opencastproject.search.impl.persistence;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.impl.jpa.JpaOrganization;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity(name = "SearchEntity")
@Table(name = "oc_search", indexes = {
    @Index(name = "IX_oc_search_series", columnList = ("series_id")),
    @Index(name = "IX_oc_search_organization", columnList = ("organization")) })
@NamedQueries({
    @NamedQuery(name = "Search.findAll", query = "SELECT s FROM SearchEntity s"),
    @NamedQuery(name = "Search.getCount", query = "SELECT COUNT(s) FROM SearchEntity s"),
    @NamedQuery(name = "Search.findById", query = "SELECT s FROM SearchEntity s WHERE s.mediaPackageId=:mediaPackageId"),
    @NamedQuery(name = "Search.findBySeriesId", query = "SELECT s FROM SearchEntity s WHERE s.seriesId=:seriesId and "
        + "s.deletionDate is null"),
    @NamedQuery(name = "Search.getNoSeries", query = "SELECT s FROM SearchEntity s WHERE s.seriesId IS NULL")})
public class SearchEntity {
  @Id
  @Column(name = "id", length = 128)
  private String mediaPackageId;
  @Column(name = "series_id", length = 128)
  protected String seriesId;
  @OneToOne(targetEntity = JpaOrganization.class)
  @JoinColumn(name = "organization", referencedColumnName = "id")
  protected JpaOrganization organization;
  @Column(name = "deletion_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date deletionDate;
  @Column(name = "modification_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modificationDate;
  @Lob
  @Column(name = "mediapackage_xml", length = 65535)
  private String mediaPackageXML;
  @Lob
  @Column(name = "access_control", length = 65535)
  protected String accessControl;
  public SearchEntity() {
  }
  public String getMediaPackageId() {
    return mediaPackageId;
  }
  public void setMediaPackageId(String mediaPackageId) {
    this.mediaPackageId = mediaPackageId;
  }
  public String getMediaPackageXML() {
    return mediaPackageXML;
  }
  public void setMediaPackageXML(String mediaPackageXML) {
    this.mediaPackageXML = mediaPackageXML;
  }
  public String getAccessControl() {
    return accessControl;
  }
  public void setAccessControl(String accessControl) {
    this.accessControl = accessControl;
  }
  public JpaOrganization getOrganization() {
    return organization;
  }
  public void setOrganization(Organization organization) {
    if (organization instanceof JpaOrganization) {
      this.organization = (JpaOrganization) organization;
    } else {
      this.organization = new JpaOrganization(organization.getId(), organization.getName(), organization.getServers(),
          organization.getAdminRole(), organization.getAnonymousRole(), organization.getProperties());
    }
  }
  public Date getDeletionDate() {
    return deletionDate;
  }
  public void setDeletionDate(Date deletionDate) {
    this.deletionDate = deletionDate;
  }
  public Date getModificationDate() {
    return modificationDate;
  }
  public void setModificationDate(Date modificationDate) {
    this.modificationDate = modificationDate;
  }
  public String getSeriesId() {
    return seriesId;
  }
  public void setSeriesId(String seriesId) {
    this.seriesId = seriesId;
  }
}
