package com.five.Maeum_Eum.entity.user.manager;

import com.five.Maeum_Eum.entity.center.Center;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;

    @Column(length = 50)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagerContact> managerContacts = new ArrayList<>();

    @Builder
    public Manager(String name , String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

}
