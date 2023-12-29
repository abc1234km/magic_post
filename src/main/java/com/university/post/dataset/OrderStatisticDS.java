package com.university.post.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticDS {
    private String typeStatistic;

    private String typeResource;

    private Integer statusComplete;

    private String typePoint;

    private String pointId;
}
