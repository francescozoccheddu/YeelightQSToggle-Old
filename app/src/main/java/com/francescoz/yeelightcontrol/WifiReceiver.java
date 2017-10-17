package com.francescoz.yeelightcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

    private static final String SSID = "\"TISCALI-Zoccheddu\"";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(), "onReceive");
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        boolean enabled = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            if (wifiInfo.getSSID().equals(SSID))
                enabled = true;
        }
        Intent mIntent = new Intent(context, QuickSettingsService.class);
        mIntent.putExtra(QuickSettingsService.INTENT_KEY_ENABLED, enabled);
        context.startService(mIntent);

    }
}
