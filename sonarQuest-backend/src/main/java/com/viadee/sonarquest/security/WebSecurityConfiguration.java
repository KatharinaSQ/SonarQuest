package com.viadee.sonarquest.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtHelper jwtHelper;

    @Value("${cors.header.active:false}")
    private boolean corsHeaderActive;

    public WebSecurityConfiguration(final UserDetailsService userDetailsService, final JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    private DaoAuthenticationProvider getAuthenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setPreAuthenticationChecks(new NoAuthenticationChecks());
        daoAuthenticationProvider.setPostAuthenticationChecks(new NoAuthenticationChecks());
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtHelper);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // h2 console to work
        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()

                .antMatchers("/socket/**").permitAll()
                .antMatchers("/socket").permitAll()
                .antMatchers("/app").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/chat").permitAll()
                .antMatchers("/raid/**").permitAll()
                .antMatchers("/raid").permitAll()
                // h2 console
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                //.antMatchers(HttpMethod.GET, "/*").permitAll()
                //.antMatchers(HttpMethod.POST, "/*").permitAll()
                .antMatchers(HttpMethod.GET, "/assets/**").permitAll()
                .anyRequest().authenticated();

        if (corsHeaderActive) {
            http.cors();
        }

        http.addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	 final CorsConfiguration configuration = new CorsConfiguration();
         configuration.setAllowedOrigins(ImmutableList.of("*"));//ImmutableList.of("http://localhost:4200")
         configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
         configuration.setAllowCredentials(true);
         configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
         final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         source.registerCorsConfiguration("/**", configuration);
         return source;
    }

}