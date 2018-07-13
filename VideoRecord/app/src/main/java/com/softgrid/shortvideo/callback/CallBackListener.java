package com.softgrid.shortvideo.callback;

/**
 * Created by tianfeng on 2018/6/5.
 */

public interface CallBackListener<T> {

    /**
     * 通过缓存完成
     *
     * @param data 完成后得到的数据
     *
     * */
    void onCache(T data);

    /**
     * 完成
     *
     * @param data 完成后得到的数据
     *
     * */
     void onComplete(T data);

    /**
     * 出错
     *
     * @param errorInfo 错误信息
     *
     * */
     void onError(ErrorInfo errorInfo);

}
