package com.five.Maeum_Eum.dto.user.elder.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElderCreateDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String gender;
    @NotNull
    private LocalDate birth;
    @NotNull
    private Integer rank;
    @NotBlank
    private String address;
    private TimeDTO mon;
    private TimeDTO tue;
    private TimeDTO wed;
    private TimeDTO thu;
    private TimeDTO fri;
    private TimeDTO sat;
    private TimeDTO sun;
    private List<String> meal;
    private List<String> toileting;
    private List<String> mobility;
    private List<String> daily;
    @NotBlank
    private String family;
    @NotBlank
    private String pet;
    private Boolean negotiable;
    private Integer wage;

}
