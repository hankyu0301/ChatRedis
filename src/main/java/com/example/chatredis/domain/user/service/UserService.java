package com.example.chatredis.domain.user.service;

import com.example.chatredis.domain.user.dto.request.UserCreateRequest;
import com.example.chatredis.domain.user.dto.response.UserCreateResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDeleteResponseDto;
import com.example.chatredis.domain.user.dto.response.UserDto;
import com.example.chatredis.domain.user.dto.request.UserUpdateRequest;
import com.example.chatredis.domain.user.entity.User;
import com.example.chatredis.domain.user.repository.UserRepository;
import com.example.chatredis.global.exception.CustomException;
import com.example.chatredis.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateResponseDto create(UserCreateRequest request) {
        validateSignUpRequest(request);
        User user = createUserFromRequest(request);
        return new UserCreateResponseDto(userRepository.save(user).getId());
    }

    @Transactional(readOnly = true)
    public UserDto findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        return UserDto.toDto(user);
    }

    @Transactional
    public UserDto update(Long id, UserUpdateRequest req) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        validateUserUpdateRequest(req);
        user.update(req);
        return UserDto.toDto(user);
    }

    @Transactional
    public UserDeleteResponseDto delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
        userRepository.delete(user);
        return new UserDeleteResponseDto(user.getId());
    }

    private void validateUserUpdateRequest(UserUpdateRequest req) {
        if(userRepository.existsByNickname(req.getNickname())) {
            throw new CustomException(ExceptionCode.DUPLICATE_NICKNAME);
        }
    }

    private void validateSignUpRequest(UserCreateRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ExceptionCode.DUPLICATE_EMAIL);
        }
        if(userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ExceptionCode.DUPLICATE_NICKNAME);
        }
    }

    private User createUserFromRequest(UserCreateRequest request) {
        return new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getUsername(),
                request.getNickname());
    }
}
