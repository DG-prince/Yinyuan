package com.example.xyx.yinyuan;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.xyx.yinyuan.Diary;



public class DiaryService {
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;

    public DiaryService(Context context) {
        dbHelper = new DBHelper(context);
    }
    /*
     * 保存日记
     */
    public void save(Diary diary) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "insert into diary(title,content,pubdate) values(?,?,?)";
        sqLiteDatabase.execSQL(
                sql,
                new String[] { diary.getTitle(), diary.getContent(),
                        diary.getPubdate() });
    }
    /*
     * 更新日记
     */
    public void update(Diary diary){
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "update diary set title=?,content=?,pubdate=? where _id=?";
        sqLiteDatabase.execSQL(
                sql,
                new Object[] { diary.getTitle(), diary.getContent(),
                        diary.getPubdate(), diary.getId()});
    }
    /*
     * 根据id删除日记
     */
    public void delete(Integer id) {
        sqLiteDatabase = dbHelper.getWritableDatabase();// 得到的是同一个数据库实例
        sqLiteDatabase.execSQL("delete from diary where _id=?",new Object[]{id});
    }
    /*
     * 根据id查询日记
     */
    public Diary find(Integer id) {
        Diary diary = null;
        sqLiteDatabase = dbHelper.getReadableDatabase();
        // 得到游标，最多只有一条数据
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select * from diary where _id=?",
                new String[] { id.toString() });
        // 如果移动成功就代表存在
        if (cursor.moveToFirst()) {
            // 只能根据列的索引来获得相应的字段值
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
            diary = new Diary(title, content, pubdate);
        }
        return diary;
    }
    /*
     * 分页查询
     */
    public List<Diary> getDiariesByPage(Integer offset, Integer maxResult) {
        Diary diary = null;
        List<Diary> diaryList = new ArrayList<Diary>();
        sqLiteDatabase = dbHelper.getReadableDatabase();
        // 得到游标，最多只有一条数据
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select * from diary limit ?,?",
                new String[] { offset.toString(), maxResult.toString() });
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
            diary = new Diary(title, content, pubdate);
            diaryList.add(diary);
        }
        cursor.close();
        return diaryList;
    }
    /*
     * 获取所有日记
     */
    public Cursor getAllDiaries(){
        sqLiteDatabase=dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from diary", null);
        return cursor;
    }
    /*
     * 获取记录总数
     */
    public long count() {
        long count=0;
        sqLiteDatabase=dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select count(*) from diary ",null);
        cursor.moveToFirst();
        count=cursor.getLong(0);
        return count;
    }

}

