package br.com.deveficiente.desafiomercadolivreauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     *nao temos mais a necessidade de criar user em memoria. Pois iremos usar do BD
     * além do UserDetailsService, pois já estamos espondo o nosso no jpaDetailsService
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("thiago")
//                .password(encoder().encode("123"))
//                .roles("ADMIN")
//                .and()
//                .withUser("lucas")
//                .password(encoder().encode("321"))
//                .roles("USER");
//    }



    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        return super.userDetailsService();
//    }

}
