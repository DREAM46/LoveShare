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

	private final int LIST_DIALOG_SINGLE_COLOR = 3; // 记录单选列表对话框的id
	private final int LIST_DIALOG_SINGLE_STROKEWIDTH = 4;

	private ImageButton save;// 保存按钮
	private ImageButton share;// 分享按钮

	public static String imgPath;

	private Bitmap saveBitmap;
	public static SaveImgListener sil;
	private ShareListener sl;

	public static boolean isEdit;// 是否修改过的标志
	public static boolean isSave;// 是否保存过的标志

	private Button btn_penColor;// 选择画笔的按钮
	private Button clear = null;// 清楚的按钮
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
						R.array.string_strokeWidth)).setTitle("选择粗细");

		btn_penColor = (Button) this.findViewById(R.id.btn_penColor);
		btn_penColor.setOnClickListener(new View.OnClickListener() { // 为Button设置OnClickListener监听器
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

					ShowCustomToast.show(ImgHangWritingActivity.this, "你没有选择粗细度");
					return;
				}

				// 设置画笔的颜色
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
						R.array.string_color)).setTitle("选择颜色");

		btn_strokeWidth = (Button) this.findViewById(R.id.btn_strokeWidth);

		btn_strokeWidth.setOnClickListener(new View.OnClickListener() { // 为Button设置OnClickListener监听器
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
					ShowCustomToast.show(ImgHangWritingActivity.this, "你没有选择颜色");
					return;
				}

				if (color.equals("红色"))
					paint.setColor(Color.RED);
				else if (color.equals("黄色"))
					paint.setColor(Color.YELLOW);
				else if (color.equals("黑色"))
					paint.setColor(Color.BLACK);
				else if (color.equals("白色"))
					paint.setColor(Color.WHITE);
				else if (color.equals("绿色"))
					paint.setColor(Color.GREEN);
				else if (color.equals("蓝色"))
					paint.setColor(Color.BLUE);
				else if (color.equals("灰色"))
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

		clearDialog = new CustomAlertDialog(this).setMsg("你确定要清除所修改的痕迹吗?")
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
		// 定义手指开始位置的坐标
		int startX;
		int startY;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 手指第一次接触屏幕
				System.out.println("手指按下");

				startX = (int) event.getX();
				startY = (int) event.getY();
				System.out.println("SX"+startX);
				System.out.println("sy"+startY);
				break;
			case MotionEvent.ACTION_MOVE: // 手指在屏幕上滑动
				System.out.println("手指移动");
				int newX = (int) event.getX();
				int newY = (int) event.getY();
				System.out.println("nX"+newX);
				System.out.println("ny"+newY);

				canvas.drawLine(startX, startY, newX, newY, paint);
				// 重新更新画笔的开始位置.
				startX = newX;
				startY = newY;
				
				//canvas = new Canvas(new1Bitmap);
				iv_handwrite.setImageBitmap(new1Bitmap);
				saveBitmap = new1Bitmap;
				sil.setSave(false);
				isEdit = true;
				break;
			case MotionEvent.ACTION_UP:// 手指离开屏幕

				break;
			}

			return true;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// 若按下的清除键，则弹出对话框，询问是否清除
		case R.id.clear:
			// 若未进行过涂鸦，也没有进行清除的必要了
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
	 * 清除所修改的痕迹
	 */
	private void clear() {
		isClear = true;
		ImgHangWritingActivity.isEdit = false;
		// new2Bitmap为原来的按下清除键之前的用于显示图片的Bitmap
		new1Bitmap = Bitmap.createBitmap(originalBitmap);
		iv_handwrite.setImageBitmap(new1Bitmap);
		canvas = new Canvas(new1Bitmap);
		saveBitmap = originalBitmap;

		clearDialog.dismiss();
	}

	// 当返回键被按下的另外一种重写写法
	@Override
	public void onBackPressed() {
		BackHint backHint = new BackHint(this, imgPath, saveBitmap,
				ImgActivity.class);
		int Saveflag = 0;// 是否已经保存的标志
		isSave = sil.isSave();

		// 当按下返回键且图片已经被改过时，则弹出返回提示框提示用户保存修改过的图片
		// 判断是否已经按下在分享的同时保存了美化后的图片文件
		/*
		 * if(!ImgHangWritingActivity.isSave){
		 * 
		 * backHint.showAlertDialog();
		 * 
		 * 无论用户是否选择了保存，都将isEdit设为false， 以防下一次进入美化界面而不进行美化提示用户保存
		 * 
		 * isEdit = false; // 将路径更新为新保存的修改过的图片文件，这样方便进行图片的连续编辑 imgPath =
		 * backHint.getImgPath(); Saveflag = 1; }else
		 */
		if (!sl.isSave()) {
			// 判断是否按下保存按钮保存了美化后的图片文件
			if (!sil.isSave()) {
				// 判断是否美化过图片，若没有美化，则也没有弹出对话框提示用户保存的必要
				if (isEdit) {

					backHint.showAlertDialog();
					/*
					 * 无论用户是否选择了保存，都将isEdit设为false， 以防下一次进入美化界面而不进行美化提示用户保存
					 */
					isEdit = false;
					// 将路径更新为新保存的修改过的图片文件，这样方便进行图片的连续编辑
					imgPath = backHint.getImgPath();
					Saveflag = 1;
				}
			} else
				// 若按下了保存按钮保存文件，则应该将文件路径设为保存后文件的路径
				imgPath = sil.getImgPath();
		} else
			// 若按下了分享按钮的同时保存文件，则应该将文件路径设为保存后文件的路径
			imgPath = sl.getImgPath();

		if (Saveflag == 0) {
			backHint.gotoActivity(imgPath);
			this.finish();
		}

	}

	// 重写onCreateDialog方法
	@Override
	protected Dialog onCreateDialog(int id) {
		// 声明一个Dialog对象用于返回
		Dialog dialog = null;
		// 对id进行判断
		switch (id) {
		case LIST_DIALOG_SINGLE_STROKEWIDTH:

			// 创建Builder对象
			Builder b = new AlertDialog.Builder(this);
			// 设置图标
			b.setIcon(R.drawable.btn_strokewidth_unchecked);
			// 设置标题
			b.setTitle(R.string.selectStrokeWidth);
			// 设置单选列表选项
			b.setSingleChoiceItems(
					// 默认选中状态 下表从零开始 区别去多选对话框
					R.array.string_strokeWidth, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 先得到选择的结果
							strokeWidth = getResources().getStringArray(
									R.array.string_strokeWidth)[which];
						}
					});
			// 添加一个按钮
			b.setPositiveButton(
			// 按钮显示的文本
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 设置画笔的颜色

						}
					});
			b.setNegativeButton(R.string.cancel, null);
			dialog = b.create(); // 生成Dialog对象
			break;
		case LIST_DIALOG_SINGLE_COLOR:
			// 创建Builder对象
			Builder b1 = new AlertDialog.Builder(this);
			// 设置图标
			b1.setIcon(R.drawable.btn_color_unchecked);
			// 设置标题
			b1.setTitle(R.string.selectColor);
			// 设置单选列表选项
			b1.setSingleChoiceItems(
					// 默认选中状态 下表从零开始 区别去多选对话框
					R.array.string_color, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 先得到选择的结果
							color = getResources().getStringArray(
									R.array.string_color)[which];
						}
					});
			// 添加一个按钮
			b1.setPositiveButton(
			// 按钮显示的文本
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 设置画笔的颜色

						}
					});
			b1.setNegativeButton(R.string.cancel, null);
			dialog = b1.create(); // 生成Dialog对象
			break;
		default:
			break;
		}
		return dialog; // 返回生成的Dialog对象
	}
}