package com.marcusantosproject.backend.enums;

public enum OrderStatus {
    PENDING(1),
    PREPARING(2),
    READY(3),
    DELIVERED(4),
    CANCELLED(5);

    private final int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING   -> next == PREPARING || next == CANCELLED;
            case PREPARING -> next == READY     || next == CANCELLED;
            case READY     -> next == DELIVERED || next == CANCELLED;
            case DELIVERED, CANCELLED -> false; // estados finais
        };
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid Order Status code");
    }
}
