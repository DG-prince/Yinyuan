package com.example.xyx.yinyuan;



import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveActivity extends Activity {

    private EditText titleText;
    private EditText contentText;
    private Button save;
    private Button delete;
    private DiaryService diaryService;
    private int idSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        titleText = (EditText) this.findViewById(R.id.title);
        contentText = (EditText) this.findViewById(R.id.content);
        save = (Button) this.findViewById(R.id.save);
        Intent intent = this.getIntent();
        //Bundle bundle=intent.getExtras();
        String msg = intent.getStringExtra("button");
        //实现添加日记
        if (msg.equals("insert")) {
            save.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Diary diary = new Diary(titleText.getText().toString(),
                            contentText.getText().toString(),
                            getCurrentTime(new Date()));
                    diaryService = new DiaryService(SaveActivity.this);
                    diaryService.save(diary);
                    Intent intent=new Intent();
                    intent.setClass(SaveActivity.this, DiaryActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SaveActivity.this, R.string.save_success,
                            Toast.LENGTH_LONG).show();
                }
            });
            //实现修改日记
        } else if (msg.equals("find")) {
            final Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                String title = bundle.getString("title");
                String content = bundle.getString("content");

                if (titleText != null) {
                    titleText.setText(title);
                }
                if (contentText != null) {
                    contentText.setText(content);
                }
            }
            save.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Diary diary = new Diary(titleText.getText().toString(),
                            contentText.getText().toString(),
                            getCurrentTime(new Date()));
                    diary.setId(bundle.getInt("id"));
                    diaryService = new DiaryService(SaveActivity.this);
                    diaryService.update(diary);
                    Intent intent = new Intent(SaveActivity.this,DiaryActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(SaveActivity.this, R.string.update_success,
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        delete=(Button)findViewById(R.id.diary_delete);
        final Bundle bundle = this.getIntent().getExtras();
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //final Bundle bundle = this.getIntent().getExtras();
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveActivity.this);
                builder.setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.info)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                diaryService = new DiaryService(SaveActivity.this);
                                //Bundle bundle=intent.getExtras();
                                idSelect=bundle.getInt("id");
                                diaryService.delete(idSelect);
                                //refreshList();
                                Intent intent = new Intent(SaveActivity.this,DiaryActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SaveActivity.this, "日记删除成功！",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();

            }
        });
    }

    public String getCurrentTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日hh时mm分ss秒");
        return simpleDateFormat.format(date);
    }


}

