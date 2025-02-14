package com.five.Maeum_Eum.dto.user.manager.response;

import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToCaregiverDTO {
    private Long contactId;
    private String center;
    private String title;
    private LocalDateTime createdAt;
    private Integer wage;
    private Boolean negotiable;

}
