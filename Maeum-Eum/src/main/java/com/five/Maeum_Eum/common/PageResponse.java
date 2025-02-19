package com.five.Maeum_Eum.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private int totalPages;
    private long totalElements;
    private int size;
    private List<T> content;
    private boolean first;
    private boolean last;
    private String address;

    public static <T> PageResponseBuilder<T> builderFor(Class<T> clazz) {
        return (PageResponseBuilder<T>) new PageResponseBuilder<>();
    }
}
