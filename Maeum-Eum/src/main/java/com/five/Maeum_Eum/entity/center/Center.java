package com.five.Maeum_Eum.entity.center;

import com.five.Maeum_Eum.entity.manager.Manager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "center")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manager> contents = new ArrayList<>();
}
