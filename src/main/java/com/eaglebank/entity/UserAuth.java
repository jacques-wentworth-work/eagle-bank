package com.eaglebank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserAuth {
    @Id
    private String id;

    @Column(nullable = false)
    private String password;
}
