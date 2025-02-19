package com.five.Maeum_Eum.dto.user.elder.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElderCreateDTO {

    private Long elderId;
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
