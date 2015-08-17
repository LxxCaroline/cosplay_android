package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.utils.CompressImageUitl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Think on 2015/7/26.
 */
public class UploadWorksListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<Course.Step> mStepsList;
    private ArrayList<String> paths;
    private LayoutInflater mInflater;

    public UploadWorksListAdapter(Context context, ArrayList<String> paths) {
        mContext = context;
        this.paths = paths;
        mStepsList = new ArrayList<>();
        for (int i = 0; paths != null && i < paths.size(); i++) {
            Course.Step step = new Course.Step(getCount() + 1);
            step.imagePath = paths.get(i);
            mStepsList.add(step);
        }
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return paths == null ? 0 : paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.upload_works_listitem, null);
            holder = new ViewHolder();
            holder.next_step = (ImageView) convertView.findViewById(R.id.next_step);
            holder.last_step = (ImageView) convertView.findViewById(R.id.last_step);
            holder.item_photo = (ImageView) convertView.findViewById(R.id.item_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bitmap bitmap = CompressImageUitl.decodeSampledBitmapFromFile(mStepsList.get(position).imagePath, 200, 200);
        holder.item_photo.setImageBitmap(bitmap);
        holder.next_step.setOnClickListener(this);
        holder.next_step.setTag(position);
        holder.last_step.setOnClickListener(this);
        holder.last_step.setTag(position);
        return convertView;
    }

    private class ViewHolder {
        ImageView last_step, next_step, item_photo;
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        Course.Step tempStep;
        position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.next_step:
                if (position == (getCount() - 1)) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.alreadyBottom), Toast.LENGTH_SHORT).show();
                    return;
                }
                tempStep = mStepsList.get(position);
                mStepsList.set(position, mStepsList.get(position + 1));
                mStepsList.set(position + 1, tempStep);
                notifyDataSetChanged();

                break;
            case R.id.last_step:
                if (position == 0) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.alreadyTop), Toast.LENGTH_SHORT).show();
                    return;
                }
                tempStep = mStepsList.get(position);
                mStepsList.set(position, mStepsList.get(position - 1));
                mStepsList.set(position - 1, tempStep);
                notifyDataSetChanged();
                break;
        }
    }
}
