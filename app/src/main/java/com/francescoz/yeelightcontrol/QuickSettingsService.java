// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.francescoz.yeelightcontrol;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.net.Socket;

public class QuickSettingsService
        extends TileService {

    public static final String INTENT_KEY_ENABLED = "enabled";
    private static final String PACKET_MESSAGE = "{\"id\":0x00000000036243b3,\"method\":\"toggle\",\"params\":[]}\r\n";
    private static final String PACKET_IP = "192.168.1.4";
    private static final int PACKET_PORT = 55443;

    @Override
    public void onClick() {
        Log.d(this.getClass().getSimpleName(), "onClick");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(PACKET_IP, PACKET_PORT);
                    socket.setKeepAlive(true);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                    bufferedOutputStream.write(PACKET_MESSAGE.getBytes());
                    bufferedOutputStream.flush();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onStartListening() {
        setEnabled(WifiReceiver.isHome(this));
    }

    private void setEnabled(boolean enabled) {
        Tile tile = getQsTile();
        if (enabled) {
            tile.setIcon(Icon.createWithResource(getApplicationContext(),
                    R.drawable.ic_lightbulb_outline_black_24dp));
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setIcon(Icon.createWithResource(getApplicationContext(),
                    R.drawable.ic_lightbulb_outline_white_24dp));
            tile.setState(Tile.STATE_UNAVAILABLE);
        }
        tile.updateTile();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(), "onStartCommand");
        boolean enabled;
        if (intent.hasExtra(INTENT_KEY_ENABLED))
            enabled = intent.getBooleanExtra(INTENT_KEY_ENABLED, false);
        else
            enabled = WifiReceiver.isHome(this);
        setEnabled(enabled);

        return START_STICKY;
    }

}
