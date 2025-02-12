package com.five.Maeum_Eum.entity.user.elder;

import com.five.Maeum_Eum.entity.user.caregiver.CareGiver;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "saved_elders")
public class SavedElders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long savedEldersId;

    @ManyToOne
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;


    @ManyToOne
    @JoinColumn(name = "caregiver_id", nullable = false)
    private CareGiver caregiver;
}
