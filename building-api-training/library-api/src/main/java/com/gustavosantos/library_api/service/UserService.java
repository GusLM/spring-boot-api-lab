package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.user.UserRequestDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.mappers.UserMapper;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.repository.UserRepository;
import com.gustavosantos.library_api.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
}
