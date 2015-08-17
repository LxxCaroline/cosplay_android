package com.think.linxuanxuan.ecos.request.recruitment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.Recruitment.RecruitType;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/***
 *
 * @ClassName: CreateRecruitmentRequest
 * @Description: 创建招募请求
 * @author enlizhang
 * @date 2015年8月2日 下午3:35:46
 *
 */
public class CreateRecruitmentRequest extends BaseRequest{


	ICreateRecruitmentResponce mCreateRecruitmentResponce;

	public void request(ICreateRecruitmentResponce createRecruitmentResponce, final Recruitment recruit)
	{
		super.initBaseRequest(createRecruitmentResponce);
		mCreateRecruitmentResponce = createRecruitmentResponce;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.CREATE_RECRUITMENT_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put("recruitJson", getRequestRecruitJson(recruit));

				traceNormal(TAG, map.toString());
				traceNormal(TAG, CreateRecruitmentRequest.this.getUrl(RequestUrlConstants.CREATE_RECRUITMENT_URL, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getQueue().add(stringRequest);
	}

	@Override
	public void responceSuccess(String jstring) {
		traceNormal(TAG, jstring);

		try {
			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
			JSONObject recruitJO = json;

			Recruitment recruit = null;
			recruit  = new Recruitment();
			recruit.recruitmentId = getString(recruitJO,"recruitId");
			recruit.averagePrice = getString(recruitJO,"recruitmentId");
			recruit.description = getString(recruitJO,"description");
			recruit.coverUrl = getString(recruitJO,"coverUrl");
			recruit.title = getString(recruitJO,"title");
			recruit.priceUnit = getString(recruitJO,"priceUnit");
			recruit.recruitType = RecruitType.getRecruitTypeByValue(getString(recruitJO,"recruitType"));
			recruit.issueTimeStamp = Long.valueOf(getString(recruitJO, "recruitType")).longValue();

			if(mCreateRecruitmentResponce!=null)
			{
				mCreateRecruitmentResponce.success(recruit);
			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			if(mBaseResponse!=null)
			{
				mBaseResponse.doAfterFailedResponse("json异常");
			}
		}

	}

	/***
	 *
	 * @ClassName: ICreateRecruitmentResponce
	 * @Description: 创建招募回调函数
	 *
	 */
	public interface ICreateRecruitmentResponce extends IBaseResponse{

		/** 创建招募成功，并返回添加的招募 */
		public void success(Recruitment recruitment);
	}


	public String getRequestRecruitJson(Recruitment recruitment){
		Map<Object,Object> jsonMap = new HashMap<Object,Object>();

		jsonMap.put("title", "xxx");
		jsonMap.put("price", recruitment.averagePrice);
		jsonMap.put("priceUnit", recruitment.priceUnit);
		jsonMap.put("description", recruitment.description);
		jsonMap.put("coverUrl", recruitment.coverUrl);
		jsonMap.put("recruitType", recruitment.recruitType.getValue());

		return new JSONObject(jsonMap).toString();
	}


	public Recruitment getTestRecruitment(){

		Recruitment recruit = null;
		for(int i=0;i<1;i++){
			recruit  = new Recruitment();
			recruit.recruitmentId = "" + i;
			recruit.averagePrice = "" + i*100;
			recruit.description = "这是一个测试描述" + i;
			recruit.coverUrl = "http://pic.jschina.com.cn/0/10/40/90/10409045_975387.jpg";
			recruit.title = "招募测试标题" + i;
			recruit.priceUnit = "人" + i;
			recruit.issueTimeStamp = System.currentTimeMillis() - i*24*60*60*1000;
			recruit.recruitType = RecruitType.妆娘;
		}
		return recruit;
	}


}

