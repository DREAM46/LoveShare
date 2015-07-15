package com.loveshare.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loveshare.activity.R;

public class CustomListDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private int mPosition = -1;//上次选中的位置
	private ArrayList<HashMap<String, Object>> arrayList;
	private String item;
	private ListDialogAdapter mAdapter;
	
	public CustomListDialog(Context context,String[] items) {
		super(context, R.style.dialo_style);

		setContentView(R.layout.view_radiobutton_dialog);
		
		this.context = context;
		
		Button view_RBdialog_negative = (Button) this
				.findViewById(R.id.view_RBdialog_negative);
		view_RBdialog_negative.setOnClickListener(this);
		
		arrayList = new ArrayList<HashMap<String, Object>>();
		
		//初始化ArrayList集合各项是否被选中的状态
		for (int i = 0; i < items.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("item", items[i]);
			hashMap.put("checked", false);
			arrayList.add(hashMap);
		}
		
		//初始化ListView并且为ListView绑定适配器
		ListView listView = (ListView)this.findViewById(R.id.view_lv_radioButton);
		mAdapter = new ListDialogAdapter(context, arrayList);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View pConvertView,
					int pPosition, long arg3) {
				//mPosition即为上次选中的子项，若重复选择，则不对ListView进行处理
				if (mPosition == pPosition) {
					return;
				} else if (-1 != mPosition) {
				//若是新选中的子项，则更新ArrayList中存储的状态
					HashMap<String, Object> map = arrayList.get(mPosition);
					map.put("checked", false);
				}
				//将新选中的覆盖旧选中的
				mPosition = pPosition;
				HashMap<String, Object> map = arrayList.get(mPosition);
				map.put("checked", true);
				//取出到底是哪个被选中
				item = (String) map.get("item");
				mAdapter.notifyDataSetChanged();

			}
		});
	}

	/**
	 * 设置对话框标题栏
	 * 
	 * @param title
	 *            对话框标题栏文字
	 * @return 对话框本身
	 */
	public CustomListDialog setTitle(String title) {
		TextView view_dialog_title = (TextView) this
				.findViewById(R.id.view_dialog_title);
		view_dialog_title.setText(title);
		return this;
	}

	/**
	 * 设置确定按钮的监听器
	 * 
	 * @param listener
	 *            确定按钮的监听器
	 * @return 对话框本身
	 */
	public CustomListDialog setPositiveListener(
			android.view.View.OnClickListener listener) {
		Button view_RBdialog_positive = (Button) this
				.findViewById(R.id.view_RBdialog_positive);
		view_RBdialog_positive.setOnClickListener(listener);
		return this;
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	public String getItem() {
		return item;
	}	
	
}

