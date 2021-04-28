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
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/user").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/profile").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/login").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/post").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/user").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/profile").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/post").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/feed").permitAll()
                .anyRequest().authenticated();
    }
}
