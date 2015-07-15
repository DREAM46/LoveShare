package com.loveshare.view;

import android.content.Context;

public class CreateCustomProgressDialog {

	public static CustomProgressDialog createCustomProgressDialog(Context context){
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.setMsg("¥¶¿Ì÷–.......");
		return dialog;
	}
}
