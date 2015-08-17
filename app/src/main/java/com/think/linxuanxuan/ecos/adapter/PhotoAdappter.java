package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.PhotoActivity;
import com.think.linxuanxuan.ecos.activity.PhotoGridItem;
import com.think.linxuanxuan.ecos.model.PhotoAibum;
import com.think.linxuanxuan.ecos.model.PhotoItem;
import com.think.linxuanxuan.ecos.utils.ImageLoader;
import com.think.linxuanxuan.ecos.utils.OnImageLoad;

import java.util.ArrayList;

public class PhotoAdappter extends BaseAdapter {

    private Context context;
    private PhotoAibum aibum;               // 相册
    private ArrayList<PhotoItem> gl_arr;    // 待显示图片
    private ImageLoader imageLoader;        // 用于加载和缓存图像数据的对象
    private GridView gridView;

    public PhotoAdappter(Context context, PhotoAibum aibum, ArrayList<PhotoItem> gl_arr) {
        this.context = context;
        this.aibum = aibum;
        this.gl_arr = gl_arr;
    }

    @Override
    public int getCount() {
        if (gl_arr == null) {   // gl_arr == null时，从相册aibum中获取图片，否则从gl_arr中获取图片
            return aibum.getBitList().size();
        } else {
            return gl_arr.size();
        }
    }

    @Override
    public PhotoItem getItem(int position) {
        if (gl_arr == null) {
            return aibum.getBitList().get(position);
        } else {
            return gl_arr.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoGridItem item;
        if (convertView == null) {
            item = new PhotoGridItem(context);
            item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        } else {
            item = (PhotoGridItem) convertView;
        }

        if (imageLoader == null)
            imageLoader = new ImageLoader();

        PhotoActivity paActivity = (PhotoActivity) context;
        gridView = (GridView) paActivity.findViewById(R.id.photo_gridview);

        String pathID;
        pathID = aibum.getBitList().get(position).getPath();
        item.getmImageView().setTag(pathID);
        item.getmSelect().setTag(position);

        imageLoader.imageLoad(aibum.getBitList().get(position).getPath(), item.getmImageView(), "/image_cache", paActivity, new OnImageLoad() {
            @Override
            public void onLoadSucc(Bitmap bitmap, String c_url) {
                ImageView imageView = (ImageView) gridView.findViewWithTag(c_url);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setTag("");
                }
            }
        }, aibum.getBitList().get(position).getPhotoID(), true);
        boolean flag = aibum.getBitList().get(position).isSelect();
        item.setChecked(flag);

        return item;
    }
}
