package com.think.linxuanxuan.ecos.request.course;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import java.util.Map;

/***
 * 
* @ClassName: PraiseRequest 
* @Description: 点赞
* @author enlizhang
* @date 2015年8月5日 上午9:12:30 
*
 */
public class PraiseRequest extends BaseRequest{
	
	//请求参数键
	/*** 操作类型(关注或取消关注),praise:点赞 cancel:取消点赞 */
	public static final String TYPE = "type";
	
	/** 点赞或取消点赞的对象的用户id*/
	public static final String REF_ID = "refId";
	
	/** 请求结束回掉函数 */
	IPraiseResponce mPraiseResponce;
	
	public String mRefId;
	boolean mpraise;
	
	
	/***
	 * 点赞或取消点赞教程
	 * @param praiseResponce
	 * @param refId
	 * @param praise true：点赞  false:取消点赞
	 */
	public void praiseCourse(IPraiseResponce praiseResponce, final String refId, final boolean praise)
	{
		praise(praiseResponce, "0", refId, praise);
	}

	/***
	 * 点赞或取消点赞作品
	 * @param praiseResponce
	 * @param refId
	 * @param praise true：点赞  false:取消点赞
	 */
	public void praiseShare(IPraiseResponce praiseResponce, final String refId, final boolean praise)
	{
		praise(praiseResponce, "1", refId, praise);
	}

	/***
	 * 点赞或取消点赞作品
	 * @param praiseResponce
	 * @param refId
	 * @param praise true：点赞  false:取消点赞
	 */
	public void praiseAssignment(IPraiseResponce praiseResponce, final String refId, final boolean praise)
	{
		praise(praiseResponce, "4", refId, praise);
	}

	/*0:教程,
	1:分享,
	2:活动,
	3:招募,
	4:教程作业*/

	public void praise(IPraiseResponce praiseResponce,final String praiseType, final String refId, final boolean praise)
	{
		super.initBaseRequest(praiseResponce);
		mPraiseResponce  = praiseResponce;
		mRefId = refId;
		mpraise = praise;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.PRAISE_URL,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(REF_ID, refId);
				map.put(TYPE, praise?"praise":"cancel");
				map.put("praiseType", praiseType);

				traceNormal(TAG, map.toString());
				traceNormal(TAG, PraiseRequest.this.getUrl(RequestUrlConstants.PRAISE_URL, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getQueue().add(stringRequest);

	}



	@Override
	public void responceSuccess(String jstring) {
		
		mPraiseResponce.success(mRefId, mpraise);
		/*try {
//			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
			
			String toUserId = getString(json, TO_USRE_ID);
			if(mFollowResponce!=null)
			{
				mFollowResponce.success(toUserId, mFollow);
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(mBaseResponse!=null)
			{
				mBaseResponse.doAfterFailedResponse("json异常");
			}
		}*/
		
	}

	
	/***
	 * 
	* @ClassName: IPraiseResponce 
	* @Description: 点赞或取消点赞某人响应回调接口
	* @author enlizhang
	* @date 2015年8月5日 上午9:14:37 
	*
	 */
	public interface IPraiseResponce extends IBaseResponse{

		/*** 
		 * 操作成功，并返回操作对象用户id和当前关注结果 
		 * @param userId 点赞或取消点赞对象id
		 * @param praise true:操作成功后处于点赞状态  false:操作成功后处于不点赞状态
		 */
		public void success(String userId, boolean praise);
	}
}

