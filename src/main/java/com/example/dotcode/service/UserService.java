package com.example.dotcode.service;

import com.example.dotcode.entity.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(int id, UserDto userDto);

    Integer delete(int id);
}
