package dali.flabbybird;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.jar.Attributes;

import draw.SurfaceViewBird;

public class MainActivity extends AppCompatActivity {
    private SurfaceViewBird surfaceViewBird;
    public static AppCompatActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        int speed = intent.getIntExtra("speed", 2);
        surfaceViewBird = new SurfaceViewBird(this,speed);
        //setContentView(R.layout.activity_main);
        setContentView(surfaceViewBird);
    }



}
