package com.eaglebank.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String line1;

    private String line2;

    private String line3;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String county;

    @Column(nullable = false)
    private String postcode;
}
