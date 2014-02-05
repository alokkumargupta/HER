package com.her.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.her.R;
import com.her.constant.DataType;
import com.her.controller.LoginController;
import com.her.model.login.CommonLoginResponse;
import com.her.model.login.LoginRquest;
import com.her.utils.ApplicationUtil;
import com.securityapp.network.Response;
import com.securityapp.utils.ConnectivityUtils;
import com.securityapp.utils.ToastUtils;

public class LoginActivity extends BaseMainActivity implements OnClickListener {
	
	private static final String LOG_TAG = "LoginActivity";
	private EditText edtEmail;
	private EditText edtPass;
	private EditText edtConfirmPass;
	private EditText edtPhone;
	private EditText edtVerificationCode;
	private Button btnRegister;
	private TextView txtClickHere;
	LinearLayout ltPhoneNo, ltConfirmPass, ltverificationCode;
	public static final int register =0;
	public static final int login =1;
	public static final int send_verfication_code =2;
	
	public static  int mode = login;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ltPhoneNo =(LinearLayout)findViewById(R.id.lt_phone_no);
		ltConfirmPass =(LinearLayout)findViewById(R.id.lt_confirm_pass);
		ltverificationCode =(LinearLayout)findViewById(R.id.lt_verification_code);
		txtClickHere =(TextView)findViewById(R.id.txtClickHere);
		txtClickHere.setOnClickListener(this);
		edtEmail = (EditText)findViewById(R.id.edt_email);
		edtPhone=(EditText)findViewById(R.id.edt_phone_no);
		edtPass=(EditText)findViewById(R.id.edt_password);
		edtConfirmPass=(EditText)findViewById(R.id.edt_confirm_password);
		edtVerificationCode=(EditText)findViewById(R.id.edt_verification_code);
		btnRegister =(Button)findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(this);
	}
	
	@Override 		  
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.txtClickHere:
			if(mode==1){
				mode =0;
				ltPhoneNo.setVisibility(View.VISIBLE);
				ltConfirmPass.setVisibility(View.VISIBLE);
			}else{
				ltPhoneNo.setVisibility(View.GONE);
				ltConfirmPass.setVisibility(View.GONE);
				mode =1;
			 }
			break;
		case R.id.btn_register:
			if(checkValidationForLoginFields()){
			if(ConnectivityUtils.isNetworkEnabled(this)){ //To check whether network available or not
				showProgressDialog(getString(R.string.login_progress_message));
				LoginRquest loginRequest = new LoginRquest();
				loginRequest.setEmail(edtEmail.getText().toString());
				loginRequest.setPhone(edtPhone.getText().toString());
				loginRequest.setPassword(edtPass.getText().toString());
				loginRequest.setDeviceIdentifier(ApplicationUtil.findDeviceID(this));
				LoginController loginController = new LoginController(this);
				loginController.getData(DataType.REGISTER, loginRequest);
				
			}else showNetworkNotAvailableDialog();
			}
			
			break;
		}
		
      }	  			    
	private boolean checkValidationForLoginFields() {

		if (edtEmail.getText().toString().trim().equals("")) {
			ToastUtils.showToast(LoginActivity.this, getString(R.string.fill_email));
			return false;
		} else if (!ApplicationUtil.checkEmail(edtEmail.getText().toString().trim())) {
			ToastUtils.showToast(LoginActivity.this, getString(R.string.valid_email));
			return false;
		} else if (edtPhone.getText().toString().trim().equals("")) {
			ToastUtils.showToast(LoginActivity.this, getString(R.string.phone_no));
			return false;
		}else if (!ApplicationUtil.checkMobileNumber(edtPhone.getText().toString().trim())) {
			ToastUtils.showToast(this, getString(R.string.phone_no_length));
			return false;
		}else if (edtPass.getText().toString().trim().equals("")) {
			ToastUtils.showToast(LoginActivity.this, getString(R.string.fill_password));
			return false;
		} else if (edtPass.getText().toString().trim().equals(edtConfirmPass.getText().toString().trim())) {
			ToastUtils.showToast(LoginActivity.this, getString(R.string.confirm_password));
			return false;
		} /*else if (!ApplicationUtil.checkPassword(edtPassword.getText().toString().trim())) {
			ToastUtils.showToast(this, getString(R.string.check_password));
			return false;
		}*/

		return true;
	}
	@Override
	protected void updateUi(Response response) {
		// TODO Auto-generated method stub
		switch (response.getDataType()) {
		case DataType.REGISTER: {
			removeProgressDialog();
			if (response.isSuccess()) {
					if(((CommonLoginResponse)response.getResponseObject()).getMessage().equals("Data saved successfully")){
						CommonLoginResponse wsCommonLoginResponse =((CommonLoginResponse)response.getResponseObject());
						edtPhone.setVisibility(View.GONE);
						edtConfirmPass.setVisibility(View.GONE);
						edtVerificationCode.setVisibility(View.GONE);
						
					}else ToastUtils.showToast(LoginActivity.this,((CommonLoginResponse)response.getResponseObject()).getMessage());
				
			} else {
				ToastUtils.showToast(LoginActivity.this,((String)response.getResponseObject()));
			}
			break;
		 }
		case DataType.LOGIN: {
			removeProgressDialog();
			if (response.isSuccess()) {
					if(((CommonLoginResponse)response.getResponseObject()).getMessage().equals("Data saved successfully")){
						CommonLoginResponse wsCommonLoginResponse =((CommonLoginResponse)response.getResponseObject());
						edtPhone.setVisibility(View.GONE);
						edtConfirmPass.setVisibility(View.GONE);
						edtVerificationCode.setVisibility(View.GONE);
						
					}else ToastUtils.showToast(LoginActivity.this,((CommonLoginResponse)response.getResponseObject()).getMessage());
				
			} else {
				ToastUtils.showToast(LoginActivity.this,((String)response.getResponseObject()));
			}
			break;
		 }
		}
	}

}
