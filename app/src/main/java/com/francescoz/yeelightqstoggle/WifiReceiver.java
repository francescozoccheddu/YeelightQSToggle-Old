package com.francescoz.yeelightqstoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

    private static final String SSID_HOME = "\"TISCALI-Zoccheddu\"";

    public static WifiHomeState getHomeState(WifiInfo wifiInfo) {
        if (wifiInfo != null) {
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                final String ssid = wifiInfo.getSSID();
                if (ssid != null && ssid.equals(SSID_HOME)) {
                    return WifiHomeState.HOME;
                }
            }
            return WifiHomeState.NOT_HOME;
        } else {
            return WifiHomeState.UNKNOWN;
        }
    }

    public static WifiHomeState getHomeState(Context context) {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return getHomeState(wifiInfo);
        } else {
            return WifiHomeState.UNKNOWN;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(), "onReceive");
        final Intent mIntent = new Intent(context, QuickSettingsService.class);
        context.startService(mIntent);
    }

    public enum WifiHomeState {
        HOME, NOT_HOME, UNKNOWN
    }
}