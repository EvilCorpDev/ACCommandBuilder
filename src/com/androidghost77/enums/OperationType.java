package com.androidghost77.enums;

public enum OperationType {
    IDLE(0), // Only with 28 celsius
    AUTO(16),
    COOL(48),
    DRY(80),
    HEAT(112),
    FAN(144);

    private int typeValue;

    OperationType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return this.typeValue;
    }
}
