package com.paidtocode.stargang.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.paidtocode.stargang.NFCApplication;
import com.paidtocode.stargang.listener.NetworkListener;

import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			boolean online = isOnline(context);
			List<NetworkListener> networkListeners = NFCApplication.getInstance().getNetworkListeners();
			for (NetworkListener listener : networkListeners) {
				listener.onNetworkChange(online);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private boolean isOnline(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			return (netInfo != null && netInfo.isConnected());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
}