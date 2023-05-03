package com.crm.controller;

import com.crm.dto.LoginDto;
import com.crm.service.AuthService;
import com.crm.common.util.ErrorHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Object login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            String token = authService.login(loginDto);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Username or password is not correct!", e);
            return new ResponseEntity<>("Username or password is not correct!", HttpStatus.BAD_REQUEST);
        }
    }

}
