package br.com.deveficiente.desafiomercadolivreauth.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory factory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("desafio-mercado-livre")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("refresh_token","password")
                .scopes("write", "read")
                .accessTokenValiditySeconds(60 * 60 * 6)
                .refreshTokenValiditySeconds(60 * 24 * 60 * 60)//refresh_token de 60 dias
                .and()



                .withClient("teste")
                .secret(passwordEncoder.encode("123"))
                .authorizedGrantTypes("password")
                .scopes("write", "read")
           .accessTokenValiditySeconds(60 * 60 * 6) //dura????o de 6 horas



        .and()
                .withClient("faturamento")
                .secret(passwordEncoder.encode("faturamento123"))
                .authorizedGrantTypes("client_credentials")
                .scopes("read","write")


                .and()
                .withClient("mercado-livre-code")
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes("authorization_code") //poderia ter refresh_tokens tmb
                .scopes("read","write")
                .redirectUris("http://aplicacao-cliente")//posso ter mais de uma URL



                .and()
                .withClient("mercado-implicit")
                .authorizedGrantTypes("implicit") //nao funciona com refresh token
                .scopes("write","read")
                .redirectUris("http://client");


    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .tokenGranter(tokenGranter(endpoints))
                .tokenStore(tokenStore());

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }

    private TokenStore tokenStore(){
        return new RedisTokenStore(factory);
    }


}
