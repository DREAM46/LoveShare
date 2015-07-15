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
	 * 为确定按钮绑定监听器
	 * 
	 * @param listener
	 *            确定按钮绑定监听器
	 * @return 对话框本身
	 */
	public CustomInputDialog setPositiveListener(
			android.view.View.OnClickListener listener) {
		Button view_input_positive = (Button) this
				.findViewById(R.id.view_input_positive);
		view_input_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * 获取输入框中的文件夹的名字
	 * 
	 * @return 输入框中的文件夹的名字
	 */
	public String getFileName() {
		return view_input_et.getText().toString().trim();
	}
	/**
	 * 将输入框中的文件夹的名字设为空
	 */
	public void setNull(){
		view_input_et.setText("");
	}
	
	public void setDefaultMsg(){
		view_input_et.setText("新建文件夹");
	}

}
