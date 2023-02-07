package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebSecurityCustomizer {
    
    private final SuccessUserHandler successUserHandler;
    
    private final DataSource dataSource;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, DataSource dataSource) {
        this.successUserHandler = successUserHandler;
        this.dataSource = dataSource;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/reg").anonymous()
                .requestMatchers("/users").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers("/admin", "/userinfo").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .anonymous().authorities("ANON");
        return http.build();
    }
    
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }
    
    @Bean
    public UserDetailsManager userDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("select username, role " +
                        "from users_roles " +
                        "right join roles r on r.role_id = users_roles.roles_role_id " +
                        "left join users u on u.user_id = users_roles.users_user_id " +
                        "where username=?;");
    }
    
    @Bean
    public PasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    public void customize(WebSecurity web) {}
    
}