package com.game.photopuzzle;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 05/04/2559.
 */
public class UserHelper {
    Context context;
    SharedPreferences sharedPerfs;
    SharedPreferences.Editor editor;

    // Prefs Keys
    static String perfsName = "UserHelper";
    static int perfsMode = 0;

    public UserHelper(Context context) {
        this.context = context;
        this.sharedPerfs = this.context.getSharedPreferences(perfsName, perfsMode);
        this.editor = sharedPerfs.edit();
    }

    public void createSession(String key) {
        editor.putBoolean("isLogin", true);
        editor.putString("userID", key);
        editor.commit();
    }

    public void deleteSession() {
        editor.clear();
        editor.commit();
    }

    public boolean isLogin() {
        return sharedPerfs.getBoolean("isLogin", false);
    }

    public String getUserID() {
        return sharedPerfs.getString("userID", null);
    }
}

/*UserHelper usrHelper = new UserHelper(this);*/

// Create Session
/*usrHelper.createSession(ID);*/

// Delete Session
       /* usrHelper.deleteSession();*/

// Check Login
       /* if (usrHelper.isLogin()) {

        } else {

        }*/

// Get UserID
       /* usrHelper.getUserID()*/
