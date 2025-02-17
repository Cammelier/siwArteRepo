package it.uniroma3.siw.configuration;

import static it.uniroma3.siw.model.Credenziali.ADMIN_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .authoritiesByUsernameQuery("SELECT username, ruolo from credenziali WHERE username=?")
            .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credenziali WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()  // Disabilitare CSRF per testare
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/", "/login", "/index", "/opere", "/artisti","/curatori","/register", "/css/**", "/images/**","/static/uploads/**", "/uploads/**", "/favicon.ico", "/dettagliArt/**", "/dettagliOpera/**","/dettagliCur/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/register", "/login","/static/uploads/artisti/**", "/uploads/artisti/**","/static/uploads/opere/**", "/uploads/opere/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/admin/**","/static/uploads/**", "/uploads/**").hasAnyAuthority(ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, "/admin/**","/static/uploads/**", "/uploads/**").hasAnyAuthority(ADMIN_ROLE)
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/success", true)
            .failureUrl("/login?error=true")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .clearAuthentication(true).permitAll();

        return httpSecurity.build();
    }
}
