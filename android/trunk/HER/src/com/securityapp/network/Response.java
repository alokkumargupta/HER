package com.securityapp.network;

import org.apache.http.Header;

import com.securityapp.data.BaseDataHandler;

/**
 * Storage class to pass data from {@link HttpClientConnection} to children of {@link BaseDataHandler}
 */
public class Response {
	/**
	 * Constant that is set in {@link ServiceRequest#setDataType(int)}
	 */
	private int dataType;
	/**
	 * request that is set in {@link ServiceRequest#setRequest(Request)}
	 */
	private Object requestData;
	/**
	 * true if {@link HttpClientConnection} has successfully fetched the requested data
	 */
	private boolean isSuccess;
	/**
	 * response in form of bytes
	 */
	private byte[] responseData;
	/**
	 * response in form of object (Usually after parsing)
	 */
	private Object responseObject;
	/**
	 * response headers
	 */
	private Header[] httpHeaders;
	/**
	 * exception, if occurred while fetching the response
	 */
	private Exception exception;
	
	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}
	/**
	 * @return the requestData
	 */
	public Object getRequestData() {
		return requestData;
	}
	/**
	 * @param requestData the requestData to set
	 */
	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * @return the responseData
	 */
	public byte[] getResponseData() {
		return responseData;
	}
	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}
	/**
	 * @return the responseObject
	 */
	public Object getResponseObject() {
		return responseObject;
	}
	/**
	 * @param responseObject the responseObject to set
	 */
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
	/**
	 * @return the httpHeaders
	 */
	public Header[] getHttpHeaders() {
		return httpHeaders;
	}
	/**
	 * @param httpHeaders the httpHeaders to set
	 */
	public void setHttpHeaders(Header[] httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
}

