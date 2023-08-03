
package org.keycloak.services.managers;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jboss.logging.Logger;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.authentication.ClientAuthenticatorFactory;
import org.keycloak.common.constants.ServiceAccountConstants;
import org.keycloak.common.util.Time;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionProvider;
import org.keycloak.models.session.UserSessionPersisterProvider;
import org.keycloak.models.utils.RepresentationToModel;
import org.keycloak.protocol.LoginProtocol;
import org.keycloak.protocol.LoginProtocolFactory;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.oidc.mappers.UserSessionNoteMapper;
import org.keycloak.representations.adapters.config.BaseRealmConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.sessions.AuthenticationSessionProvider;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
public class ClientManager {
    private static final Logger logger = Logger.getLogger(ClientManager.class);
    protected RealmManager realmManager;
    public ClientManager(RealmManager realmManager) {
        this.realmManager = realmManager;
    }
    public ClientManager() {
    }
    public static ClientModel createClient(KeycloakSession session, RealmModel realm, ClientRepresentation rep, boolean addDefaultRoles) {
        ClientModel client = RepresentationToModel.createClient(session, realm, rep, addDefaultRoles);
        if (rep.getProtocol() != null) {
            LoginProtocolFactory providerFactory = (LoginProtocolFactory) session.getKeycloakSessionFactory().getProviderFactory(LoginProtocol.class, rep.getProtocol());
            providerFactory.setupClientDefaults(rep, client);
        }
        if (rep.getProtocolMappers() == null && rep.getClientTemplate() != null) {
            Set<ProtocolMapperModel> mappers = client.getProtocolMappers();
            for (ProtocolMapperModel mapper : mappers) client.removeProtocolMapper(mapper);
        }
        return client;
    }
    public boolean removeClient(RealmModel realm, ClientModel client) {
        if (realm.removeClient(client.getId())) {
            UserSessionProvider sessions = realmManager.getSession().sessions();
            if (sessions != null) {
                sessions.onClientRemoved(realm, client);
            }
            UserSessionPersisterProvider sessionsPersister = realmManager.getSession().getProvider(UserSessionPersisterProvider.class);
            if (sessionsPersister != null) {
                sessionsPersister.onClientRemoved(realm, client);
            }
            AuthenticationSessionProvider authSessions = realmManager.getSession().authenticationSessions();
            if (authSessions != null) {
                authSessions.onClientRemoved(realm, client);
            }
            UserModel serviceAccountUser = realmManager.getSession().users().getServiceAccount(client);
            if (serviceAccountUser != null) {
                new UserManager(realmManager.getSession()).removeUser(realm, serviceAccountUser);
            }
            return true;
        } else {
            return false;
        }
    }
    public Set<String> validateRegisteredNodes(ClientModel client) {
        Map<String, Integer> registeredNodes = client.getRegisteredNodes();
        if (registeredNodes == null || registeredNodes.isEmpty()) {
            return Collections.emptySet();
        }
        int currentTime = Time.currentTime();
        Set<String> validatedNodes = new TreeSet<String>();
        if (client.getNodeReRegistrationTimeout() > 0) {
            List<String> toRemove = new LinkedList<String>();
            for (Map.Entry<String, Integer> entry : registeredNodes.entrySet()) {
                Integer lastReRegistration = entry.getValue();
                if (lastReRegistration + client.getNodeReRegistrationTimeout() < currentTime) {
                    toRemove.add(entry.getKey());
                } else {
                    validatedNodes.add(entry.getKey());
                }
            }
            for (String node : toRemove) {
                client.unregisterNode(node);
            }
        } else {
            validatedNodes.addAll(registeredNodes.keySet());
        }
        return validatedNodes;
    }
    public void enableServiceAccount(ClientModel client) {
        client.setServiceAccountsEnabled(true);
        if (realmManager.getSession().users().getServiceAccount(client) == null) {
            String username = ServiceAccountConstants.SERVICE_ACCOUNT_USER_PREFIX + client.getClientId();
            logger.debugf("Creating service account user '%s'", username);
            UserModel user = realmManager.getSession().userLocalStorage().addUser(client.getRealm(), username);
            user.setEnabled(true);
            user.setServiceAccountClientLink(client.getId());
        }
        if (client.getProtocolMapperByName(OIDCLoginProtocol.LOGIN_PROTOCOL, ServiceAccountConstants.CLIENT_ID_PROTOCOL_MAPPER) == null) {
            logger.debugf("Creating service account protocol mapper '%s' for client '%s'", ServiceAccountConstants.CLIENT_ID_PROTOCOL_MAPPER, client.getClientId());
            ProtocolMapperModel protocolMapper = UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_ID_PROTOCOL_MAPPER,
                    ServiceAccountConstants.CLIENT_ID,
                    ServiceAccountConstants.CLIENT_ID, "String",
                    true, true);
            client.addProtocolMapper(protocolMapper);
        }
        if (client.getProtocolMapperByName(OIDCLoginProtocol.LOGIN_PROTOCOL, ServiceAccountConstants.CLIENT_HOST_PROTOCOL_MAPPER) == null) {
            logger.debugf("Creating service account protocol mapper '%s' for client '%s'", ServiceAccountConstants.CLIENT_HOST_PROTOCOL_MAPPER, client.getClientId());
            ProtocolMapperModel protocolMapper = UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_HOST_PROTOCOL_MAPPER,
                    ServiceAccountConstants.CLIENT_HOST,
                    ServiceAccountConstants.CLIENT_HOST, "String",
                    true, true);
            client.addProtocolMapper(protocolMapper);
        }
        if (client.getProtocolMapperByName(OIDCLoginProtocol.LOGIN_PROTOCOL, ServiceAccountConstants.CLIENT_ADDRESS_PROTOCOL_MAPPER) == null) {
            logger.debugf("Creating service account protocol mapper '%s' for client '%s'", ServiceAccountConstants.CLIENT_ADDRESS_PROTOCOL_MAPPER, client.getClientId());
            ProtocolMapperModel protocolMapper = UserSessionNoteMapper.createClaimMapper(ServiceAccountConstants.CLIENT_ADDRESS_PROTOCOL_MAPPER,
                    ServiceAccountConstants.CLIENT_ADDRESS,
                    ServiceAccountConstants.CLIENT_ADDRESS, "String",
                    true, true);
            client.addProtocolMapper(protocolMapper);
        }
    }
    public void clientIdChanged(ClientModel client, String newClientId) {
        logger.debugf("Updating clientId from '%s' to '%s'", client.getClientId(), newClientId);
        UserModel serviceAccountUser = realmManager.getSession().users().getServiceAccount(client);
        if (serviceAccountUser != null) {
            String username = ServiceAccountConstants.SERVICE_ACCOUNT_USER_PREFIX + newClientId;
            serviceAccountUser.setUsername(username);
        }
    }
    @JsonPropertyOrder({"realm", "realm-public-key", "bearer-only", "auth-server-url", "ssl-required",
            "resource", "public-client", "verify-token-audience", "credentials",
            "use-resource-role-mappings"})
    public static class InstallationAdapterConfig extends BaseRealmConfig {
        @JsonProperty("resource")
        protected String resource;
        @JsonProperty("use-resource-role-mappings")
        protected Boolean useResourceRoleMappings;
        @JsonProperty("bearer-only")
        protected Boolean bearerOnly;
        @JsonProperty("public-client")
        protected Boolean publicClient;
        @JsonProperty("credentials")
        protected Map<String, Object> credentials;
        @JsonProperty("verify-token-audience")
        protected Boolean verifyTokenAudience;
        @JsonProperty("policy-enforcer")
        protected PolicyEnforcerConfig enforcerConfig;
        public Boolean isUseResourceRoleMappings() {
            return useResourceRoleMappings;
        }
        public void setUseResourceRoleMappings(Boolean useResourceRoleMappings) {
            this.useResourceRoleMappings = useResourceRoleMappings;
        }
        public String getResource() {
            return resource;
        }
        public void setResource(String resource) {
            this.resource = resource;
        }
        public Map<String, Object> getCredentials() {
            return credentials;
        }
        public void setCredentials(Map<String, Object> credentials) {
            this.credentials = credentials;
        }
        public Boolean getVerifyTokenAudience() {
            return verifyTokenAudience;
        }
        public void setVerifyTokenAudience(Boolean verifyTokenAudience) {
            this.verifyTokenAudience = verifyTokenAudience;
        }
        public Boolean getPublicClient() {
            return publicClient;
        }
        public void setPublicClient(Boolean publicClient) {
            this.publicClient = publicClient;
        }
        public Boolean getBearerOnly() {
            return bearerOnly;
        }
        public void setBearerOnly(Boolean bearerOnly) {
            this.bearerOnly = bearerOnly;
        }
        public PolicyEnforcerConfig getEnforcerConfig() {
            return this.enforcerConfig;
        }
        public void setEnforcerConfig(PolicyEnforcerConfig enforcerConfig) {
            this.enforcerConfig = enforcerConfig;
        }
    }
    public InstallationAdapterConfig toInstallationRepresentation(RealmModel realmModel, ClientModel clientModel, URI baseUri) {
        InstallationAdapterConfig rep = new InstallationAdapterConfig();
        rep.setAuthServerUrl(baseUri.toString());
        rep.setRealm(realmModel.getName());
        rep.setSslRequired(realmModel.getSslRequired().name().toLowerCase());
        if (clientModel.isPublicClient() && !clientModel.isBearerOnly()) rep.setPublicClient(true);
        if (clientModel.isBearerOnly()) rep.setBearerOnly(true);
        if (clientModel.getRoles().size() > 0) rep.setUseResourceRoleMappings(true);
        rep.setResource(clientModel.getClientId());
        if (showClientCredentialsAdapterConfig(clientModel)) {
            Map<String, Object> adapterConfig = getClientCredentialsAdapterConfig(clientModel);
            rep.setCredentials(adapterConfig);
        }
        return rep;
    }
    public String toJBossSubsystemConfig(RealmModel realmModel, ClientModel clientModel, URI baseUri) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<secure-deployment name=\"WAR MODULE NAME.war\">\n");
        buffer.append("    <realm>").append(realmModel.getName()).append("</realm>\n");
        buffer.append("    <auth-server-url>").append(baseUri.toString()).append("</auth-server-url>\n");
        if (clientModel.isBearerOnly()){
            buffer.append("    <bearer-only>true</bearer-only>\n");
        } else if (clientModel.isPublicClient()) {
            buffer.append("    <public-client>true</public-client>\n");
        }
        buffer.append("    <ssl-required>").append(realmModel.getSslRequired().name()).append("</ssl-required>\n");
        buffer.append("    <resource>").append(clientModel.getClientId()).append("</resource>\n");
        String cred = clientModel.getSecret();
        if (showClientCredentialsAdapterConfig(clientModel)) {
            Map<String, Object> adapterConfig = getClientCredentialsAdapterConfig(clientModel);
            for (Map.Entry<String, Object> entry : adapterConfig.entrySet()) {
                buffer.append("    <credential name=\"" + entry.getKey() + "\">");
                Object value = entry.getValue();
                if (value instanceof Map) {
                    buffer.append("\n");
                    Map<String, Object> asMap = (Map<String, Object>) value;
                    for (Map.Entry<String, Object> credEntry : asMap.entrySet()) {
                        buffer.append("        <" + credEntry.getKey() + ">" + credEntry.getValue().toString() + "</" + credEntry.getKey() + ">\n");
                    }
                    buffer.append("    </credential>\n");
                } else {
                    buffer.append(value.toString()).append("</credential>\n");
                }
            }
        }
        if (clientModel.getRoles().size() > 0) {
            buffer.append("    <use-resource-role-mappings>true</use-resource-role-mappings>\n");
        }
        buffer.append("</secure-deployment>\n");
        return buffer.toString();
    }
    private boolean showClientCredentialsAdapterConfig(ClientModel client) {
        if (client.isPublicClient()) {
            return false;
        }
        if (client.isBearerOnly() && client.getNodeReRegistrationTimeout() <= 0) {
            return false;
        }
        return true;
    }
    private Map<String, Object> getClientCredentialsAdapterConfig(ClientModel client) {
        String clientAuthenticator = client.getClientAuthenticatorType();
        ClientAuthenticatorFactory authenticator = (ClientAuthenticatorFactory) realmManager.getSession().getKeycloakSessionFactory().getProviderFactory(ClientAuthenticator.class, clientAuthenticator);
        return authenticator.getAdapterConfiguration(client);
    }
}
