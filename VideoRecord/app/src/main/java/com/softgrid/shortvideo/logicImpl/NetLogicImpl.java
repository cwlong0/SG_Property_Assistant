package com.softgrid.shortvideo.logicImpl;

import android.content.Context;

import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.http.HttpRequestInfo;
import com.softgrid.shortvideo.iLogic.INetLogic;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Notice;
import com.softgrid.shortvideo.model.SearchCondition;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.model.User;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2018/6/6.
 */

public class NetLogicImpl implements INetLogic {

    @Override
    public void register(Context context, User user, String verification, CallBackListener<User> listener) {

        String url = null;
        String body = null;
        try {
            JSONObject params = new JSONObject();
            params.put("mail", user.getEmail());

            String paramsString = params.toString();

            url = "http://www.baidu.com";
            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "account/binding", paramsString);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void login(Context context, CallBackListener<User> listener) {

    }

    @Override
    public void login(Context context, String email, String pass, CallBackListener<User> listener) {

    }

    @Override
    public void auth(Context context, User user, CallBackListener<User> listener) {

    }

    @Override
    public void updateInfo(Context context, User user, CallBackListener<User> listener) {

    }

    @Override
    public void updatePass(Context context, String oldPass, String newPass, CallBackListener<Integer> listener) {

    }

    @Override
    public void createBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void updateBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void searchBuilding(Context context, String keyword, int page, SearchCondition condition, CallBackListener<Building> listener) {

    }

    @Override
    public void followBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void unFollowBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void getFollow(Context context, int page, CallBackListener<ArrayList<Building>> listener) {

    }

    @Override
    public void bookBuilding(Context context, Building building, Loans loans, CallBackListener<Transaction> listener) {

    }

    @Override
    public void transactionUpdate(Context context, Transaction transaction, CallBackListener<Transaction> listener) {

    }

    @Override
    public void createLoans(Context context, Loans loans, CallBackListener<Loans> listener) {

    }

    @Override
    public void updateLoans(Context context, Loans loans, CallBackListener<Loans> listener) {

    }

    @Override
    public void getLoans(Context context, CallBackListener<ArrayList<Loans>> listener) {

    }

    @Override
    public void getNotice(Context context, int page, CallBackListener<ArrayList<Notice>> listener) {

    }
}
