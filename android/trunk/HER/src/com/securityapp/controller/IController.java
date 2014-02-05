package com.securityapp.controller;

import com.securityapp.network.Response;
import com.securityapp.ui.IScreen;


public interface IController {

	public IScreen getScreen ();

    public Object getData (int dataType, Object requestData);
    
    public void onResponse( Response response );
}

