package com.loveshare.share;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.scribe.builder.api.SohuWeiboApi;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.kaixin.KaiXin;
import cn.sharesdk.netease.microblog.NetEaseMicroBlog;
import cn.sharesdk.onekeyshare.WeiboGridView;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sohu.suishenkan.SohuSuishenkan;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.loveshare.UI.ImgActivity;
import com.loveshare.UI.StartActivity;
import com.loveshare.activity.R;
import com.loveshare.imgProcessUtil.GetOpinions;
import com.loveshare.view.CustomAlertDialog;
import com.loveshare.view.ShowCustomToast;

public class SendActivity extends Activity implements Callback,
		OnClickListener, OnEditorActionListener {

	private static final int FLAG_CHOOSE = 11;
	private static final int FLAG_HANDLEBACK = 12;

	public static String shareimgPath = null;

	private static final int BIANJI = 1;
	private static final int DELETEIMG = 2;

	private String name = null;
	private Platform platForm = null;
	private Handler handler = null;

	private NotificationManager nm = null;
	private TextView inputText = null;
	private EditText inputEditText = null;
	private Button sendButton = null; // ����ť
	private ImageButton positionButton = null; // ��ת������
	private String URI = ""; // ��Ƭ��URI
	private ImageView iv_picture = null; // ��Ƭ
	private CustomAlertDialog dialog;

	private Intent intent = null;
	private Bitmap bit;

	private boolean flag; // �Ƿ�ѡ���˼����������Ƭ�ı�־

	private String content;
	private String str = "";

	private ImageButton btn_deleteAll = null;

	private BitmapFactory.Options options;

	private static int winWidth;
	private static int winHeight;

	private Button button;
	private Button view_btn_change;
	private Button view_btn_delete;
	private Button cancleButton;
	private PopupWindow popupWindow;
	private View popupWindowView;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (URI != null
					|| !TextUtils.isEmpty(inputEditText.getText().toString()
							.trim())) {
				dialog.setTitleText("��ܰ����").setMsg("��ȷ��Ҫ�������η�����")
						.setPositiveText("ȷ��")
						.setPositiveOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SendActivity.this.finish();
								dialog.dismiss();
							}
						}).setNeutralGone().show();
			} else {
				SendActivity.this.finish();
			}
		}
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			sendWeibo();
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendwb1);
		ShareSDK.initSDK(this);

		intent = this.getIntent(); // �õ���ͼ����
		inputEditText = (EditText) this.findViewById(R.id.inputEditText);
		inputEditText.setText(intent.getStringExtra("shareText"));

		inputEditText.setOnEditorActionListener(this);

		btn_deleteAll = (ImageButton) this.findViewById(R.id.deleteAll);
		btn_deleteAll.setOnClickListener(this);

		inputText = (TextView) this.findViewById(R.id.inputText);
		content = inputEditText.getText().toString();
		if (content.length() != 0)
			btn_deleteAll.setVisibility(View.VISIBLE);
		inputText.setText("������������" + (140 - content.length()));

		name = intent.getExtras().getString("name");
		URI = intent.getExtras().getString("shareImgPath");

		iv_picture = (ImageView) this.findViewById(R.id.sendimageView);

		// �õ��ֻ��Ŀ��
		WindowManager manager = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		winWidth = manager.getDefaultDisplay().getWidth();
		winHeight = manager.getDefaultDisplay().getHeight();

		// ���Bitmap���󣬲���������mImageView��
		// ���Bitmap���󣬲���������mImageView��
		/*
		 * BitmapFactory.Options options = new BitmapFactory.Options();
		 * options.inSampleSize = 4;//ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
		 */
		if (ImgActivity.options != null) {
			options = ImgActivity.options;
		} else {
			/*
			 * options = GetOpinions.getGetOpinions(URI, winWidth, winHeight,
			 * null);
			 */
			options = GetOpinions.getGetOpinions();
		}

		bit = BitmapFactory.decodeFile(URI, options); // �Զ���//·��
		if (bit != null)
			flag = true;
		iv_picture.setImageBitmap(bit);
		if (bit == null)
			iv_picture.setImageResource(R.drawable.plsu1);

		str = "������" + name;
		
		
		if (name.equals("����΢��")) {
			platForm = ShareSDK.getPlatform(SendActivity.this, SinaWeibo.NAME);
		} else if (name.equals("��Ѷ΢��")) {
			platForm = ShareSDK.getPlatform(SendActivity.this, TencentWeibo.NAME);
		} else if (name.equals("����΢��")) {
			platForm = ShareSDK.getPlatform(SendActivity.this, NetEaseMicroBlog.NAME);
		}else if (name.equals("΢�ź���")) {
			platForm = ShareSDK.getPlatform(SendActivity.this, Wechat.NAME);
		}
		if (platForm.isValid()) {
			str += "��" + platForm.getDb().get("nickname");
		}

		this.setTitle(str);
		handler = new Handler(this);

		sendButton = (Button) this.findViewById(R.id.sendButton);
		positionButton = (ImageButton) this.findViewById(R.id.positionButton);
		sendButton.setOnClickListener(this);
		positionButton.setOnClickListener(this);
		iv_picture.setOnClickListener(this);

		inputEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				if(content.trim().equals("")){
					content = inputEditText.getText().toString().trim();
				}else	content = inputEditText.getText().toString();

				// �������΢�����������ݣ���ɾ��ȫ��΢����һ��ť���֣����򣬲�����
				if (content.length() != 0 && !(content.equals("")))
					btn_deleteAll.setVisibility(View.VISIBLE);
				else
					btn_deleteAll.setVisibility(View.GONE);

				inputText.setText("������������" + (140 - content.length()));
			}

		});
		dialog = new CustomAlertDialog(this).setNeutralGone();

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		popupWindowView = inflater.inflate(R.layout.view_popupwindow, null);
		popupWindow = new PopupWindow(popupWindowView,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ����PopupWindow�ĵ�������ʧЧ��

		cancleButton = (Button) popupWindowView.findViewById(R.id.cancleButton);
		cancleButton.setOnClickListener(this);
		popupWindow.setAnimationStyle(R.style.popupAnimation);
		view_btn_change = (Button) popupWindowView
				.findViewById(R.id.view_btn_select);
		view_btn_change.setText("����ͼƬ");
		view_btn_change.setOnClickListener(this);

		view_btn_delete = (Button) popupWindowView
				.findViewById(R.id.view_btn_enter);
		view_btn_delete.setText("ɾ��ͼƬ");
		view_btn_delete.setOnClickListener(this);
	}

	private class SendListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			//����Ƿ��Ϳ�΢�������򲻷���΢��
			String content = inputEditText.getText().toString().trim();
			if(URI == null && (TextUtils.isEmpty(content) || content.equals(""))){
				ShowCustomToast.show(SendActivity.this, "����ʧ�ܣ�΢������Ϊ��");
				dialog.dismiss();
				return;
			}
			
			dialog.dismiss();
			sendWeibo();
		}
	}

	/**
	 * ����΢��
	 */
	private void sendWeibo() {
		platForm.setPlatformActionListener(new PlatformActionListener() {
			
			@Override
			public void onError(Platform platForm, int action, Throwable t) {
				t.printStackTrace();

				Message msg = new Message();
				msg.arg1 = 2;
				msg.arg2 = action;
				msg.obj = platForm;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onComplete(Platform platForm, int action, HashMap<String, Object> arg2) {
				Message msg = new Message();
				msg.arg1 = 1;
				msg.arg2 = action;
				msg.obj = platForm;
				handler.sendMessage(msg);
				
			}
			
			@Override
			public void onCancel(Platform arplatFormg0, int action) {
				Message msg = new Message();
				msg.arg1 = 3;
				msg.arg2 = action;
				msg.obj = platForm;
				handler.sendMessage(msg);
				
			}
		});
		ShareSDK.initSDK(this);
		Platform.ShareParams sp = new Platform.ShareParams();
		if (name.equals("����΢��")||name.equals("��Ѷ΢��")||name.equals("������")||name.equals("����΢��")) {
			sp.setText(inputEditText.getText().toString());
			sp.setImagePath(URI);
			platForm.share(sp);
		}else if (name.equals("΢�ź���")) {
			sp.setText(inputEditText.getText().toString());
			sp.setImagePath(URI);
			sp.setTitle("������");
			sp.setShareType(Wechat.SHARE_IMAGE);
			platForm.share(sp);
		}
		nm = (NotificationManager) getSystemService(SendActivity.this.NOTIFICATION_SERVICE); // ֪ͨ��
		Notification notification = new Notification();
		notification.when = System.currentTimeMillis();
		notification.icon = R.drawable.logo;
		notification.tickerText = "���ڷ���";
		Intent intent = new Intent(SendActivity.this, StartActivity.class);
		PendingIntent pi = PendingIntent.getActivity(SendActivity.this, 0,
				intent, 0);
		notification
				.setLatestEventInfo(SendActivity.this, "������", "���ڷ���...", pi);
		nm.notify(1, notification);
		
		//������ɺ�΢�������������Ϊ��
		inputEditText.setText("");
		deleteImg();
	}
	
	/**
	 * ������ͼƬɾ��
	 */
	private void deleteImg() {
		iv_picture.setImageResource(R.drawable.plsu1);
		URI = null;
	}

	// ��������Button�¼�
	public void onClick(View v) {

		if (v == cancleButton) {
			popupWindow.dismiss();
		} else if (v == view_btn_delete) {

			dialog.setMsg("��ȷ��Ҫɾ��ͼƬ��")
					.setPositiveOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							deleteImg();
							dialog.dismiss();
						}

					
					}).show();
			popupWindow.dismiss();
		} else if (v == view_btn_change) {
			dialog.setMsg("��ȷ��Ҫ����ͼƬ��").setNeutralGone()
					.setPositiveOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							choosePic();
							dialog.dismiss();
						}
					}).show();
			popupWindow.dismiss();
		} else if (v == sendButton) {
			

			
			dialog.setMsg("��ȷ��Ҫ����΢����?")
					.setPositiveOnClickListener(new SendListener()).show();

		} else if (v == positionButton) {

			dialog.setMsg("��ȷ��Ҫ������?").setNeutralGone()
					.setPositiveOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, 1);
							dialog.dismiss();
						}
					}).show();

		} else if (v == iv_picture) {
			if (URI != null) {
				popupWindow.showAtLocation(view_btn_change, Gravity.CENTER, 0,
						0);
			} else {
				choosePic();
			}

		}

		// ɾ��ȫ��΢������
		if (v == btn_deleteAll) {
			dialog.setMsg("��ȷ��ɾ��ȫ��������").setNeutralGone()
					.setPositiveOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							inputEditText.setText("");
							dialog.dismiss();
						}
					}).show();
		}

	}

	private final void choosePic() {
		flag = true;
		Intent intent1 = new Intent();
		intent1.setAction(Intent.ACTION_PICK);
		intent1.setType("image/*");
		startActivityForResult(intent1, FLAG_CHOOSE);

	}

	// ������ʾ��
	private void playHintMusic() {
		MediaPlayer.create(this, R.raw.music_hint).start();
	}

	// �ֻ���
	private void vibrator() {
		Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(1500);
	}

	@Override
	protected void onDestroy() {
//		AbstractWeibo.stopSDK(this);
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	public boolean handleMessage(Message msg) {
//		weibo = (AbstractWeibo) msg.obj;
		String text = null;
		switch (msg.arg1) {
		case 1: {
			// �ɹ�
			SendActivity.this.setTitle("������" + name + "��"
					+ platForm.getDb().get("nickname"));
			text = "�ɹ�����";
			SharedPreferences preferences = SendActivity.this
					.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
			// �����������ʾ��������΢���ɹ��󷢳���ʾ��
			if (preferences.getBoolean("isSounds", false)) {
				playHintMusic();
			}
			// ����������񶯣�����΢���ɹ����ֻ���
			if (preferences.getBoolean("isVibration", false)) {
				vibrator();
			}
			break;
		}

		case 2: {
			// ʧ��
			text = "����ʧ�ܣ�������������";

			showDialogToSetNetwork();
			break;
		}

		case 3: { // ȡ��
			text = "��֤ȡ��";
			break;
		}
		}
		nm.cancel(1);
		// Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		ShowCustomToast.show(this, text);
		return false;
	}

	private void showDialogToSetNetwork() {
		dialog.setMsg("�Ƿ�������������������")
				.setPositiveOnClickListener(new OnClickListener() {

					Intent intent = null;

					@Override
					public void onClick(View v) {
						// �ж��ֻ�ϵͳ�İ汾 ��API����10 ����3.0�����ϰ汾
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						SendActivity.this.startActivity(intent);
						dialog.dismiss();
					}
				}).show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem bianji = menu.add(0, BIANJI, 0, "ע���û�");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem mi) {
		switch (mi.getItemId()) {
		// �༭
		case BIANJI:
			if (platForm.isValid()) {
				dialog.setMsg("��ȷ��Ҫע����ǰ�û���")
						.setPositiveOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								platForm.removeAccount();
								str = "������" + name;
								SendActivity.this.setTitle(str);
								ShowCustomToast.show(SendActivity.this, "ע���ɹ�");
								dialog.dismiss();
							}
						}).show();

			} else {
				ShowCustomToast.show(this, "�㻹δ��½");
			}
			break;

		case DELETEIMG:

			break;

		}
		return false;

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		// ����ϵͳ�����
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case FLAG_CHOOSE:

				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						ShowCustomToast.show(this, "no found");
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					/*
					 * options = GetOpinions.getGetOpinions(path, winWidth,
					 * winHeight, null);
					 */
					if (options == null) {
						options = GetOpinions.getGetOpinions();
					}
					Log.d("may", "path=" + path);
					iv_picture.setImageBitmap(BitmapFactory.decodeFile(path,
							options));
					URI = path;
					System.out.println(URI);
				} else {
					Log.d("may", "path=" + uri.getPath());
					/*
					 * options = GetOpinions.getGetOpinions(uri.getPath(),
					 * winWidth, winHeight, null);
					 */
					if (options == null) {
						options = GetOpinions.getGetOpinions();
					}
					iv_picture.setImageBitmap(BitmapFactory.decodeFile(
							uri.getPath(), options));
					startActivityForResult(intent, FLAG_HANDLEBACK);
				}
				System.out.println("flag_choose");
				break;

			case FLAG_HANDLEBACK:

				Log.d("may", "path=" + data.getStringExtra("path"));
				String imagePath = data.getStringExtra("path");
				URI = imagePath;
				if (options == null) {
					options = GetOpinions.getGetOpinions();
				}
				iv_picture.setImageBitmap(BitmapFactory.decodeFile(imagePath,
						options));

				Log.d("may", "back");
				System.out.println("flag_handBack");
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
				File dir = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + this.getString(R.string.imgPath));

				// ���з������Ƭ��·��
				shareimgPath = dir.getAbsolutePath() + "/" + name;

				dir.mkdir();
				// File jpgFile = new
				// File(dir,System.currentTimeMillis()+".jpg");
				File jpgFile = new File(dir, name);
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
						// Toast.makeText(this, name, Toast.LENGTH_LONG).show();
						// Toast.makeText(this,"��Ƭ����ɹ�"+jpgFile.getAbsolutePath(),
						// Toast.LENGTH_LONG).show();
						ShowCustomToast.show(this, "��Ƭ����·��Ϊ��"
								+ jpgFile.getAbsolutePath().toString());

					} catch (IOException e) {
						// Toast.makeText(this, "��Ƭ����ʧ��2",
						// Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
				iv_picture.setImageBitmap(BitmapFactory.decodeFile(
						jpgFile.getAbsolutePath(), options));
				URI = jpgFile.getAbsolutePath();
				System.out.println("default");
			}
		}
		// ģ��һ����Ϣ ֪ͨϵͳsd�������¹�����.
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		sendBroadcast(intent);
	}
}
