package com.androidghost77.enums;

public enum OperationType {
    COOL(48),
    DRY(80),
    HEAT(112),
    AUTO(16);

    private int typeValue;

    OperationType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return this.typeValue;
    }
}
