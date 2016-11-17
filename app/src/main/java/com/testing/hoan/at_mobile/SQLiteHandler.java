package com.testing.hoan.at_mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Hoan on 9/12/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    public static final String TAG=SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="android_api";
    private static final String TABLE_USER="user";
    private static final String ID_COLUNM="id";
    private static final String USER_NAME_COLUNM="username";
    private static final String TOKEN_COLUNM="token";
    private static final String ACTIVE_COLUNM="active";
    private static final String ID_ROLE_COLUNM="id_role";
    private static final String LAST_LOGIN_COLUNM="last_login";
    private static final String CREATED_COLUNM="created";
    private static final String UPDATE_COLUNM="update";
    private static final String FULL_NAME_COLUNM="full_name";
    private static final String PHONE_COLUNM="phone";
    private static final String EMAIL_COLUNM="email";
    private static final String ADDRESS_COLUNM="address";
    private static final String CITY_COLUNM="city";
    private static final String COUNTRY_COLUNM="country";
    private static final String AVATAR_COLUNM="avatar";
    private static final String GENDER_COLUNM="gender";
    private static final String BIRTHDAY_COLUNM="birthday";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE="CREATE TABLE "+TABLE_USER+"("+ID_COLUNM+"INTEGER PRIMARY KEY,"+USER_NAME_COLUNM+"TEXT,"+
                TOKEN_COLUNM+"TEXT,"+ACTIVE_COLUNM+"INTEGER,"+ID_ROLE_COLUNM+"INTEGER,"+LAST_LOGIN_COLUNM+"NUMERIC,"+CREATED_COLUNM+"NUMERIC"+UPDATE_COLUNM+"NUMERIC"+FULL_NAME_COLUNM+"TEXT"+
                PHONE_COLUNM+"NUMERIC"+EMAIL_COLUNM+"TEXT"+CITY_COLUNM+"TEXT"+ADDRESS_COLUNM+"TEXT"+COUNTRY_COLUNM+"TEXT"+AVATAR_COLUNM+"TEXT"+GENDER_COLUNM+"NUMERIC"+BIRTHDAY_COLUNM+"TEXT"+")";
                db.execSQL(CREATE_USER_TABLE);
        Log.d(TAG,"Created user table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST"+TABLE_USER);
        onCreate(db);
    }
    public void addUser(int ID,String name,int active,String last_login,String created,String update,String fullName,String phone,String email,String address,String city,String country,String avatar,int gender,String birthday,int role_level,String token){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ID_COLUNM,ID);
        values.put(USER_NAME_COLUNM,name);
        values.put(ACTIVE_COLUNM,active);
        values.put(TOKEN_COLUNM,token);
        values.put(ID_ROLE_COLUNM,role_level);
        values.put(LAST_LOGIN_COLUNM,last_login);
        values.put(CREATED_COLUNM,created);
        values.put(UPDATE_COLUNM,update);
        values.put(FULL_NAME_COLUNM,fullName);
        values.put(PHONE_COLUNM,phone);
        values.put(EMAIL_COLUNM,email);
        values.put(ADDRESS_COLUNM,address);
        values.put(CITY_COLUNM,city);
        values.put(COUNTRY_COLUNM,country);
        values.put(AVATAR_COLUNM,avatar);
        values.put(GENDER_COLUNM,gender);
        values.put(BIRTHDAY_COLUNM,birthday);
        Long id=db.insert(TABLE_USER,null,values);
        db.close();
        Log.d(TAG,"New user has been created at row"+id);
    }
    public HashMap<String,String> getUser(){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(ID_COLUNM,cursor.getString(1));
            user.put(USER_NAME_COLUNM,cursor.getString(2));
            user.put(ACTIVE_COLUNM,cursor.getString(3));
            user.put(TOKEN_COLUNM,cursor.getString(4));
            user.put(ID_ROLE_COLUNM,cursor.getString(5));
            user.put(LAST_LOGIN_COLUNM,cursor.getString(6));
            user.put(CREATED_COLUNM,cursor.getString(7));
            user.put(UPDATE_COLUNM,cursor.getString(8));
            user.put(FULL_NAME_COLUNM,cursor.getString(9));
            user.put(PHONE_COLUNM,cursor.getString(10));
            user.put(EMAIL_COLUNM,cursor.getString(11));
            user.put(ADDRESS_COLUNM,cursor.getString(12));
            user.put(CITY_COLUNM,cursor.getString(13));
            user.put(COUNTRY_COLUNM,cursor.getString(14));
            user.put(AVATAR_COLUNM,cursor.getString(15));
            user.put(GENDER_COLUNM,cursor.getString(16));
            user.put(BIRTHDAY_COLUNM,cursor.getString(17));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    public void removeUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
