package br.com.deveficiente.desafiomercadolivreauth.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class JwtCustomClaimsTokensEnhacer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if (authentication.getPrincipal() instanceof AuthUser) {

            AuthUser authUser = (AuthUser) authentication.getPrincipal();

            HashMap<String, Object> informacoes = new HashMap<>();

            informacoes.put("nome", authUser.getFullName());
            informacoes.put("user_id", authUser.getId());

            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;

            oAuth2AccessToken.setAdditionalInformation(informacoes);
        }

        return accessToken;
    }
}
