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

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(nullable = false)
    private int elderRank;

    @Column(nullable = false)
    private Boolean elder_pet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ElderFamily elder_family;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column
    private boolean negotiable;

    // 서비스 요구 수준
    private int mealLevel;
    private int toiletingLevel;
    private int mobilityLevel;
    private int dailyLevel;

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

}
