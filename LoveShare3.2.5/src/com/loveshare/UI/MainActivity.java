package com.loveshare.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loveshare.activity.R;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.ShowCustomToast;

public class MainActivity extends Activity implements OnItemSelectedListener,
		OnItemClickListener {

	public static int win_width;// ��Ļ���
	public static int win_height;// ��Ļ�߶�

	private GridView gridView = null;
	// ���弴����ʾ��gridView�ϵ�ͼƬ��Դ����
	private int[] imgs = new int[] { R.drawable.btn_m_camera,
			R.drawable.btn_m_picdb, /* R.drawable.btn_m_puzzle, */
			R.drawable.btn_m_share1, R.drawable.btn_m_recommend,
			R.drawable.btn_m_setting,R.drawable.btn_m_cloud/* R.drawable.btn_m_book */};

	private String[] texts = new String[] { "����", "����", /* "ƴͼ", */"����", "�Ƽ�",
			"����","΢����"};

	public static int dh;
	public static int dw;

	// ����gridView�ϵ�ͼƬ��ť��λ��Ϊ����
	private static final int POSITION_CAMERA = 0;
	private static final int POSITION_PICDB = 1;
	/* private static final int POSITION_PUZZLE = 2; */
	private static final int POSITION_SHARE = 2;
	private static final int POSITION_RECOMMAND = 3;
	private static final int POSITION_SETTING = 4;
	private static final int POSITION_BOX = 5;
	private static final int POSITION_BOOK = 6;

	public static String shareImgPath = null;

	private static final int FLAG_CHOOSE = 11;
	private static final int FLAG_HANDLEBACK = 12;

	public static String picturePath = null;

	private boolean isExit = false;

	public static Uri uri;

	private LinearLayout mainActivity = null;

	private CustomAlertDialog dialog;

	private SharedPreferences preferences;

	private void setActivityBackground() {
		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", R.drawable.background_winter);
		mainActivity.setBackgroundResource(drawable);
	}

	// ��дonStart,��ȡ�����ļ������ݣ�����Activity����
	@Override
	protected void onStart() {
		setActivityBackground();
		// ���SharedPreferences
		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);

		if (dialog != null) {
			dialog.dismiss();
		}

		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mainActivity = (LinearLayout) this.findViewById(R.id.mainActivity);

		WindowManager manager = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		win_width = manager.getDefaultDisplay().getWidth();
		win_height = manager.getDefaultDisplay().getHeight();

		// �õ���Ļ���
		dw = this.getWindowManager().getDefaultDisplay().getWidth();
		// �õ���Ļ�߶�
		dh = this.getWindowManager().getDefaultDisplay().getHeight();

		gridView = (GridView) this.findViewById(R.id.main_gridView);
		gridView.setAdapter(new MyAdapter());

		// ΪgridView��OnItemClickListener��OnItemSelectedListener����������
		gridView.setOnItemClickListener(this);
		gridView.setOnItemSelectedListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("menu inflate1");
		this.getMenuInflater().inflate(R.menu.main, menu);
		System.out.println("menu inflate2");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_exit:
			final CustomAlertDialog dialog = new CustomAlertDialog(this)
					.setTitleText("��ܰ����").setMsg("�����Ҫ�뿪\"������\"��")
					.setPositiveText("�뿪").setNegativeText("ȡ��")
					.setNeutralGone();
			dialog.setPositiveOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exit();
					dialog.dismiss();
				}

			});
			dialog.show();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * �˳�����
	 */
	private void exit() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		// ��ת�����ս���
		case POSITION_CAMERA:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 1);
			break;

		// ��ת��ͼƬ�����
		case POSITION_PICDB:
			/*
			 * Intent intent1 = new Intent();
			 * intent1.setAction(Intent.ACTION_PICK);
			 * intent1.setType("image/*");
			 */
			Intent intent1 = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent1, FLAG_CHOOSE);
			break;

		// ��ת��ƴͼ����
		/*
		 * case POSITION_PUZZLE: Intent intent2 = new Intent(this,
		 * PuzzleActivity.class); this.startActivity(intent2); break;
		 */

		// ��ת��������Ƭ����
		case POSITION_SHARE:
			Intent intent3 = new Intent(this, ShareActivity.class);
			this.startActivity(intent3);
			break;

		// ��ת���Ƽ�����Ľ���
		case POSITION_RECOMMAND:
			this.startActivity(new Intent(this, RecommendActivity.class));
			break;

		// ��ת��������õĽ���
		case POSITION_SETTING:
			this.startActivity(new Intent(this, SettingActivity.class));
			break;

		/*
		 * case POSITION_BOOK: this.startActivity(new Intent(this,
		 * StrategyActivity.class)); break;
		 */

		case POSITION_BOX:
			this.startActivity(new Intent(this,CloudActivity.class));
			break;
		default:
			break;
		}
		// this.finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		// ����ϵͳ�����
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case FLAG_CHOOSE:
				uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						/*
						 * Toast.makeText(this, "no found", Toast.LENGTH_SHORT)
						 * .show();
						 */
						ShowCustomToast.show(this, "no found");
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d("may", "path=" + path);
					Intent intent = new Intent(this, ImgActivity.class);
					intent.putExtra("imgPath", path);
					startActivityForResult(intent, FLAG_HANDLEBACK);

				} else {
					Log.d("may", "path=" + uri.getPath());
					Intent intent = new Intent(this, ImgActivity.class);
					intent.putExtra("imgPath", uri.getPath());
					startActivityForResult(intent, FLAG_HANDLEBACK);
				}
				break;

			case FLAG_HANDLEBACK:

				String imagePath = data.getStringExtra("path");
				// mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
				Log.d("may", "back");
				break;

			default:

				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				String name = new DateFormat().format("yyyyMMdd_hh-mm-ss",
						Calendar.getInstance(Locale.CHINA)) + ".jpg";
				// String name = System.currentTimeMillis() + ".jpg";

				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ

				FileOutputStream b = null;
				// ???????????????????????????????Ϊʲô����ֱ�ӱ�����ϵͳ���λ���أ�����������������������
				/*
				 * File dir = new File(Environment.getExternalStorageDirectory()
				 * .getAbsolutePath() + this.getString(R.string.imgPath));
				 */

				// ���з������Ƭ��·��
				File dir = new File(preferences.getString("savePath", null));
				shareImgPath = dir.getAbsolutePath() + "/" + name;

				dir.mkdir();
				// File jpgFile = new
				// File(dir,System.currentTimeMillis()+".jpg");
				File jpgFile = new File(dir, name);
				System.out.println(jpgFile.getAbsolutePath());
				try {
					b = new FileOutputStream(jpgFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
				} catch (FileNotFoundException e) {
					// Toast.makeText(this, "��Ƭ����ʧ��1",
					// Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
						/*
						 * Toast.makeText(this, "��Ƭ ����·��Ϊ��" + shareImgPath +
						 * "ร�", Toast.LENGTH_LONG).show();
						 */
						ShowCustomToast.show(this, "��Ƭ����·��Ϊ��" + shareImgPath);

					} catch (IOException e) {
						// Toast.makeText(this, "��Ƭ����ʧ��2",
						// Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}

				// �����Ի���ѯ���û�������ɺ��Ƿ�����������Ƭ

				final CustomAlertDialog dialog1 = new CustomAlertDialog(this);
				System.out.println("create dialog");
				dialog1.setTitleText("��ܰ����")
						.setMsg("�������Ҫ��ʲô��?")
						.setPositiveText("����")
						.setPositiveOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(MainActivity.this,
										ImgActivity.class);
								intent.putExtra("imgPath", shareImgPath);
								MainActivity.this.startActivity(intent);
								dialog1.dismiss();
							}

						}).setNeutralText("����")
						.setNeutralOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(MainActivity.this,
										ShareActivity.class);
								intent.putExtra("shareImgPath", shareImgPath);
								MainActivity.this.startActivity(intent);
								dialog1.dismiss();
							}
						}).setNegativeText("ȡ��").show();

				// ģ��һ����Ϣ ֪ͨϵͳsd�������¹�����.
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
				intent.setData(Uri.fromFile(Environment
						.getExternalStorageDirectory()));
				sendBroadcast(intent);

			}
		}
	}

	// ʵ��˫�����ؼ��˳��ó���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (!isExit) {
				isExit = true;// ׼���˳��ı�־
				/*
				 * Toast.makeText(this, R.string.onceAgainExit,
				 * Toast.LENGTH_LONG) .show();
				 */
				ShowCustomToast.show(this, R.string.onceAgainExit);

				/*
				 * ���û��Զ�ִ�е�TimerTask�� �����2����֮�ڲ���һ�ΰ��·��ؼ�������isExit��Ϊfalse����ȡ����׼���˳�
				 * �����2����֮����һ�ΰ��·��ؼ�����ʱisExit��Ϊtrue����ʱִ��else�еķ���
				 */
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						isExit = false;// ȡ��׼���˳��ı�־
					}

				}, 2000);

			} else {
				exit();
			}
		}
		return true;
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(MainActivity.this,
					R.layout.activity_main_cell, null);

			int gh = (int) (win_height * 0.8);
			int gw = (int) (win_width * 0.8);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					gw / 2, gh / imgs.length / 2);
			ImageView imageViewOfGridView = (ImageView) view
					.findViewById(R.id.imageViewOfGridView);
			TextView textViewOfGridView = (TextView) view
					.findViewById(R.id.textViewOfGridView);
			imageViewOfGridView.setImageResource(imgs[position]);
			textViewOfGridView.setText(texts[position]);
			return view;
		}

	}

}
