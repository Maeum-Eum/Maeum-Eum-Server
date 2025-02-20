package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.entity.user.elder.SavedElders;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caregiver")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Caregiver {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long caregiverId;

    @Column(nullable = false)
    private String name;

    private String loginId;

    private String password;

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

    // 한줄 소개
    @Column
    private String introduction;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    // 이력서
    @OneToOne(mappedBy = "caregiver", cascade = CascadeType.ALL)
    private Resume resume;

    @Column(nullable = false)
    private boolean isResumeRegistered;

    // 저장한 어르신
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL)
    private List<SavedElders> savedElders = new ArrayList<>();

    // 관리자 연락
    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerContact> managerContact = new ArrayList<>();

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerBookmark> managerBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Apply> applys = new ArrayList<>();

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public void setHasCaregiverCertificate(boolean b) {
        hasCaregiverCertificate = b;
    }

    // 매칭 상태
    public enum JobState {
        IDLE, MATCHING, MATCHED
    }

    @OneToMany(mappedBy = "caregiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> experience;

    public void toggleJobOpenState(){
        this.isJobOpen = !this.isJobOpen;
    }

    public void setResumeRegistered(boolean resumeRegistered) { this.isResumeRegistered = resumeRegistered; }
}
