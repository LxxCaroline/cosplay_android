package com.think.linxuanxuan.ecos.request.initial;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.request.BaseRequest;


public abstract class InitialRequest extends BaseRequest{
	
	/***
	 * 请求状态
	 */
	protected int requestState = 0;
	
	/***未开始请求*/
	public static final int NONE = 0;
	
	
	/***正在请求*/
	public static final int IS_REQUESTING = 2;
	
	/***请求结束*/
	public static final int FINISHED = 1;
	
	public abstract int getRequestState();

	/***
	 * 请求是否结束
	 * @return true,结束; false, 未结束
	 */
	public abstract boolean isFinished();
	
	/***
	 * 请求是否成功
	 * @return true,成功; false, 失败
	 */
	public abstract boolean isSuccess();
	
	
	/***
	 * 返回错误信息
	 * @return VolleyError
	 */
	public abstract VolleyError getError();

	/***
	 * 再次请求，事先必须已经请求过一次。并且调用前一定要调用resetRequestState()来重置状态
	 */
	public abstract void requestAgain(); 
	
	/***
	 * 在调用requestAgain()前一定要调用这个方法
	 */
	public void resetRequestState()
	{
		requestState = NONE;
	}

}
