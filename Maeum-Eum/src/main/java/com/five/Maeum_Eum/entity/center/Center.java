package com.five.Maeum_Eum.entity.center;

import com.five.Maeum_Eum.dto.center.request.ModifyCenterReq;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

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

    @Column(length = 3)
    private String field8;

    @Column(nullable = false, length = 11)
    private String centerCode;

    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(nullable = false)
    private boolean hasCar;

    @Column(length = 1)
    private String finalGrade; // 최종 등급

    @Column(length = 100)
    private String oneLineIntro; // 한 줄 소개

    @OneToMany(mappedBy = "center", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manager> contents = new ArrayList<>();

    public void registerManager(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public void updateOneLineIntro(ModifyCenterReq modifyReq){
        this.oneLineIntro = modifyReq.getOneLineIntro();
    }
}
