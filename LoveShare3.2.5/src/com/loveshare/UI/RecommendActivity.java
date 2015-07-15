package com.loveshare.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loveshare.activity.R;
import com.loveshare.service.SendSms;
import com.loveshare.service.SetTheme;
import com.loveshare.share.SendActivity;
import com.loveshare.view.ShowCustomToast;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RecommendActivity extends Activity implements OnItemClickListener {

	private ListView recommendList;

	private LinearLayout linear_title;
	private TextView text_title;

	private static final int POSITION_SMS = 0;
	private static final int POSITION_WEIXIN = 1;
	private static final int POSITION_SINA = 2;
	private static final int POSITION_TENCENT = 3;
	private static final int POSITION_NETEASE = 4;

	private String shareText = "�Ұ�װ�ˡ��������������ʵ���������������ĵ���Ƭ��"
			+ "�����ֻ�����е���Ƭ����������������Ƭ�ٷ���" + "��ô�����ף���Ҳ������������ɣ����ص�ַ��"
			+ "http://mm.10086.cn/android/info/221629.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_feedback);

		// ͼƬ��Դ
		int[] img_weibo = new int[] { R.drawable.sms, R.drawable.weixin,
				R.drawable.weibo_sina, R.drawable.weibo_tencent,
				R.drawable.weibo_netease };

		String[] name_weibo = new String[] { "����", "΢�ź���", "����΢��", "��Ѷ΢��",
				"����΢��" };

		recommendList = (ListView) this
				.findViewById(R.id.recommend_feedback_List);

		linear_title = (LinearLayout) this.findViewById(R.id.linear_title);
		text_title = (TextView) this.findViewById(R.id.text_title);

		processPrefer();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < img_weibo.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img_weibo", img_weibo[i]);
			map.put("name_weibo", name_weibo[i]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.activity_recommend_feedbak_cell, new String[] {
						"img_weibo", "name_weibo" }, new int[] {
						R.id.img_recommend, R.id.name_recommend });

		recommendList.setAdapter(adapter);
		recommendList.setOnItemClickListener(this);

	}

	private void processPrefer() {
		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", 0);
		if (drawable != 0)
			SetTheme.setBackground(linear_title, drawable, text_title);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(this, SendActivity.class);
		intent.putExtra("shareText", this.shareText);

		if (position == POSITION_SMS) {
			SendSms.sendSms(this, this.shareText);
			return;
		}

		switch (position) {

		case POSITION_WEIXIN:
			if (!isWeChatAvilible()) {
				ShowCustomToast.show(this, "�밲װ΢��");
				return;
			}

			intent.putExtra("name", "΢�ź���");
			break;

		case POSITION_SINA:
			intent.putExtra("name", "����΢��");
			break;
		case POSITION_TENCENT:
			intent.putExtra("name", "��Ѷ΢��");
			break;
		case POSITION_NETEASE:
			intent.putExtra("name", "����΢��");
			break;

		}
		this.startActivity(intent);
		this.finish();

	}

	private boolean isWeChatAvilible() {
		final PackageManager packageManager = this.getPackageManager();// ��ȡpackagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// ��ȡ�����Ѱ�װ����İ���Ϣ
		// ��pinfo�н���������һȡ����ѹ��pName list��
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}
}
