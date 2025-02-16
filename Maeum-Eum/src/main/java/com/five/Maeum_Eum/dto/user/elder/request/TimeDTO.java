package com.five.Maeum_Eum.dto.user.elder.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeDTO {
    private LocalTime start;
    private LocalTime end;
}
