package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.PhotoAlbumActivity;
import com.think.linxuanxuan.ecos.model.PhotoAibum;
import com.think.linxuanxuan.ecos.utils.ImageLoader;
import com.think.linxuanxuan.ecos.utils.OnImageLoad;

import java.util.List;

/**
 * 相册显示适配
 */

public class PhotoAibumAdapter extends BaseAdapter {

    private List<PhotoAibum> aibumList;    // 相册列表
    private Context context;
    private ViewHolder holder;
    private GridView gridView;
    private ImageLoader imageLoader;    // 用于加载和缓存图像数据的对象

    public PhotoAibumAdapter(List<PhotoAibum> list, Context context) {
        this.aibumList = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return aibumList.size();
    }

    @Override
    public Object getItem(int position) {
        return aibumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.photoalbum_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.photoalbum_item_image);
            holder.tv = (TextView) convertView.findViewById(R.id.photoalbum_item_name);
            convertView.setTag(holder);
            Log.w("Convert","convertView inflate again");
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tv.setText(aibumList.get(position).getName() + " ( " + aibumList.get(position).getCount() + " )");   // 显示相册名称和包含图片数量，TextView不需要异步

        if (imageLoader == null)
            imageLoader = new ImageLoader();

        String pathID = aibumList.get(position).getPathID();    // 以相册第一张图片的路径作为相册的url
        holder.iv.setTag(pathID);
        PhotoAlbumActivity paActivity = (PhotoAlbumActivity) context;
        gridView = (GridView) paActivity.findViewById(R.id.album_gridview);

        imageLoader.imageLoad(pathID, holder.iv, "/image_cache", paActivity, new OnImageLoad() {
            @Override
            public void onLoadSucc(Bitmap bitmap, String c_url) {
                ImageView imageView = (ImageView) gridView.findViewWithTag(c_url);
                if (imageView != null) {
                    Log.w("Load","image load");
                    imageView.setImageBitmap(bitmap);
                    imageView.setTag("");
                }
            }
        }, aibumList.get(position).getBitList().get(0).getPhotoID(), true);

        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
