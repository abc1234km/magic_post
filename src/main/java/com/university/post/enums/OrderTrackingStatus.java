package com.university.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderTrackingStatus {
    NEW_ORDER(1, "Tạo đơn hàng thành công"),

    PROCESSING_ORDER(2, "Đơn hàng đang trong quá trình xử lý"),

    LEAVE_TRANSACTION_POINT(2, "Đơn hàng đã rời điểm giao dịch"),

    TO_TRANSACTION_POINT(3, "Đơn hàng đã đến điểm giao dịch: "),

    LEAVE_GATHERING_POINT(4, "Đơn hàng đã rời điểm tập kết"),

    TO_GATHERING_POINT(5, "Đơn hàng đã đến điểm tập kết: "),

    DELIVERY_RECEIVER(6, "Đơn hàng đang được giao đến người nhận"),

    SUCCESS_RECEIVER(7, "Đơn hàng đã được giao thành công"),

    UNSUCCESS_RECEIVER(8, "Đơn hàng không được giao thành công");

    private final Integer status;

    private final String message;
}
