package com.spotify.sdk.spotify_test_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import spotify.Scopes;
import utils.Constants;

public class MainActivity extends AppCompatActivity implements PlayerNotificationCallback
        , ConnectionStateCallback{

    private final String TAG = MainActivity.class.getSimpleName();
    private final int SPOTIFY_REQUEST_CODE = 1337;

    private Player mPlayer;
//    private PlayerStateCallback mPlayerStateCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Authentication builder
        AuthenticationRequest.Builder mAuthBuilder = new AuthenticationRequest.Builder(Constants.CLIENT_ID
        , AuthenticationResponse.Type.TOKEN
        , Constants.REDIRECT_URI);

        //set Scopes
        mAuthBuilder.setScopes(new String[]{Scopes.USER_READ_PRIVATE, Scopes.STREAMING});

        //Building Authentication request
        AuthenticationRequest mAuthRequest = mAuthBuilder.build();

        //Init spotify login Activity
        AuthenticationClient.openLoginActivity(this, SPOTIFY_REQUEST_CODE, mAuthRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Log.e(TAG, "[ERROR_ACTIVITY_result] Intent data is NULL!!!");
            return;
        }

        if (requestCode == SPOTIFY_REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), Constants.CLIENT_ID);

                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {

                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
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
        switch (eventType) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Log.d("TAG", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }
}
