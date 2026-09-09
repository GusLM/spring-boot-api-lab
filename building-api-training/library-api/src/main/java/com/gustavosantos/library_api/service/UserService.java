package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.user.UserRequestDTO;
import com.gustavosantos.library_api.mappers.UserMapper;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.model.UserRole;
import com.gustavosantos.library_api.repository.UserRepository;
import com.gustavosantos.library_api.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // Mesmo PasswordEncoder usado na autenticação; garante que a senha salva seja comparável depois.
    private final PasswordEncoder encoder;
    private final UserMapper mapper;
    private final UserValidator validator;

    @Transactional
    public User save(UserRequestDTO dto) {
        User user = mapper.toEntity(dto);

        validator.checkIfAlreadyExists(user);

        if (user.getPassword() == null) {
            return userRepository.save(user);
        }

        // Nunca salva senha em texto puro: o BCrypt transforma a senha em hash antes de persistir.
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        // Ponto central de busca usado tanto pela autenticação quanto por services que precisam do usuário logado.
        return userRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public User findOrCreateSocialUser(String email, String provider, String providerId) {
        User user = userRepository.findByAuthProviderAndProviderId(provider, providerId);

        if (user != null) {
            return user;
        }

        user = new User();
        user.setLogin(createUniqueLoginWithEmail(email));
        user.setEmail(email);
        user.setPassword(null);
        user.setAuthProvider(provider);
        user.setProviderId(providerId);
        user.setRoles(List.of(UserRole.USER));

        return userRepository.save(user);
    }

    private String createUniqueLoginWithEmail(String email) {
        String baseLogin = email.substring(0, email.indexOf("@"));
        baseLogin = baseLogin.toLowerCase();

        for (int attempt = 0; attempt < 10; attempt++) {
            String candidate;

            if (attempt == 0) {
                candidate = truncate(baseLogin, 30);
            } else {
                String suffix = "-" + UUID.randomUUID().toString().substring(0, 4);
                candidate = truncate(baseLogin, 30 - suffix.length()) + suffix;
            }

            if (!userRepository.existsByLogin(candidate)) {
                return candidate;
            }
        }

        return "user-" + UUID.randomUUID().toString().substring(0, 25);
    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength);
    }
}
