package com.her.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Window;

import com.her.R;
import com.securityapp.network.Response;
import com.securityapp.ui.BaseActivity;

public class BaseMainActivity extends BaseActivity {
	
	private ProgressDialog	mProgressDialog;

	@Override
	protected void updateUi(Response response) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param bodyText
	 * @param isCancelable
	 */
	public void showProgressDialog(String bodyText) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(BaseMainActivity.this);
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
						return true; //
					}
					return false;
				}
			});
		}

		mProgressDialog.setMessage(bodyText);

		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	public void removeProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	
	public void showNetworkNotAvailableDialog(){
		AlertDialog.Builder _builder = new AlertDialog.Builder(this);
		_builder.setTitle("HER")
		.setMessage(R.string.network_is_not_available)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//-----------When Click on Logout perform Logout & Stop IVX Service--------------//
			}
		});
		
		AlertDialog alert = _builder.create();
		//------------Show Alert Dialog with Yes & No Before Performing Exit Application Operation------------------//
		alert.show();	
	}
}
