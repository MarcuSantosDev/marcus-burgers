package com.marcusantosproject.backend.enums;

public enum OrderStatus {
    PENDING(1),
    PREPARING(2),
    READY(3),
    DELIVERED(4),
    CANCELLED(5);

    private int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderStatus valueOf(int code){
        for(OrderStatus value: OrderStatus.values()){
            if (value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid Order Status code");
    }
}
