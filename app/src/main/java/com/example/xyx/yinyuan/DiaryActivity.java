package com.example.xyx.yinyuan;


import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DiaryActivity extends ListActivity {
    private DiaryService diaryService;
    private int idSelect;
    private ImageButton add;
    private TextView musicStatus, musicTime, musicTotal,musicName;
    private SeekBar seekBar;

    private Button btnPlayOrPause, btnNext, btnQuit;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private boolean tag1 = false;
    private boolean tag2 = false;
    private MusicService musicService;
    private  String[] names={"Taylor Swift - Speak Now","Taylor SwiftThe  - Safe & Sound "};
    public static int i=0;

    private void bindServiceConnection() {
        Intent intent = new Intent(DiaryActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }

    //  回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService();
            Log.i("musicService", musicService + "");
            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    //  通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicTime.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
            seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(musicService.mediaPlayer.getDuration());
            musicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);

        }
    };

    private void findViewById() {
        musicTime = (TextView) findViewById(R.id.MusicTime);
        musicTotal = (TextView) findViewById(R.id.MusicTotal);
        seekBar = (SeekBar) findViewById(R.id.MusicSeekBar);
        btnPlayOrPause = (Button) findViewById(R.id.BtnPlayorPause);
        btnNext = (Button) findViewById(R.id.BtnNext);
        btnQuit = (Button) findViewById(R.id.BtnQuit);
        musicStatus = (TextView) findViewById(R.id.MusicStatus);
        musicName=(TextView)findViewById(R.id.MusicName);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        findViewById();
        bindServiceConnection();
        myListener();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        refreshList();
        //点击事件，当点击某篇日记时..
        getListView().setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("button", "find");
                intent.setClass(DiaryActivity.this, SaveActivity.class);
                Bundle bundle = new Bundle();
                Diary diary = diaryService.find((int) id);
                bundle.putString("title", diary.getTitle());
                bundle.putString("content", diary.getContent());
                bundle.putInt("id", (int)id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        getListView().setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                idSelect = (int) id;

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        add=(ImageButton)findViewById(R.id.diary_add);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handler.removeCallbacks(runnable);
                unbindService(serviceConnection);
                Intent intent = new Intent(DiaryActivity.this, MusicService.class);
                stopService(intent);
                Intent intentl = new Intent();
                intentl.putExtra("button", "insert");
                intentl.setClass(DiaryActivity.this, SaveActivity.class);
                startActivity(intentl);
            }
        });


    }
    private void refreshList() {
        diaryService = new DiaryService(this);
        Cursor cursor = diaryService.getAllDiaries();
        startManagingCursor(cursor);
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.item_diary, cursor, new String[] { "title", "pubdate" },
                new int[] { R.id.title, R.id.pubdate });
        setListAdapter(simpleCursorAdapter);

    }
    private void myListener() {
        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (musicService.mediaPlayer != null) {
                    seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
                    seekBar.setMax(musicService.mediaPlayer.getDuration());
                }
                //  由tag的变换来控制事件的调用
                if (musicService.tag != true) {
                    //btnPlayOrPause.setText("PAUSE");
                    musicStatus.setText("Playing");
                    musicName.setText(names[i%2]);
                    musicService.playOrPause();
                    musicService.tag = true;

                } else {
                    //btnPlayOrPause.setText("PLAY");
                    musicStatus.setText("Paused");
                    musicService.playOrPause();

                    musicService.tag = false;
                }
                if (tag2 == false) {
                    handler.post(runnable);
                    tag2 = true;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                musicStatus.setText("Playing");
               // btnPlayOrPause.setText("PAUSE");
                musicService.next();
                i=i+1;
                musicName.setText(names[i%2]);
                tag1=false;
                //animator.pause();
                //musicService.tag = false;
            }
        });

        //  停止服务时，必须解除绑定，写入btnQuit按钮中
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(DiaryActivity.this);
                builder.setTitle("退出" ) ;
                builder.setMessage("确定要离开嘛？" ) ;
                builder.setPositiveButton("是",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeCallbacks(runnable);
                        unbindService(serviceConnection);
                        Intent intent = new Intent(DiaryActivity.this, MusicService.class);
                        stopService(intent);
                        try {
                            DiaryActivity.this.finish();
                            Intent intent1=new Intent(DiaryActivity.this,LoginActivity.class);
                            startActivity(intent1);
                        } catch (Exception e) {

                        }
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });

    }

    //  获取并设置返回键的点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //unbindService(serviceConnection);
    }

}
