package com.think.linxuanxuan.ecos.request.recruitment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Recruitment.RecruitType;
import com.think.linxuanxuan.ecos.model.User.Gender;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: RecruitmentListRequest
 * @Description: 招募列表
 * @date 2015年7月30日 下午8:48:26
 */
public class GetRecruitmentDetailRequest extends BaseRequest {

    //请求参数键
    /**
     * 招募id
     */
    public static final String KEY_RECRUITMENT_ID = "recruitId";


    //响应参数列表
    /**
     * 招募列表JSONArray,内含JSONObject
     */
    public static final String JA_RECRUITMENTS = "recruitments";

    /**
     * 用户id
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * 云信id
     */
    public static final String KEY_IM_ID = "imId";

    /**
     * 标题
     */
    public static final String KEY_TITLE = "title";

    /**
     * 描述
     */
    public static final String KEY_DESCRIPTION = "description";

    /**
     * 头像url
     */
    public static final String KEY_AVATAR_URL = "avatarUrl";

    /**
     * 昵称
     */
    public static final String KEY_NICKNAME = "nickname";

    /**
     * 性别
     */
    public static final String KEY_GENDER = "gender";

    /**
     * 封面图url
     */
    public static final String KEY_COVER_URL = "coverUrl";

    /**
     * 发布时间时间戳
     */
    public static final String KEY_ISSUE_TIME_STAMP = "issueTimeStamp";

    /**
     * 城市
     */
    public static final String KEY_CITY_CODE = "cityCode";

    /**
     * 价格
     */
    public static final String KEY_PRICE = "price";

    /**
     * 价格单位
     */
    public static final String KEY_PRICE_UNIT = "priceUnit";

    /**
     * 招募分类
     */
    public static final String KEY_RECRUIT_TYPE = "recruitType";

    /**
     * 招募分类
     */
    public static final String KEY_DISTANCE = "distance";

    IGetRecruitmentLDetailResponse mGetRecruitmentLDetailResponse;


    public void request(IGetRecruitmentLDetailResponse getRecruitmentLDetailResponse, final String recruitId) {
        super.initBaseRequest(getRecruitmentLDetailResponse);
        mGetRecruitmentLDetailResponse = getRecruitmentLDetailResponse;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_RECRUITMENT_DETAIL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(KEY_RECRUITMENT_ID, recruitId);

                traceNormal(TAG, map.toString());
                traceNormal(TAG, GetRecruitmentDetailRequest.this.getUrl(RequestUrlConstants.GET_RECRUITMENT_DETAIL_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }

    @Override
    public void responceSuccess(String jstring) {
        traceNormal(TAG, jstring);

        try {
            JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
            JSONObject asJO = json;
            Recruitment recruit = null;
            recruit = new Recruitment();
            recruit.recruitmentId = getString(asJO, KEY_RECRUITMENT_ID);
            recruit.cityCode = getString(asJO, KEY_CITY_CODE);
            recruit.title = getString(asJO, KEY_TITLE);
            recruit.description = getString(asJO, KEY_DESCRIPTION);
            recruit.averagePrice = getString(asJO, KEY_PRICE);
            recruit.priceUnit = getString(asJO, KEY_PRICE_UNIT);
            recruit.recruitType = RecruitType.getRecruitTypeByValue(getString(asJO, KEY_RECRUIT_TYPE));
            recruit.userId = getString(asJO, KEY_USER_ID);
            recruit.imId = getString(asJO, KEY_IM_ID);
            recruit.avatarUrl = getString(asJO, KEY_AVATAR_URL);
            recruit.nickname = getString(asJO, KEY_NICKNAME);
            recruit.gender = Gender.getGender(getString(asJO, KEY_GENDER));
            recruit.distanceKM = getString(asJO, KEY_DISTANCE);
            if (asJO.has(KEY_ISSUE_TIME_STAMP) && !asJO.isNull(KEY_ISSUE_TIME_STAMP)) {
                recruit.issueTimeStamp = asJO.getLong(KEY_ISSUE_TIME_STAMP);
            }

            if (mGetRecruitmentLDetailResponse != null) {
                mGetRecruitmentLDetailResponse.success(recruit);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (mBaseResponse != null) {
                mBaseResponse.doAfterFailedResponse("json异常");
            }
        }
    }


    /**
     * @author enlizhang
     * @ClassName: IRecruitmentListResponse
     * @Description: 获取招募详情请求回调接口
     * @date 2015年7月31日 上午8:58:15
     */
    public interface IGetRecruitmentLDetailResponse extends IBaseResponse {

        /**
         * 请求成功回调函数，返回招募详情
         */
        public void success(Recruitment recruitment);

    }


}

