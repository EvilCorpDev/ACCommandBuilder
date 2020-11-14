package com.androidghost77.model;

import com.androidghost77.enums.FanMode;
import com.androidghost77.enums.OperationType;

public class ACCommand {

    private static final int CMD_BODY_LENGTH = 24;

    private boolean boot = true;
    private int temperature;
    private OperationType operationType;
    private FanMode fanMode;
    private boolean turboMode;
    private boolean ecoMode;
    private byte correlationId;

    public ACCommand setBoot(boolean boot) {
        this.boot = boot;
        return this;
    }

    public ACCommand setTemperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public ACCommand setOperationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public ACCommand setFanMode(FanMode fanMode) {
        this.fanMode = fanMode;
        return this;
    }

    public ACCommand setTurboMode(boolean turboMode) {
        if (turboMode && this.ecoMode) {
            throw new IllegalArgumentException("Can't set both turbo and eco mode to true");
        }
        this.turboMode = turboMode;
        return this;
    }

    public ACCommand setEcoMode(boolean ecoMode) {
        if (ecoMode && this.turboMode) {
            throw new IllegalArgumentException("Can't set both eco and turbo mode to true");
        }
        this.ecoMode = ecoMode;
        return this;
    }

    public ACCommand setCorrelationId(byte correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public int[] toBytes() {
        int[] bytes = new int[CMD_BODY_LENGTH];

        bytes[0] = 64; // Don't know what this mean, some start byte;
        bytes[1] = boot ? 67 : 66;
        bytes[2] = operationType.getTypeValue() + temperature;
        bytes[3] = fanMode.getFanValue();
        bytes[4] = 127;
        bytes[5] = 127;
        bytes[6] = 0;
        bytes[7] = 48; //relates to wind direction
        bytes[8] = turboMode ? 32 : 0;
        bytes[9] = ecoMode ? 128 : 0;
        bytes[10] = turboMode ? 2 : 0;
        bytes[11] = 0;
        bytes[12] = 0;
        bytes[13] = 0;
        bytes[14] = 0;
        bytes[15] = 0;
        bytes[16] = 0;
        bytes[17] = 0;
        bytes[18] = 0;
        bytes[19] = 30;
        bytes[20] = 0;
        bytes[21] = 0;
        bytes[22] = 0;
        bytes[23] = correlationId;

        return bytes;
    }
}
