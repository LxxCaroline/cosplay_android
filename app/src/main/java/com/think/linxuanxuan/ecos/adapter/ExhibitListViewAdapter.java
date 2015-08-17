package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.Image;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Think on 2015/7/23.
 */
public class ExhibitListViewAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Image> imageList;
    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    RequestQueue queue;
    ImageLoader imageLoader;

    public ExhibitListViewAdapter(Context context) {
        this.mcontext = context;
        imageList = new ArrayList<>();
        queue = MyApplication.getRequestQueue();
        imageCache = new SDImageCache(300,200);
        imageLoader = new ImageLoader(queue, imageCache);
    }

    public void updateDataList(List<Image> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.exhibit_list_view_item, null);
            viewHolder = new ImageViewHolder();
            viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.exhibit_lsvw_item_img);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ImageViewHolder) convertView.getTag();
        if (imageList.get(position).originUrl != null && !imageList.get(position).originUrl.equals(""))
            viewHolder.imageView.setImageUrl(imageList.get(position).originUrl, imageLoader);
        return convertView;
    }

    class ImageViewHolder {
        NetworkImageView imageView;
    }
}
