package com.five.Maeum_Eum.entity.user.manager;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerBookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "elder_id")
    private Elder elder;


}
