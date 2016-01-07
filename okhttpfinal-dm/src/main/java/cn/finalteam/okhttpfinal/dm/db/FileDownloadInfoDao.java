/*
 * Copyright (C) 2015 彭建波(pengjianbo@finalteam.cn), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.finalteam.okhttpfinal.dm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/7 0007 16:01
 */
public class FileDownloadInfoDao extends AbstractDao<FileDownloadInfo> {

    private static final String TABLE_NAME = "FilesInfo";

    public FileDownloadInfoDao(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(_id integer primary key autoincrement, " +
                "url text, progress integer, targetPath text, downloadLength long, " +
                "totalLength long)");
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public void insert(FileDownloadInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into " + TABLE_NAME
                        + "(url, progress, targetPath, downloadLength, totalLength) values(?, ?, ?, ?, ?)",
                new Object[]{info.getUrl(), info.getProgress(), info.getTargetPath(), info.getDownloadLength(), info.getTotalLength()});
    }

    public void delete(String url) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where url = ?", new String[]{url});
    }

    public void update(String url, long downloadLength, long totalLength, int progress) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update "
                        + TABLE_NAME
                        + " set downloadLength = ?, totalLength=?, progress=?"
                        + " where url = ?",
                new Object[]{downloadLength, totalLength, progress, url});
    }

    /**
     * 获取单个下载实例
     * @param url
     * @return
     */
    public FileDownloadInfo getFileDownload(String url) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "
                        + TABLE_NAME
                        + " where url = ?",
                new String[]{url});
        FileDownloadInfo info = null;
        if (cursor.moveToNext()) {
            info = new FileDownloadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setDownloadLength(cursor.getLong(cursor.getColumnIndex("downloadLength")));
            info.setTargetPath(cursor.getString(cursor.getColumnIndex("targetPath")));
            info.setTotalLength(cursor.getLong(cursor.getColumnIndex("totalLength")));
            info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
        }
        cursor.close();
        return info;
    }

    /**
     * 获取所有下载
     * @return
     */
    public List<FileDownloadInfo> getAllFileDownlod() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<FileDownloadInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            FileDownloadInfo info = new FileDownloadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setDownloadLength(cursor.getLong(cursor.getColumnIndex("downloadLength")));
            info.setTargetPath(cursor.getString(cursor.getColumnIndex("targetPath")));
            info.setTotalLength(cursor.getLong(cursor.getColumnIndex("totalLength")));
            info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            list.add(info);
        }

        return list;
    }

    public List<FileDownloadInfo> getAllFileDownlodFinish() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where progress != 100", null);
        List<FileDownloadInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            FileDownloadInfo info = new FileDownloadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setDownloadLength(cursor.getLong(cursor.getColumnIndex("downloadLength")));
            info.setTargetPath(cursor.getString(cursor.getColumnIndex("targetPath")));
            info.setTotalLength(cursor.getLong(cursor.getColumnIndex("totalLength")));
            info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            list.add(info);
        }

        return list;
    }

    public List<FileDownloadInfo> getAllFileDownlodUnfinish() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where progress = 100", null);
        List<FileDownloadInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            FileDownloadInfo info = new FileDownloadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setDownloadLength(cursor.getLong(cursor.getColumnIndex("downloadLength")));
            info.setTargetPath(cursor.getString(cursor.getColumnIndex("targetPath")));
            info.setTotalLength(cursor.getLong(cursor.getColumnIndex("totalLength")));
            info.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            list.add(info);
        }

        return list;
    }

    /**
     * 判断是否存在
     * @param url
     * @return
     */
    public boolean exists(String url) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                        + TABLE_NAME
                        + " where url = ?",
                new String[]{url});
        boolean isExists = cursor.moveToNext();
        cursor.close();
        return isExists;
    }
}
