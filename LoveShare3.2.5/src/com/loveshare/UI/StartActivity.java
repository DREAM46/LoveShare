package com.loveshare.UI;

import com.loveshare.activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.Preference;
import android.widget.Toast;

public class StartActivity extends Activity {

	// public static ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		SharedPreferences preferences = this.getSharedPreferences(
				"sp_LoveShare", MODE_PRIVATE);
		boolean isStarted = preferences.getBoolean("isStarted", false);
		final boolean isStarted1 = isStarted;

		Editor editor = preferences.edit();

		// 如果是SharedPreferences中的isStarted为false，即用户第一次打开程序，则将isStarted设为true
		// 并且默认有提示音和手机振动的效果
		if (!isStarted) {
			createDeskShortCut();
			editor.putBoolean("isStarted", true);
			editor.putBoolean("isSounds", true);
			editor.putBoolean("isVibration", true);
			editor.putString("savePath",
					Environment.getExternalStorageDirectory().getAbsolutePath()
							+ this.getString(R.string.imgPath));
			editor.putInt("theme", R.drawable.background_winter);// 设置默认背景
			editor.commit();
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent();
				if (!isStarted1)
					intent.setClass(StartActivity.this, WelcomeActivity.class);
				else
					intent.setClass(StartActivity.this, MainActivity.class);
				StartActivity.this.startActivity(intent);
				StartActivity.this.overridePendingTransition(
						R.anim.alpha_enter, R.anim.alpha_exit);
				// 由于此界面不需要回退，所以跳转完立刻将其finish
				StartActivity.this.finish();

			}
		}, 300);

		/*
		 * dialog = new ProgressDialog(this);
		 * dialog.setIcon(R.drawable.ic_launcher);
		 * dialog.setMessage("please wait……");
		 */

	}

	/** 创建快捷方式 * */
	public void createDeskShortCut() {
		// 创建快捷方式的Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建 ，如果重复的话就会有多个快捷方式了
		shortcutIntent.putExtra("duplicate", false);
		// 这个就是应用程序图标下面的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.lable));
		// 快捷图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.logo1);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 这个MainActivity是调用此方法的Activity
		Intent intent = new Intent(getApplicationContext(), StartActivity.class);
		// 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 点击快捷图片，运行的程序主入口
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 最后一步就是发送广播
		sendBroadcast(shortcutIntent);
		// Toast.makeText(this, "已在手机屏幕创建快捷方式", Toast.LENGTH_LONG).show();
	}
}
