package com.androidghost77;

import com.androidghost77.enums.FanMode;
import com.androidghost77.enums.OperationType;
import com.androidghost77.model.ACCommand;

import java.util.Arrays;
import java.util.stream.IntStream;

public class App {

    private static final int[] CRC8_854_TABLE = {0, 94, 188, 226, 97, 63, 221, 131, 194, 156, 126, 32, 163, 253, 31, 65, 157, 195, 33, 127, 252, 162, 64, 30, 95, 1, 227, 189, 62, 96, 130, 220, 35, 125, 159, 193, 66, 28, 254, 160, 225, 191, 93, 3, 128, 222, 60, 98, 190, 224, 2, 92, 223, 129, 99, 61, 124, 34, 192, 158, 29, 67, 161, 255, 70, 24, 250, 164, 39, 121, 155, 197, 132, 218, 56, 102, 229, 187, 89, 7, 219, 133, 103, 57, 186, 228, 6, 88, 25, 71, 165, 251, 120, 38, 196, 154, 101, 59, 217, 135, 4, 90, 184, 230, 167, 249, 27, 69, 198, 152, 122, 36, 248, 166, 68, 26, 153, 199, 37, 123, 58, 100, 134, 216, 91, 5, 231, 185, 140, 210, 48, 110, 237, 179, 81, 15, 78, 16, 242, 172, 47, 113, 147, 205, 17, 79, 173, 243, 112, 46, 204, 146, 211, 141, 111, 49, 178, 236, 14, 80, 175, 241, 19, 77, 206, 144, 114, 44, 109, 51, 209, 143, 12, 82, 176, 238, 50, 108, 142, 208, 83, 13, 239, 177, 240, 174, 76, 18, 145, 207, 45, 115, 202, 148, 118, 40, 171, 245, 23, 73, 8, 86, 180, 234, 105, 55, 213, 139, 87, 9, 235, 181, 54, 104, 138, 212, 149, 203, 41, 119, 244, 170, 72, 22, 233, 183, 85, 11, 136, 214, 52, 106, 43, 117, 151, 201, 74, 20, 246, 168, 116, 42, 200, 150, 21, 75, 169, 247, 182, 232, 10, 84, 215, 137, 107, 53};
    private static final byte[] CMD_HEADER = {-86, 35, -84, 0, 0, 0, 0, 0, 3, 2};

    private static final int CMD_BODY_LENGTH = 24;
    private static final int CMD_LENGTH = 36;

    public static void main(String[] args) {

        int[] acCommand = new ACCommand().setCorrelationId((byte)52)
                .setBoot(true)
                .setFanMode(FanMode.LOW)
                .setOperationType(OperationType.IDLE)
                .setTemperature(30)
                .setMoveWingsVertically(false)
                .toBytes();

        System.out.println(Arrays.toString(createUnsignedCommand(acCommand, CMD_LENGTH)));
    }

    public static int[] createUnsignedCommand(int[] cmdBody, int outputLength) {
        int[] resultUnsignedBytes;
        byte[] resultBytes = new byte[outputLength];

        byte[] cmdByteBody = new byte[cmdBody.length];
        IntStream.range(0, cmdByteBody.length)
                .forEach(i -> cmdByteBody[i] = convertUnsignedToSigned(cmdBody[i]));

        System.arraycopy(CMD_HEADER, 0, resultBytes, 0, CMD_HEADER.length);
        System.arraycopy(cmdByteBody, 0, resultBytes, CMD_HEADER.length, cmdByteBody.length);
        resultBytes[CMD_HEADER.length + cmdBody.length] = (byte)getCRC(cmdByteBody, CMD_BODY_LENGTH);
        resultBytes[outputLength - 1] = makeSum(resultBytes, outputLength - 1);

        resultUnsignedBytes = IntStream.range(0, outputLength)
                .map(i -> convertSignedToUnsigned(resultBytes[i]))
                .toArray();

        return resultUnsignedBytes;
    }

    private static int convertSignedToUnsigned(byte input) {
        return input < 0 ? 256 + input : input;
    }

    private static byte convertUnsignedToSigned(int input) {
        return (byte) input; // same as input > 127 ? input - 256 : input;
    }

    private static int getCRC(byte[] inputData, int length) {
        int var3 = 0;
        if (inputData != null) {
            if (length == 0) {
                return 0;
            } else {
                int var2;
                for(var2 = 0; var3 < length; ++var3) {
                    int var4 = var2 ^ inputData[var3];
                    var2 = var4;
                    if (var4 > 256) {
                        var2 = var4 - 256;
                    }

                    var4 = var2;
                    if (var2 < 0) {
                        var4 = var2 + 256;
                    }

                    var2 = CRC8_854_TABLE[var4];
                }

                return var2;
            }
        } else {
            return 0;
        }
    }

    private static byte makeSum(byte[] inputData, int length) {
        byte accumulator = 0;

        for(int var3 = 1; var3 < length; ++var3) {
            accumulator += inputData[var3];
        }

        return (byte)(255 - accumulator % 256 + 1);
    }
}
