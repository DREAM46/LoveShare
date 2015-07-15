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
	private int mPosition = -1;//�ϴ�ѡ�е�λ��
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
		
		//��ʼ��ArrayList���ϸ����Ƿ�ѡ�е�״̬
		for (int i = 0; i < items.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("item", items[i]);
			hashMap.put("checked", false);
			arrayList.add(hashMap);
		}
		
		//��ʼ��ListView����ΪListView��������
		ListView listView = (ListView)this.findViewById(R.id.view_lv_radioButton);
		mAdapter = new ListDialogAdapter(context, arrayList);
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View pConvertView,
					int pPosition, long arg3) {
				//mPosition��Ϊ�ϴ�ѡ�е�������ظ�ѡ���򲻶�ListView���д���
				if (mPosition == pPosition) {
					return;
				} else if (-1 != mPosition) {
				//������ѡ�е���������ArrayList�д洢��״̬
					HashMap<String, Object> map = arrayList.get(mPosition);
					map.put("checked", false);
				}
				//����ѡ�еĸ��Ǿ�ѡ�е�
				mPosition = pPosition;
				HashMap<String, Object> map = arrayList.get(mPosition);
				map.put("checked", true);
				//ȡ���������ĸ���ѡ��
				item = (String) map.get("item");
				mAdapter.notifyDataSetChanged();

			}
		});
	}

	/**
	 * ���öԻ��������
	 * 
	 * @param title
	 *            �Ի������������
	 * @return �Ի�����
	 */
	public CustomListDialog setTitle(String title) {
		TextView view_dialog_title = (TextView) this
				.findViewById(R.id.view_dialog_title);
		view_dialog_title.setText(title);
		return this;
	}

	/**
	 * ����ȷ����ť�ļ�����
	 * 
	 * @param listener
	 *            ȷ����ť�ļ�����
	 * @return �Ի�����
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

