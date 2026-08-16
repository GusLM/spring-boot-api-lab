package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void checkIfAlreadyExists(User user) {
        checkIfAlreadyExists(user.getId(), user.getLogin());
    }

    public void checkIfAlreadyExists(Integer currentUserId, String userLogin) {
        if (existUserRegistered(currentUserId, userLogin)) {
            throw new DuplicateRecordException("User login already exist");
        }
    }

    private boolean existUserRegistered(Integer currentUserId, String userLogin) {
        Optional<User> possibleDuplicated = userRepository.findByLogin(userLogin);

        if (possibleDuplicated.isEmpty()) {
            return false;
        }

        if (currentUserId == null) {
            return true;
        }

        User registeredUser = possibleDuplicated.get();

        return !registeredUser.getId().equals(currentUserId);
    }

}
