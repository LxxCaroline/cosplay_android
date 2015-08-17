package com.think.linxuanxuan.ecos.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.PhotoAibumAdapter;
import com.think.linxuanxuan.ecos.model.PhotoAibum;
import com.think.linxuanxuan.ecos.model.PhotoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/26.
 */
public class PhotoAlbumActivity extends Activity {
    private GridView aibumGV;
    private List<PhotoAibum> aibumList;

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoalbum);
        ButterKnife.inject(this);
        initTitle();
        aibumGV = (GridView) findViewById(R.id.album_gridview);
        aibumList = getPhotoAlbum();
        aibumGV.setAdapter(new PhotoAibumAdapter(aibumList, PhotoAlbumActivity.this));
        aibumGV.setOnItemClickListener(aibumClickListener);
    }

    private void initTitle() {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("选择图片");
    }

    AdapterView.OnItemClickListener aibumClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent intent = new Intent(PhotoAlbumActivity.this, PhotoActivity.class);
            intent.putExtra("aibum", aibumList.get(position));
            startActivity(intent);
            finish();
        }
    };

    private List<PhotoAibum> getPhotoAlbum() {
        List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
        Cursor cursor = MediaStore.Images.Media.query(getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        Map<String, PhotoAibum> countMap = new HashMap<String, PhotoAibum>();
        PhotoAibum pa = null;
        while (cursor.moveToNext()) {
            String path = cursor.getString(1);
            Log.w("Path", path);
            String id = cursor.getString(3);
            String dir_id = cursor.getString(4);
            String dir = cursor.getString(5);
            Log.e("info", "id===" + id + "==dir_id==" + dir_id + "==dir==" + dir + "==path=" + path);
            if (!countMap.containsKey(dir_id)) {
                pa = new PhotoAibum();
                pa.setName(dir);
                pa.setPathID(path);
                pa.setAibumID(Integer.parseInt(id));
                pa.setCount("1");
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id), path));
                countMap.put(dir_id, pa);
            } else {
                pa = countMap.get(dir_id);
                pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
                pa.getBitList().add(new PhotoItem(Integer.valueOf(id), path));
            }
        }
        cursor.close();
        Iterable<String> it = countMap.keySet();
        for (String key : it) {
            aibumList.add(countMap.get(key));
        }
        return aibumList;
    }
}
