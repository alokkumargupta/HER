package com.her.model.login;

import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class CommonLoginResponse implements Parcelable {
	
	private int code;
	private String data;
	private String message;
	private ScalerDataModel scalerData;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	private static class ScalerDataModel implements Parcelable
	{
        private String deviceIdentifier;
        private String email;
        private int id;
        private boolean isVarified;
        private String password;
        private String phone;
        
        public String getDeviceIdentifier() {
			return deviceIdentifier;
		}

		public void setDeviceIdentifier(String deviceIdentifier) {
			this.deviceIdentifier = deviceIdentifier;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public boolean isVarified() {
			return isVarified;
		}

		public void setVarified(boolean isVarified) {
			this.isVarified = isVarified;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

        
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub 
			return 0;
		}

		public ScalerDataModel fromJson(String json) throws JSONException
		{
		            Gson GSON = new Gson();
		            ScalerDataModel wsLoginResponse = GSON.fromJson(json, ScalerDataModel.class);

			return wsLoginResponse;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			
			dest.writeString(deviceIdentifier);
			dest.writeString(email);
			dest.writeInt(id);
			dest.writeBooleanArray(new boolean[] {isVarified});
			dest.writeString(password);
			dest.writeString(phone);
			
		}
		
		public ScalerDataModel() {
			// TODO Auto-generated constructor stub
		}
		
		
		public ScalerDataModel(Parcel parcel){
			
			deviceIdentifier=parcel.readString();
			email=parcel.readString();
			id=parcel.readInt();
			boolean[] myBooleanArr = new boolean[1];
			parcel.readBooleanArray(myBooleanArr);
			isVarified = myBooleanArr[0];
			password=parcel.readString();
			phone=parcel.readString();
			
		}
		public static Parcelable.Creator<ScalerDataModel> CREATOR = new Parcelable.Creator<ScalerDataModel>()
		{
			public ScalerDataModel createFromParcel(Parcel in)
			{
				return new ScalerDataModel(in);
			}
			public ScalerDataModel[] newArray(int size)
			{
				return new ScalerDataModel[size];
			}
		};	
		
		
	}
	public ScalerDataModel getScalerData() {
		return scalerData;
	}
	public void setScalerData(ScalerDataModel scalerData) {
		this.scalerData = scalerData;
	}
	private int status;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public CommonLoginResponse fromJson(String json) throws JSONException
	{
	            Gson GSON = new Gson();
	            CommonLoginResponse wsLoginResponse = GSON.fromJson(json, CommonLoginResponse.class);

		return wsLoginResponse;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(code);
		dest.writeString(data);
		dest.writeString(message);
		if( scalerData!=null) {
			dest.writeByte((byte)1);
			scalerData.writeToParcel(dest, flags);
		} else {
			dest.writeByte((byte)0);
		}
		dest.writeInt(status);
	}
	
	public CommonLoginResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public CommonLoginResponse(Parcel parcel){
		code=parcel.readInt();
		data=parcel.readString();
		message=parcel.readString();
		int taskCommonDetailsRequired = parcel.readByte();
		if( taskCommonDetailsRequired == 1 ) {
			scalerData=new ScalerDataModel(parcel);
		}
		status=parcel.readInt();
	}
	public static Parcelable.Creator<CommonLoginResponse> CREATOR = new Parcelable.Creator<CommonLoginResponse>()
	{
		public CommonLoginResponse createFromParcel(Parcel in)
		{
			return new CommonLoginResponse(in);
		}
		public CommonLoginResponse[] newArray(int size)
		{
			return new CommonLoginResponse[size];
		}
	};	
	
}
