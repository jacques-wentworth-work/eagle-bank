package com.eaglebank.service.user;

import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    UserResponse get(String id);
    UserResponse update(String id, UserUpdateRequest request);
    void delete(String id);
}
