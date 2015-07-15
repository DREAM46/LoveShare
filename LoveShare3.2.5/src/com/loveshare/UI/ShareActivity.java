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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ShareActivity extends Activity implements OnItemClickListener {

	private ListView recommendList;
	private TextView text_recommend;
	private LinearLayout linear_title;

	private String shareImgPath;
	private int[] img_weibos;
	private String[] name_weibos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend_feedback);

		text_recommend = (TextView) this.findViewById(R.id.setting_title);
		text_recommend.setText("图片分享");

		shareImgPath = this.getIntent().getStringExtra("shareImgPath");
		// 图片资源
		img_weibos = new int[] { R.drawable.weixin, R.drawable.weibo_sina,
				R.drawable.weibo_tencent, R.drawable.weibo_netease };

		name_weibos = new String[] { "微信好友", "新浪微博", "腾讯微博", "网易微博" };

		recommendList = (ListView) this
				.findViewById(R.id.recommend_feedback_List);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < img_weibos.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img_weibo", img_weibos[i]);
			map.put("name_weibo", name_weibos[i]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.activity_recommend_feedbak_cell, new String[] {
						"img_weibo", "name_weibo" }, new int[] {
						R.id.img_recommend, R.id.name_recommend });

		recommendList.setAdapter(adapter);
		recommendList.setOnItemClickListener(this);

		linear_title = (LinearLayout) this.findViewById(R.id.linear_title);
		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", 0);
		if (drawable != 0)
			SetTheme.setBackground(linear_title, drawable, text_recommend);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		// 跳转至发送微博界面的intent
		Intent intent = new Intent(ShareActivity.this, SendActivity.class);
		if (shareImgPath != null)
			intent.putExtra("shareImgPath", shareImgPath);// 即将分享的图片的路径

		if (position == 0 && !isWeChatAvilible()) {
			ShowCustomToast.show(this, "请安装微信");
			return;
		}
		intent.putExtra("name", name_weibos[position] + "");// 微博的图标
		this.finish();
		this.startActivity(intent);

	}

	/**
	 * 判断手机是否已经安装微信应用程序
	 * 
	 * @return
	 */
	private boolean isWeChatAvilible() {
		final PackageManager packageManager = this.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		// 从pinfo中将包名字逐一取出，压入pName list中
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
