package com.securityapp.data;

import com.securityapp.controller.IController;
import com.securityapp.network.Response;

public interface IDataHandller {

    public void handleData (Response response) ;

    public IController getController();

}

