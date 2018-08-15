package com.softgrid.shortvideo.iLogic;

import android.content.Context;

import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.model.Banner;
import com.softgrid.shortvideo.model.Bespoke;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Msg;
import com.softgrid.shortvideo.model.SearchCondition;
import com.softgrid.shortvideo.model.Tag;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.model.User;

import java.util.ArrayList;

/**
 * Created by tianfeng on 2018/6/5.
 */

public interface INetLogic {

    /**
     *
     * 注册
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

    /**
     * 自动登录
     * */
    void login(Context context, CallBackListener<User> listener);

    /**
     * 账号密码登录
     * */
    void login(Context context, String email, String pass, CallBackListener<User> listener);

    /**
     * 授权登录
     * */
    void auth(Context context, User user,  String authId, CallBackListener<User> listener);

    /**
     * 修改个人信息
     * */
    void updateInfo(Context context, User user, CallBackListener<User> listener);

    /**
     * 修改密码
     * */
    void updatePass(Context context, String oldPass, String newPass, CallBackListener<Integer> listener);

    /**
     * 录入房产
     * */
    void createBuilding(Context context, Building building, CallBackListener<Building> listener);

    /**
     * 修改房产信息
     * */
    void updateBuilding(Context context, Building building, CallBackListener<Building> listener);

    /**
     * 搜索房产
     *
     * @param context
     *
     * @param keyword 关键字
     *
     * @param page 页码
     *
     * @param condition 搜索条件实体
     *
     * @param listener 数据回调
     *
     * */
    void searchBuilding(Context context, String keyword, int page, SearchCondition condition, CallBackListener<Building> listener);

    /**
     * 关注房产
     * */
    void followBuilding(Context context, Building building, CallBackListener<Building> listener);

    /**
     * 取消关注
     * */
    void unFollowBuilding(Context context, Building building, CallBackListener<Building> listener);

    /**
     * 获取关注列表
     * */
    void getFollow(Context context, int page, CallBackListener<ArrayList<Building>> listener);

    /**
     * 预约看房
     * */
    void bespokeBuilding(Context context, Bespoke bespoke, CallBackListener<Bespoke> listener);

    /**获取预约列表*/
    void getBespoke(Context context, int page, CallBackListener<ArrayList<Bespoke>> listener);

    /**
     * 购买房产
     * */
    void bookBuilding(Context context, Transaction transaction, CallBackListener<Transaction> listener);

    /**获取交易列表*/
    void getTransaction(Context context, int page, CallBackListener<ArrayList<Transaction>> listener);

    /**
     * 交易状态更新
     * */
    void transactionUpdate(Context context, Transaction transaction, CallBackListener<Transaction> listener);

    /**
     * 为交易增加律师
     * */
    void transactionAddLawyer(Context context, Transaction transaction, User lawyer, CallBackListener<Transaction> listener);

    /**
     * 创建贷款
     * */
    void createLoans(Context context, Loans loans, CallBackListener<Loans> listener);

    /**
     * 更新贷款
     * */
    void updateLoans(Context context, Loans loans, CallBackListener<Loans> listener);

    /**
     * 获取贷款
     * */
    void getLoans(Context context, int page, CallBackListener<ArrayList<Loans>> listener);

    /**
     * 获取通知
     * */
    void getNotice(Context context, int page, CallBackListener<ArrayList<Msg>> listener);

    /**
     * 获取配置tag
     * */
    void getTags(Context context, CallBackListener<ArrayList<Tag>> listener);

    /**
     * 轮播推荐
     * */
    void getCarousel(Context context, CallBackListener<ArrayList<Banner>> listener);

    /**
     * 房屋推荐
     * */
    void getRecommendBuilding(Context context, int type, int page, CallBackListener<ArrayList<Building>> listener);

    /**
     * 获取中介推荐
     * */
    void getIntermediary(Context context, int page, CallBackListener<ArrayList<User>> listener);
}
