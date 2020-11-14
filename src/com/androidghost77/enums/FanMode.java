package com.androidghost77.enums;

public enum FanMode {
    LOW(40),
    MIDDLE(60),
    HIGH(80),
    AUTO(102);

    private int fanValue;

    FanMode(int fanValue) {
        this.fanValue = fanValue;
    }

    public int getFanValue() {
        return fanValue;
    }
}
