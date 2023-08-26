package com.feijian.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feijian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserService userService;
    /*@Autowired
    public void setUserService(UserDetailsService userService) {
        this.userService = userService;
    }*/
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 如果不定义该bean，可能先配置security，这是service还没有初始化，出现问题
     * @return
     * @throws Exception
     */
    @Bean
    public DaoAuthenticationProvider myAuthProvider() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws  Exception{
        /*auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());*/
        auth.authenticationProvider(myAuthProvider());
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler() {
            ObjectMapper objectMapper = new ObjectMapper();
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException e) throws IOException, ServletException {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String,Object> data = new HashMap<>();
                data.put("timestamp", Calendar.getInstance().getTime());
                data.put("exception",e.getMessage());
                response.getOutputStream().println(objectMapper.writeValueAsString(data));
            }
        };
    }
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN>ROLE_USER");
        return hierarchy;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/plugins/**","/image/**","/fonts/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index")
//                .failureUrl("/login_fail")
                .failureHandler(authenticationFailureHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/register","/regSuccess").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/signout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf().disable();
    }
}
