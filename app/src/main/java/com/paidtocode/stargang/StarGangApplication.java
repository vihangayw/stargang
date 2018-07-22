package com.paidtocode.stargang;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.paidtocode.stargang.listener.NetworkListener;

import java.util.ArrayList;
import java.util.List;

public class StarGangApplication extends MultiDexApplication {
	private static final String TAG = StarGangApplication.class.getSimpleName();
	private static StarGangApplication application;
	private final List<NetworkListener> networkListeners = new ArrayList<>();

	public static StarGangApplication getInstance() {
		return application;
	}

	/**
	 * Called when the application is starting, before any activity, service,
	 * or receiver objects (excluding content providers) have been created.
	 * Implementations should be as quick as possible (for example using
	 * lazy initialization of state) since the time spent in this function
	 * directly impacts the performance of starting the first activity,
	 * service, or receiver in a process.
	 * If you override this method, be sure to call super.onCreate().
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		MultiDex.install(this);
	}

	public void addNetworkListener(NetworkListener listener) {
		networkListeners.add(listener);
	}

	public void removeNetworkListeners() {
		networkListeners.clear();
	}

	public List<NetworkListener> getNetworkListeners() {
		return networkListeners;
	}

	public String getVersion() {
		String versionName = null;
		try {
			versionName = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("Error", e.getMessage());
		}
		return versionName;
	}

	public void restartApplication() {
		Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

}
