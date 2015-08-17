package com.think.linxuanxuan.ecos.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.AccountDataService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyStringRequest extends Request<String>{

    private final Listener<String> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MyStringRequest(int method, String url, Listener<String> listener,
                           ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MyStringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap localHashMap = new HashMap();
        String value="";
        String token = AccountDataService.getSingleAccountDataService(MyApplication.getContext()).getToken();
        Log.e("MyStringRequest", "token---------"  + token);
        if(token!=null && !"".equals(token)){
            value = value+"TOKEN=" + token + ";" ;
        }

        String sessionId = AccountDataService.getSingleAccountDataService(MyApplication.getContext()).getAutocodeCookie();
        Log.e("MyStringRequest", "sessionId---------"  + sessionId);
        if(sessionId!=null && !"".equals(sessionId)){
            value = value+"SESSIONID=" + sessionId + ";" ;
        }

        localHashMap.put("Cookie", value);
        Log.e("MyStringRequest", "COOKIE VALUE---------" + value);
        return localHashMap;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");

            Log.e("Cookie-Set", "---------------" + rawCookies);

            if(rawCookies!=null){
                for(String value:rawCookies.split(";")){
                    String token = null;
                    String sessionId = null;
                    if(value.contains("TOKEN=")){
                        token = value.substring(6);
                        AccountDataService.getSingleAccountDataService(MyApplication.getContext()).saveToken(token);
                    }
                    if(value.contains("SESSIONID=")){
                        sessionId = value.substring(10);
                        System.out.println(sessionId);
                        AccountDataService.getSingleAccountDataService(MyApplication.getContext()).saveAutocodeCookie(sessionId);
                    }

                    Log.e("TOKEN", "---------------" + token);
                    Log.e("SESSIONID", "---------------" + sessionId);
                }
            }

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }


}
