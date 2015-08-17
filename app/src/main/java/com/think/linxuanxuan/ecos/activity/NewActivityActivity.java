package com.think.linxuanxuan.ecos.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.ContactListAdapter;
import com.think.linxuanxuan.ecos.database.CityDBService;
import com.think.linxuanxuan.ecos.database.ProvinceDBService;
import com.think.linxuanxuan.ecos.dialog.SetPhotoDialog;
import com.think.linxuanxuan.ecos.model.ActivityModel;
import com.think.linxuanxuan.ecos.model.ActivityModel.ActivityType;
import com.think.linxuanxuan.ecos.model.City;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.model.ModelUtils;
import com.think.linxuanxuan.ecos.model.Province;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.activity.CreateActivityRequest;
import com.think.linxuanxuan.ecos.utils.SetPhotoHelper;
import com.think.linxuanxuan.ecos.utils.UploadImageTools;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Think on 2015/7/28.
 */
public class NewActivityActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {
    private final String TAG = "Ecos---NewActivity";
    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.activityCoverImgVw)
    ImageView coverImgVw;
    @InjectView(R.id.activityNameEdTx)
    EditText activityNameEdTx;
    @InjectView(R.id.activityTypeSpinner)
    Spinner activityTypeSpinner;
    @InjectView(R.id.activityProvinceSpinner)
    Spinner activityProvinceSpinner;
    @InjectView(R.id.activityCitySpinner)
    Spinner activityCitySpinner;
    @InjectView(R.id.addressEdTx)
    EditText addressEdTx;
    @InjectView(R.id.beginDateEdTx)
    EditText beginDateEdTx;
    @InjectView(R.id.endDateEdTx)
    EditText endDateEdTx;
    @InjectView(R.id.beginTimeEdTx)
    EditText beginTimeEdTx;
    @InjectView(R.id.endTimeEdTx)
    EditText endTimeEdTx;
    @InjectView(R.id.expenseEdTx)
    EditText expenseEdTx;
    @InjectView(R.id.newIcon)
    LinearLayout newIcon;
    @InjectView(R.id.activityDesrpEdTx)
    EditText activityDesrpEdTx;
    @InjectView(R.id.contactListView)
    ExtensibleListView contactListView;


    private ContactListAdapter contactListAdapter;

    //定义显示时间控件
    private Calendar calendar; //通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    //the types of activity
    private ActivityType[] activityTypes = new ActivityType[]{ActivityType.LIVE,
            ActivityType.主题ONLY, ActivityType.动漫节, ActivityType.同人展,
            ActivityType.官方活动, ActivityType.派对, ActivityType.舞台祭, ActivityType.赛事};
    private ArrayAdapter<ActivityType> activityTypeAdapter;
    private ArrayAdapter<Province> provinceAdapter;
    private ArrayAdapter<City> cityAdapter;
    //choose the photo
    private SetPhotoHelper mSetPhotoHelper;
    //record the cover image path
    private String coverImagePath = "";
    //for request
    private CreateActivityRequest createActivityRequest;
    private CreateActivityResponce createActivityResponce;
    //get the province list and city list.
    private ProvinceDBService provinceDBService;
    private CityDBService cityDBService;
    private List<Province> provinceList;
    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_layout);
        ButterKnife.inject(this);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_right_text.setText("发布");
        title_text.setText("发布活动");
    }

    void initData() {
        provinceDBService = ProvinceDBService.getProvinceDBServiceInstance(NewActivityActivity.this);
        cityDBService = CityDBService.getCityDBServiceInstance(NewActivityActivity.this);
        provinceList = provinceDBService.getProvinceList();
        cityList = cityDBService.getCityListByProvinceId(provinceList.get(0).getProvinceCode());
        //init the adapter
        contactListAdapter = new ContactListAdapter(this);
        activityTypeAdapter = new ArrayAdapter<ActivityType>(this, android.R.layout.simple_list_item_1, activityTypes);
        provinceAdapter = new ArrayAdapter<Province>(this, android.R.layout.simple_list_item_1, provinceList);
        cityAdapter = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cityList);
        //init the calendar
        calendar = Calendar.getInstance();
        //choose the cover image
        mSetPhotoHelper = new SetPhotoHelper(this, null);
        //图片裁剪后输出宽度
        final int outPutWidth = 300;
        //图片裁剪后输出高度
        final int outPutHeight = 450;
        mSetPhotoHelper.setOutput(outPutWidth, outPutHeight);
        mSetPhotoHelper.setAspect(2, 3);
    }

    void initView() {
        //set adapter
        contactListView.setAdapter(contactListAdapter);
        activityTypeSpinner.setAdapter(activityTypeAdapter);
        activityProvinceSpinner.setAdapter(provinceAdapter);
        activityCitySpinner.setAdapter(cityAdapter);
        //set listener
        coverImgVw.setOnClickListener(this);
        newIcon.setOnClickListener(this);
        beginDateEdTx.setOnTouchListener(this);
        endDateEdTx.setOnTouchListener(this);
        beginTimeEdTx.setOnTouchListener(this);
        endTimeEdTx.setOnTouchListener(this);
        activityProvinceSpinner.setOnItemSelectedListener(this);
        //set input type
        expenseEdTx.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_right_action:
                if (!checkAll()) {
                    Toast.makeText(NewActivityActivity.this, getResources().getString(R.string.notAlreadyFinished), Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(coverImagePath);
                UploadImageTools.uploadImageSys(file, new UploadWorkCallBack(), NewActivityActivity.this, false);
                break;
            case R.id.lly_left_action:
                NewActivityActivity.this.finish();
                break;
            case R.id.activityCoverImgVw:
                SetPhotoDialog dialog = new SetPhotoDialog(NewActivityActivity.this, new SetPhotoDialog.ISetPhoto() {
                    @Override
                    public void choosePhotoFromLocal() {
                        mSetPhotoHelper.choosePhotoFromLocal();
                    }

                    @Override
                    public void takePhoto() {
                        mSetPhotoHelper.takePhoto(true);
                    }
                });
                dialog.showSetPhotoDialog();
                break;
            case R.id.newIcon:
                contactListAdapter.addItem(getDataFromListView());
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cityList = cityDBService.getCityListByProvinceId(provinceList.get(position).getProvinceCode());
        cityAdapter = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cityList);
        activityCitySpinner.setAdapter(cityAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean checkAll() {
        if (coverImagePath.equals(""))
            return false;
        if (activityNameEdTx.getText().toString().equals(""))
            return false;
        if (addressEdTx.getText().toString().equals(""))
            return false;
        if (beginDateEdTx.getText().toString().equals(""))
            return false;
        if (endDateEdTx.getText().toString().equals(""))
            return false;
        if (beginTimeEdTx.getText().toString().equals(""))
            return false;
        if (endTimeEdTx.getText().toString().equals(""))
            return false;
        if (activityDesrpEdTx.getText().toString().equals(""))
            return false;
        if (expenseEdTx.getText().toString().equals(""))
            return false;

        if (activityNameEdTx.getText().length() > InputLength.ActivityTitle_max) {
            Toast.makeText(NewActivityActivity.this, "标题限制 " + InputLength.ActivityTitle_max + " 字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (addressEdTx.getText().length() > InputLength.ActivityPosition_max) {
            Toast.makeText(NewActivityActivity.this, "详细地址限制 " + InputLength.ActivityPosition_max + " 字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (activityDesrpEdTx.getText().length() > InputLength.ActivityDetail_max) {
            Toast.makeText(NewActivityActivity.this, "活动简介限制 " + InputLength.ActivityDetail_max + " 字", Toast.LENGTH_SHORT).show();
            return false;
        }
            View view;
            for (int i = 0; i < contactListAdapter.getCount(); i++) {
                view = contactListView.getChildAt(i);
                EditText editText = (EditText) view.findViewById(R.id.contactDetailEdTx);
                if (editText.getText().toString().equals(""))
                    return false;
            }
            return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SetPhotoHelper.REQUEST_BEFORE_CROP:
                    mSetPhotoHelper.setmSetPhotoCallBack(
                            new SetPhotoHelper.SetPhotoCallBack() {
                                @Override
                                public void success(String imagePath) {
                                    coverImagePath = imagePath;
                                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                    coverImgVw.setImageBitmap(bitmap);
                                }
                            });
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_BEFORE_CROP, data);
                    return;
                case SetPhotoHelper.REQUEST_AFTER_CROP:
                    mSetPhotoHelper.handleActivityResult(SetPhotoHelper.REQUEST_AFTER_CROP, data);
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.beginDateEdTx:
                    setDate(beginDateEdTx);
                    break;
                case R.id.endDateEdTx:
                    setDate(endDateEdTx);
                    break;
                case R.id.beginTimeEdTx:
                    setTime(beginTimeEdTx, true);
                    break;
                case R.id.endTimeEdTx:
                    setTime(endTimeEdTx, false);
                    break;
            }
        }
        return true;
    }

    void setDate(final EditText editText) {
        //点击日期按钮布局 设置日期
        new MyDatePickDialog(NewActivityActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                //更新EditText控件日期 小于10加0
                editText.setText(new StringBuilder().append(mYear).append("-")
                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                        .append("-")
                        .append((mDay < 10) ? "0" + mDay : mDay));
                if (beginDateEdTx.getText().toString().compareTo(editText.getText().toString()) > 0) {
                    Toast.makeText(NewActivityActivity.this, "结束日期不可以小于开始日期", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    void setTime(final EditText editText, boolean isNow) {
        //点击时间按钮布局 设置时间
        new MyTimePickDialog(NewActivityActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                mHour = hour;
                mMinute = minute;
                //更新EditText控件时间 小于10加0
                editText.setText(new StringBuilder()
                        .append(mHour < 10 ? "0" + mHour : mHour).append(":")
                        .append(mMinute < 10 ? "0" + mMinute : mMinute).append(":00"));
                if (beginTimeEdTx.getText().toString().compareTo(editText.getText().toString()) > 0) {
                    Toast.makeText(NewActivityActivity.this, "结束时间不可以小于开始时间", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),
                isNow ? calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + 1, true).show();
    }


    ArrayList<ActivityModel.Contact> getDataFromListView() {
        /*clear all the data in the contactWaysList;
        * read the data from the listview and add them in the contactWayList.
        */
        View view;
        ArrayList<ActivityModel.Contact> contactWayArrayList = new ArrayList<>();
        for (int i = 0; i < contactListAdapter.getCount(); i++) {
            view = contactListView.getChildAt(i);
            Spinner spinner = (Spinner) view.findViewById(R.id.contactTypeSpinner);
            EditText editText = (EditText) view.findViewById(R.id.contactDetailEdTx);
            ActivityModel.Contact contact = new ActivityModel.Contact();
            if (spinner.getSelectedItemPosition() == 0)
                contact.contactWay = ActivityModel.ContactWay.QQ;
            else if (spinner.getSelectedItemPosition() == 1)
                contact.contactWay = ActivityModel.ContactWay.QQ群;
            else if (spinner.getSelectedItemPosition() == 2)
                contact.contactWay = ActivityModel.ContactWay.微信;
            else
                contact.contactWay = ActivityModel.ContactWay.电话;
            contact.value = editText.getText().toString();
            contactWayArrayList.add(contact);
        }
        return contactWayArrayList;
    }

    class CreateActivityResponce extends BaseResponceImpl implements CreateActivityRequest.ICreateActivityResponce {

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(NewActivityActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            dismissProcessBar();
            Toast.makeText(NewActivityActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void success(ActivityModel activity) {
            dismissProcessBar();
            Toast.makeText(NewActivityActivity.this, getResources().getString(R.string.realiseActivitySuccessfully), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    class UploadWorkCallBack implements UploadImageTools.UploadCallBack {

        @Override
        public void success(String originUrl, String thumbUrl) {
            //for request
            createActivityRequest = new CreateActivityRequest();
            createActivityResponce = new CreateActivityResponce();
            ActivityModel activityModel = new ActivityModel();
            activityModel.title = activityNameEdTx.getText().toString();
            activityModel.coverUrl = originUrl;
            activityModel.introduction = activityDesrpEdTx.getText().toString();
            activityModel.fee = expenseEdTx.getText().toString();
            //set the date
            String date[] = beginDateEdTx.getText().toString().split("-");
            activityModel.activityTime.startDateStamp = ModelUtils.getTimeStampByDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            date = endDateEdTx.getText().toString().split("-");
            activityModel.activityTime.endDateStamp = ModelUtils.getTimeStampByDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            //set the time
            activityModel.activityTime.dayStartTime = beginTimeEdTx.getText().toString();
            activityModel.activityTime.dayEndTime = endTimeEdTx.getText().toString();
            activityModel.activityType = activityTypes[activityTypeSpinner.getSelectedItemPosition()];
            activityModel.location.address = addressEdTx.getText().toString();
            activityModel.location.province.provinceCode = provinceList.get(activityProvinceSpinner.getSelectedItemPosition()).getProvinceCode();
            activityModel.location.city.cityCode = cityList.get(activityCitySpinner.getSelectedItemPosition()).getCityCode();
            //set the contact way list
            activityModel.contactWayList = getDataFromListView();
            showProcessBar(getResources().getString(R.string.loading));
            createActivityRequest.request(createActivityResponce, activityModel);
        }

        @Override
        public void fail() {
            Log.e("test", "failed to upload the image.");
        }

        @Override
        public void onProcess(Object fileParam, long current, long total) {
            Log.i(TAG, "总数" + total + "  ," + "已上传" + current);
        }

    }

    public static class MyDatePickDialog extends DatePickerDialog {
        public MyDatePickDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        protected void onStop() {
        }
    }

    public static class MyTimePickDialog extends TimePickerDialog {
        public MyTimePickDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
        }

        @Override
        protected void onStop() {
        }
    }
}
