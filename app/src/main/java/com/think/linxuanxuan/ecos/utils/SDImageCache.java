package com.think.linxuanxuan.ecos.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.constants.FileManager;

import java.io.File;


public class SDImageCache implements ImageLoader.ImageCache {

    private static final String TAG = "SDImageCache";

    public int mRequestWidth = 150;
    public int mRequestHeight = 150;


    public SDImageCache(){

    }

    public SDImageCache(int requestWidth,int requestHeight){
        mRequestWidth = requestWidth;
        mRequestHeight =  requestHeight;
    }

    @Override
    public synchronized Bitmap getBitmap(String url) {
//		System.out.println("getBitmap(String url, Bitmap bitmap)");
        File imageFile = FileManager.getInstance().getImgFile();

        File file = new File(imageFile, StringUtils.hashKeyForDisk(url) + ".png");
        if (ImageTools.isImageExist(file)) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Log.e("图片","file:" + file.getAbsolutePath());
            return bitmap;
        }
        return null;
    }


    @Override
    public synchronized void putBitmap(String url, Bitmap bitmap) {
        System.out.println("putBitmap(String url, Bitmap bitmap)");

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width:" + width + "  ,height" + height);

        int reqWidth = mRequestWidth;
        int reqHeight = mRequestHeight;
        int inSampleSize = 1;
        if (height > reqHeight && width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        Log.i(TAG, "inSampleSize:" + inSampleSize);
        Matrix matrix = new Matrix();

        matrix.postScale(1.0f / inSampleSize, 1.0f / inSampleSize);

        bitmap = bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, false);

        File imageFile = FileManager.getInstance().getImgFile();

        File file = new File(imageFile, StringUtils.hashKeyForDisk(url) + ".png");
        ImageTools.saveBitmap(bitmap, file, false);

        MyApplication.getMediaScanner().scanFile(file.getAbsolutePath(), "image/png");
    }

}
