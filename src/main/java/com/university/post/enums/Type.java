package com.university.post.enums;

public interface Type {
    public static final String SENDER_CUSTOMER = "SC";

    public static final Integer RECEIVER_CUSTOMER = 5;

    public static final Integer DOCUMENT_ORDER = 6;

    public static final Integer PRODUCT_ORDER = 7;

    public static final String GATHERING_POINT_LOCATION = "GP";

    public static final String TRANSACTION_POINT_LOCATION = "TP";

    public static final String RECEIVER_CUSTOMER_LOCATION = "RC";

    public static final Integer FORMAT_ID = 10000;

    public static final Integer ORDER_ALL = 0;

    public static final Integer ORDER_INCOMING = 1;

    public static final Integer ORDER_PROCESSING = 2;

    public static final Integer ORDER_LEAVE = 3;

    public static final Integer CUSTOMER_ORDER = 1;

    public static final Integer GATHERING_ORDER = 2;

    public static final Integer TRANSACTION_ORDER = 3;

    public static final Integer DELIVERY_ORDER = 4;

    public static final String PREFIX_ORDER = "HPD";

    public static final String POSTFIX_ORDER = "VN";
}
