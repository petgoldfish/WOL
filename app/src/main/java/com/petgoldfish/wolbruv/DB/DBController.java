package com.petgoldfish.wolbruv.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.petgoldfish.wolbruv.DeviceData;

import java.util.ArrayList;
import java.util.List;

public class DBController {
    // Database fields
    private DBHelper DBHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBController(Context context) {
        DBHelper = new DBHelper(context);
    }

    public void close() {
        DBHelper.close();
    }

    public void addDeviceData(DeviceData deviceData) {

        database = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_MAC, deviceData.getMAC());
        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_IP, deviceData.getIP());
        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_ALIAS, deviceData.getAlias());

        database.insert(com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME, null, values);

        System.out.println("Record Added");
        database.close();
    }

    public DeviceData getDeviceData(int _id) {

        database = DBHelper.getReadableDatabase();

        Cursor cursor = database.query(com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME, com.petgoldfish.wolbruv.DB.DBHelper.columns, com.petgoldfish.wolbruv.DB.DBHelper.COL_ID + " =?", new String[]{String.valueOf(_id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        assert cursor != null;
        DeviceData deviceData = new DeviceData(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        cursor.close();

        return deviceData;
    }

    // Getting All Employees
    public List<DeviceData> getAllDeviceDatas() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        List<DeviceData> dataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DeviceData deviceData = new DeviceData();
                deviceData.setId(Integer.parseInt(cursor.getString(0)));
                deviceData.setMAC(cursor.getString(1));
                deviceData.setIP(cursor.getString(2));
                deviceData.setAlias(cursor.getString(3));
                // Adding contact to list
                dataList.add(deviceData);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return contact list
        return dataList;
    }

    // Updating single employee
    public int updateDeviceData(DeviceData deviceData) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_MAC, deviceData.getMAC());
        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_IP, deviceData.getIP());
        values.put(com.petgoldfish.wolbruv.DB.DBHelper.COL_ALIAS, deviceData.getAlias());

        // updating row
        return db.update(com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME, values, com.petgoldfish.wolbruv.DB.DBHelper.COL_ID + " = ?",
                new String[]{String.valueOf(deviceData.getId())});
    }

    // Deleting single employee
    public void deleteDeviceData(DeviceData note) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        db.delete(com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME,  com.petgoldfish.wolbruv.DB.DBHelper.COL_ID + " = ?",
                new String[]{String.valueOf(note.getId())});

        System.out.println("Record Deleted");
        db.close();
    }

    // Deleting single employee
    public void deleteDeviceData(int _id) {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        db.delete(com.petgoldfish.wolbruv.DB.DBHelper.TABLE_NAME,  com.petgoldfish.wolbruv.DB.DBHelper.COL_ID + " = ?",
                new String[]{String.valueOf(_id)});
        db.close();
    }
}