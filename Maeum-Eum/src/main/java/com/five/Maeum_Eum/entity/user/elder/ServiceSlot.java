package com.five.Maeum_Eum.entity.user.elder;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_slot")
public class ServiceSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long serviceSlotId;

    @ManyToOne
    @JoinColumn(name = "elder_id", nullable = false)
    private Elder elder;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek serviceSlotDay;

    @Column(nullable = false)
    private LocalTime serviceSlotStart;

    @Column(nullable = false)
    private LocalTime serviceSlotEnd;
}