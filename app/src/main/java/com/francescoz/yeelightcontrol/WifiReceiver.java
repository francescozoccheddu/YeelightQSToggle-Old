package com.francescoz.yeelightcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
/*
 Tested (I didn't test with the WPS "Wi-Fi Protected Setup" standard):
 In API15 (ICE_CREAM_SANDWICH) this method is called when the new Wi-Fi network state is:
 DISCONNECTED, OBTAINING_IPADDR, CONNECTED or SCANNING

 In API19 (KITKAT) this method is called when the new Wi-Fi network state is:
 DISCONNECTED (twice), OBTAINING_IPADDR, VERIFYING_POOR_LINK, CAPTIVE_PORTAL_CHECK
 or CONNECTED

 (Those states can be obtained as NetworkInfo.DetailedState objects by calling
 the NetworkInfo object method: "networkInfo.getDetailedState()")
*/
    /*
     * NetworkInfo object associated with the Wi-Fi network.
     * It won't be null when "android.net.wifi.STATE_CHANGE" action intent arrives.
     */
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if (networkInfo != null && networkInfo.isConnected()) {
            // TODO: Place the work here, like retrieving the access point's SSID

        /*
         * WifiInfo object giving information about the access point we are connected to.
         * It shouldn't be null when the new Wi-Fi network state is CONNECTED, but it got
         * null sometimes when connecting to a "virtualized Wi-Fi router" in API15.
         */
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            String ssid = wifiInfo.getSSID();
            Toast.makeText(context, "Ciao da " + ssid, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Addio", Toast.LENGTH_LONG).show();

        }
    }
}
