package com.francescoz.yeelightqstoggle;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.net.Socket;

public class QuickSettingsService extends TileService {

    public static final String INTENT_KEY_ENABLED = "enabled";
    private static final String PACKET_MESSAGE = "{\"id\":0x00000000036243b3,\"method\":\"toggle\",\"params\":[]}\r\n";
    private static final String PACKET_IP = "192.168.1.4";
    private static final int PACKET_PORT = 55443;

    private boolean tileActive;

    @Override
    public void onClick() {
        Log.d(this.getClass().getSimpleName(), "onClick");

        final Runnable toggleRunnable = new Runnable() {

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

        };

        new Thread(toggleRunnable).start();
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_tile));
            tile.setState(tileActive ? Tile.STATE_ACTIVE : Tile.STATE_UNAVAILABLE);
            tile.updateTile();
        }
    }

    @Override
    public void onStartListening() {
        updateTileState();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(), "onStartCommand");

        if (intent.hasExtra(INTENT_KEY_ENABLED)) {
            tileActive = intent.getBooleanExtra(INTENT_KEY_ENABLED, false);
        } else {
            tileActive = WifiReceiver.isHome(this);
        }

        updateTileState();

        return START_STICKY;
    }

}