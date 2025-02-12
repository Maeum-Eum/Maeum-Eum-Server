package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.entity.user.elder.SavedElders;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caregiver")
@Getter
public class Caregiver {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long caregiverId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    // 토큰 분리 요구
    @Column(nullable = false)
    private String address;

    // 매칭 상태
    @Column(nullable = false)
    private JobState jobState;

    @Column(nullable = false)
    private boolean isJobOpen;

    @Column(nullable = false)
    private boolean hasCaregiverCertificate;

    // 자격증
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL)
    @Size(max = 3)
    private List<Certificate> certificates = new ArrayList<>();

    // 이력서
    @OneToOne(mappedBy = "caregiver", cascade = CascadeType.ALL)
    private Resume resume;

    // 저장한 어르신
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL)
    private List<SavedElders> savedElders = new ArrayList<>();

    // 관리자 연락
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerContact> managerContact = new ArrayList<>();

    // 매칭 상태
    public enum JobState {
        IDLE, MATCHING, MATCHED
    }
}
