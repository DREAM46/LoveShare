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

		// �����SharedPreferences�е�isStartedΪfalse�����û���һ�δ򿪳�����isStarted��Ϊtrue
		// ����Ĭ������ʾ�����ֻ��񶯵�Ч��
		if (!isStarted) {
			createDeskShortCut();
			editor.putBoolean("isStarted", true);
			editor.putBoolean("isSounds", true);
			editor.putBoolean("isVibration", true);
			editor.putString("savePath",
					Environment.getExternalStorageDirectory().getAbsolutePath()
							+ this.getString(R.string.imgPath));
			editor.putInt("theme", R.drawable.background_winter);// ����Ĭ�ϱ���
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
				// ���ڴ˽��治��Ҫ���ˣ�������ת�����̽���finish
				StartActivity.this.finish();

			}
		}, 300);

		/*
		 * dialog = new ProgressDialog(this);
		 * dialog.setIcon(R.drawable.ic_launcher);
		 * dialog.setMessage("please wait����");
		 */

	}

	/** ������ݷ�ʽ * */
	public void createDeskShortCut() {
		// ������ݷ�ʽ��Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// �������ظ����� ������ظ��Ļ��ͻ��ж����ݷ�ʽ��
		shortcutIntent.putExtra("duplicate", false);
		// �������Ӧ�ó���ͼ�����������
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.lable));
		// ���ͼƬ
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.logo1);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// ���MainActivity�ǵ��ô˷�����Activity
		Intent intent = new Intent(getApplicationContext(), StartActivity.class);
		// ��������������Ϊ�˵�Ӧ�ó���ж��ʱ���� �ϵĿ�ݷ�ʽ��ɾ��
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// ������ͼƬ�����еĳ��������
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// ���һ�����Ƿ��͹㲥
		sendBroadcast(shortcutIntent);
		// Toast.makeText(this, "�����ֻ���Ļ������ݷ�ʽ", Toast.LENGTH_LONG).show();
	}
}
