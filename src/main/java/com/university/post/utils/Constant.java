package com.university.post.utils;

import com.university.post.enums.Type;

import java.time.LocalDate;

public interface Constant {
    Integer CONFIRM_STATUS = 9;

    Integer CREATED_STATUS = 12;

    Integer LEFT_STATUS = 15;

    Integer DELIVERY_STATUS = 18;

    Integer SUCCESS_DELVIERY_STATUS = 21;

    Integer UNSUCCESS_DELIVERY_POINT = 24;

    String TRANSACTION_PREFIX_ID = "TP";

    String GATHERING_PREFIX_ID = "GP";

    Long POINT_PREFIX_ID = 10000000L;

    Integer STAFF_PREFIX_ID = Math.multiplyExact(LocalDate.now().getYear(), Type.FORMAT_ID);

    Integer DEFAULT_PAGE_NUMBER = 1;

    Integer DEFAULT_PAGE_SIZE = 10;
}
