package com.securityapp.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.securityapp.network.Response;
import com.securityapp.network.SyncAgent;

/**
 * This class is used as base-class for application-base-activity. 
 */
public abstract class BaseActivity extends Activity implements IScreen
{
	private SyncAgent 	mSyncAgent;
	private Handler		mHandler;
	
	////////////////////////////////////////////// IScreen interface
	
	@Override
	public final void handleUiUpdate( Response response) {
		Message _message = new Message();
		_message.obj = response;
		mHandler.sendMessage(_message);
	}

	public SyncAgent getSyncAgent() {
		return mSyncAgent;
	}

	public void setSyncAgent(SyncAgent syncAgent) {
		mSyncAgent = syncAgent;
	}

    //////////////////////////////// activity methods

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	Log.i(getClass().getSimpleName(), "onCreate()");
		super.onCreate(savedInstanceState);
		
		mSyncAgent = new SyncAgent(true);
		
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				try {
					super.handleMessage(msg);
					if ( ! isFinishing() && mSyncAgent.isValid() ) {
						updateUi((Response)msg.obj);
					}
				} catch (Exception e) {
					Log.e(getClass().getSimpleName(), "updateUi()", e);
				}
			}
		};
	}
	
				
	
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.i(getClass().getSimpleName(), "onResume()");
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	Log.i(getClass().getSimpleName(), "onNewIntent()");
    	super.onNewIntent(intent);
    }
    
    /////////////////////// abstract method
    
    protected abstract void updateUi( Response response);
    
    //////////////////////////////// show and hide key-board

    protected void showVirturalKeyboard() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (m != null) {
					m.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
				}
			}
		}, 100);
	}
    
    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || 
					y < w.getTop() || y > w.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		}
		return ret;
	}
}