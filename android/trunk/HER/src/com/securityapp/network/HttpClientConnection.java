package com.securityapp.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

import com.securityapp.controller.IController;
import com.securityapp.data.IDataHandller;
import com.securityapp.ui.IScreen;
import com.securityapp.utils.ConnectivityUtils;
import com.securityapp.utils.DataUtils;


public class HttpClientConnection extends Thread
{
	private final String LOG_TAG = "HttpClientConnection";
	
	private static HttpClientConnection instance;
	
	public static HttpClientConnection getInstance() {
		if (instance == null) {
			instance = new HttpClientConnection();
			instance.execute();
		}
		return instance;
	}
	
	private boolean isRunning;
	private Vector<ServiceRequest> highPriorityQueue;
	private Vector<ServiceRequest> lowPriorityQueue;
	
	public void execute() {
		highPriorityQueue = new Vector<ServiceRequest>();
		lowPriorityQueue = new Vector<ServiceRequest>();
		isRunning = true;
		start();
	}
	
	private ServiceRequest currentRequest;

	/**
	 * {@link ServiceRequest} with {@link PRIORITY#HIGH} are executed before {@link ServiceRequest} with {@link PRIORITY#LOW}
	 */
	public static interface PRIORITY
	{
		/**
		 * When-ever a new {@link ServiceRequest} with {@link PRIORITY#LOW} is added,
		 * it gets lower priority than previous requests with same priority.
		 */
		public static byte LOW = 0;
		/**
		 * When-ever a new {@link ServiceRequest} with {@link PRIORITY#HIGH} is added,
		 * it gets higher priority than previous requests with same priority.
		 */
		public static byte HIGH = 1;
	}

	public static interface HTTP_METHOD
	{
		public static byte GET = 0;
		public static byte POST = 1;
	}

	@Override
	public void run() {
		while (isRunning) {
			if (nextRequest()) {
				executeRequest();
			} else {
				try {
					Thread.sleep(10 * 60 * 1000);// 10 min sleep
				} catch (InterruptedException e) {
					Log.i(LOG_TAG, "" + e);
				}
			}
		}
	}

	private boolean nextRequest() {
		if(highPriorityQueue.size() > 0){
			currentRequest = (ServiceRequest)highPriorityQueue.remove(0);
		}
		else if(lowPriorityQueue.size() > 0) {
			currentRequest = (ServiceRequest)lowPriorityQueue.remove(0);
		} else {
			currentRequest = null;
		}
		
		return currentRequest != null;
	}

	public void executeRequest() {
		if( ! currentRequest.getSyncAgent().isValid() ) {
			return;
		}
		/**
		 * Check if device is connected.
		 */
		IDataHandller dataHandler = currentRequest.getIDataHandler();
		IScreen screen = ((IController)dataHandler.getController()).getScreen();
		if( screen instanceof Context ) {
			Context context = (Context) screen;
			if( ! ConnectivityUtils.isNetworkEnabled(context) ) {
				notifyError("Device is out of network coverage.", null);
				return;
			}
		}
		
		HttpClient httpClient = new DefaultHttpClient();

		int requestTimeOut = currentRequest.getRequestTimeOut();
		if( requestTimeOut > 0 ) {
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, requestTimeOut);
			HttpConnectionParams.setSoTimeout(params, requestTimeOut);
		}

		HttpResponse httpResponse = null;

		Log.i( LOG_TAG, "Request URL: " + currentRequest.getUrl());

		try {
			HttpRequestBase getOrPost = null;
			
			if(currentRequest.getHttpMethod() == HTTP_METHOD.POST) {
				getOrPost = new HttpPost();
				if(currentRequest.getPostData() != null) {
					((HttpPost)getOrPost).setEntity(currentRequest.getPostData() );
				}
			} else if(currentRequest.getHttpMethod() == HTTP_METHOD.GET) {
				getOrPost = new HttpGet();
			}
			
			getOrPost.setURI(new URI(currentRequest.getUrl()));
			
			String[] headerNames = currentRequest.getHeaderNames();
	    	if( headerNames != null ) {
	    		String[] headerValues = currentRequest.getHeaderValues();
	    		for( int i = 0; i < headerNames.length; i++ ) {
	    			getOrPost.addHeader(headerNames[i], headerValues[i]);
	    			Log.i( LOG_TAG, "Header: " + headerNames[i] + " = " + headerValues[i]);
	    		}
	    	}
	    	
			httpResponse = httpClient.execute(getOrPost);
			
			Log.i( LOG_TAG, "Response Received : " + httpResponse.getStatusLine().getStatusCode() );

			if( ! currentRequest.getSyncAgent().isValid() ) {
				return;
			}

			switch (httpResponse.getStatusLine().getStatusCode())
			{
			case HttpStatus.SC_OK:
			case 422: //TODO
			{
				InputStream is = httpResponse.getEntity().getContent();
				if(currentRequest.getIsCompressed()) {
					is = new GZIPInputStream(is);
				}
				if( ! currentRequest.getSyncAgent().isValid() ) {
					return;
				}
				Response response = new Response();
				response.setDataType(currentRequest.getDataType());
				response.setRequestData(currentRequest.getRequestData());
				//Log.i( LOG_TAG, "convertStreamToBytes() started." );
				response.setResponseData(DataUtils.convertStreamToBytes(is));
				//Log.i( LOG_TAG, "convertStreamToBytes() finished." );
				response.setHttpHeaders(httpResponse.getAllHeaders());
				response.setSuccess(true);
				if( ! currentRequest.getSyncAgent().isValid() ) {
					return;
				}
				dataHandler.handleData(response);
				break;
			}
			default:
			{
				 notifyError("We are facing some technical Difficulties.", null);
			     Log.e(LOG_TAG,httpResponse.getStatusLine().getReasonPhrase());
				//notifyError(httpResponse.getStatusLine().getReasonPhrase(), null);
				
				break;
			}
			}
		}
		catch (URISyntaxException e1) {
			notifyError("Invalid URL.", e1);
		}
		catch (UnknownHostException e) {
			notifyError("Server not found.", e);
		}
		catch (SocketException esoc) {
			notifyError("Time out.", esoc );
		}
		catch (IOException e) {
			notifyError("There are IO error.", e );
		}
		catch (Exception e) {
			notifyError("There are some problem.", e );
		}
		finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	private void notifyError(String message, Exception exception) {
		if( exception == null ) {
			Log.e( LOG_TAG, "Error Response: " + message );
		} else {
			Log.e( LOG_TAG, "Error Response: " + message, exception );
		}
		Response response = new Response();
		response.setRequestData(currentRequest.getRequestData());
		response.setResponseData(message.getBytes());
		response.setResponseObject(message);
		response.setSuccess(false);
		response.setException(exception);
		if( ! currentRequest.getSyncAgent().isValid() ) {
			return;
		}
		currentRequest.getIDataHandler().handleData(response);
	}

    public void addRequest (ServiceRequest request) {
    	try {
	    	if(request.getPriority() == PRIORITY.HIGH) {
	    		highPriorityQueue.add(0,request);
	    	} else {
	    		lowPriorityQueue.addElement(request);
	    	}
	    	interrupt();
	    } catch(Exception ex){ 
	    	Log.e( LOG_TAG, "addRequest()", ex ); 
	    }
    }

	/**
	 * @return the currentRequest
	 */
	public ServiceRequest getCurrentRequest() {
		return currentRequest;
	}
	
	/**
	 * @return the nextRequest
	 */
	public ServiceRequest getNextRequest() {
		if(highPriorityQueue.size() > 0){
			return highPriorityQueue.get(0);
		} else if(lowPriorityQueue.size() > 0) {
			return lowPriorityQueue.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * @return true if pRequest is found and removed from high/low queue.
	 */
	public boolean removeRequest(ServiceRequest pRequest, Comparator<ServiceRequest> pComparator) {
		ServiceRequest tempRq = null;
		Vector<ServiceRequest> targetQueue = lowPriorityQueue;
		if( pRequest.getPriority() == PRIORITY.HIGH ) {
			targetQueue = highPriorityQueue;
		}
		for( int i = 0; i < targetQueue.size(); i++ ) {
			try { tempRq = targetQueue.get(i); }
			catch(Exception e) { return false; }
			if( tempRq != null && pComparator.compare(tempRq, pRequest) == 0 ) {
				return targetQueue.removeElement(tempRq);
			}
		}
		return false;
	}
}
