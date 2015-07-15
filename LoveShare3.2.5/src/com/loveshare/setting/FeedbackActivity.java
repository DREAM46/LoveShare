package com.loveshare.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loveshare.activity.R;
import com.loveshare.service.SetTheme;
import com.loveshare.share.SendActivity;

public class FeedbackActivity extends Activity implements OnItemClickListener {

	private LinearLayout linear_title;
	private ListView feedbackList;
	private TextView text_title;

	private static final int POSITION_MAIL = 0;
	private static final int POSITION_SINA = 1;

	private String shareText = "#�԰�������������#@������_ISHARE  ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_feedback);

		// ͼƬ��Դ
		int[] img_feedback = new int[] { R.drawable.mail, R.drawable.weibo_sina };

		String[] name_feedback = new String[] { "�ʼ�", "����΢��" };

		linear_title = (LinearLayout) this.findViewById(R.id.linear_title);
		feedbackList = (ListView) this
				.findViewById(R.id.recommend_feedback_List);
		text_title = (TextView) this.findViewById(R.id.setting_title);
		text_title.setText(this.getResources().getString(
				R.string.linear_feedback));

		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", 0);
		if (drawable != 0)
			SetTheme.setBackground(linear_title, drawable, text_title);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < img_feedback.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img_feedback", img_feedback[i]);
			map.put("name_feedback", name_feedback[i]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.activity_recommend_feedbak_cell, new String[] {
						"img_feedback", "name_feedback" }, new int[] {
						R.id.img_recommend, R.id.name_recommend });
		System.out.println("adapter is null" + (adapter == null));

		feedbackList.setAdapter(adapter);
		feedbackList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, SendActivity.class);
		switch (position) {
		case POSITION_MAIL:
			sendMail();
			break;
		case POSITION_SINA:
			Intent intent1 = new Intent(this, SendActivity.class);
			intent1.putExtra("name", "����΢��");
			intent1.putExtra("shareText", this.shareText);
			this.startActivity(intent1);
			break;
		}

	}

	private void sendMail() {
		Intent email = new Intent(android.content.Intent.ACTION_SENDTO);
		email.setType("plain/text");
		// String[] emailReciver = new String[]{"loveshare_hwh@163.com"};
		/*
		 * String emailSubject = "�������";
		 * email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
		 * email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		 */
		// �����ʼ�Ĭ�ϵ�ַ
		email.setData(Uri.parse("mailto:loveshare_hwh@163.com"));
		// �����ʼ�Ĭ������
		email.putExtra(Intent.EXTRA_SUBJECT, "�������");
		// ����ϵͳ���ʼ�ϵͳ
		startActivity(email);
	}

}
