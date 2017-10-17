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
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

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
        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(), "onStartCommand");

        boolean enabled = intent.getBooleanExtra(INTENT_KEY_ENABLED, false);
        Tile tile = getQsTile();
        tile.setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_UNAVAILABLE);
        tile.updateTile();
        return START_STICKY;
    }

    /*
    // Changes the appearance of the tile.
    private void updateTile() {

        Tile tile = this.getQsTile();

        Icon newIcon;
        String newLabel;
        int newState;

        // Change the tile to match the service status.
        if (isActive) {

            newLabel = String.format(Locale.US,
                    "%s %s",
                    getString(R.string.tile_label),
                    getString(R.string.service_active));

            newIcon = Icon.createWithResource(getApplicationContext(),
                    R.drawable.ic_android_black_24dp);

            newState = Tile.STATE_ACTIVE;

        } else {
            newLabel = String.format(Locale.US,
                    "%s %s",
                    getString(R.string.tile_label),
                    getString(R.string.service_inactive));

            newIcon =
                    Icon.createWithResource(getApplicationContext(),
                            android.R.drawable.ic_dialog_alert);

            newState = Tile.STATE_INACTIVE;
        }

        // Change the UI of the tile.
        tile.setLabel(newLabel);
        tile.setIcon(newIcon);
        tile.setState(newState);

        // Need to call updateTile for the tile to pick up changes.
        tile.updateTile();
    }
    */
}
