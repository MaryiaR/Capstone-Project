package com.udacity.rasulava.capstone_project;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.rasulava.capstone_project.model.response.ResponseFood;
import com.udacity.rasulava.capstone_project.model.response.FoodDetails;
import com.udacity.rasulava.capstone_project.model.response.FoodDetailsResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mrasulava on 8/9/2016.
 */
public class RequestHelper {

    private static final String LOG_TAG = "RequestHelper";

    private static final String APP_METHOD_GET = "GET";
    private static final String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private OkHttpClient client = new OkHttpClient();

    /**
     * Search product by id
     *
     * @param id
     * @return
     */
    public void getFoodById(final Context context, String id, final ResultListener<FoodDetails> listener) {
        if (!Utils.haveInternetConnection(context)) {
            listener.onFailure();
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("food_id", id);
        URL url = getUrl(context, "food.get", map);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            Handler mainHandler = new Handler(context.getMainLooper());

            @Override
            public void onFailure(final Call call, IOException e) {
                e.printStackTrace();
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                if (!TextUtils.isEmpty(res)) {
                    try {
                        final FoodDetails foods = new Gson().fromJson(res, FoodDetailsResponse.class).getFoods();
                        List<FoodDetails.Serving> servingList = new ArrayList<>();
                        FoodDetails.Servings servings = new FoodDetails.Servings();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject servingsJson = jsonObject.optJSONObject("food").optJSONObject("servings");
                        JSONObject serving = servingsJson.optJSONObject("serving");
                        if (serving == null) {
                            JSONArray servingArray = servingsJson.optJSONArray("serving");
                            if (servingArray != null) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<FoodDetails.Serving>>() {
                                }.getType();
                                List<FoodDetails.Serving> list = gson.fromJson(servingArray.toString(), type);
                                servingList.addAll(list);
                                servings.setServing(servingList);
                                foods.setmServings(servings);
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onSuccess(foods);
                                    }
                                });
                            } else {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure();
                                    }
                                });
                            }
                        } else {
                            servingList.add(new Gson().fromJson(serving.toString(), FoodDetails.Serving.class));
                            servings.setServing(servingList);
                            foods.setmServings(servings);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onSuccess(foods);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (!TextUtils.isEmpty(res)) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(new Gson().fromJson(res, FoodDetailsResponse.class).getFoods());
                        }
                    });
                } else {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure();
                        }
                    });
                }
            }
        });
    }


    /**
     * Search products by name
     *
     * @param name
     * @return
     */
    public List<ResponseFood> getFood(Context context, String name) {
        List<ResponseFood> resultList = new ArrayList<>();
        if (!Utils.haveInternetConnection(context)) {
            return resultList;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("search_expression", name);
        URL url = getUrl(context, "foods.search", map);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String responseJson = response.body().string();
            if (!TextUtils.isEmpty(responseJson)) {
                try {
                    JSONObject jsonObject = new JSONObject(responseJson);
                    JSONObject foods = jsonObject.optJSONObject("foods");
                    if (foods != null) {
                        JSONObject food = foods.optJSONObject("food");
                        if (food == null) {
                            JSONArray foodArray = foods.optJSONArray("food");
                            if (foodArray != null) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<ResponseFood>>() {
                                }.getType();
                                List<ResponseFood> list = gson.fromJson(foodArray.toString(), type);
                                resultList.addAll(list);
                            }
                        } else {
                            resultList.add(new Gson().fromJson(food.toString(), ResponseFood.class));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public URL getUrl(Context context, String methodName, HashMap<String, String> paramsMap) {
        List<String> params = new ArrayList<>(Arrays.asList(getOauthParams(context)));
        String[] template = new String[1];
        params.add("method=" + methodName);
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }
        params.add("oauth_signature=" + getOauthSignature(context, APP_METHOD_GET, APP_URL, params.toArray(template)));
        URL url = null;
        try {
            url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String[] getOauthParams(Context context) {
        return new String[]{
                "oauth_consumer_key=" + context.getString(R.string.fatsecret_api_key),
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" + Long.MAX_VALUE,
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"};
    }


    private static String getOauthSignature(Context context, String method, String uri, String[] params) {
        String appSecret = context.getString(R.string.fatsecret_app_secret);
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        appSecret += "&";
        SecretKey sk = new SecretKeySpec(appSecret.getBytes(), HMAC_SHA1_ALGORITHM);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w(LOG_TAG, e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w(LOG_TAG, e.getMessage());
            return null;
        }
    }

    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }

}
