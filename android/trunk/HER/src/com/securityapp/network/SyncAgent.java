package com.securityapp.network;



public class SyncAgent {

	private boolean mValid;
	
	/**
	 * @param mValid
	 */
	public SyncAgent(boolean mValid) {
		super();
		this.mValid = mValid;
	}

    public boolean isValid () {
        return mValid;
    }

    public void setValid(boolean valid){
    	//Log.i("SyncAgent", "" + valid );
    	mValid = valid;
    }
}

