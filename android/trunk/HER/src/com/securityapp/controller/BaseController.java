package com.securityapp.controller;


import com.securityapp.network.Response;
import com.securityapp.ui.IScreen;

/**
 * This class will be used as a base class for all controllers
 */
public abstract class BaseController implements IController {
	
	private IScreen mScreen;
	
	public BaseController( IScreen pScreen ) {
		mScreen = pScreen;
	}
	
	@Override
	public IScreen getScreen() {
		return mScreen;
	}
	
	@Override
	public void onResponse( Response response ) {
		getScreen().handleUiUpdate(response);
	}
	
	@Override
	public abstract Object getData(int requestType, Object requestData);
}