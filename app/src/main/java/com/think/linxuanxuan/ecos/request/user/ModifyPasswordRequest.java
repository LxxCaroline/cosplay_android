package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.MyStringRequest;
import com.think.linxuanxuan.ecos.request.NorResponce;
import com.think.linxuanxuan.ecos.utils.StringUtils;

import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: ModifyPasswordRequest
 * @Description: 修改密码
 * @date 2015年7月26日 上午10:31:04
 */
public class ModifyPasswordRequest extends BaseRequest {

    //请求参数键
    /**
     * 精度
     */
    public static final String OLD_PWD = "oldPwd";

    /**
     * 维度
     */
    public static final String NEW_PWD = "newPwd";


    //响应参数键

    NorResponce mNorResponce;

    public void request(NorResponce norResponce, final String oldPwd, final String newPwd) {
        super.initBaseRequest(norResponce);
        mNorResponce = norResponce;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.MODIFY_PASSWORD, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(OLD_PWD, StringUtils.hashKeyForDisk(oldPwd));
                map.put(NEW_PWD, StringUtils.hashKeyForDisk(newPwd));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, ModifyPasswordRequest.this.getUrl(RequestUrlConstants.MODIFY_PASSWORD, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }

    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);
        if (mNorResponce != null) {
            mNorResponce.success();
        } else {
            traceError(TAG, "回调接口为null");
        }
    }


}

