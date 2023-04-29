package com.crm.service;

import com.crm.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
