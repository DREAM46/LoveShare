package com.loveshare.imgProcessUtil;

import android.content.Context;
import android.os.Handler;

public class ImgHandler extends Handler {

	private Context context;
	public static final int FAIL = 0;
	public static final int EDIT_SUCCESS = 1;
	public static final int DECODE_SUCCESS = 2;
	public static final int PROGREE_SUCCESS = 3;

	public ImgHandler(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return this.context;
	}

	private void onSuccess() {

	}

	private void onFail() {

	}

}
