package com.loveshare.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loveshare.activity.R;

public class CustomInputDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText view_input_et;

	public CustomInputDialog(Context context) {
		super(context, R.style.dialo_style);
		setContentView(R.layout.view_input_dialog);

		Button view_input_negative = (Button) this
				.findViewById(R.id.view_input_negative);
		view_input_negative.setOnClickListener(this);

		view_input_et = (EditText) this.findViewById(R.id.view_input_et);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	/**
	 * Ϊȷ����ť�󶨼�����
	 * 
	 * @param listener
	 *            ȷ����ť�󶨼�����
	 * @return �Ի�����
	 */
	public CustomInputDialog setPositiveListener(
			android.view.View.OnClickListener listener) {
		Button view_input_positive = (Button) this
				.findViewById(R.id.view_input_positive);
		view_input_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * ��ȡ������е��ļ��е�����
	 * 
	 * @return ������е��ļ��е�����
	 */
	public String getFileName() {
		return view_input_et.getText().toString().trim();
	}
	/**
	 * ��������е��ļ��е�������Ϊ��
	 */
	public void setNull(){
		view_input_et.setText("");
	}
	
	public void setDefaultMsg(){
		view_input_et.setText("�½��ļ���");
	}

}
