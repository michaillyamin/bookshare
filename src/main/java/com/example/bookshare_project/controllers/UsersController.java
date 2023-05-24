package com.example.bookshare_project.controllers;

import com.example.bookshare_project.models.dto.UserDto;
import com.example.bookshare_project.models.entities.User;
import com.example.bookshare_project.models.mappers.UserMapper;
import com.example.bookshare_project.services.UserService;
import com.example.bookshare_project.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @GetMapping()
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userMapper.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        UserDto userDto = userMapper.userToUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                              BindingResult bindingResult,
                                              Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDto, principal);
        UserDto updatedUser = userMapper.userToUserDto(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
