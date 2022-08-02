package com.devsuperior.bds03.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// Endpoints publicos
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	// Endpoints de operador e admin
	private static final String[] OPERATOR = { "/departments/**", "/employees/**" };
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;

	// Verifica se o token é válido
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) { // Se for perfil de teste
			http.headers().frameOptions().disable(); // Liberar H2 console
		}
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()// Acessar aos endpoints públicos não exigiram login
		.antMatchers(HttpMethod.GET, OPERATOR).hasAnyRole("ADMIN","OPERATOR")
		.anyRequest().hasAnyRole("ADMIN");

	}

}
