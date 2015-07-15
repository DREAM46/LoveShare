package com.loveshare.setting;

import com.loveshare.UI.SettingActivity;
import com.loveshare.activity.R;
import com.loveshare.service.SetTheme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeActivity extends Activity implements OnClickListener {

	private LinearLayout linear_theme1;
	private LinearLayout linear_theme2;
	private LinearLayout linear_theme3;
	private LinearLayout linear_theme4;

	private ImageView img_theme1;
	private ImageView img_theme2;
	private ImageView img_theme3;
	private ImageView img_theme4;
	private ImageView[] imgs;
	private LinearLayout titleLinear;
	private TextView setting_title;

	private SharedPreferences preferences;
	private Editor editor;

	private int pic;

	private boolean isChanged = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);

		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
		editor = preferences.edit();

		this.init();
		this.setOnClickListener();
	}

	private void init() {

		linear_theme1 = (LinearLayout) this.findViewById(R.id.linear_theme1);
		linear_theme2 = (LinearLayout) this.findViewById(R.id.linear_theme2);
		linear_theme3 = (LinearLayout) this.findViewById(R.id.linear_theme3);
		linear_theme4 = (LinearLayout) this.findViewById(R.id.linear_theme4);

		img_theme1 = (ImageView) this.findViewById(R.id.img_theme1);
		img_theme2 = (ImageView) this.findViewById(R.id.img_theme2);
		img_theme3 = (ImageView) this.findViewById(R.id.img_theme3);
		img_theme4 = (ImageView) this.findViewById(R.id.img_theme4);

		titleLinear = (LinearLayout) this.findViewById(R.id.titleLinear);
		setting_title = (TextView) this.findViewById(R.id.setting_title);

		imgs = new ImageView[] { img_theme1, img_theme2, img_theme3, img_theme4 };

		this.setChecked(preferences.getInt("theme", 0));
	}

	private void setOnClickListener() {
		for (int i = 0; i < imgs.length; i++) {
			imgs[i].setOnClickListener(this);
		}
		linear_theme1.setOnClickListener(this);
		linear_theme2.setOnClickListener(this);
		linear_theme3.setOnClickListener(this);
		linear_theme4.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		// preferences = this.getSharedPreferences("sp_LoveShare",
		// MODE_PRIVATE);
		// pic = preferences.getInt("theme", 0);

		this.setUnchecked();
		setChecked(view.getId());

		switch (view.getId()) {
		case R.id.linear_theme1:
		case R.id.img_theme1:

			/*
			 * if (pic == R.drawable.background_winter) isChanged = false;
			 */

			editor.putInt("theme", R.drawable.background_winter);
			break;
		case R.id.linear_theme2:
		case R.id.img_theme2:

			/*
			 * if (pic == R.drawable.theme2) isChanged = false;
			 */

			editor.putInt("theme", R.drawable.theme2);
			break;
		case R.id.linear_theme3:
		case R.id.img_theme3:

			/*
			 * if (pic == R.drawable.theme3) isChanged = false;
			 */

			editor.putInt("theme", R.drawable.theme3);
			break;
		case R.id.linear_theme4:
		case R.id.img_theme4:

			/*
			 * if (pic == R.drawable.theme4) isChanged = false;
			 */

			editor.putInt("theme", R.drawable.theme4);
			break;
		}
		editor.commit();
	}

	// 让被选中的主题呈现红心标志
	private void setChecked(int id) {

		switch (id) {
		case R.drawable.background_winter:
		case R.id.linear_theme1:
		case R.id.img_theme1:
			img_theme1.setImageResource(R.drawable.love_checked);
			SetTheme.setBackground(titleLinear, R.drawable.background_winter,
					setting_title);
			break;
		case R.drawable.theme2:
		case R.id.linear_theme2:
		case R.id.img_theme2:
			img_theme2.setImageResource(R.drawable.love_checked);
			SetTheme.setBackground(titleLinear, R.drawable.theme2,
					setting_title);
			break;
		case R.drawable.theme3:
		case R.id.linear_theme3:
		case R.id.img_theme3:
			img_theme3.setImageResource(R.drawable.love_checked);
			SetTheme.setBackground(titleLinear, R.drawable.theme3,
					setting_title);
			break;
		case R.drawable.theme4:
		case R.id.linear_theme4:
		case R.id.img_theme4:
			img_theme4.setImageResource(R.drawable.love_checked);
			SetTheme.setBackground(titleLinear, R.drawable.theme4,
					setting_title);
			break;
		default:
			break;
		}

	}

	// 将所用可选按钮变成未选中状态
	private void setUnchecked() {
		for (int i = 0; i < imgs.length; i++) {
			imgs[i].setImageResource(R.drawable.love_unchecked);
		}
	}

	@Override
	public void onBackPressed() {
		this.startActivity(new Intent(this, SettingActivity.class));
		super.onBackPressed();
	}
}
