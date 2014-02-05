/**
 * 
 */
package com.her.controller;

import android.util.Log;

import com.her.constant.DataType;
import com.her.constant.WebServiceConstants;
import com.her.model.login.CommonLoginResponse;
import com.her.model.login.LoginRquest;
import com.securityapp.controller.BaseController;
import com.securityapp.data.BaseDataHandler;
import com.securityapp.network.HttpClientConnection;
import com.securityapp.network.Response;
import com.securityapp.network.ServiceRequest;
import com.securityapp.ui.IScreen;

/**
 * @author kiwitech
 *
 */
public class LoginController extends BaseController {
	private static final String LOG_TAG = "LoginController";
	public LoginController(IScreen pScreen) {
		super(pScreen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getData(int requestType, Object requestData) {
		// TODO Auto-generated method stub
		switch (requestType) {
		case DataType.REGISTER: {
           ServiceRequest serviceRq = new ServiceRequest();
			
			LoginRquest request = (LoginRquest)requestData;
		
			BaseDataHandler wsResponseDataHandler = new BaseDataHandler(this) {
				@Override
				public void handleData(Response response) {
					parseRegisterUserInfoResponse(response);
					getScreen().handleUiUpdate(response);
				}
			};

			serviceRq.setIDataHandler(wsResponseDataHandler);
			serviceRq.setDataType(requestType);
			serviceRq.setHttpHeaders(WebServiceConstants.WS_HEADER_NAMES, WebServiceConstants.WS_HEADER_VALUES);

			serviceRq.setUrl(WebServiceConstants.WS_URL_LOGIN+"/"+request.getEmail()+"/"+request.getPhone()+"/"+request.getPassword()+"/"+request.getDeviceIdentifier());;

			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			getScreen().getSyncAgent().setValid(true);
			serviceRq.setSyncAgent(getScreen().getSyncAgent());

			HttpClientConnection connection = HttpClientConnection.getInstance();
			connection.addRequest(serviceRq);	
			break;
		  }
		}
		return null;
	}

	private void parseRegisterUserInfoResponse(Response response) {
		if( ! response.isSuccess() ) {
			return;
		}
		CommonLoginResponse wsCommonLoginResponse=null;
		boolean signInSuccess = false;
		String responseJsonStr = new String(response.getResponseData());
		  Log.d(LOG_TAG, "responseJsonStr"+ responseJsonStr);
	      try{
	    	  wsCommonLoginResponse = new CommonLoginResponse().fromJson(responseJsonStr);
				  Log.d(LOG_TAG, "wsGetUserInfoResponse"+ wsCommonLoginResponse.getMessage());
				signInSuccess = true;
			    Log.d(LOG_TAG, "signInSuccess"+ signInSuccess);
				
	      }catch(Exception e){
	    	 Log.e(LOG_TAG, "parseLoginResponse", e);
	      }
		response.setSuccess(signInSuccess);
		response.setResponseObject(wsCommonLoginResponse);
	}
	
}
