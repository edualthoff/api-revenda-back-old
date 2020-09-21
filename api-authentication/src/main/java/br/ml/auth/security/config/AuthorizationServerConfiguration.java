package br.ml.auth.security.config;

import java.security.KeyPair;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import br.ml.auth.security.jwt.CustomTokenEnhancer;
import br.ml.auth.security.jwt.SecurityProperties;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	@Qualifier("oauthJdbcTemplate")
	private JdbcTemplate oauthJdbcTemplate;

    @Autowired
    private DefaultWebResponseExceptionTranslator oauth2ResponseExceptionTranslator;
    
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//System.out.println("db.. "+oauthJdbcTemplate.getDataSource().getConnection().getSchema());
		clients.jdbc(oauthJdbcTemplate.getDataSource());
		//clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
		endpoints.tokenStore(tokenStore())
				.reuseRefreshTokens(false)
				.tokenEnhancer(tokenEnhancerChain)
				.accessTokenConverter(jwtAccessTokenConverter())
				// .tokenServices(tokenServices())
				// .authorizationCodeServices(codeServices)
				.authenticationManager(authenticationManager)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
				.userDetailsService(userDetailsService)
				.exceptionTranslator(oauth2ResponseExceptionTranslator);
				//.tokenGranter(configTokenGranter.tokenGranter(endpoints));
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		//oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT', 'ROLE_CLIENT')").checkTokenAccess("isAuthenticated()");
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		KeyPair keyPair = new KeyStoreKeyFactory(securityProperties.getJwt().getKeyStore(),
				securityProperties.getJwt().getKeyStorePassword().toCharArray())
						.getKeyPair(securityProperties.getJwt().getKeyPairAlias());
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyPair);
		// converter.setSigningKey("123");
		return converter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}
}
