package com.company.services.user;

import com.company.exception.types.NotAvailableException;
import com.company.exception.types.NotFoundException;
import com.company.models.domain.User;
import com.company.models.dto.SignUpAdminDto;
import com.company.models.dto.SignUpUserDto;
import com.company.models.enums.Authority;
import com.company.models.request.UserRequest;
import com.company.models.response.UserResponse;
import com.company.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse get(int id) {
        var user = userRepo.find(id).orElseThrow(
                () -> new NotFoundException("User Not Found")
        );
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .imgUrl(user.getImgUrl())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .build();
    }

    @Override
    public UserResponse get(String username) {
        var user = userRepo.find(username).orElseThrow(
                () -> new NotFoundException("User Not Found")
        );
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .imgUrl(user.getImgUrl())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .build();
    }

    @Override
    public UserResponse get() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .imgUrl(user.getImgUrl())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .build();
    }

    @Override
    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found")
        );
    }

    @Override
    public void add(SignUpUserDto signUpUserDto) {
        if (userRepo.existsByUsername(signUpUserDto.getUsername())) {
            throw new NotAvailableException("username is already in use");
        }
        userRepo.save(User.builder()
                .name(signUpUserDto.getName())
                .username(signUpUserDto.getUsername())
                .password(passwordEncoder.encode(signUpUserDto.getPassword()))
                .isActivity(true)
                .authority(Authority.USER)
                .build());
    }

    @Override
    public void add(SignUpAdminDto signUpAdminDto) {
        if (userRepo.existsByUsername(signUpAdminDto.getUsername())) {
            throw new NotAvailableException("username is already in use");
        }
        userRepo.save(User.builder()
                .username(signUpAdminDto.getUsername())
                .password(passwordEncoder.encode(signUpAdminDto.getPassword()))
                .isActivity(true)
                .authority(Authority.ADMIN)
                .build());
    }

    @Override
    public void update(UserRequest userRequest) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepo.existsByUsername(userRequest.getUsername()) && !auth.getUsername().equalsIgnoreCase(userRequest.getUsername())) {
            throw new NotAvailableException("username is already in use");
        }
        userRepo.update(User.builder()
                .id(auth.getId())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .imgUrl(userRequest.getImgUrl())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .gender(userRequest.getGender())
                .build());
    }

    @Override
    public void deactive() {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepo.deactive(auth.getId());
    }

    @Override
    public void active(int id) {
        userRepo.active(id);
    }

}
