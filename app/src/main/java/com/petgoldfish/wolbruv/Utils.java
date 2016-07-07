package com.petgoldfish.wolbruv;

import java.util.regex.Pattern;

/**
 * Created by petgoldfish on 07-07-2016.
 */
public class Utils {

    private static final Pattern IP_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static final Pattern MAC_PATTERN = Pattern.compile(
            "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    public static boolean validateInput(final String mac, final String ip) {
        return MAC_PATTERN.matcher(mac).matches() || IP_PATTERN.matcher(ip).matches();
    }

}