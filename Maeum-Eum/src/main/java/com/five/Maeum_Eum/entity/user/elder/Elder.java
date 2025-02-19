package com.five.Maeum_Eum.entity.user.elder;

import com.five.Maeum_Eum.converter.GenericListConverter;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "elder")
public class Elder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long elderId;

    @Column(nullable = false)
    private String elderName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate elderBirth;

    @Column(nullable = false)
    private String elderAddress;

    @Setter
    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(nullable = false)
    private int elderRank;

    @Column(nullable = false)
    private Boolean elder_pet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ElderFamily elder_family;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column
    private boolean negotiable;

    // 서비스 요구 수준
    private boolean dailyFilter1;
    private boolean dailyFilter2;
    private boolean dailyFilter3;
    private boolean dailyFilter4;
    private boolean dailyFilter5;
    private boolean dailyFilter6;
    private Integer mealLevel;
    private Integer toiletingLevel;
    private Integer mobilityLevel;
    private Integer dailyLevel;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> meal;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> toileting;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> mobility;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> daily;

    @Column(nullable = false)
    private int wage; // 시급만

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerContact> managerContacts = new ArrayList<>();

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerBookmark> managerBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "elder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceSlot> serviceSlots = new ArrayList<>();

    public void setServiceSlots(List<ServiceSlot> serviceSlots) {
        this.serviceSlots = serviceSlots;
    }

    public void update(String gender, LocalDate elderBirth, String elderAddress, Integer elderRank, Boolean negotiable, Integer wage, Manager manager, List<String> meal, List<String> toileting, List<String> mobility, List<String> daily, ElderFamily elder_family, Boolean elder_pet, Point location, Integer mealLevel, Integer toiletingLevel, Integer mobilityLevel, Integer dailyLevel) {
        this.gender = gender == null ? this.gender : gender ;
        this.elderBirth = elderBirth == null ? this.elderBirth : elderBirth ;
        this.elderAddress = elderAddress == null ? this.elderAddress : elderAddress ;
        this.elderRank = elderRank == null ? this.elderRank : elderRank;
        this.negotiable = negotiable == null ? this.negotiable : negotiable ;
        this.wage = wage == null ? this.wage : wage ;
        this.manager = manager == null ? this.manager : manager ;
        this.meal = meal == null ? this.meal : meal ;
        this.toileting = toileting == null ? this.toileting : toileting ;
        this.mobility = mobility == null ? this.mobility : mobility ;
        this.daily = daily == null ? this.daily : daily ;
        this.elder_family = elder_family == null ? this.elder_family : elder_family ;
        this.elder_pet = elder_pet == null ? this.elder_pet : elder_pet ;
        this.location = location == null ? this.location : location ;
        this.mealLevel = mealLevel == null ? this.mealLevel : mealLevel ;
        this.toiletingLevel = toiletingLevel == null ? this.toiletingLevel : toiletingLevel ;
        this.mobilityLevel = mobilityLevel == null ? this.mobilityLevel : mobilityLevel ;
        this.dailyLevel = dailyLevel == null ? this.dailyLevel : dailyLevel ;
    }
}
