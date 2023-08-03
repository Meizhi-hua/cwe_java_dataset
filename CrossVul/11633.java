
package io.milton.http.http11.auth;
import io.milton.common.Utils;
import io.milton.http.OAuth2TokenResponse;
import io.milton.resource.OAuth2Resource.OAuth2ProfileDetails;
import io.milton.resource.OAuth2Provider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class OAuth2Helper {
	private static final Logger log = LoggerFactory.getLogger(OAuth2Helper.class);
	public static URL getOAuth2URL(OAuth2Provider provider) {
		log.trace("getOAuth2URL {}", provider);
		String oAuth2Location = provider.getAuthLocation();
		String oAuth2ClientId = provider.getClientId();
		String scopes = Utils.toCsv(provider.getPermissionScopes(), false);
		try {
			OAuthClientRequest oAuthRequest = OAuthClientRequest
					.authorizationLocation(oAuth2Location)
					.setClientId(oAuth2ClientId)
					.setResponseType("code")
					.setScope(scopes)
					.setState(provider.getProviderId())
					.setRedirectURI(provider.getRedirectURI())
					.buildQueryMessage();
			return new URL(oAuthRequest.getLocationUri());
		} catch (OAuthSystemException oAuthSystemException) {
			throw new RuntimeException(oAuthSystemException);
		} catch (MalformedURLException malformedURLException) {
			throw new RuntimeException(malformedURLException);
		}
	}
	private final NonceProvider nonceProvider;
	public OAuth2Helper(NonceProvider nonceProvider) {
		this.nonceProvider = nonceProvider;
	}
	public OAuthAccessTokenResponse obtainAuth2Token(OAuth2Provider provider, String accessCode) throws OAuthSystemException, OAuthProblemException {
		log.trace("obtainAuth2Token code={}, provider={}", accessCode, provider);
		String oAuth2ClientId = provider.getClientId();
		String oAuth2TokenLocation = provider.getTokenLocation();
		String oAuth2ClientSecret = provider.getClientSecret();
		String oAuth2RedirectURI = provider.getRedirectURI();
		OAuthClientRequest oAuthRequest = OAuthClientRequest
				.tokenLocation(oAuth2TokenLocation)
				.setGrantType(GrantType.AUTHORIZATION_CODE)
				.setRedirectURI(oAuth2RedirectURI)
				.setCode(accessCode)
				.setClientId(oAuth2ClientId)
				.setClientSecret(oAuth2ClientSecret)
				.buildBodyMessage();
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		OAuthAccessTokenResponse oAuth2Response2 = oAuthClient.accessToken(oAuthRequest, OAuth2TokenResponse.class);
		OAuthJSONAccessTokenResponse o;
		return oAuth2Response2;
	}
	public OAuthResourceResponse getOAuth2Profile(OAuthAccessTokenResponse oAuth2Response, OAuth2Provider provider)
			throws OAuthSystemException, OAuthProblemException {
		log.trace("getOAuth2Profile start {}", oAuth2Response);
		String accessToken = oAuth2Response.getAccessToken();
		String userProfileLocation = provider.getProfileLocation();
		OAuthClientRequest bearerClientRequest
				= new OAuthBearerClientRequest(userProfileLocation)
				.setAccessToken(accessToken)
				.buildQueryMessage();
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		return oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
	}
	public OAuth2ProfileDetails getOAuth2UserInfo(OAuthResourceResponse resourceResponse, OAuthAccessTokenResponse tokenResponse, String oAuth2Code) {
		log.trace(" getOAuth2UserId start..." + resourceResponse);
		if (resourceResponse == null) {
			return null;
		}
		String resourceResponseBody = resourceResponse.getBody();
		log.trace(" OAuthResourceResponse, body{}" + resourceResponseBody);
		Map responseMap = JSONUtils.parseJSON(resourceResponseBody);
		String userID = (String) responseMap.get("id");
		String userName = (String) responseMap.get("username");
		OAuth2ProfileDetails user = new OAuth2ProfileDetails();
		user.setCode(oAuth2Code);
		user.setAccessToken(tokenResponse.getAccessToken());
		user.setDetails(responseMap);
		if (log.isTraceEnabled()) {
			log.trace(" userID{}" + userID);
			log.trace(" userName{}" + userName);
			log.trace(" oAuth2Code{}" + oAuth2Code);
			log.trace(" AccessToken{}" + user.getAccessToken());
			log.trace("\n\n");
		}
		return user;
	}
}
