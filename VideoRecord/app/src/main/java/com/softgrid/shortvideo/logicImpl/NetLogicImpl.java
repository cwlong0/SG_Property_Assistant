package com.softgrid.shortvideo.logicImpl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.softgrid.shortvideo.callback.CallBackListener;
import com.softgrid.shortvideo.callback.ErrorInfo;
import com.softgrid.shortvideo.http.HttpRequestInfo;
import com.softgrid.shortvideo.iLogic.INetLogic;
import com.softgrid.shortvideo.info.UserInfo;
import com.softgrid.shortvideo.model.Banner;
import com.softgrid.shortvideo.model.Bespoke;
import com.softgrid.shortvideo.model.Building;
import com.softgrid.shortvideo.model.HotWord;
import com.softgrid.shortvideo.model.Loans;
import com.softgrid.shortvideo.model.Msg;
import com.softgrid.shortvideo.model.SearchCondition;
import com.softgrid.shortvideo.model.Tag;
import com.softgrid.shortvideo.model.Transaction;
import com.softgrid.shortvideo.model.User;
import com.softgrid.shortvideo.tool.SecurityTool;
import com.softgrid.shortvideo.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tianfeng on 2018/6/6.
 */

public class NetLogicImpl implements INetLogic {

    private final String TAG = "NetLogicImpl";
    private final int ERROR = 0;
    private final int COMPLETE = 1;

    private final String KEY_CALLBACK = "l";
    private final String KEY_DATA = "d";

    private Handler handler;
    private OkHttpClient client;
    private Gson gson;

    @SuppressLint("HandlerLeak")
    public NetLogicImpl(){
        client = new OkHttpClient();
        gson = new Gson();

        handler  = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                HashMap<String, Object> obj = (HashMap)msg.obj;
                CallBackListener listener = (CallBackListener) obj.get(KEY_CALLBACK);
                Object dataObject = obj.get(KEY_DATA);

                switch (msg.what) {
                    case COMPLETE:
                        if (listener != null) {
                            if (dataObject != null) {
                                listener.onComplete(dataObject);
                            }
                            else {
                                listener.onComplete(null);
                            }
                        }
                        break;

                    case ERROR:
                        if (listener != null) {
                            if (dataObject != null) {
                                listener.onError((ErrorInfo) dataObject);
                            }
                            else {
                                listener.onError(null);
                            }

                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void register(final Context context, final User user, String verification, final CallBackListener<User> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();
            params.put("mail", user.getEmail());
            params.put("pass", SecurityTool.md5_32Bit(user.getPass()));
            params.put("name", user.getName());

            if (user.getImage() != null){
                params.put("image", user.getImage());
            }
            if (user.getType() > 0){
                params.put("type", user.getType());
            }
            if (user.getChannel() > 0){
                params.put("channel", user.getChannel());
            }
            if (user.getWechat() != null){
                params.put("wechat", user.getWechat());
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/register", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User userModel = gson.fromJson(dataObject.getString("info"), User.class);
                    userModel.setPass(user.getPass());

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(userModel);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, userModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });

    }

    @Override
    public void login(final Context context, final CallBackListener<User> listener) {

        final  User user = UserInfo.getInstance().getUser();

        String body = null;
        try {
            JSONObject params = new JSONObject();
            params.put("mail", user.getEmail());
            params.put("pass", SecurityTool.md5_32Bit(user.getPass()));

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/login", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User user = gson.fromJson(dataObject.getString("info"), User.class);

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(user);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, user);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void login(final Context context, String email, final String pass, final CallBackListener<User> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();
            params.put("mail", email);
            params.put("pass", SecurityTool.md5_32Bit(pass));

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/login", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User user = gson.fromJson(dataObject.getString("info"), User.class);
                    user.setPass(pass);

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(user);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, user);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void auth(final Context context, User user, String authId, final CallBackListener<User> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("unionid", authId);
            params.put("nickname", user.getName());

            if (user.getImage() != null){
                params.put("image", user.getImage());
            }
            if (user.getType() > 0){
                params.put("type", user.getType());
            }
            if (user.getChannel() > 0){
                params.put("channel", user.getChannel());
            }
            if (user.getGender() > 0){
                params.put("sex", user.getGender());
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/auth", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User user = gson.fromJson(dataObject.getString("info"), User.class);

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(user);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, user);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void updateInfo(final Context context, User user,final CallBackListener<User> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            if (user.getEmail() != null){
                params.put("email", user.getEmail());
            }
            if (user.getName() != null){
                params.put("name", user.getName());
            }
            if (user.getNric() != null){
                params.put("nric", user.getNric());
            }
            if (user.getTel() != null){
                params.put("tel", user.getTel());
            }
            if (user.getTel2() != null){
                params.put("tel2", user.getTel2());
            }
            if (user.getAddress() != null){
                params.put("address", user.getAddress());
            }
            if (user.getImage() != null){
                params.put("image", user.getImage());
            }
            if (user.getCompany() != null){
                params.put("company", user.getCompany());
            }
            if (user.getIntro() != null){
                params.put("intro", user.getIntro());
            }
            if (user.getBusiness() != null){
                params.put("business", user.getBusiness());
            }
            if (user.getTags() != null && user.getTags().size() > 0){
                String ids = null;
                Tag tag;
                for (int i = 0; i < user.getTags().size(); i++){
                    tag = user.getTags().get(i);
                    if (ids == null){
                        ids = tag.getId();
                    }
                    else {
                        ids = ids + "," + tag.getId();
                    }
                }
                params.put("tags", ids);
            }
            if (user.getType() > 0){
                params.put("type", user.getType());
            }
            if (user.getWechat() != null){
                params.put("wechat", user.getWechat());
            }
            if (user.getBirthday() > 0){
                params.put("birthday", user.getBirthday());
            }
            if (user.getGender() > 0){
                params.put("gender", user.getGender());
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/update", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User user = gson.fromJson(dataObject.getString("info"), User.class);

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(user);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, user);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void updatePass(final Context context, String oldPass, final String newPass, final CallBackListener<Integer> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("oldPass", SecurityTool.md5_32Bit(oldPass));
            params.put("newPass", SecurityTool.md5_32Bit(newPass));

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/updatePass", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    String token = dataObject.getString("token");
                    User user = gson.fromJson(dataObject.getString("info"), User.class);
                    user.setPass(newPass);

                    UserInfo.getInstance().setToken(context, token);
                    UserInfo.getInstance().setUser(user);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, user);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void createBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void updateBuilding(Context context, Building building, CallBackListener<Building> listener) {

    }

    @Override
    public void searchBuilding(final Context context, String keyword, int page, SearchCondition condition, final CallBackListener<ArrayList<Building>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("key", keyword);
            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);
            if (condition != null) {
                if (condition.getIsResale() >= 0) {
                    params.put("isResale", condition.getIsResale());
                }
                if (condition.getIsAuth() >= 0) {
                    params.put("isAuth", condition.getIsAuth());
                }
                if (condition.getType() > 0) {
                    params.put("type", condition.getType());
                }
                if (condition.getRooms() != null) {
                    params.put("rooms", condition.getRooms());
                }
                if (condition.getDecorate() > 0) {
                    params.put("decorate", condition.getDecorate());
                }
                if (condition.getOrientation() > 0) {
                    params.put("orientation", condition.getOrientation());
                }
                if (condition.getRegion() > 0) {
                    params.put("region", condition.getRegion());
                }
                if (condition.getPd() > 0) {
                    params.put("pd", condition.getPd());
                }
                if (condition.getLongitude() > 0) {
                    params.put("longitude", condition.getLongitude());
                }
                if (condition.getLatitude() > 0) {
                    params.put("latitude", condition.getLatitude());
                }
                if (condition.getRange() > 0) {
                    params.put("range", condition.getRange());
                }
                if (condition.getArea() != null) {
                    params.put("area", condition.getArea());
                }
                if (condition.getUnitPrice() != null) {
                    params.put("unitPrice", condition.getUnitPrice());
                }
                if (condition.getTotalPrice() != null) {
                    params.put("totalPrice", condition.getTotalPrice());
                }
                if (condition.getFeatureTags() != null && condition.getFeatureTags().size() > 0){
                    String ids = null;
                    Tag tag;
                    for (int i = 0; i < condition.getFeatureTags().size(); i++){
                        tag = condition.getFeatureTags().get(i);
                        if (ids == null){
                            ids = tag.getId();
                        }
                        else {
                            ids = ids + "," + tag.getId();
                        }
                    }
                    params.put("featureTags", ids);
                }
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/search", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Building> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Building building = gson.fromJson(item.toString(), Building.class);
                        dataList.add(building);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Building> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        Building building = new Building(true);
                        building.setValid(true);
                        dataList.add(building);
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }


    @Override
    public void followBuilding(final Context context, Building building, final CallBackListener<Building> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("buildId", building.getId());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/follow", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    int count = dataObject.getJSONObject("info").getInt("count");

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, count);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void unFollowBuilding(final Context context, Building building, final CallBackListener<Building> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("buildId", building.getId());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/unFollow", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    int count = dataObject.getJSONObject("info").getInt("count");

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, count);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getFollow(final Context context, int page, final CallBackListener<ArrayList<Building>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/getFollow", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Building> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Building building = gson.fromJson(item.toString(), Building.class);
                        dataList.add(building);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Building> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Building(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void bespokeBuilding(final Context context, Bespoke bespoke, final CallBackListener<Bespoke> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("id", bespoke.getBuilding().getId());
            params.put("bespokeTime", bespoke.getBespokeTime());
            if (bespoke.getIntermediary() != null){
                params.put("intermediary", bespoke.getIntermediary().getId());
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "bespoke/create", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Bespoke bespokeModel = gson.fromJson(dataObject.getString("info"),
                            Bespoke.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, bespokeModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getBespoke(Context context, int page, final CallBackListener<ArrayList<Bespoke>> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "bespoke/getBespoke", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Transaction> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Transaction transaction = gson.fromJson(item.toString(), Transaction.class);
                        dataList.add(transaction);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Bespoke> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Bespoke(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void bookBuilding(final Context context, Transaction transaction, final CallBackListener<Transaction> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("buildId", transaction.getBuilding().getId());
            params.put("buyTime", transaction.getBuyTime());
            if (transaction.getIntermediary() != null){
                params.put("intermediary", transaction.getIntermediary().getId());
            }

            if (transaction.getLoans() != null){
                params.put("haveLoans", 1);
                JSONObject loansParams = new JSONObject();
                if (transaction.getLoans().getDesc() != null){
                    loansParams.put("desc", transaction.getLoans().getDesc());
                }
                if (transaction.getLoans().getTotal() > 0){
                    loansParams.put("total", transaction.getLoans().getTotal());
                }
                if (transaction.getLoans().getYear() > 0){
                    loansParams.put("year", transaction.getLoans().getYear());
                }
                if (transaction.getLoans().getBanker() != null){
                    loansParams.put("banker", transaction.getLoans().getBanker().getId());
                }
                params.put("loans", loansParams);
            }
            else {
                params.put("haveLoans", 0);
            }

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/book", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Transaction transactionModel = gson.fromJson(dataObject.getString("info"),
                            Transaction.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, transactionModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getTransaction(final Context context, int page, final CallBackListener<ArrayList<Transaction>> listener){
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/getTransaction", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Transaction> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Transaction transaction = gson.fromJson(item.toString(), Transaction.class);
                        dataList.add(transaction);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Transaction> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Transaction(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void transactionUpdate(final Context context, Transaction transaction, final CallBackListener<Transaction> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("tId", transaction.getId());
            params.put("code", transaction.getStatus().getCode());
            params.put("desc", transaction.getStatus().getDesc());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/transactionUpdate", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Transaction transactionModel = gson.fromJson(dataObject.getString("info"),
                            Transaction.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, transactionModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void transactionAddLawyer(final Context context, Transaction transaction, User lawyer, final CallBackListener<Transaction> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("tId", transaction.getId());
            params.put("userId", lawyer.getId());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/transactionAddLawyer", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Transaction transactionModel = gson.fromJson(dataObject.getString("info"),
                            Transaction.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, transactionModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void createLoans(final Context context, Loans loans, final CallBackListener<Loans> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("tId", loans.getTransaction().getId());
            params.put("buildId", loans.getBuilding().getId());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/addLoans", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Loans loansModel = gson.fromJson(dataObject.getString("info"),
                            Loans.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, loansModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void updateLoans(final Context context, Loans loans, final CallBackListener<Loans> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("lId", loans.getId());
            params.put("code", loans.getCode());
            params.put("desc", loans.getDesc());

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/loansUpdate", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");

                    Loans loansModel = gson.fromJson(dataObject.getString("info"),
                            Loans.class);

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, loansModel);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getLoans(final Context context, int page, final CallBackListener<ArrayList<Loans>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "buy/getLoans", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Loans> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Loans loans = gson.fromJson(item.toString(), Loans.class);
                        dataList.add(loans);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);

                    ArrayList<Loans> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Loans(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });

    }

    @Override
    public void getNotice(final Context context, int page, final CallBackListener<ArrayList<Msg>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "notice/getNotice", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Msg> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Msg notice = gson.fromJson(item.toString(), Msg.class);
                        dataList.add(notice);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Msg> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Msg(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getTags(final Context context, final CallBackListener<ArrayList<Tag>> listener) {

        String body = null;
        body = HttpRequestInfo.getInstance()
                .getRequestParams(context, "config/tags", "");

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Tag> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Tag tag = gson.fromJson(item.toString(), Tag.class);
                        dataList.add(tag);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getCarousel(final Context context, final CallBackListener<ArrayList<Banner>> listener) {
        String body = null;
        body = HttpRequestInfo.getInstance()
                .getRequestParams(context, "config/carousel", "");

        //加密
        body = SecurityTool.encodeStatic(body);

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Banner> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Banner banner = gson.fromJson(item.toString(), Banner.class);
                        dataList.add(banner);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {
                    ArrayList<Banner> dataList = new ArrayList<>();
                    for (int i = 0; i < 5; i++){
                        dataList.add(new Banner(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public void getRecommendBuilding(final Context context, int type, int page, final CallBackListener<ArrayList<Building>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("type", type);
            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "config/recmmendBuilding", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<Building> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        Building building = gson.fromJson(item.toString(), Building.class);
                        dataList.add(building);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<Building> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new Building(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getIntermediary(Context context, int page, final CallBackListener<ArrayList<User>> listener) {
        String body = null;
        try {
            JSONObject params = new JSONObject();

            params.put("page", page);
            params.put("count", Constant.PAGE_COUNT);

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "user/getIntermediary", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<User> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        User user = gson.fromJson(item.toString(), User.class);
                        dataList.add(user);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<User> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new User(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });
    }

    @Override
    public void getHotWords(Context context, final CallBackListener<ArrayList<HotWord>> listener) {

        String body = null;
        try {
            JSONObject params = new JSONObject();

            String paramsString = params.toString();

            body = HttpRequestInfo.getInstance()
                    .getRequestParams(context, "word/getHotwords", paramsString);

            //加密
            body = SecurityTool.encodeStatic(body);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url(HttpRequestInfo.HOST)
                .post(RequestBody.create(mediaType, body))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONObject object = new JSONObject(response.body().string());
                    JSONObject dataObject = object.getJSONObject("d");
                    JSONArray dataArray = dataObject.getJSONArray("info");

                    ArrayList<HotWord> dataList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++){

                        JSONObject item = dataArray.getJSONObject(i);
                        HotWord wordModel = gson.fromJson(item.toString(), HotWord.class);
                        dataList.add(wordModel);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    ArrayList<HotWord> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++){
                        dataList.add(new HotWord(true));
                    }
                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                    msg.obj = hashMap;
                    handler.sendMessage(msg);

//                    msg.obj = hashMap;
//                    handler.sendMessage(msg);
                }

            }
        });

    }

    @Override
    public void searchThink(final Context context, final String key, final CallBackListener<ArrayList<String>> listener) {
        // TODO Auto-generated method stub

        MediaType mediaType = MediaType.parse(HttpRequestInfo.MEDIA_TYPE_DEF);
        Request request = new Request.Builder()
                .url("http://api.bing.com/osjson.aspx?query=" + URLEncoder.encode(key))
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                msg.what = ERROR;
                ErrorInfo errorInfo = new ErrorInfo(e);
                hashMap.put(KEY_DATA, errorInfo);
                msg.obj = hashMap;
                handler.sendMessage(msg);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Message msg = handler.obtainMessage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(KEY_CALLBACK, listener);

                try{
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    ArrayList<String> dataList = new ArrayList();
                    JSONArray dataJsonArray = jsonArray.getJSONArray(1);
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        String stringData = dataJsonArray.getString(i);
                        dataList.add(stringData);
                    }

                    msg.what = COMPLETE;
                    hashMap.put(KEY_DATA, dataList);
                }
                catch (Exception e){

                    msg.what = ERROR;
                    ErrorInfo errorInfo = new ErrorInfo(e);
                    hashMap.put(KEY_DATA, errorInfo);

                }
                finally {

                    msg.obj = hashMap;
                    handler.sendMessage(msg);
                }

            }
        });
    }
}
