package com.university.post.payload.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatisticPointResponse {
    private LocalDate date;

    private Integer numberOrderIncoming = 0;

    private Integer numberOrderLeave =0;

    public OrderStatisticPointResponse(LocalDate date) {
        this.date = date;
    }
}
