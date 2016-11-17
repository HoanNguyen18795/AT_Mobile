package com.testing.hoan.at_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Hoan on 9/11/2016.
 */
public class SessionManager {
    private static String TAG=SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE=0;
    private static final String PREF_NAME="HNK";
    private static final String KEY_IS_LOGGED_IN="isLoggedIN";
    public SessionManager(Context context){
        _context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }
    public void setLogin(boolean isLoggedin){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedin);
        editor.commit();
        Log.d(TAG,"user login modified");
    }
    public boolean isLoggedin(){
        return pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }
}
