
package org.opencastproject.kernel.security;
import org.opencastproject.security.api.JaxbOrganization;
import org.opencastproject.security.api.JaxbRole;
import org.opencastproject.security.api.JaxbUser;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.User;
import org.opencastproject.security.api.UserDirectoryService;
import org.opencastproject.security.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public class SecurityServiceSpringImpl implements SecurityService {
  private static final Logger logger = LoggerFactory.getLogger(SecurityServiceSpringImpl.class);
  private static final ThreadLocal<User> delegatedUserHolder = new ThreadLocal<User>();
  private static final ThreadLocal<String> delegatedUserIPHolder = new ThreadLocal<String>();
  private static final ThreadLocal<Organization> organization = new ThreadLocal<Organization>();
  private UserDirectoryService userDirectory;
  @Override
  public Organization getOrganization() {
    return SecurityServiceSpringImpl.organization.get();
  }
  @Override
  public void setOrganization(Organization organization) {
    SecurityServiceSpringImpl.organization.set(organization);
  }
  @Override
  public User getUser() throws IllegalStateException {
    Organization org = getOrganization();
    if (org == null)
      throw new IllegalStateException("No organization is set in security context");
    User delegatedUser = delegatedUserHolder.get();
    if (delegatedUser != null) {
      return delegatedUser;
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    JaxbOrganization jaxbOrganization = JaxbOrganization.fromOrganization(org);
    if (auth != null) {
      Object principal = auth.getPrincipal();
      if ((principal != null) && (principal instanceof UserDetails)) {
        UserDetails userDetails = (UserDetails) principal;
        User user = null;
        if (userDirectory != null) {
          user = userDirectory.loadUser(userDetails.getUsername());
          if (user == null) {
            logger.debug(
                    "Authenticated user '{}' could not be found in any of the current UserProviders. Continuing anyway...",
                    userDetails.getUsername());
          }
        } else {
          logger.debug("No UserDirectory was found when trying to search for user '{}'", userDetails.getUsername());
        }
        Set<JaxbRole> roles = new HashSet<JaxbRole>();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities != null) {
          for (GrantedAuthority ga : authorities) {
            roles.add(new JaxbRole(ga.getAuthority(), jaxbOrganization));
          }
        }
        if (user == null) {
          user = new JaxbUser(userDetails.getUsername(), null, jaxbOrganization, roles);
        } else {
          user = JaxbUser.fromUser(user, roles);
        }
        delegatedUserHolder.set(user);
        return user;
      }
    }
    return SecurityUtil.createAnonymousUser(jaxbOrganization);
  }
  @Override
  public void setUser(User user) {
    delegatedUserHolder.set(user);
  }
  @Override
  public String getUserIP() {
    return delegatedUserIPHolder.get();
  }
  @Override
  public void setUserIP(String userIP) {
    delegatedUserIPHolder.set(userIP);
  }
  void setUserDirectory(UserDirectoryService userDirectory) {
    this.userDirectory = userDirectory;
  }
  void removeUserDirectory() {
    this.userDirectory = null;
  }
}
