package com.francescoz.yeelightqstoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

    private static final String SSID_HOME = "\"TISCALI-Zoccheddu\"";

    public static boolean isHome(WifiInfo wifiInfo) {
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
            return wifiInfo.getSSID().equals(SSID_HOME);
        return false;
    }

    public static boolean isHome(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return isHome(wifiInfo);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(), "onReceive");
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        boolean enabled = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            enabled = isHome(wifiInfo);
        }
        Intent mIntent = new Intent(context, QuickSettingsService.class);
        mIntent.putExtra(QuickSettingsService.INTENT_KEY_ENABLED, enabled);
        context.startService(mIntent);

    }
}