package com.university.post.enums;

import com.university.post.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    ORDER_CREATED(Constant.CONFIRM_STATUS, "Tạo đơn hàng thành công"),

    TRANSACTION_POINT_LEFT(Constant.LEFT_STATUS, "Đơn hàng đã rời điểm giao dịch"),

    GATHERING_POINT_ARRIVED(Constant.CONFIRM_STATUS, "Đơn hàng đã đến điểm tập kết"),

    GATHERING_POINT_LEFT(Constant.LEFT_STATUS, "Đơn hàng đã rời điểm tập kết"),

    TRANSACTION_POINT_ARRIVED(Constant.CONFIRM_STATUS, "Đơn hàng đã đến điểm giao dịch"),

    RECEIVER_DELIVERY(Constant.DELIVERY_STATUS, "Đơn hàng đang giao đến người nhận"),

    SUCCESS_DELIVERY(Constant.SUCCESS_DELVIERY_STATUS, "Đơn hàng đã giao thành công"),

    UNSUCESS_DELIVERY(Constant.UNSUCCESS_DELIVERY_POINT, "Đơn hàng giao không thành công");

    private final Integer value;

    private final String message;
}
