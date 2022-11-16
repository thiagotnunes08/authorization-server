package br.com.deveficiente.desafiomercadolivreauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private JwtKeyStoreProperties jwtKeyStoreProperties;
    @Autowired
    private DataSource dataSource;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.jdbc(dataSource);
    }

//        clients.inMemory()
//                .withClient("desafio-mercado-livre")
//                .secret(passwordEncoder.encode("123"))
//                .authorizedGrantTypes("refresh_token","password")
//                    .scopes("WRITE", "READ")
//                .accessTokenValiditySeconds(60 * 60 * 6)
//                .refreshTokenValiditySeconds(60 * 24 * 60 * 60)//refresh_token de 60 dias
//                .and()
//
//
//
//                .withClient("teste")
//                .secret(passwordEncoder.encode("123"))
//                .authorizedGrantTypes("password")
//                .scopes("WRITE", "READ")
//           .accessTokenValiditySeconds(60 * 60 * 6) //duração de 6 horas
//
//
//
//        .and()
//                .withClient("faturamento")
//                .secret(passwordEncoder.encode("faturamento123"))
//                .authorizedGrantTypes("client_credentials")
//                .scopes("READ","WRITE")
//
//
//                .and()
//                .withClient("mercado-livre-code")
//                .secret(passwordEncoder.encode(""))
//                .authorizedGrantTypes("authorization_code") //poderia ter refresh_tokens tmb
//                .scopes("READ","WRITE")
//                .redirectUris("http://aplicacao-cliente")//posso ter mais de uma URL
//
//
//
//                .and()
//                .withClient("mercado-implicit")
//                .authorizedGrantTypes("implicit") //nao funciona com refresh token
//                .scopes("WRITE","READ")
//                .redirectUris("http://client");
//
//    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        var enhancer = new TokenEnhancerChain();
        enhancer.setTokenEnhancers(List.of(new JwtCustomClaimsTokensEnhacer(),jwtAccessTokenConverter()));

        endpoints.authenticationManager(authenticationManager)
          //   .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(enhancer)
                .approvalStore(approvalStore(endpoints.getTokenStore()))
                .tokenGranter(tokenGranter(endpoints));
    }
    //granula acessos: read/write
    private ApprovalStore approvalStore(TokenStore tokenStore){
        TokenApprovalStore approvalStore = new TokenApprovalStore();
         approvalStore.setTokenStore(tokenStore);
         return approvalStore;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
      //  jwtAccessTokenConverter.setSigningKey("DHSAUIHDuidhuiHDIUSAHDUIhduISAHIUDAIUSDHUihduiasduiah");

        ClassPathResource jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
        String keyStorePassword = jwtKeyStoreProperties.getPassword();
        String keyPairAlias = jwtKeyStoreProperties.getKeypairAlias();

        KeyStoreKeyFactory keyStoreFactory = new KeyStoreKeyFactory(jksResource,keyStorePassword.toCharArray());

        KeyPair keyPair = keyStoreFactory.getKeyPair(keyPairAlias);

        jwtAccessTokenConverter.setKeyPair(keyPair);


        return jwtAccessTokenConverter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()")
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





}
