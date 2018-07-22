package com.paidtocode.stargang.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.PostRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;

public class PostUploadService extends Service {

	private NotificationManager systemService;

	public PostUploadService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();
			String caption = bundle.getString("caption");

			systemService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			notification();

			addPost(caption);
		} else stopService();
		return START_STICKY;
	}

	private void notification() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			CharSequence name = "star.gang";

			NotificationChannel mChannel = new NotificationChannel("107", name, NotificationManager.IMPORTANCE_HIGH);
			mChannel.setDescription("Post Uploading...");
			if (mNotificationManager != null)
				mNotificationManager.createNotificationChannel(mChannel);
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notification = new NotificationCompat.Builder(this, "107")
					.setContentTitle("Uploading")
					.setContentText(getString(R.string.uploading))
					.setSmallIcon(android.R.drawable.stat_sys_upload)
					.setProgress(0, 0, true)
					.setLargeIcon(BitmapFactory.decodeResource(getResources(),
							R.mipmap.ic_launcher))
					.setPriority(NotificationCompat.PRIORITY_LOW)
					.setColor(ContextCompat.getColor(this, R.color.colorAccent))//app name color
					.setColorized(true)
					.setTimeoutAfter(1000 * 60 * 30)//30min
					.setAutoCancel(false)
					.build();
			if (mNotificationManager != null)
				mNotificationManager.notify(101, notification);
		} else {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "107")
					.setContentTitle("Uploading")
					.setContentText(getString(R.string.uploading))
					.setSmallIcon(android.R.drawable.stat_sys_upload)
					.setProgress(0, 0, true)
					.setLargeIcon(BitmapFactory.decodeResource(getResources(),
							R.mipmap.ic_launcher))
					.setPriority(NotificationCompat.PRIORITY_LOW)
					.setColor(ContextCompat.getColor(this, R.color.colorAccent))//app name color
					.setColorized(true)
					.setTimeoutAfter(1000 * 60 * 30)//30min
					.setAutoCancel(false);
			android.app.Notification notification = mBuilder.build();
			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
			notificationManager.notify(101, notification);
		}
	}

	private void addPost(String caption) {
		Toast.makeText(this, "Uploading start", Toast.LENGTH_SHORT).show();
		new PostRequestHelperImpl().addPost(caption, new APIHelper.PostManResponseListener() {
			@Override
			public void onResponse(Ancestor ancestor) {
				if (ancestor.getStatus()) {
					Toast.makeText(PostUploadService.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
				}

				stopService();
			}

			@Override
			public void onError(Error error) {
				if (error != null) {
					if (!TextUtils.isEmpty(error.getMessage())) {
						Toast.makeText(PostUploadService.this,
								error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}

				stopService();
			}
		});
	}

	private void stopService() {
		if (systemService != null)
			systemService.cancelAll();
		stopForeground(true);
		stopSelf();
	}
}
