/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.carbonrom.carbonfibers.crstats;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import android.provider.Settings.Secure;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;

public class Utilities {
    static final String STATS_COLLECTION = "stats_collection";

    public static String getUniqueID(Context context) {
        final String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return digest(context.getPackageName() + id);
    }

    public static String getCarrier(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrier = tm.getNetworkOperatorName();
        if (TextUtils.isEmpty(carrier)) {
            carrier = "Unknown";
        }
        return carrier;
    }

    public static String getCarrierId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierId = tm.getNetworkOperator();
        if (TextUtils.isEmpty(carrierId)) {
            carrierId = "0";
        }
        return carrierId;
    }

    public static String getCountryCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "Unknown";
        }
        return countryCode;
    }

    public static String getDevice() {
        return SystemProperties.get("ro.carbon.device", Build.PRODUCT);
    }

    public static String getModVersion() {
        return SystemProperties.get("ro.carbon.version", Build.DISPLAY);
    }

    public static String digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return new BigInteger(1, md.digest(input.getBytes())).toString(16).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check to see if global stats are enabled.
     * @param context
     * @return Whether or not stats collection is enabled.
     */
    public static boolean isStatsCollectionEnabled(Context context) {
        return Secure.getInt(context.getContentResolver(),
                STATS_COLLECTION, 1) != 0;
    }

    /**
     * Enabled or disable stats collection
     * @param context
     * @param enabled Boolean that sets collection being enabled.
     */
    public static void setStatsCollectionEnabled(Context context, boolean enabled) {
        int enable = (enabled) ? 1 : 0;
        Secure.putInt(context.getContentResolver(),
                STATS_COLLECTION, enable);
    }
}