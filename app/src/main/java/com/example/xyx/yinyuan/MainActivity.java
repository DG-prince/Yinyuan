package com.example.xyx.yinyuan;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button enterBtn;
    private MediaPlayer mediaPlayer;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer= MediaPlayer.create(this,R.raw.song);
        mediaPlayer.start();
        enterBtn= (Button) findViewById(R.id.enter_party);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("好久不见!" ) ;
                builder.setMessage("开启音园世界，记录点滴生活" ) ;
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediaPlayer.stop();
                        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            mediaPlayer.stop();
            finish();
            System.exit(0);
        }
    }
}