package com.paidtocode.stargang.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.factory.AncestorsFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Vihanga on 27/2/18.
 */
public class APIHelper {
	private static APIHelper instance;

	private APIHelper() {
	}

	public static APIHelper getInstance() {
		if (instance == null) instance = new APIHelper();
		return instance;
	}

	public void sendStringRequestsWithParams(final PostManResponseListener context,
	                                         final AncestorsFactory factory,
	                                         int httpMethod, String apiUrl,
	                                         final Map<String, String> paramMap) {

		StringRequest request = new StringRequest(httpMethod, apiUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (context != null) {
					try {
						context.onResponse(factory.parse(new JSONObject(response)));
					} catch (IOException e) {
						e.printStackTrace();
						context.onError(new Error("IOException", e.getLocalizedMessage(), false));
					} catch (JSONException e) {
						e.printStackTrace();
						context.onError(new Error("JSONException", e.getLocalizedMessage(), false));
					}

				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (context != null)
					context.onError(VolleySingleton.getInstance().getErrorMessage(error));
			}
		}) {
			@Override
			protected Map<String, String> getParams() {

				return paramMap;
			}

			@Override
			public Map<String, String> getHeaders() {
				return VolleySingleton.getInstance().getAPIHeaderUrlEncoded();
			}
		};

		VolleySingleton.getInstance().addToRequestQueue(request);
	}


	public void sendAuthStringRequestsWithParams(final PostManResponseListener context,
	                                             final AncestorsFactory factory,
	                                             int httpMethod, String apiUrl,
	                                             final Map<String, String> paramMap) {

		StringRequest request = new StringRequest(httpMethod, apiUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if (context != null) {
					try {
						context.onResponse(factory.parse(new JSONObject(response)));
					} catch (IOException e) {
						e.printStackTrace();
						context.onError(new Error("IOException", e.getLocalizedMessage(), false));
					} catch (JSONException e) {
						e.printStackTrace();
						context.onError(new Error("JSONException", e.getLocalizedMessage(), false));
					}
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (context != null)
					context.onError(VolleySingleton.getInstance().getErrorMessage(error));
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				return paramMap;
			}

			@Override
			public Map<String, String> getHeaders() {
				return VolleySingleton.getInstance().getAPIHeaderUrlEncodedWithAuth();
			}
		};

		VolleySingleton.getInstance().addToRequestQueue(request);
	}


	public interface PostManResponseListener {
		void onResponse(Ancestor ancestor);

		void onError(Error error);
	}
}