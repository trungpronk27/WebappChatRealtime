package com.messages.config;

import com.messages.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new iUserService();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    // Token stored in Memory (Of Web Server).
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
//                .antMatchers("/login").permitAll()      // không check authen trên link "login"
                .antMatchers(
                        "/sign-up**",
                        "/forgot**",
                        "/reset**",
                        "/js/**",
                        "/css/**",
                        "/img/**").permitAll()

                .antMatchers("/").hasRole("MEMBER")     // chỉ cho phép các user có GrantedAuthority là ROLE_roleName mới được phép truy cập
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()                       // check authen trên tất cả các link khác
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/chat") // sau khi login thành công
                .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
                .rememberMe()
                .tokenValiditySeconds(604800)//luu trong 7 ngay
                .userDetailsService(userService);

//           .rememberMe()
//                .key("rem-me-key")
//                .rememberMeParameter("remember") // it is name of checkbox at login page
//                .rememberMeCookieName("rememberlogin") // it is name of the cookie
//                .tokenValiditySeconds(100); // remember for number of seconds
    }
}
