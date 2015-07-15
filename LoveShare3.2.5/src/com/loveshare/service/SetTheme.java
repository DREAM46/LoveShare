package com.loveshare.service;

import android.widget.LinearLayout;
import android.widget.TextView;

public class SetTheme {
	public static void setBackground(LinearLayout titleLinear, int drawable,
			TextView text_title) {
		titleLinear.setBackgroundResource(drawable);
	}
}
