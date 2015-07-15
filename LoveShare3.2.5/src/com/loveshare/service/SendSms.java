package com.loveshare.service;

import com.loveshare.UI.RecommendActivity;
import com.loveshare.activity.R;

import android.content.Context;
import android.content.Intent;

public class SendSms {

	public static void sendSms(Context context,String shareText) {
		
		Intent mIntent = new Intent(Intent.ACTION_VIEW);
		mIntent.putExtra("sms_body", shareText);
		mIntent.setType("vnd.android-dir/mms-sms");
		context.startActivity(mIntent);
	}
}
