package com.gustavosantos.library_api.security;

import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assert userDetails != null;
        String login = userDetails.getUsername();
        return userService.findByLogin(login);
    }
}
