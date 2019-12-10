package com.nvisia.meetup.airport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring configuration class to enable web security, and override defaults
 *
 * @author [Julio Cesar Villalta III](mailto:jvillalta@nvisia.com)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // enable method security, including pre and post method invocations
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Override the default http security config, opening all endpoints to not enforce requests to be authenticate,
     * at least from here.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**")
            .permitAll()
            .anyRequest()
            .authenticated();
    }

}