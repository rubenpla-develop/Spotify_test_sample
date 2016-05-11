package com.spotify.sdk.spotify_test_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;

public class MainActivity extends AppCompatActivity implements PlayerNotificationCallback, ConnectionStateCallback{

    private final String TAG = MainActivity.class.getSimpleName();

    private Player mPlayer;
    private PlayerStateCallback mPLayerStateCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("TAG", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
        Log.d("TAG", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("TAG", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("TAG", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("TAG", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Log.d("TAG", "Playback error received: " + errorType.name());
    }
}
