package com.think.linxuanxuan.ecos.request;

import android.util.Log;

import com.think.linxuanxuan.ecos.request.user.ResetPasswordRequest;

/**
 * 类描述：
 * Created by enlizhang on 2015/8/11.
 */
public class RequestFailUtils {


    /*** 账号未注册 */
    protected final static int RETURN_CODE_PHONE_UNREGISTERED = 8125;

    public static String parseResetPwdFailedCode(BaseRequest request, int code){

        Log.i("操作错误之","--------------------------");
        if(request instanceof ResetPasswordRequest){
            Log.i("操作错误之","--------------------------重置密码，code: " + code);
            String message = "重置密码失败";

            if(code == RETURN_CODE_PHONE_UNREGISTERED){
                Log.i("操作错误之","--------------------------手机未注册");
                message = "该手机未注册";
                return message;
            }

            return message;
        }

        return "操作失败";
    }


}
