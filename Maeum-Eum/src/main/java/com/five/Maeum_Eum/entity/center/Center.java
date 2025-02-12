package com.five.Maeum_Eum.entity.center;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "center")
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long centerId;

    @Column(nullable = false, length = 30)
    private String centerName;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false)
    private LocalDate designatedTime;

    @Column(nullable = false)
    private LocalDate installationTime;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false, length = 5)
    private String zipCode;

    @Column(nullable = false, length = 3)
    private String field8;

    @Column(nullable = false, length = 11)
    private String centerCode;
}
