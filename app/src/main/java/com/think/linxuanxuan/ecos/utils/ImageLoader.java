package com.think.linxuanxuan.ecos.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.think.linxuanxuan.ecos.utils.OnImageLoad;
import com.think.linxuanxuan.ecos.utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片异步下载类，包括图片的软应用缓存以及将图片存放到SDCard或者文件中
 *
 * @author yanbin
 */
public class ImageLoader {
    private static final String TAG = "ImageDownloader";
    private HashMap<String, MyAsyncTask> map = new HashMap<String, MyAsyncTask>();    // 存放异步线程
    private Map<String, SoftReference<Bitmap>> imageCaches = new HashMap<String, SoftReference<Bitmap>>();    // 图片缓存区，以url为索引

    /**
     * @param url        用于寻找对应的ImageView
     * @param mImageView 匹配使用url获得的ImageView
     * @param myPath     用户自定义的文件存储路径
     * @param mActivity  包含ImageView的Activity
     * @param loader     OnImageDownload回调接口，在onPostExecute()中被调用
     * @param imageID    需要被加载的图片的ID，用于获取图片的缩略图
     */
    public void imageLoad(String url, ImageView mImageView, String myPath, Activity mActivity, OnImageLoad loader, int imageID, boolean thumbnailFlag) {

        System.out.println("输入的图片URL是："+url);
        SoftReference<Bitmap> currBitmap = imageCaches.get(url);
        Bitmap softRefBitmap = null;
        if (currBitmap != null) {
            softRefBitmap = currBitmap.get();    // get方法返回对象的强引用，使得对象不会被内存回收
        }
        String imageName = "";
        if (url != null) {
            imageName = Util.getInstance().getImageName(url);
        }
        // 先从软引用中拿数据（即所需图片存于内存的情况）
        if (currBitmap != null && mImageView != null && softRefBitmap != null && url.equals(mImageView.getTag())) {
            System.out.println("从软引用中拿数据--imageName==" + imageName);
            mImageView.setImageBitmap(softRefBitmap);
        } else if (mImageView != null && url.equals(mImageView.getTag())) {
            // 软体中没有，此时根据mImageView的tag，即url去判断该url对应的task是否已经在执行，如果在执行，本次操作不创建新的线程，否则创建新的线程。
            if (url != null && needCreateNewTask(mImageView)) {
                MyAsyncTask task = new MyAsyncTask(url, mImageView, myPath, mActivity, loader, imageID, thumbnailFlag);
                if (mImageView != null) {
                    Log.i(TAG, "执行MyAsyncTask --> " + Util.flag);
                   Util.flag++;
                    task.execute();
                    //将对应的url对应的任务存起来
                    map.put(url, task);
                }
            }
        }
    }

    /**
     * 判断是否需要重新创建线程下载图片，如果需要，返回值为true。
     *
     * @param mImageView
     * @return
     */
    private boolean needCreateNewTask(ImageView mImageView) {
        boolean b = true;
        if (mImageView != null) {
            String curr_task_url = (String) mImageView.getTag();
            if (isTasksContains(curr_task_url)) {
                b = false;
            }
        }
        return b;
    }

    /**
     * 检查该url（最终反映的是当前的ImageView的tag，tag会根据position的不同而不同）对应的task是否存在
     *
     * @param url
     * @return
     */
    private boolean isTasksContains(String url) {
        boolean b = false;
        if (map != null && map.get(url) != null) {
            b = true;
        }
        return b;
    }

    /**
     * 删除map中该url的信息，这一步很重要，不然MyAsyncTask的引用会“一直”存在于map中
     *
     * @param url
     */
    private void removeTaskFormMap(String url) {
        if (url != null && map != null && map.get(url) != null) {
            map.remove(url);
            System.out.println("当前map的大小==" + map.size());
        }
    }

    /**
     * 从文件中拿图片
     *
     * @param mActivity
     * @param imageName 图片名字
     * @param myPath    用户自定义的图片存放路径
     * @return 返回为空，说明文件缓存中也没有图像数据
     */
    private Bitmap getBitmapFromFile(Activity mActivity, String imageName, String myPath) {
        Bitmap bitmap = null;
        if (imageName != null) {
            File file = null;
            String real_path = "";
            try {
                if (Util.getInstance().hasSDCard()) {
                    real_path = Util.getInstance().getExtPath() + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);   // 三目运算符优先级低于加号，获得路径/SDCard/path
                } else {
                    real_path = Util.getInstance().getPackagePath(mActivity) + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);  // 获得路径/data/data/yanbin/imagelazyload/path
                }
                file = new File(real_path, imageName);
                if (file.exists())  //判断real_path路径下名为imageName的文件是否存在，如果不存在，bitmap为null
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
        }
        return bitmap;
    }

    /**
     * 将下载好的图片存放到文件中
     *
     * @param myPath    用户自定义的图片路径，非全路径
     * @param mActivity
     * @param imageName 图片名字
     * @param bitmap    图片
     * @return
     */
    private boolean setBitmapToFile(String myPath, Activity mActivity, String imageName, Bitmap bitmap) {
        File file = null;
        String real_path = "";
        try {
            /* 1.获取文件路径 */
            if (Util.getInstance().hasSDCard()) {
                real_path = Util.getInstance().getExtPath() + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);
            } else {
                real_path = Util.getInstance().getPackagePath(mActivity) + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);
            }
            /* 2.创建文件 */
            file = new File(real_path, imageName);
            if (!file.exists()) {
                File file2 = new File(real_path + "/");
                file2.mkdirs(); // 如果文件不存在就尝试创建文件夹
            }
            file.createNewFile();   // 创建一个空文件
            FileOutputStream fos = null;
            if (Util.getInstance().hasSDCard()) {
                fos = new FileOutputStream(file);
            } else {
                /**
                 openFileOutput函数会自动生成文件，文件保存在/data/data/<package name>/目录下
                 Context.MODE_PRIVATE    =  0       默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容
                 Context.MODE_APPEND    =  32768    模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
                 Context.MODE_WORLD_READABLE =  1   表示当前文件可以被其他应用读取
                 Context.MODE_WORLD_WRITEABLE =  2  表示当前文件可以被其他应用写入
                 如果希望文件被其他应用读和写，可以传入：
                 openFileOutput(“itcast.txt”, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
                 */
                fos = mActivity.openFileOutput(imageName, Context.MODE_PRIVATE);
            }
            /* 3.写入文件 */
            if (imageName != null && (imageName.contains(".png") || imageName.contains(".PNG"))) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);    // compress负责将bitmap图像压缩，100表示不压缩，压缩后的图像存放在fos中
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }
            fos.flush();    // 将位于缓冲区的图像刷新到（写入）文件中
            if (fos != null) {
                fos.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 辅助方法，一般不调用
     *
     * @param myPath
     * @param mActivity
     * @param imageName
     */
    private void removeBitmapFromFile(String myPath, Activity mActivity, String imageName) {
        File file = null;
        String real_path = "";
        try {
            if (Util.getInstance().hasSDCard()) {
                real_path = Util.getInstance().getExtPath() + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);
            } else {
                real_path = Util.getInstance().getPackagePath(mActivity) + (myPath != null && myPath.startsWith("/") ? myPath : "/" + myPath);
            }
            file = new File(real_path, imageName);
            if (file != null)
                file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步下载图片的方法
     *
     * @author yanbin
     */
    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String url;
        private OnImageLoad loader;
        private String myPath;
        private Activity mActivity;
        private int imageID;
        private boolean thumbnailFlag;

        public MyAsyncTask(String url, ImageView mImageView, String myPath, Activity mActivity, OnImageLoad loader, int imageID, boolean thumbnailFlag) {
            this.mImageView = mImageView;
            this.url = url;
            this.myPath = myPath;
            this.mActivity = mActivity;
            this.loader = loader;
            this.imageID = imageID;
            this.thumbnailFlag = thumbnailFlag;
        }

        /**
         * 从网上获取图片并存于文件和内存中
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) { // String... params表示能够传入任意个数的String类型参数
            Bitmap bitmap = null;
            if (url != null) {
                try {
//                    bitmap = BitmapFactory.decodeFile(url);
//                    smallBitmap = zoomImage(bitmap, imageCacheSize);    // zoomImage函数产生一个控制图片宽度为imageCacheSize的缩放新图
                    System.out.println("here");
                    if (thumbnailFlag)  // 大图
                        bitmap = MediaStore.Images.Thumbnails.getThumbnail(mActivity.getContentResolver(), imageID, MediaStore.Images.Thumbnails.MINI_KIND, null);  // MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
                    else
                        bitmap = MediaStore.Images.Thumbnails.getThumbnail(mActivity.getContentResolver(), imageID, MediaStore.Images.Thumbnails.MICRO_KIND, null);
                    // 将图像数据存于缓存中
                    imageCaches.put(url, new SoftReference<Bitmap>(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //回调设置图片
            if (loader != null) {
                /**
                 * 回调函数，由创建imageDownload对象的主线程实现接口函数的定义
                 * 并将接口对象传入子线程中，在异步子线程中调用接口对象的成员函数
                 * 由于接口函数在主线程定义，因此可以方便的在接口函数中使用主线程的变量
                 */
                loader.onLoadSucc(result, url);   // 向ImageView中写入数据
                //该url对应的task已经下载完成，从map中将其删除
                removeTaskFormMap(url);
            }
            super.onPostExecute(result);
        }
    }

    /**
     * 设定新图宽度，等比缩放图片
     *
     * @param bgimage
     * @param newWidth
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage, int newWidth) {

        int width = bgimage.getWidth();                 // 获取图片的宽和高
        int height = bgimage.getHeight();
        float scaleWidth = ((float) newWidth) / width;  // 计算缩放率
        int newHeight = (int) (height * scaleWidth);    // 保证等比缩放
        Bitmap bitmap = bgimage.createScaledBitmap(bgimage, newWidth, newHeight, true);
        return bitmap;
    }
}

