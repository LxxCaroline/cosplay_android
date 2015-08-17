package com.think.linxuanxuan.ecos.constants;

import android.os.Environment;

import java.io.File;

/**
 * 类描述：文件操作类
 * Created by enlizhang on 2015/7/22.
 */
public class FileManager {

    /** 文件根目录名称 */
    public static final String APP_DIRECTORY  = "Ecos";

    /** 下载图片目录名称 */
    public static final String DOWNLOAD_IMG_FILE_NAME  = "downloadImage";

    /** 图片目录名称 */
    public static final String IMAGE_FILE_NAME  = "image";

    /** 图片缓存目录名称 */
    public static final String TEMP_IMG_FILE_NAME  = "tempImage";

    //文件名
    /*** 拍照或者上传图片后(裁剪前)的图片文件名   */
    private final String TEMP_PHOTO_NAME_BEFORE_CROP = "fileBeforeCrop.png";


    /** 图片文件夹，用来存储所有图片   */
    private File mImgFile;

    /** 下载图片文件夹，用来存储下载过的图片   */
    private File mDownloadImgFile;

    /** 缓存图片文件夹，存储上传图片过程所涉及的缓存图片  */
    private File mTempImgFile;

    /*** 选择本地图片或者拍照后的缓存图片，还未进行裁剪  */
    private File mTempFileBeforeCrop;

    /*** 裁剪后的图片默认存储文件  */
    private File mTempFileAfterCrop;


    private FileManager(){
        initFile();
    }

    /*** 文件管理单例对象 */
    private static FileManager fileManagerInstance;


    /**
     * 获取应用文件管理对象
     * @return
     */
    public synchronized static FileManager getInstance(){
        if(fileManagerInstance == null)
        {
            fileManagerInstance = new FileManager();
        }
        return fileManagerInstance;
    }

    /**
     * 初始化文件对象
     */
    private void initFile()
    {
        final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        //初始化根文件夹路径
        File appPath = new File(rootPath + APP_DIRECTORY);
        if (!appPath.exists()) {
            appPath.mkdirs();
        }

        //初始化图片文件夹路径
        mImgFile = new File(appPath, IMAGE_FILE_NAME);
        if (!mImgFile.exists()) {
            mImgFile.mkdirs();
        }

        //初始化下载图片文件夹路径
        mDownloadImgFile = new File(appPath, DOWNLOAD_IMG_FILE_NAME);
        if (!mDownloadImgFile.exists()) {
            mDownloadImgFile.mkdirs();
        }


        //初始化缓存图片文件夹路径
        mTempImgFile = new File(appPath, TEMP_IMG_FILE_NAME);
        if (!mTempImgFile.exists()) {
            mTempImgFile.mkdirs();
        }

        //初始化选择本地图片或者拍照后的图片文件
        mTempFileBeforeCrop = new File(mTempImgFile, TEMP_PHOTO_NAME_BEFORE_CROP);

        //裁剪后的默认图片文件
        mTempFileAfterCrop = new File(mTempImgFile, TEMP_PHOTO_NAME_BEFORE_CROP);
    }


    /**
     * 获取图片文件夹路径
     * @return
     */
    public File getDownloadImgFile() {
        return mDownloadImgFile;
    }

    /**
     * 获取图片文件夹路径
     * @return
     */
    public File getImgFile() {
        return mImgFile;
    }

    /**
     * 获取缓存图片文件夹路径
     * @return
     */
    public File getTempImgFile() {
        return mTempImgFile;
    }


    /**
     * 获取裁剪前的缓存图片存储路径
     * @return
     */
    public File getTempFileBeforeCrop() {

        return mTempFileBeforeCrop;
    }

    /***
     * 获取裁剪后的图片存储路径
     * @return
     */
    public File getDefFileAfterCrop()
    {
        return mTempFileAfterCrop;
    }



    /***
     * 获取拍照图片输出路径
     * @return
     */
    public File getPhotoOutFile(){
        return new File(mImgFile,String.valueOf(System.currentTimeMillis()) + ".png");
    }
}
