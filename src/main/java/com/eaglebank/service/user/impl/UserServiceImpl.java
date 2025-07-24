package com.eaglebank.service.user.impl;

import com.eaglebank.entity.Address;
import com.eaglebank.entity.User;
import com.eaglebank.exception.DuplicateEmailException;
import com.eaglebank.exception.UserNotFoundException;
import com.eaglebank.mapper.UserMapper;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.resource.AddressResource;
import com.eaglebank.resource.UserCreateRequest;
import com.eaglebank.resource.UserResponse;
import com.eaglebank.resource.UserUpdateRequest;
import com.eaglebank.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private static final String ID_PREFIX = "usr-";

    @Override
    public UserResponse create(UserCreateRequest request) {
        duplicateEmailValidation(request.email());

        String id = generateUserId(request.name());
        return UserMapper.toResponse(repository.save(UserMapper.toEntity(id, request)));
    }

    @Override
    public UserResponse get(String id) {
        return repository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(String.format("User (%s) not found", id)));
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        setEntity(request, user);
        return UserMapper.toResponse(repository.save(user));
    }

    private void setEntity(UserUpdateRequest request, User user) {
        if (request.name() != null && !request.name().isEmpty()) {
            user.setName(request.name());
        }

        if (request.phoneNumber() != null && !request.phoneNumber().isEmpty()) {
            user.setPhoneNumber(request.phoneNumber());
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            duplicateEmailValidation(request.email());
            user.setEmail(request.email());
        }

        if (request.address() != null) {
            setAddressEntity(request.address(), user);
        }
    }

    private static void setAddressEntity(AddressResource request, User user) {
        Address userAddress = user.getAddress();
        if (userAddress == null) {
            userAddress = new Address();
        }

        if (request.line1() != null && !request.line1().isEmpty()) {
            userAddress.setLine1(request.line1());
        }

        if (request.line2() != null && !request.line2().isEmpty()) {
            userAddress.setLine2(request.line2());
        }

        if (request.line3() != null && !request.line3().isEmpty()) {
            userAddress.setLine3(request.line3());
        }

        if (request.town() != null && !request.town().isEmpty()) {
            userAddress.setTown(request.town());
        }

        if (request.county() != null && !request.county().isEmpty()) {
            userAddress.setCounty(request.county());
        }

        if (request.postcode() != null && !request.postcode().isEmpty()) {
            userAddress.setPostcode(request.postcode());
        }
    }

    @Override
    public void delete(String id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    private void duplicateEmailValidation(String email) {
        repository.findByEmail(email)
                .ifPresent(u -> {
                    throw new DuplicateEmailException(String.format("Email (%s) already registered", email));
                });
    }

    /**
     * Generate the user's ID from the user name field. Old school 6 digits from user's name, with a
     * numerical counter suffix.
     *
     * @param name The user name
     * @return
     */
    private String generateUserId(String name) {
        String idPrefix = ID_PREFIX + getBaseName(name);
        Integer idCount = getNextIdCount(idPrefix);

        return idPrefix + idCount;
    }

    private static String getBaseName(String name) {
        String[] parts = name.trim().toLowerCase().split("\\s+");
        String base;
        if (parts.length >= 2) {
            String first = parts[0].length() >= 3 ? parts[0].substring(0, 3) : parts[0];
            String second = parts[1].length() >= 3 ? parts[1].substring(0, 3) : parts[1];
            base = first + second;
        } else {
            String cleaned = name.replaceAll("\\s+", "").toLowerCase();
            base = cleaned.length() > 6 ? cleaned.substring(0, 6) : cleaned;
        }
        return base;
    }

    private Integer getNextIdCount(String idPrefix) {
        return repository.findTopByIdStartingWithOrderByIdDesc(idPrefix)
                .map(existing -> {
                    String suffix = existing.getId().substring(idPrefix.length());
                    return suffix.matches("\\d+") ? Integer.parseInt(suffix) + 1 : 1;
                })
                .orElse(1);
    }
}
