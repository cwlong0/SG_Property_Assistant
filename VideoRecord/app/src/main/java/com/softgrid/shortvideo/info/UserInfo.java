package com.softgrid.shortvideo.info;

import android.content.Context;
import android.content.SharedPreferences;

import com.softgrid.shortvideo.model.User;

/**
 * Created by tianfeng on 2018/6/11.
 */

public class UserInfo {

    private final String CONFIG_INFO = "config_user_info";

    private static UserInfo info;

    private User user;


    private UserInfo(){

    }

    public static UserInfo getInstance(){

        if (info == null){
            info = new UserInfo();
        }

        return info;
    }

    public void setToken(Context context, String token){

        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_token", token);
        editor.commit();
    }

    public String getToken(Context context){

        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);
        String token = sp.getString("user_token", null);
        return token;

    }

    public String getUserId(Context context){

        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);
        String id = sp.getString("user_id", null);
        return id;

    }
}
