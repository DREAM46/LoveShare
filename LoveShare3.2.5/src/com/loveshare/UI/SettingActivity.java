package com.loveshare.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loveshare.activity.R;
import com.loveshare.help.HelpActivity;
import com.loveshare.service.SetTheme;
import com.loveshare.setting.AboutActivity;
import com.loveshare.setting.FeedbackActivity;
import com.loveshare.setting.FileExplorerActivity;
import com.loveshare.setting.ThemeActivity;
import com.loveshare.view.ShowCustomToast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SettingActivity extends Activity implements OnTouchListener,
		OnCheckedChangeListener {

	private SharedPreferences preferences;
	private Editor editor;

	private LinearLayout linear_theme;
	private LinearLayout linear_about;
	private LinearLayout linear_feedback;
	private LinearLayout linear_navigation;
	private LinearLayout linear_path;
//	private LinearLayout linear_help;

	private CheckBox check_sounds;
	private CheckBox check_vibration;

	private LinearLayout linear_title;
	private TextView text_title;

	private ImageView checked_theme;
	private TextView checked_path;

	@Override
	protected void onStart() {
		checked_path.setText("±£´æ    " + preferences.getString("savePath", null));
		int drawable = preferences.getInt("theme", 0);
		checked_theme.setImageResource(drawable);
		if (drawable != 0)
			SetTheme.setBackground(linear_title, drawable, text_title);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		init();
		setListener();
	}

	private void init() {

		linear_theme = (LinearLayout) this.findViewById(R.id.linear_theme);
		linear_about = (LinearLayout) this.findViewById(R.id.linear_about);
		linear_feedback = (LinearLayout) this
				.findViewById(R.id.linear_feedback);
		linear_navigation = (LinearLayout) this
				.findViewById(R.id.linear_navigation);
		linear_path = (LinearLayout) this.findViewById(R.id.linear_path);

	//	linear_help = (LinearLayout)this.findViewById(R.id.linear_help);
		
		check_sounds = (CheckBox) this.findViewById(R.id.check_sounds);

		check_vibration = (CheckBox) this.findViewById(R.id.check_vibration);

		linear_title = (LinearLayout) this.findViewById(R.id.linear_title);
		text_title = (TextView) this.findViewById(R.id.text_title);

		checked_theme = (ImageView) this.findViewById(R.id.checked_theme);

		checked_path = (TextView) this.findViewById(R.id.checked_path);

		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
		editor = preferences.edit();

		if (preferences.getBoolean("isSounds", false))
			check_sounds.setChecked(true);
		if (preferences.getBoolean("isVibration", false))
			check_vibration.setChecked(true);

	}

	private void setListener() {
		
		linear_theme.setOnTouchListener(this);
		linear_path.setOnTouchListener(this);
		linear_about.setOnTouchListener(this);
		linear_feedback.setOnTouchListener(this);
		linear_navigation.setOnTouchListener(this);
	//	linear_help.setOnTouchListener(this);
		
		check_sounds.setOnCheckedChangeListener(this);
		check_vibration.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.check_sounds:
			if (check_sounds.isChecked()) {
				editor.putBoolean("isSounds", true);
				ShowCustomToast.show(SettingActivity.this, R.string.isSounds);
			} else {
				editor.putBoolean("isSounds", false);
				ShowCustomToast.show(SettingActivity.this, R.string.notSounds);
			}
			break;
		case R.id.check_vibration:
			if (check_vibration.isChecked()) {
				editor.putBoolean("isVibration", true);
				ShowCustomToast.show(SettingActivity.this, R.string.isVibration);
			} else {
				editor.putBoolean("isVibration", false);
				ShowCustomToast.show(SettingActivity.this, R.string.notVibration);
			}
			break;
		}
		editor.commit();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_UP) {
			Intent intent = new Intent();

			switch (v.getId()) {

			case R.id.linear_theme:
				intent.setClass(this, ThemeActivity.class);
				break;
			case R.id.linear_about:
				intent.setClass(this, AboutActivity.class);
				break;
			case R.id.linear_navigation:
				intent.setClass(this, WelcomeActivity.class);
				intent.putExtra("isNavigation", true);
				break;
			case R.id.linear_path:
				intent.setClass(this, FileExplorerActivity.class);
				break;
			case R.id.linear_feedback:
				intent.setClass(this, FeedbackActivity.class);
				break;
		/*	case R.id.linear_help:
				intent.setClass(this,HelpActivity.class);
				break;*/
			}
			startAct(v.getId(), intent);
		}
		return true;
	}

	private void startAct(int id, Intent intent) {
		this.startActivity(intent);
		if (id == R.id.linear_theme)
			this.finish();
	}
}