package com.marcusantosproject.backend.enums;


public enum Role {

    CLIENT(1),
    ADMIN(2);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role valueOf(int code) {
        for (Role value : Role.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new IllegalArgumentException("Invalid Role code");
    }
}
