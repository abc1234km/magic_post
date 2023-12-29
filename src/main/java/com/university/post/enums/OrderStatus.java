package com.university.post.enums;

public interface OrderStatus {
    Integer NEW_ORDER = 1;

    Integer PROCESSING_ORDER = 2;

    Integer CREATE_SEND_ORDER = 3;

    Integer CONFIRM_SEND = 4;

    Integer CONFIRM_RECEIVER = 5;

    Integer SUCCESS_DELIVERY = 6;

    Integer UNSUCESS_DELIVERY = 7;

    Integer COMPLETED_ORDER = 8;
}
