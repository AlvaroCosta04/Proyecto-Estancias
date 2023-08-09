package com.estancias.Estancias;

import com.egg.sp.exceptions.ServicesException;
import com.estancias.Estancias.services.UserService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/booking-reserve").authenticated()
                .antMatchers("/booking-reserve/booking-*").authenticated()
                .antMatchers("/css/*", "/js/*", "/img/*", "/**").
                permitAll()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().csrf()
                .disable();
    }*/
        http.authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/booking-reserve").authenticated()
                .antMatchers("/booking-reserve/booking-*").authenticated()
                .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
    }

    /*public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    protected MediaType getMediaTypeForResource(Resource resource) {
                        return MediaType.valueOf("text/css");
                    }
                });
    }*/
}
