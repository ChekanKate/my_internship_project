package com.leverx.internship.project.config;

import com.leverx.internship.project.security.jwt.JwtConfigurer;
import com.leverx.internship.project.security.model.Permission;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtConfigurer jwtConfigurer;
  private static final String[] ALL_USERS_PERMISSION = new String[]{"/api/users/**",
      "/api/projects/{id}", "/api/departments/{id}"};
  private static final String[] LOGOUT_ENDPOINT = new String[]{"/api/auth/logout"};
  private static final String[] PERMIT_ALL =
      new String[]{"/api/auth/login", "/api/swagger-ui/**", "/api/v3/api-docs"};
  private static final String[] WRITE_DELETE_USER_PERMISSION = new String[]{"/api/departments/**",
      "/api/projects/**", "/api/report/**"};

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .disable()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(PERMIT_ALL)
        .permitAll()
        .antMatchers(HttpMethod.GET, ALL_USERS_PERMISSION)
        .hasAnyAuthority(Permission.USER_READ.getPermission())
        .antMatchers(HttpMethod.POST, LOGOUT_ENDPOINT)
        .hasAnyAuthority(Permission.USER_READ.getPermission())
        .antMatchers(HttpMethod.GET, WRITE_DELETE_USER_PERMISSION)
        .hasAnyAuthority(Permission.USER_WRITE.getPermission())
        .antMatchers(HttpMethod.PUT)
        .hasAnyAuthority(Permission.USER_DELETE.getPermission())
        .antMatchers(HttpMethod.POST)
        .hasAnyAuthority(Permission.USER_DELETE.getPermission())
        .antMatchers(HttpMethod.DELETE)
        .hasAnyAuthority(Permission.USER_DELETE.getPermission())
        .anyRequest()
        .authenticated()
        .and()
        .apply(jwtConfigurer);
  }

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}