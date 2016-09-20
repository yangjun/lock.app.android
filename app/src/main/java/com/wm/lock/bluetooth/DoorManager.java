package com.wm.lock.bluetooth;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

public class DoorManager {

    private Context mContext;

    private static final byte[] DoorFeedERROR;
    private static final byte[] DoorFeedOK;

    static {
        byte[] bArr = new byte[5];
        bArr[0] = (byte) 4;
        bArr[1] = (byte) -4;
        bArr[2] = (byte) 1;
        bArr[4] = (byte) 1;
        DoorFeedOK = bArr;

        bArr = new byte[5];
        bArr[0] = (byte) 4;
        bArr[1] = (byte) -4;
        bArr[2] = (byte) 2;
        bArr[4] = (byte) 2;
        DoorFeedERROR = bArr;
    }

    public DoorManager(Context mContext) {
        this.mContext = mContext;
    }

    public byte[] open(String cardNum) throws DoorException {
        final String formatCardNum = format(cardNum);
        final byte[] bytes = HexStringtoHexBytes(formatCardNum);
        return sendcmd((byte) 0x82, (byte) 0x0F, bytes);
    }

    public DoorPackageResult receive(byte[] receives) throws DoorException {
        checkReceive(receives);
        if (receives != null && receives.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(receives.length);
            final int length = receives.length;
            for (int i = 0; i < length; i++) {
                stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(receives[i])}));
            }
            Log.v("receive byte[] data", stringBuilder.toString());
            Log.v("receiver string data", new String(receives));

            final DoorPackageResult result = new DoorPackageResult();
            if (equals(receives, DoorFeedOK)) {
                result.code = DoorPackageResult.SUCCESS;
            } else if (equals(receives, DoorFeedERROR)) {
                result.code = DoorPackageResult.FAIL;
            } else {
                result.code = DoorPackageResult.UNKNOWN;
            }
            result.description = stringBuilder.toString();
            return result;
        }
        return null;
    }






    private String format(String input) {
        final char[] chars = input.toCharArray();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (i > 0 && i % 2 == 0) {
                builder.append(" ");
            }
            builder.append(chars[i]);
        }
        return builder.toString();
    }

    private byte[] sendcmd(byte CID, byte length, byte[] data) {
        DoorPackage sendpackage = new DoorPackage(new byte[]{(byte) 1, (byte) -4}, CID, length, data);
        if (!sendpackage.Constructed) {
            return null;
        }
        Log.v("CID", sendpackage.getCIDString());
        Log.v("Length", sendpackage.getLengthString());
        Log.v("Data", sendpackage.getDataString());
        Log.v("Check", sendpackage.getCheckByteString());
        byte[] send = sendpackage.getBytes();
        String datastring = "";
        for (int i = 0; i < send.length; i++) {
            datastring = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(datastring)).append(String.format("%02X ", new Object[]{Byte.valueOf(send[i])})).toString())).append(" ").toString();
        }
        Log.v("Send", datastring);
        return sendpackage.getBytes();
    }

    private DoorPackage checkReceive(byte[] receives) throws DoorException {
        if (receives.length < 5) {
            throw new DoorException("返回数据格式错误");
        }
        byte[] Data;
        byte[] SOI = new byte[]{(byte) 4, (byte) -4};
        int i = 0 + SOI.length;
        byte CID = receives[i];
        Log.v("receive CID", new StringBuilder(String.valueOf(CID)).toString());
        i++;
        byte Length = receives[i];
        Log.v("receive length", new StringBuilder(String.valueOf(Length)).toString());
        if (Length > 0) {
            Data = new byte[Length];
            for (byte j = (byte) 0; j < Length; j++) {
                i++;
                Data[j] = receives[i];
            }
        } else {
            Data = null;
        }
        byte CHECK_BYTE = receives[i + 1];
        DoorPackage receivepackage = new DoorPackage(SOI, CID, Length, Data);
        if (!receivepackage.Constructed) {
            return null;
        }
        Log.v("Length", receivepackage.getLengthString());
        Log.v("Data", receivepackage.getDataString());
        Log.v("Check", receivepackage.getCheckByteString());
        Log.v("CID", receivepackage.getCIDString());
        if (CHECK_BYTE != receivepackage.getCHECK_BYTE()) {
            Log.v("Receive CHECK_BYTE", new StringBuilder(String.valueOf(CHECK_BYTE)).toString());
            Log.v("Count CHECK_BYTE", new StringBuilder(String.valueOf(receivepackage.getCHECK_BYTE())).toString());
            throw new DoorException("数据校验位不匹配");
        }
        return receivepackage;
    }

    private byte[] HexStringtoHexBytes(String hexstring) throws DoorException {
        String[] hexs = hexstring.split(" ");
        byte[] result = null;
        if (hexs.length > 0) {
            result = new byte[hexs.length];
            int length = hexs.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                String hex = hexs[i];
                if (hex.length() < 2) {
                    hex = "0" + hex;
                }
                else if (hex.length() > 2) {
                    throw new DoorException("输入卡号格式错误,每两位卡号之间以“空格”分隔");
                }
                int i3 = i2 + 1;
                result[i2] = Chars2Byte(hex);
                i++;
                i2 = i3;
            }
        } else {
            throw new DoorException("传输数据为空");
        }
        return result;
    }

    private byte Chars2Byte(String hexstring) throws DoorException {
        char[] temp = hexstring.toCharArray();
        return (byte) ((((char2int(temp[0]) & MotionEventCompat.ACTION_MASK) << 4) & 240) + (char2int(temp[1]) & 15));
    }

    private int char2int(char inputstring) throws DoorException {
        if (inputstring >= '0' && inputstring <= '9') {
            return inputstring - 48;
        }
        if (inputstring >= 'A' && inputstring <= 'F') {
            return inputstring - 55;
        }
        throw new DoorException("输入卡号有误，请输入16进制的卡号");
    }

    private boolean equals(byte[] data1, byte[] data2) {
        if (data1.length != data2.length) {
            return false;
        }
        for (int i = 0; i < data1.length; i++) {
            if (data1[i] != data2[i]) {
                return false;
            }
        }
        return true;
    }

}
