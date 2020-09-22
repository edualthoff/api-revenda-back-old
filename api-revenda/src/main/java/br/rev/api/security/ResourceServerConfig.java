package br.rev.api.security;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;


@Configuration
@EnableResourceServer
@Order(value = 0)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value(value ="${security.oauth2.resource.id}")
	private String resourceId;
	
	private static final String ROOT_PATTERN = "/pr**";
	
	@Autowired TokenStore tokenStore;
	@Autowired
	private AccessDeniedHandlerImpl accessDeniedHandlerImp;
	
    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
    	resources.resourceId(resourceId).tokenStore(tokenStore).accessDeniedHandler(accessDeniedHandlerImp);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, ROOT_PATTERN).access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.PATCH, ROOT_PATTERN).access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, ROOT_PATTERN).access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, ROOT_PATTERN).access("#oauth2.hasScope('write')");
    }
}
