package com.softgrid.shortvideo.factory;

import com.softgrid.shortvideo.iLogic.INetLogic;
import com.softgrid.shortvideo.logicImpl.NetLogicImpl;

/**
 * Created by tianfeng on 2018/6/6.
 */

public class LogicFactory {

    private static LogicFactory factory;

    private INetLogic netLogic;

    private LogicFactory(){

    }

    public static LogicFactory getInstance(){

        if (factory == null){
            factory = new LogicFactory();
        }

        return factory;
    }

    public INetLogic getNetLogic(){
        if (netLogic == null){
            netLogic = new NetLogicImpl();
        }
        return netLogic;
    }

}
