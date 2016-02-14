package com.petgoldfish.wolbruv;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by petgoldfish on 14-02-2016.
 */
public class WakeOnLan implements Runnable {
    private static final String TAG = "WOL";
    private final String mIpStr;
    private final String mMacAddress;
    private static final int PORT = 9;
    private final int MAC_SIZE = 6;

    public WakeOnLan(String macAddress, String ip) {
        this.mMacAddress = macAddress;
        this.mIpStr = ip;
    }

    @Override
    public void run() {
        send();
    }

    private void send() {
        try {
            byte[] macBytes = getMacBytes(mMacAddress);
            byte[] bytes = new byte[MAC_SIZE + 16 * macBytes.length];
            for (int i = 0; i < MAC_SIZE; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = MAC_SIZE; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            InetAddress address = InetAddress.getByName(mIpStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    private byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[MAC_SIZE];
        String[] hex = macStr.split(":");
        if (hex.length != MAC_SIZE) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < MAC_SIZE; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}