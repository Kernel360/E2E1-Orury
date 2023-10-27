package com.kernel360.orury.global.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Api<T> {
    private T body;
    private Pagination pagination;
}
