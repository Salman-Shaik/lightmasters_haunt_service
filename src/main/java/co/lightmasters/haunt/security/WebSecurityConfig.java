package co.lightmasters.haunt.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/configuration/ui", "/swagger-resources/**", "/configuration/**",
                        "/swagger-ui.html", "/webjars/**", "/actuator/**","/v2/api-docs").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/*").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/*").permitAll()
                .antMatchers(HttpMethod.DELETE, "/v1/*").permitAll()
                .anyRequest().authenticated();
    }
}
