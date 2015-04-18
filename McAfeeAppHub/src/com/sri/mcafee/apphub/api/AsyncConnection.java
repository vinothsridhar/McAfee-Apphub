package com.sri.mcafee.apphub.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

import com.sri.mcafee.apphub.utils.L;

public class AsyncConnection extends AsyncTask<Void, Void, Void> {

	private static final String TAG = "AsyncConnection";
	private String method, urlString, urlParams, finalResult;
	private ConnectionListener connectionListener;
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	private Map<String, String> headers = new HashMap<String, String>();
	private int status;
	private boolean isConnectionError;
	private int errorCode=ServerCodes.UNKNOWN_EXCEPTION;

	public AsyncConnection(String method, String urlString, String urlParams, Map<String, String> headers, ConnectionListener connectionListener) {
		this.method = method;
		this.urlString = urlString;
		this.urlParams = urlParams;
		this.headers = headers;
		this.connectionListener = connectionListener;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		L.i(TAG, "Async url:" + urlString);
		final StringBuilder builder = new StringBuilder();

		try {
			final URL url = new URL(urlString);

			L.i(TAG, "Method="+method);
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (headers != null) {
				L.d(TAG, "Headers:" + headers);
				for (String key : headers.keySet()) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			if (urlParams != null) {
				if (method.equals(METHOD_GET)) {
					urlString = urlString + "?" + urlParams;
				}

				if (method.equals(METHOD_POST)) {
					conn.setRequestProperty(HTTP.CONTENT_LEN, "" + Integer.toString(urlParams.getBytes().length));
					L.d(TAG, "Url params:"+urlParams);
					// Send request
					conn.setDoOutput(true);
					final DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
					wr.writeBytes(urlParams);
					wr.flush();
					wr.close();
				}
			}

			conn.setRequestMethod(method);
			status = conn.getResponseCode();
			L.d(TAG, "Status="+status);
			InputStream is = null;
			if (status >= HttpStatus.SC_BAD_REQUEST) // 400 or up
				is = conn.getErrorStream();
			else
				is = conn.getInputStream();

			final BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = rd.readLine()) != null) {
				builder.append(line);
			}
			rd.close();
		} catch (MalformedURLException e) {
			isConnectionError=true;
			errorCode=ServerCodes.URL_EXCEPTION;
			L.d(TAG, e.toString());
		} catch (IOException e) {
			isConnectionError=true;
			errorCode=ServerCodes.CONNECTION_EXCEPTION;
			L.d(TAG, e.toString());
		}

		finalResult = builder.toString();

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if(isConnectionError) {
			connectionListener.OnConnectionError(errorCode);
			L.e(TAG, "Connection error:"+ServerCodes.getErrorMsg(errorCode));
			return;
		}
		if (status >= HttpStatus.SC_BAD_REQUEST) {
			L.e(TAG, "Failure:"+finalResult);
			connectionListener.OnFailure(finalResult, status);
		}
		else {
			L.d(TAG, "Success:"+finalResult);
			connectionListener.OnSuccess(finalResult);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
}
