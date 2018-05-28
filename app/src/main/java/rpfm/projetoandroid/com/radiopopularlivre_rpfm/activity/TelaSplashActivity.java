package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;

public class TelaSplashActivity extends Activity {
    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;
    private ImageView imgSplash;

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(TelaSplashActivity.this).load(R.drawable.gif_splash).asGif().into(imgSplash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);
        imgSplash = findViewById(R.id.imgSplashId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    // Esse método será executado sempre que o timer acabar
                    // E inicia a activity principal
                Intent intent = new Intent(TelaSplashActivity.this, LoginActivity.class);
                startActivity(intent);
                    // Fecha a activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
