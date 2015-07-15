package com.loveshare.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loveshare.activity.R;
/**
 * 自定义提示对话框
 * @author 温坤哲
 *
 */
public class CustomAlertDialog extends Dialog implements OnClickListener {

	private boolean isSuccess;

	private Button view_dialog_positive;
	private Button view_dialog_negative;
	private Button view_dialog_neutral;

	public CustomAlertDialog(Context context) {
		super(context, R.style.dialo_style);
		setContentView(R.layout.view_alert_dialog);

		view_dialog_positive = (Button) this.findViewById(R.id.view_dialog_positive);

		view_dialog_negative = (Button) this
				.findViewById(R.id.view_dialog_negative);
		view_dialog_negative.setOnClickListener(this);

		view_dialog_neutral = (Button) this
				.findViewById(R.id.view_dialog_neutral);
		view_dialog_negative.setOnClickListener(this);
	}

	/**
	 * 返回点击按对话框按钮之后的成功标志
	 * 
	 * @return 点击按对话框按钮之后的成功标志
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * 设置对话框标题的文字
	 * 
	 * @param str
	 *            要设置的确定按钮的文字
	 * @return 对话框本身
	 */
	public CustomAlertDialog setTitleText(String str) {
		TextView view_dialog_title = (TextView) this
				.findViewById(R.id.view_dialog_title);
		view_dialog_title.setText(str);
		return this;
	}

	/**
	 * 设置成功按钮的文字
	 * 
	 * @param str
	 *            要设置的确定按钮的文字
	 * @return 对话框本身
	 */
	public CustomAlertDialog setPositiveText(String str) {
		view_dialog_positive.setText(str);
		return this;
	}

	/**
	 * 设置取消按钮的文字
	 * 
	 * @param str
	 *            要设置的取消按钮的文字
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNegativeText(String str) {
		view_dialog_negative.setText(str);
		return this;
	}

	/**
	 * 设置取消按钮的文字
	 * 
	 * @param str
	 *            要设置的中立按钮的文字
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNeutralText(String str) {
		view_dialog_neutral.setText(str);
		return this;
	}

	/**
	 * 设置确定按钮不可见
	 * 
	 * @return 对话框本身
	 */
	public CustomAlertDialog setPositiveGone() {
		view_dialog_positive.setVisibility(View.GONE);
		return this;
	}

	/**
	 * 设置取消按钮不可见
	 * 
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNegativeGone() {
		view_dialog_positive.setVisibility(View.GONE);
		return this;
	}

	/**
	 * 设置中立按钮不可见
	 * 
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNeutralGone() {
		view_dialog_neutral.setVisibility(View.GONE);
		((LinearLayout) this.findViewById(R.id.layout_line))
				.setVisibility(View.GONE);
		return this;
	}

	/**
	 * 设置对话框主题信息内容
	 * 
	 * @param str
	 *            对话框主题信息内容
	 * @return 对话框本身
	 */
	public CustomAlertDialog setMsg(String str) {
		TextView view_dialog_msg = (TextView) this
				.findViewById(R.id.view_dialog_msg);
		view_dialog_msg.setText(str);
		return this;
	}

	/**
	 * 设置确定按钮的监听器
	 * 
	 * @param listener
	 *            监听器
	 * @return 对话框本身
	 */
	public CustomAlertDialog setPositiveOnClickListener(
			android.view.View.OnClickListener listener) {
		view_dialog_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置取消按钮的监听器
	 * 
	 * @param listener
	 *            监听器
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNegativeOnClickListener(
			android.view.View.OnClickListener listener) {
		view_dialog_negative.setOnClickListener(listener);
		return this;
	}

	/**
	 * 设置中立按钮的监听器
	 * 
	 * @param listener
	 *            监听器
	 * @return 对话框本身
	 */
	public CustomAlertDialog setNeutralOnClickListener(
			android.view.View.OnClickListener listener) {
		view_dialog_neutral.setOnClickListener(listener);
		return this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_dialog_negative:
			this.dismiss();
			break;
		}
	}

}
