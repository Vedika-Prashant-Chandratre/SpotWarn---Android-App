package com.example.spotwarn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.VideoView;

public class splashscreenbackend extends AppCompatActivity {

    private VideoView v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Always show the splash screen (welcome video)
        setContentView(R.layout.splashscreen);
        setupWelcomeVideo();
    }

    private void setupWelcomeVideo() {
        v1 = findViewById(R.id.vv);
        v1.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash);

        // Set video to full screen
        ViewGroup.LayoutParams layoutParams = v1.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        v1.setLayoutParams(layoutParams);

        // Start video automatically
        v1.start();

        // After 5 seconds, navigate to Login activity
        new Handler().postDelayed(() -> {
            if (v1.isPlaying()) {
                v1.stopPlayback();
            }
            startActivity(new Intent(splashscreenbackend.this, MainDashboard.class));
            finish();
        }, 5000);
    }
}
