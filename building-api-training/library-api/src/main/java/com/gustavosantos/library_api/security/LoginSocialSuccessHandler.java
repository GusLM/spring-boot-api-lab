package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oAuth2AuthenticationToken.getPrincipal();

        String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        String email = oauth2User.getAttribute("email");

        if (email == null || email.isBlank()) {
            throw new ServletException("OAuth2 provider did not return an email.");
        }

        String providerId = getProviderId(provider, oauth2User);
        
        if (providerId == null || providerId.isBlank()) {
            throw new ServletException("OAuth2 provider did not return a provider id.");
        }

        User user = userService.findOrCreateSocialUser(email, provider, providerId);

        Authentication customAuthentication = new CustomAuthentication(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, customAuthentication);
    }

    private String getProviderId(String provider, OAuth2User oauth2User) {
        return switch (provider) {
            case "google" -> oauth2User.getAttribute("sub");
            case "github" -> String.valueOf(Objects.requireNonNull(oauth2User.getAttribute("id")));
            default -> oauth2User.getName();
        };
    }
}

