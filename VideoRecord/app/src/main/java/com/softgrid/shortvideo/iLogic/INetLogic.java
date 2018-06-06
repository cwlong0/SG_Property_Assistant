package com.softgrid.shortvideo.iLogic;

import android.content.Context;

import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Notice;
import com.softgrid.shortvideo.model.SearchCondition;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.model.User;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2018/6/5.
 */

public interface INetLogic {

    /**
     *
     * 用户注册
     *
     * @param context 上下文
     *
     * @param user 用户实体
     *
     * @param verification 验证码
     *
     * @param listener 回调接口
     *
     * */
    void register(Context context, User user, String verification, CallBackListener<User> listener);

    void login(Context context, CallBackListener<User> listener);

    void login(Context context, String email, String pass, CallBackListener<User> listener);

    void auth(Context context, User user, CallBackListener<User> listener);

    void updateInfo(Context context, User user, CallBackListener<User> listener);

    void updatePass(Context context, String oldPass, String newPass, CallBackListener<Integer> listener);

    void createBuilding(Context context, Building building, CallBackListener<Building> listener);

    void updateBuilding(Context context, Building building, CallBackListener<Building> listener);

    void searchBuilding(Context context, String keyword, int page, SearchCondition condition, CallBackListener<Building> listener);

    void followBuilding(Context context, Building building, CallBackListener<Building> listener);

    void unFollowBuilding(Context context, Building building, CallBackListener<Building> listener);

    void getFollow(Context context, int page, CallBackListener<ArrayList<Building>> listener);

    void bookBuilding(Context context, Building building, Loans loans, CallBackListener<Transaction> listener);

    void transactionUpdate(Context context, Transaction transaction, CallBackListener<Transaction> listener);

    void createLoans(Context context, Loans loans, CallBackListener<Loans> listener);

    void updateLoans(Context context, Loans loans, CallBackListener<Loans> listener);

    void getLoans(Context context, CallBackListener<ArrayList<Loans>> listener);

    void getNotice(Context context, int page, CallBackListener<ArrayList<Notice>> listener);
}
