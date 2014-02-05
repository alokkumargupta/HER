package com.securityapp.ui;

import com.securityapp.network.Response;
import com.securityapp.network.SyncAgent;


public interface IScreen {

    public void handleUiUpdate (Response response);
    
    public SyncAgent getSyncAgent ();

    public void setSyncAgent (SyncAgent syncAgent);
}

