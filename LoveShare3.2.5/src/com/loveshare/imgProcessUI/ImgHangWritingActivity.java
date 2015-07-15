package com.loveshare.imgProcessUI;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.MainActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.BackHint;
import com.loveshare.imgProcessUtil.HandWritingView;
import com.loveshare.imgSave.CreateOriginFile;
import com.loveshare.imgSave.SaveImgListener;
import com.loveshare.share.ShareListener;
import com.loveshare.view.ShowCustomToast;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.CustomListDialog;

public class ImgHangWritingActivity extends Activity implements OnClickListener {

	private final int LIST_DIALOG_SINGLE_COLOR = 3; // ��¼��ѡ�б�Ի����id
	private final int LIST_DIALOG_SINGLE_STROKEWIDTH = 4;

	private ImageButton save;// ���水ť
	private ImageButton share;// ����ť

	public static String imgPath;

	private Bitmap saveBitmap;
	public static SaveImgListener sil;
	private ShareListener sl;

	public static boolean isEdit;// �Ƿ��޸Ĺ��ı�־
	public static boolean isSave;// �Ƿ񱣴���ı�־

	private Button btn_penColor;// ѡ�񻭱ʵİ�ť
	private Button clear = null;// ����İ�ť
	private Button btn_strokeWidth;

	private ImageView iv_handwrite;

	private Bitmap originalBitmap;
	private Bitmap new1Bitmap;
	private Bitmap new2Bitmap;
	private Canvas canvas;
	private Paint paint;

	private String strokeWidth = "5.0f";
	private String color;

	private boolean isClear;

	private CustomListDialog colorsDialog;
	private CustomListDialog strokeWidthsDialog;
	private CustomAlertDialog clearDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imgPath = this.getIntent().getStringExtra("imgPath");
		setContentView(R.layout.activity_imghandwriting);

		iv_handwrite = (ImageView) this.findViewById(R.id.iv_handwrite);

		originalBitmap = BitmapFactory.decodeFile(
				ImgHangWritingActivity.imgPath, ImgActivity.options).copy(
				Bitmap.Config.ARGB_8888, true);
		new1Bitmap = Bitmap.createBitmap(originalBitmap);
		iv_handwrite.setImageBitmap(new1Bitmap);

		setPaint();
		
		canvas = new Canvas(new1Bitmap);
		canvas.drawBitmap(originalBitmap, new Matrix(), paint);
	
		iv_handwrite.setOnTouchListener(new MyTouchListener1());

		save = (ImageButton) this.findViewById(R.id.save);
		share = (ImageButton) this.findViewById(R.id.share);

		clear = (Button) findViewById(R.id.clear);
		clear.setOnClickListener(this);

		strokeWidthsDialog = new CustomListDialog(ImgHangWritingActivity.this,
				ImgHangWritingActivity.this.getResources().getStringArray(
						R.array.string_strokeWidth)).setTitle("ѡ���ϸ");

		btn_penColor = (Button) this.findViewById(R.id.btn_penColor);
		btn_penColor.setOnClickListener(new View.OnClickListener() { // ΪButton����OnClickListener������
					@Override
					public void onClick(View v) {
						strokeWidthsDialog.show();
					}
				});

		strokeWidthsDialog.setPositiveListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				strokeWidth = strokeWidthsDialog.getItem();
				strokeWidthsDialog.dismiss();

				if (strokeWidth == null) {

					ShowCustomToast.show(ImgHangWritingActivity.this, "��û��ѡ���ϸ��");
					return;
				}

				// ���û��ʵ���ɫ
				if (strokeWidth.equals("5px"))
					paint.setStrokeWidth(5.0f);
				else if (strokeWidth.equals("10px"))
					paint.setStrokeWidth(10.0f);
				else if (strokeWidth.equals("15px"))
					paint.setStrokeWidth(15.0f);
				else if (strokeWidth.equals("20px"))
					paint.setStrokeWidth(20.0f);
				else if (strokeWidth.equals("25px"))
					paint.setStrokeWidth(25.0f);
				else if (strokeWidth.equals("30px"))
					paint.setStrokeWidth(30.0f);
				else if (strokeWidth.equals("35px"))
					paint.setStrokeWidth(35.0f);
				else if (strokeWidth.equals("40px"))
					paint.setStrokeWidth(40.0f);

			}
		});

		colorsDialog = new CustomListDialog(ImgHangWritingActivity.this,
				ImgHangWritingActivity.this.getResources().getStringArray(
						R.array.string_color)).setTitle("ѡ����ɫ");

		btn_strokeWidth = (Button) this.findViewById(R.id.btn_strokeWidth);

		btn_strokeWidth.setOnClickListener(new View.OnClickListener() { // ΪButton����OnClickListener������
					@Override
					public void onClick(View v) {
						colorsDialog.show();
					}
				});

		colorsDialog.setPositiveListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String color = colorsDialog.getItem();
				colorsDialog.dismiss();
				if (color == null) {
					ShowCustomToast.show(ImgHangWritingActivity.this, "��û��ѡ����ɫ");
					return;
				}

				if (color.equals("��ɫ"))
					paint.setColor(Color.RED);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.YELLOW);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.BLACK);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.WHITE);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.GREEN);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.BLUE);
				else if (color.equals("��ɫ"))
					paint.setColor(Color.GRAY);

			}

		});

		saveBitmap = new1Bitmap;
		sl = new ShareListener(this, saveBitmap, imgPath);
		share.setOnClickListener(sl);

		File originFile = new CreateOriginFile(this).createOriginFile(new File(
				imgPath), ImgActivity.savePath);
		sil = new SaveImgListener(this, originFile, saveBitmap);
		save.setOnClickListener(sil);

		clearDialog = new CustomAlertDialog(this).setMsg("��ȷ��Ҫ������޸ĵĺۼ���?")
				.setPositiveOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						clear();
					}
				}).setNeutralGone();

	}

	private void setPaint() {
		paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
	}

	private class MyTouchListener1 implements OnTouchListener {
		// ������ָ��ʼλ�õ�����
		int startX;
		int startY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // ��ָ��һ�νӴ���Ļ
				System.out.println("��ָ����");

				startX = (int) event.getX();
				startY = (int) event.getY();
				System.out.println("SX"+startX);
				System.out.println("sy"+startY);
				break;
			case MotionEvent.ACTION_MOVE: // ��ָ����Ļ�ϻ���
				System.out.println("��ָ�ƶ�");
				int newX = (int) event.getX();
				int newY = (int) event.getY();
				System.out.println("nX"+newX);
				System.out.println("ny"+newY);

				canvas.drawLine(startX, startY, newX, newY, paint);
				// ���¸��»��ʵĿ�ʼλ��.
				startX = newX;
				startY = newY;
				
				//canvas = new Canvas(new1Bitmap);
				iv_handwrite.setImageBitmap(new1Bitmap);
				saveBitmap = new1Bitmap;
				sil.setSave(false);
				isEdit = true;
				break;
			case MotionEvent.ACTION_UP:// ��ָ�뿪��Ļ

				break;
			}

			return true;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// �����µ���������򵯳��Ի���ѯ���Ƿ����
		case R.id.clear:
			// ��δ���й�Ϳѻ��Ҳû�н�������ı�Ҫ��
			if (!isEdit) {
				/*Toast.makeText(this, R.string.clearError, Toast.LENGTH_SHORT)
						.show();*/
				ShowCustomToast.show(this, R.string.clearError);
			} else {

				clearDialog.show();
			}
			break;

		case R.id.btn_penColor:

			break;
		}
	}

	/**
	 * ������޸ĵĺۼ�
	 */
	private void clear() {
		isClear = true;
		ImgHangWritingActivity.isEdit = false;
		// new2BitmapΪԭ���İ��������֮ǰ��������ʾͼƬ��Bitmap
		new1Bitmap = Bitmap.createBitmap(originalBitmap);
		iv_handwrite.setImageBitmap(new1Bitmap);
		canvas = new Canvas(new1Bitmap);
		saveBitmap = originalBitmap;

		clearDialog.dismiss();
	}

	// �����ؼ������µ�����һ����дд��
	@Override
	public void onBackPressed() {
		BackHint backHint = new BackHint(this, imgPath, saveBitmap,
				ImgActivity.class);
		int Saveflag = 0;// �Ƿ��Ѿ�����ı�־
		isSave = sil.isSave();

		// �����·��ؼ���ͼƬ�Ѿ����Ĺ�ʱ���򵯳�������ʾ����ʾ�û������޸Ĺ���ͼƬ
		// �ж��Ƿ��Ѿ������ڷ����ͬʱ�������������ͼƬ�ļ�
		/*
		 * if(!ImgHangWritingActivity.isSave){
		 * 
		 * backHint.showAlertDialog();
		 * 
		 * �����û��Ƿ�ѡ���˱��棬����isEdit��Ϊfalse�� �Է���һ�ν������������������������ʾ�û�����
		 * 
		 * isEdit = false; // ��·������Ϊ�±�����޸Ĺ���ͼƬ�ļ��������������ͼƬ�������༭ imgPath =
		 * backHint.getImgPath(); Saveflag = 1; }else
		 */
		if (!sl.isSave()) {
			// �ж��Ƿ��±��水ť�������������ͼƬ�ļ�
			if (!sil.isSave()) {
				// �ж��Ƿ�������ͼƬ����û����������Ҳû�е����Ի�����ʾ�û�����ı�Ҫ
				if (isEdit) {

					backHint.showAlertDialog();
					/*
					 * �����û��Ƿ�ѡ���˱��棬����isEdit��Ϊfalse�� �Է���һ�ν������������������������ʾ�û�����
					 */
					isEdit = false;
					// ��·������Ϊ�±�����޸Ĺ���ͼƬ�ļ��������������ͼƬ�������༭
					imgPath = backHint.getImgPath();
					Saveflag = 1;
				}
			} else
				// �������˱��水ť�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
				imgPath = sil.getImgPath();
		} else
			// �������˷���ť��ͬʱ�����ļ�����Ӧ�ý��ļ�·����Ϊ������ļ���·��
			imgPath = sl.getImgPath();

		if (Saveflag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}

	}

	// ��дonCreateDialog����
	@Override
	protected Dialog onCreateDialog(int id) {
		// ����һ��Dialog�������ڷ���
		Dialog dialog = null;
		// ��id�����ж�
		switch (id) {
		case LIST_DIALOG_SINGLE_STROKEWIDTH:

			// ����Builder����
			Builder b = new AlertDialog.Builder(this);
			// ����ͼ��
			b.setIcon(R.drawable.btn_strokewidth_unchecked);
			// ���ñ���
			b.setTitle(R.string.selectStrokeWidth);
			// ���õ�ѡ�б�ѡ��
			b.setSingleChoiceItems(
					// Ĭ��ѡ��״̬ �±���㿪ʼ ����ȥ��ѡ�Ի���
					R.array.string_strokeWidth, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// �ȵõ�ѡ��Ľ��
							strokeWidth = getResources().getStringArray(
									R.array.string_strokeWidth)[which];
						}
					});
			// ���һ����ť
			b.setPositiveButton(
			// ��ť��ʾ���ı�
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ���û��ʵ���ɫ

						}
					});
			b.setNegativeButton(R.string.cancel, null);
			dialog = b.create(); // ����Dialog����
			break;
		case LIST_DIALOG_SINGLE_COLOR:
			// ����Builder����
			Builder b1 = new AlertDialog.Builder(this);
			// ����ͼ��
			b1.setIcon(R.drawable.btn_color_unchecked);
			// ���ñ���
			b1.setTitle(R.string.selectColor);
			// ���õ�ѡ�б�ѡ��
			b1.setSingleChoiceItems(
					// Ĭ��ѡ��״̬ �±���㿪ʼ ����ȥ��ѡ�Ի���
					R.array.string_color, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// �ȵõ�ѡ��Ľ��
							color = getResources().getStringArray(
									R.array.string_color)[which];
						}
					});
			// ���һ����ť
			b1.setPositiveButton(
			// ��ť��ʾ���ı�
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ���û��ʵ���ɫ

						}
					});
			b1.setNegativeButton(R.string.cancel, null);
			dialog = b1.create(); // ����Dialog����
			break;
		default:
			break;
		}
		return dialog; // �������ɵ�Dialog����
	}
}