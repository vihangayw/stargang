package com.paidtocode.stargang.api;

import android.graphics.Bitmap;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.factory.AncestorsFactory;
import com.paidtocode.stargang.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Vihanga on 27/2/18.
 */
public class APIHelper {
	private static APIHelper instance;
	private static final String JSON_PREFIX = "{\"data\":";

	private APIHelper() {
	}

	public static APIHelper getInstance() {
		if (instance == null) instance = new APIHelper();
		return instance;
	}

	public void sendMultipartRequestProfile(final PostManResponseListener context,
	                                        final AncestorsFactory factory,
	                                        int httpMethod, String apiUrl,
	                                        final Map<String, String> paramMap) {

		VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(httpMethod, apiUrl,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						if (context != null) {
							try {
								String reStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
								context.onResponse(factory.parse(new JSONObject(reStr)));
							} catch (IOException e) {
								e.printStackTrace();
								context.onError(new Error("IOException", e.getLocalizedMessage(), false));
							} catch (JSONException e) {
								e.printStackTrace();
								context.onError(new Error("JSONException", e.getLocalizedMessage(), false));
							}
						}
					}
				},
				new Response.ErrorListener() {
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
			protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
				Map<String, DataPart> params = new HashMap<>();
				// for now just get bitmap data from ImageView
				if (Constants.cover != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					Constants.cover.compress(Bitmap.CompressFormat.JPEG, 50, stream);
					byte[] byteArray = stream.toByteArray();
					params.put("pimage", new DataPart("pimage.jpg",
							byteArray, "image/jpeg"));
				}
				if (Constants.profilePic != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					Constants.profilePic.compress(Bitmap.CompressFormat.JPEG, 50, stream);
					byte[] byteArray = stream.toByteArray();
					params.put("cimage", new DataPart("cimage.jpg",
							byteArray, "image/jpeg"));
				}

				return params;
			}
		};

		VolleySingleton.getInstance().addToRequestQueue(multipartRequest);

	}

	public void sendMultipartRequestPosts(final PostManResponseListener context,
	                                      final AncestorsFactory factory,
	                                      int httpMethod, String apiUrl,
	                                      final Map<String, String> paramMap) {

		VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(httpMethod, apiUrl,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						if (context != null) {
							try {
								String reStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
								context.onResponse(factory.parse(new JSONObject(reStr)));
							} catch (IOException e) {
								e.printStackTrace();
								context.onError(new Error("IOException", e.getLocalizedMessage(), false));
							} catch (JSONException e) {
								e.printStackTrace();
								context.onError(new Error("JSONException", e.getLocalizedMessage(), false));
							}
						}
					}
				},
				new Response.ErrorListener() {
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
			protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
				Map<String, DataPart> params = new HashMap<>();
				// for now just get bitmap data from ImageView
				Random random = new Random();
				for (int i = 0; i < Constants.bitmaps.size(); i++) {
					Bitmap bitmap = Constants.bitmaps.get(i);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
					byte[] byteArray = stream.toByteArray();
					params.put("image[" + i + "]", new DataPart("post_" + i + "_" + Math.abs(random.nextLong()) + ".jpg",
							byteArray, "image/jpeg"));
				}
				return params;
			}
		};

		VolleySingleton.getInstance().addToRequestQueue(multipartRequest);

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