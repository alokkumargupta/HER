package com.securityapp.data;

import com.securityapp.controller.IController;
import com.securityapp.network.Response;

public abstract class BaseDataHandler implements IDataHandller {

	private IController mController;

	/**
	 * @param pController
	 */
	public BaseDataHandler(IController pController) {
		mController = pController;
	}
	
	@Override
	public IController getController() {
		return mController; 
	}

	@Override
	public abstract void handleData( Response response );

}