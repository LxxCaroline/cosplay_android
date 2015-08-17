package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.util.List;

/**
 * Created by Think on 2015/7/31.
 */
public class NewDisplayListAdater extends BaseAdapter {
    private Context mcontext;
    private LayoutInflater layoutInflater;

    public NewDisplayListAdater(Context context) {
        this.mcontext = context;
        this.layoutInflater = LayoutInflater.from(mcontext);

        isChecked = new boolean[getCount()];
        resetCheckState();
    }

    /**
     * 分享列表
     */
    public List<Share> mShareList;


    /**
     * 标志某个分享是否被选中，与{@link #mShareList}一一对应
     */
    public boolean isChecked[];

    public NewDisplayListAdater(Context context, List<Share> shareList) {
        this.mcontext = context;
        this.layoutInflater = LayoutInflater.from(mcontext);
        mShareList = shareList;

        resetCheckState();
    }

    @Override
    public int getCount() {
        return (mShareList == null) ? 1 : mShareList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    View vTop = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayItemViewHolder viewHolder = null;
        if (position == 0) {
            if (vTop == null) {
                convertView = layoutInflater.inflate(R.layout.item_new_recruitment_top, null);
                vTop = convertView;
            } else {
                convertView = vTop;
            }
        } else {
            viewHolder = new DisplayItemViewHolder();
            if (convertView == null || convertView.getTag() == null) {

                convertView = layoutInflater.inflate(R.layout.item_new_display_list, null);
                viewHolder.iv_cover = (NetworkImageView) convertView.findViewById(R.id.displayCoverImVw);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.displayTitleTxVw);
                viewHolder.tv_praise_num = (TextView) convertView.findViewById(R.id.displayFavorTxVw);
                viewHolder.iv_choose = (ImageView) convertView.findViewById(R.id.checkbox);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (DisplayItemViewHolder) convertView.getTag();
            }

            viewHolder.iv_choose.setTag(position - 1);
            setData(viewHolder, position - 1);
        }

        return convertView;
    }

    void setData(DisplayItemViewHolder viewHolder, int position) {

        Share share = mShareList.get(position);
        Log.e("tag", share.toString());

        if (share.coverUrl != null && !share.coverUrl.equals("")) {
            ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(), new SDImageCache(300,200));
            viewHolder.iv_cover.setImageUrl(share.coverUrl, imageLoader);
        }

        viewHolder.tv_title.setText(share.title);
        Log.e("tag", String.valueOf(share.praiseNum));
        viewHolder.tv_praise_num.setText(String.valueOf(share.praiseNum));

        //设置是否选中
        if (isChecked[position])
            viewHolder.iv_choose.setImageResource(R.mipmap.ic_choose_true);
        else
            viewHolder.iv_choose.setImageResource(R.mipmap.ic_choose_false);

        viewHolder.iv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("选择图片", "选择图片");
                //重置选择状态
                resetCheckState();

                int position = (int) view.getTag();
                isChecked[position] = true;

                notifyDataSetChanged();
            }
        });
    }


    TopViewHolder mTopViewHolder = new TopViewHolder();

    class TopViewHolder {
        /**
         * 价格
         */
        EditText priceEdTx;

        /**
         * 描述
         */
        EditText descrpEdTx;
    }


    class DisplayItemViewHolder {
        NetworkImageView iv_cover;
        TextView tv_title, tv_praise_num;

        ImageView iv_choose;
    }

    /**
     * 重置选择状态，包括初始化选中数组和重置选中状态为false
     */
    public void resetCheckState() {
        isChecked = new boolean[getCount()];

        for (int i = 0; i < getCount(); i++) {
            isChecked[i] = false;
        }
    }

    public void reflesh(List<Share> shareList) {
        if (mShareList == null)
            mShareList = shareList;
        else {

            int length = mShareList.size();

            for (Share newShare : shareList) {

                boolean same = false;
                for (int i = 0; i < length; i++) {
                    Share existedShare = mShareList.get(i);

                    if (newShare.shareId.equals(existedShare.shareId)) {
                        same = true;
                    }
                }

                if (!same)
                    mShareList.add(newShare);
            }
        }
        resetCheckState();
        notifyDataSetChanged();
    }


    /**
     * 获取选中的作品封面图
     *
     * @return
     */
    public String getCheckedCoverUrl() {
        for (int i = 0; i < isChecked.length; i++) {
            if (isChecked[i])
                return mShareList.get(i).coverUrl;
        }
        return null;
    }

    /**
     * @return
     */
    public boolean isThereCoverUrl() {
        for (int i = 0; i < isChecked.length; i++) {
            if (isChecked[i]) {
                Log.e("选择的封面", "第" + (i + 1) + "张");
                return true;
            }
        }
        return false;
    }


    public boolean isTopViewEmpty() {

        EditText price = (EditText) vTop.findViewById(R.id.priceEdTx);
        EditText descrpEdTx = (EditText) vTop.findViewById(R.id.descrpEdTx);

        if ("".equals(price.getText().toString()) || "".equals(descrpEdTx.getText().toString())) {
            return true;
        }
        return false;
    }

    public String getPrice() {
        EditText price = (EditText) vTop.findViewById(R.id.priceEdTx);
        return price.getText().toString();
    }

    public String getDes() {
        EditText descrpEdTx = (EditText) vTop.findViewById(R.id.descrpEdTx);
        return descrpEdTx.getText().toString();
    }

}
