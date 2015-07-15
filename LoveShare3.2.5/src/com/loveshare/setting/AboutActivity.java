package com.loveshare.setting;

import com.loveshare.activity.R;
import com.loveshare.service.SetTheme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private LinearLayout aboutActivity = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		aboutActivity = (LinearLayout)this.findViewById(R.id.aboutActivity);
	}
	
	private void setActivityBackground() {
		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", 0);
		aboutActivity.setBackgroundResource(drawable);
	}
	

	//重写onStart,读取配置文件的内容，设置Activity背景
	@Override
	protected void onStart() {
		setActivityBackground();
		super.onStart();
	}

}
