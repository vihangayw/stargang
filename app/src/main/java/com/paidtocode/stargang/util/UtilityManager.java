package com.paidtocode.stargang.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paidtocode.stargang.R;

import java.text.SimpleDateFormat;

public class UtilityManager {


	public static String formatTicketNumber(int number) {
		if (number == 0) {
			return "000";
		}
		if (number <= 9) {
			return "00" + (number);
		} else if (number <= 99) {
			return "0" + (number);
		} else {
			return "" + (number);
		}
	}

	public static void showSnack(String msg, int duration, CoordinatorLayout coordinatorLayout) {
		Snackbar.make(coordinatorLayout, msg, duration).show();
	}

	public static ProgressDialog showProgressAlert(final Context context, String msg) {
		ProgressDialog builder = new ProgressDialog(context, R.style.DialogStyle);
		builder.setMessage(msg);
		builder.setCancelable(false);
		builder.show();
		return builder;
	}

	public static ObjectMapper getDefaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(getDefaultSimpleDateFormat());
		return objectMapper;
	}

	private static SimpleDateFormat getDefaultSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", java.util.Locale.getDefault());
	}

}
