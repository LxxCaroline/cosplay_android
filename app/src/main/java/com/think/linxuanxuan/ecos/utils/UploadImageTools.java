package com.think.linxuanxuan.ecos.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.netease.cloud.nos.android.core.CallRet;
import com.netease.cloud.nos.android.core.Callback;
import com.netease.cloud.nos.android.core.UploadTaskExecutor;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.core.WanNOSObject;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;

import org.json.JSONException;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 类描述：NOS上传图片工具类
 * Created by enlizhang on 2015/7/23.
 */
public class UploadImageTools {

    public static String LOGTAG = "上传图片";


    static String accessKey = "08536894201c464fb5c0c9dd8b28151d";
    static String secretKey = "5bfa6c25c4294622ade7bad17c57b8b1";
    static String BUCKET_NAME = "urscdn";

    private static UploadTaskExecutor executor;

    /*** 缩略图像素数(长*宽) */
    static String THUMB_PIXELS = "20000";

    /***
     * 上传图片
     * @param file 图片文件对象
     * @param callBack 回掉函数{@link UploadCallBack}
     * @param context
     * @param isSys 是否同步，true:同步 false:异步
     */
    public static void uploadImageSys(final File file, final UploadCallBack callBack,
                          final Context context,boolean isSys){
        if(file==null || !file.exists()){
            Log.e("上传图片","无效的file对象" );
            if(file!=null)
                Log.e("上传图片","file路径:" + file.getAbsolutePath());

            callBack.fail();
        }

        WanNOSObject wanNOSObject = new WanNOSObject();

        long expires = System.currentTimeMillis() / 1000 + 1000;

        try {
            String prfix = String.valueOf(System.currentTimeMillis());

            String key = StringUtils.hashKeyForDisk(prfix+file.getName());
            Log.e("上传图片",key);
            final String token = Util.getToken(BUCKET_NAME, key, expires, accessKey, secretKey, null, null);
            LogUtil.d(LOGTAG, "token is: " + token);

            wanNOSObject.setNosBucketName(BUCKET_NAME);
            wanNOSObject.setUploadToken(token);
//            final String key = fileName;
//            final String key = file.getName();
            wanNOSObject.setNosObjectName(key);

            if (file.getName().contains(".jpg")) {
                wanNOSObject.setContentType("image/jpeg");
            }
            if (file.getName().contains(".png")) {
                wanNOSObject.setContentType("image/png");
            }
            if (file.getName().contains(".mp4")) {
                wanNOSObject.setContentType("video/mp4");
            }

            String uploadContext = null;
            if (Util.getData(context, file.getAbsolutePath()) != null
                    && !Util.getData(context, file.getAbsolutePath()).equals("")) {
                uploadContext = Util.getData(context, file.getAbsolutePath());
            }

            if(isSys)
                try {
                    uploadImageSyn(wanNOSObject, file,key, callBack, context, uploadContext);
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                }
            else
                try {
                    uploadImageAsy(wanNOSObject, file,key, callBack, context, uploadContext);
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                }


        } catch (JSONException e) {
            e.printStackTrace();
            callBack.fail();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            callBack.fail();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            callBack.fail();
        }
    }

    /***
     *
     * @param wanNOSObject Nos上传对象
     * @param wanNOSObject
     * @param file
     * @param callBack
     * @param context
     * @param uploadContext
     * @return
     */
    private static void uploadImageSyn( WanNOSObject wanNOSObject,final File file,final String key,
                                                      final UploadCallBack callBack, final Context context,
                                                      String uploadContext) throws InvalidParameterException {

        WanAccelerator.putFileByHttp(context, file, file.getAbsoluteFile(), uploadContext, wanNOSObject, new Callback() {
            @Override
            public void onUploadContextCreate(Object fileParam, String oldUploadContext, String newUploadContext) {
                LogUtil.e(LOGTAG, "context create: " + fileParam + ", newUploadContext: " + newUploadContext);
                Util.setData(context, fileParam.toString(), newUploadContext);
            }

            @Override
            public void onProcess(Object fileParam, long current, long total) {
                LogUtil.e(LOGTAG, "on process: " + current + ", total: " + total);

                Toast.makeText(context, "onProcess " + current + "/" + total, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(CallRet ret) {
                //原图url
                String originImageUrl = "http://nos.think.linxuanxuan.com" + "/" + BUCKET_NAME + "/" + key;

                //缩略图url，进行等比缩放，总像素数为THUMB_PIXELS;
                String thumbUrl = originImageUrl + "?" + "imageView&thumbnail=" + THUMB_PIXELS;
                Log.i("图片上传","原图路径" + originImageUrl);
                Log.i("图片上传","缩略图路径" + thumbUrl);

                callBack.success(originImageUrl, thumbUrl);

            }

            @Override
            public void onFailure(CallRet ret) {
                LogUtil.e(LOGTAG, "上传失败");
            }

            @Override
            public void onCanceled(CallRet ret) {
                LogUtil.e(LOGTAG, "is cancel: ");
            }
        });

    }

    /***
     *
     * @param wanNOSObject Nos上传对象
     * @param wanNOSObject
     * @param file
     * @param callBack
     * @param context
     * @param uploadContext
     * @return
     */
    private static void uploadImageAsy( WanNOSObject wanNOSObject,final File file,final String key,
                                                         final UploadCallBack callBack, final Context context,
                                                         String uploadContext) throws InvalidParameterException {

//        final String key = file.getName();

        Log.e("uploadImageAsy","key:" + key);
        WanAccelerator.putFileByHttp(
                context, file,
                file.getAbsoluteFile(), uploadContext,
                wanNOSObject, new Callback() {
                    @Override
                    public void onUploadContextCreate(Object fileParam,String oldUploadContext,String newUploadContext) {
                        LogUtil.e(
                                LOGTAG,
                                "context create: "
                                        + fileParam
                                        + ", newUploadContext: "
                                        + newUploadContext);
                        Util.setData(context, fileParam.toString(), newUploadContext);
                    }

                    @Override
                    public void onProcess(
                            Object fileParam,
                            long current, long total) {
//                        LogUtil.e(LOGTAG,
//                                "on process: "
//                                        + current
//                                        + ", total: "
//                                        + total);

                        callBack.onProcess(fileParam,current,total);

                    }

                    @Override
                    public void onSuccess(CallRet ret) {
                        //原图url
                        String originImageUrl = "http://nos.think.linxuanxuan.com" + "/" + BUCKET_NAME + "/" + key;

                        //缩略图url，进行等比缩放，总像素数为THUMB_PIXELS;
                        String thumbUrl = originImageUrl + "?" + "imageView&pixel=" + THUMB_PIXELS;
//                        Log.i("图片上传","原图路径" + originImageUrl);
//                        Log.i("图片上传","缩略图路径" + thumbUrl);

                        callBack.success(originImageUrl, thumbUrl);
                    }

                    @Override
                    public void onFailure(CallRet ret) {
                        LogUtil.e(LOGTAG, "上传失败");
                    }

                    @Override
                    public void onCanceled(CallRet ret) {
                        LogUtil.e(LOGTAG, "is cancel: ");
                    }
                });


    }


    /***
     * 上传图片回掉函数，包括成功和失败
     */
    public interface UploadCallBack{

        /***
         * 上传成功回掉函数
         * @param originUrl 图片原始路径,不为null
         * @param thumbUrl 图片缩略图路径，可为null
         */
        void success(String originUrl, String thumbUrl);

        /***
         * 上传失败回掉函数
         */
        void fail();

        void onProcess(Object fileParam,
                long current, long total);
    }
}
