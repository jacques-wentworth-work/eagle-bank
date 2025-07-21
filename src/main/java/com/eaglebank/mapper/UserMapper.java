package com.eaglebank.mapper;

import com.eaglebank.entity.Address;
import com.eaglebank.entity.User;
import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;

public class UserMapper {

    public static User toEntity(String id, UserCreateRequest resource) {
        return User.builder()
                .id(id)
                .name(resource.name())
                .email(resource.email())
                .phoneNumber(resource.phoneNumber())
                .address(toEntity(resource.address()))
                .build();
    }

    private static Address toEntity(AddressResource resource) {
        return Address.builder()
                .line1(resource.line1())
                .line2(resource.line2())
                .line3(resource.line3())
                .town(resource.town())
                .county(resource.county())
                .postcode(resource.postcode())
                .build();
    }

    public static UserResponse toResponse(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .address(toResponse(entity.getAddress()))
                .build();
    }

    private static AddressResource toResponse(Address entity) {
        return AddressResource.builder()
                .line1(entity.getLine1())
                .line2(entity.getLine2())
                .line3(entity.getLine3())
                .town(entity.getTown())
                .county(entity.getCounty())
                .postcode(entity.getPostcode())
                .build();
    }
}
