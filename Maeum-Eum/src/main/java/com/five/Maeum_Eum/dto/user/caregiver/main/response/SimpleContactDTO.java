package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleContactDTO {
    private Long contactId;
    private String center;
    private String title;
    private LocalDateTime createdAt;
    private Integer wage;
    private Boolean negotiable;
    private Boolean bookmarked;

}
