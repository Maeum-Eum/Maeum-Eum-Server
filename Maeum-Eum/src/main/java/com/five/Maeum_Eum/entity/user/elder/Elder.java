package com.five.Maeum_Eum.entity.user.elder;

import com.five.Maeum_Eum.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
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

    @Column(nullable = false)
    private int elderRank;

    @Column(nullable = false)
    private Boolean elder_pet;

    @Column(nullable = false)
    private Boolean elder_family;


    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private String meal;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private String toileting;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private String mobility;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private String daily;


}
