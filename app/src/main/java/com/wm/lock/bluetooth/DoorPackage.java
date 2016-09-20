package com.wm.lock.bluetooth;

import java.text.DecimalFormat;

public class DoorPackage {
    private byte CHECK_BYTE;
    private byte CID;
    public boolean Constructed;
    private byte[] Data;
    private byte Length;
    private byte[] SOI;

    public DoorPackage(byte[] SOI, byte CID, byte Length, byte[] data) {
        this.Constructed = true;
        this.SOI = SOI;
        this.CID = CID;
        this.Length = Length;
        if (Length != 0 && data == null) {
            this.Data = null;
            this.Constructed = false;
        } else if ((Length == 0 && data == null) || data.length == Length) {
            this.Data = data;
        } else if (data.length < Length) {
            this.Data = new byte[Length];
            byte m = (byte) 0;
            int i = 0;
            while (i < data.length) {
//                byte m2 = m + 1;
                byte m2 = (byte) (m + (byte) 1);
                this.Data[m] = data[i];
                i++;
                m = m2;
            }
            while (m < Length) {
                this.Data[m] = (byte) 0;
                m++;
            }
        } else {
            this.Constructed = false;
        }
        this.CHECK_BYTE = GenerateCheckByte();
    }

    public byte GenerateCheckByte() {
        byte temp = (byte) (this.CID ^ this.Length);
        if (this.Data != null) {
            for (byte b : this.Data) {
                temp = (byte) (temp ^ b);
            }
        }
        return temp;
    }

    public byte getCID() {
        return this.CID;
    }

    public void setCID(byte cID) {
        this.CID = cID;
    }

    public byte getLength() {
        return this.Length;
    }

    public void setLength(byte length) {
        this.Length = length;
    }

    public byte[] getData() {
        return this.Data;
    }

    public void setData(byte[] data) {
        this.Data = data;
    }

    public byte getCHECK_BYTE() {
        return this.CHECK_BYTE;
    }

    public void setCHECK_BYTE(byte cHECK_BYTE) {
        this.CHECK_BYTE = cHECK_BYTE;
    }

    public byte[] getSOI() {
        return this.SOI;
    }

    public void setSOI(byte[] SOI) {
        this.SOI = SOI;
    }

    public String getSOIString() {
        return String.format("%02X", new Object[]{this.SOI});
    }

    public String getCIDString() {
        return String.format("%02X", new Object[]{Byte.valueOf(this.CID)});
    }

    public String getLengthString() {
        return String.format("%02X", new Object[]{Byte.valueOf(this.Length)});
    }

    public String getDataString() {
        String datastring = "";
        for (byte i = (byte) 0; i < this.Length; i++) {
            datastring = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(datastring)).append(String.format("%02X ", new Object[]{Byte.valueOf(this.Data[i])})).toString())).append(" ").toString();
        }
        return datastring;
    }

    public String getDataBinary() {
        DecimalFormat format = new DecimalFormat("00000000");
        String datastring = "";
        for (byte i = (byte) 0; i < this.Length; i++) {
            datastring = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(datastring)).append(format.format(Integer.valueOf(Integer.toBinaryString(this.Data[i])))).toString())).append(" ").toString();
        }
        return datastring;
    }

    public String getCheckByteString() {
        return String.format("%02X", new Object[]{Byte.valueOf(this.CHECK_BYTE)});
    }

    public byte[] getBytes() {
        byte[] result = new byte[((((this.SOI.length + 1) + 1) + this.Length) + 1)];
        int bytestoBytes = bytestoBytes(this.SOI, result, 0);
        int i = bytestoBytes + 1;
        result[bytestoBytes] = this.CID;
        bytestoBytes = i + 1;
        result[i] = this.Length;
        if (this.Data != null && this.Length > 0) {
            bytestoBytes = bytestoBytes(this.Data, result, bytestoBytes);
        }
        result[bytestoBytes] = this.CHECK_BYTE;
        return result;
    }

    private int bytestoBytes(byte[] from, byte[] to, int index) {
        int i = 0;
        while (i < from.length) {
            int index2 = index + 1;
            to[index] = from[i];
            i++;
            index = index2;
        }
        return index;
    }
}
