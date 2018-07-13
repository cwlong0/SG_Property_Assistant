package com.softgrid.shortvideo.logicImpl;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.base64.Base64;
import com.google.gson.Gson;
import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.callback.ErrorInfo;
import com.softgrid.shortvideo.db.DBHelper;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.FavModel;
import com.softgrid.shortvideo.model.HisModel;
import java.util.ArrayList;

/**
 * Created by tianfeng on 2017/9/17.
 */

public class LocalApi {

    private final String TAG = "LocalApi";

    private final String CONFIG_INFO = "config_local_info";
    private final String split = "..,,..";

    private static LocalApi localApi;

    private Gson gson;
    private DBHelper dbHelper;

    private LocalApi(Context context){

        gson = new Gson();
        dbHelper = new DBHelper(context);
    }

    public static LocalApi getInstance(Context context){
        if (localApi == null){
            localApi = new LocalApi(context);
        }
        return localApi;
    }

    public boolean favIsExist(Object dataModel){

        FavModel favModel = new FavModel();

        if (dataModel instanceof Building){
            Building building = (Building) dataModel;
            favModel.setType(FavModel.FAV_BUILDING);
            favModel.setFavId(building.getId());
            FavModel existModel = dbHelper.favIsExist(favModel, true);
            if (existModel != null){
                return true;
            }
        }

        return false;
    }

    public void saveFav(Object dataModel, CallBackListener<FavModel> listener){


        long id;

        FavModel favModel = new FavModel();
        favModel.setFavCreatedTime(String.valueOf(System.currentTimeMillis()));

        if (dataModel instanceof Building){
            Building building = (Building) dataModel;
            favModel.setType(FavModel.FAV_BUILDING);
            favModel.setFavId(building.getId());
            favModel.setFavUpdatedTime(String.valueOf(building.getUpdateAt()));
            favModel.setFavContent(Base64.encode(gson.toJson(building).getBytes()));
        }

        dbHelper.openDB();
        FavModel existModel = dbHelper.favIsExist(favModel, false);
        if (existModel != null){
            dbHelper.deleteFav(existModel, false);
        }
        id = dbHelper.saveFav(favModel, false);
        dbHelper.closeDB(true);

        if (listener != null){
            if (id > -1){
                existModel.setId(id);
                listener.onComplete(favModel);
            }
            else {
                listener.onError(new ErrorInfo());
            }
        }
    }

    public void deleteFav(Object dataModel, CallBackListener<FavModel> listener){

        FavModel favModel = new FavModel();
        if (dataModel instanceof FavModel){
            favModel = (FavModel) dataModel;
        }
        if(dataModel instanceof Building){
            Building building = (Building)dataModel;
            favModel.setType(FavModel.FAV_BUILDING);
            favModel.setFavId(String.valueOf(building.getId()));
        }

        dbHelper.deleteFav(favModel, true);

        if (listener != null){
            listener.onComplete(null);
        }
    }

    public void deleteFavs(ArrayList<FavModel> dataList, CallBackListener listener){

        if (dataList != null && dataList.size() > 0){
            dbHelper.openDB();
            for (int i = 0; i < dataList.size(); i++){
                dbHelper.deleteFav(dataList.get(i), false);
            }
            dbHelper.closeDB(true);
        }

        if (listener != null){
            listener.onComplete(null);
        }
    }

    public void getFavByType(int type, CallBackListener<ArrayList<FavModel>> listener){

        ArrayList<FavModel> dataList = dbHelper.getFavByType(type, true);

        for (int i = 0; i < dataList.size(); i++){
            FavModel favModel = dataList.get(i);
            try {

                if (type == FavModel.FAV_BUILDING) {
                    String dataString = new String(Base64.decode(favModel.getFavContent().getBytes()));
                    Building building = gson.fromJson(dataString, Building.class);
                    favModel.setFavContentModel(building);
                }

            }catch (Exception e){
                e.printStackTrace();
                if (listener != null){
                    listener.onError(new ErrorInfo(e));
                }
                return;
            }
        }
        if (listener != null){
            listener.onComplete(dataList);
        }
    }

    public void saveHis(Building building, CallBackListener<HisModel> listener){
        if (building == null){
            return;
        }

        long id;

        HisModel hisModel = new HisModel();
        hisModel.setHisCreatedTime(String.valueOf(System.currentTimeMillis()));

        hisModel.setHisType(HisModel.HIS_BUILDING);
        hisModel.setHisId(String.valueOf(building.getId()));
        hisModel.setHisUpdated(0);
        hisModel.setHisUpdatedTime(String.valueOf(building.getUpdateAt()));
        hisModel.setHisContent(Base64.encode(gson.toJson(building).getBytes()));

        dbHelper.openDB();
        HisModel existModel = dbHelper.hisIsExist(hisModel, false);
        if (existModel != null){
            dbHelper.deleteHis(existModel, false);
        }
        id = dbHelper.saveHis(hisModel, false);
        dbHelper.closeDB(true);

        if (listener != null){
            if (id > -1){
                hisModel.setId(id);
                listener.onComplete(hisModel);
            }
            else {
                listener.onError(new ErrorInfo());
            }
        }
    }


    public void getHisByType(int type, CallBackListener<ArrayList<HisModel>> listener){

        ArrayList<HisModel> dataList = dbHelper.getHis(type, true);

        for (int i = 0; i < dataList.size(); i++){
            HisModel hisModel = dataList.get(i);
            try {

                if (type == HisModel.HIS_BUILDING){
                    String contentString = new String(Base64.decode(hisModel.getHisContent().getBytes()));
                    Building building = gson.fromJson(contentString, Building.class);
                    hisModel.setHisContentModel(building);
                }

            }catch (Exception e){
                e.printStackTrace();
                if (listener != null){
                    listener.onError(new ErrorInfo(e));
                }
                return;
            }
        }
        if (listener != null){
            listener.onComplete(dataList);
        }
    }

    public void getFirstHisByType(int type, CallBackListener<HisModel> listener){

        ArrayList<HisModel> dataList = dbHelper.getHis(type, true);
        HisModel hisModel = null;
        if (dataList.size() > 0){
            hisModel = dataList.get(0);
            try {

                if (type == HisModel.HIS_BUILDING){
                    String contentString = new String(Base64.decode(hisModel.getHisContent().getBytes()));
                    Building building = gson.fromJson(contentString, Building.class);
                    hisModel.setHisContentModel(building);
                }

            }catch (Exception e){
                e.printStackTrace();
                if (listener != null){
                    listener.onError(new ErrorInfo(e));
                }
                return;
            }
        }
        if (listener != null){
            if (hisModel != null){
                listener.onComplete(hisModel);
            }

        }
    }

    public void deleteHis(ArrayList<HisModel> dataList, CallBackListener listener){
        if (dataList != null && dataList.size() > 0){
            dbHelper.openDB();
            for (int i = 0; i < dataList.size(); i++){
                dbHelper.deleteHis(dataList.get(i), false);
            }
            dbHelper.closeDB(true);
        }

        if (listener != null){
            listener.onComplete(null);
        }
    }

    public void clearHis(CallBackListener listener){

        dbHelper.deleteAllHis(true);

        if (listener != null){
            listener.onComplete(null);
        }
    }

    public void addKeyWordHis(Context context, String keyword){

        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);

        String keywords = sp.getString("keyword_history", null);

        if (keywords != null){
            if (keywords.contains(keyword + split)){
                keywords = keywords.replace(keyword + split, "");
            }
            else if(keywords.contains(split + keyword)){
                keywords = keywords.replace(split + keyword, "");
            }
            else if (keywords.equals(keyword)){
                keywords = keywords.replace(keyword, "");
            }
        }

        if (keywords != null && !keywords.equals("")){
            keywords = keyword + split + keywords;
        }
        else {
            keywords = keyword;
        }

        if (keywords != null){
            String[] keys = keywords.split(split);
            if (keys.length > 20){
                keywords = keywords.substring(0, keywords.lastIndexOf(split));
            }
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("keyword_history", keywords);
        editor.commit();
    }

    public ArrayList<String> getKeyWordHis(Context context){

        ArrayList<String> keywordList = null;

        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);
        String keywords = sp.getString("keyword_history", null);
        if (keywords != null){
            String[] keys = keywords.split(split);
            keywordList = new ArrayList<>();
            for (int i = 0; i < keys.length; i++){
                keywordList.add(keys[i]);
            }
        }
        return keywordList;
    }

    public void clearKeyWordHis(Context context){
        SharedPreferences sp = context.getSharedPreferences(CONFIG_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("keyword_history");
        editor.commit();
    }


}
