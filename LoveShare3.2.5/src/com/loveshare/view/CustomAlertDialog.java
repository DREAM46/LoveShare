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
 * �Զ�����ʾ�Ի���
 * @author ������
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
	 * ���ص�����Ի���ť֮��ĳɹ���־
	 * 
	 * @return ������Ի���ť֮��ĳɹ���־
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * ���öԻ�����������
	 * 
	 * @param str
	 *            Ҫ���õ�ȷ����ť������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setTitleText(String str) {
		TextView view_dialog_title = (TextView) this
				.findViewById(R.id.view_dialog_title);
		view_dialog_title.setText(str);
		return this;
	}

	/**
	 * ���óɹ���ť������
	 * 
	 * @param str
	 *            Ҫ���õ�ȷ����ť������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setPositiveText(String str) {
		view_dialog_positive.setText(str);
		return this;
	}

	/**
	 * ����ȡ����ť������
	 * 
	 * @param str
	 *            Ҫ���õ�ȡ����ť������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setNegativeText(String str) {
		view_dialog_negative.setText(str);
		return this;
	}

	/**
	 * ����ȡ����ť������
	 * 
	 * @param str
	 *            Ҫ���õ�������ť������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setNeutralText(String str) {
		view_dialog_neutral.setText(str);
		return this;
	}

	/**
	 * ����ȷ����ť���ɼ�
	 * 
	 * @return �Ի�����
	 */
	public CustomAlertDialog setPositiveGone() {
		view_dialog_positive.setVisibility(View.GONE);
		return this;
	}

	/**
	 * ����ȡ����ť���ɼ�
	 * 
	 * @return �Ի�����
	 */
	public CustomAlertDialog setNegativeGone() {
		view_dialog_positive.setVisibility(View.GONE);
		return this;
	}

	/**
	 * ����������ť���ɼ�
	 * 
	 * @return �Ի�����
	 */
	public CustomAlertDialog setNeutralGone() {
		view_dialog_neutral.setVisibility(View.GONE);
		((LinearLayout) this.findViewById(R.id.layout_line))
				.setVisibility(View.GONE);
		return this;
	}

	/**
	 * ���öԻ���������Ϣ����
	 * 
	 * @param str
	 *            �Ի���������Ϣ����
	 * @return �Ի�����
	 */
	public CustomAlertDialog setMsg(String str) {
		TextView view_dialog_msg = (TextView) this
				.findViewById(R.id.view_dialog_msg);
		view_dialog_msg.setText(str);
		return this;
	}

	/**
	 * ����ȷ����ť�ļ�����
	 * 
	 * @param listener
	 *            ������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setPositiveOnClickListener(
			android.view.View.OnClickListener listener) {
		view_dialog_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * ����ȡ����ť�ļ�����
	 * 
	 * @param listener
	 *            ������
	 * @return �Ի�����
	 */
	public CustomAlertDialog setNegativeOnClickListener(
			android.view.View.OnClickListener listener) {
		view_dialog_negative.setOnClickListener(listener);
		return this;
	}

	/**
	 * ����������ť�ļ�����
	 * 
	 * @param listener
	 *            ������
	 * @return �Ի�����
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
