package com.loveshare.UI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loveshare.activity.R;
import com.loveshare.db.OperateChatDB;
import com.loveshare.domin.Chat;
import com.loveshare.service.SetTheme;
import com.loveshare.utils.InputStreamToString;
import com.loveshare.view.CreateCustomProgressDialog;
import com.loveshare.view.CustomProgressDialog;
import com.loveshare.view.ShowCustomToast;

public class CloudActivity extends Activity implements OnClickListener {

	private static final String HINT = "欢迎来到“微服务”\n"
			/* + "天气查询 :输入 “城市+天气”，如 南昌天气，即可返回南昌最新的天气预报。\n" */
		/*	+ "火车班次及站点查询:输入“列车 发车点  终点站  时间”或“班次 火车班次”，如“列车 广州 雷州 2014-5-30” 或“班次 K407” ，即可获取站点信息或班次信息\n"*/
			+ "单词翻译:用户输入 “ $单词”即可收到翻译回复，如 $开心，返回结果 happy\n"
			+ "手机归属地查询:用户输入“#手机号码”即可得到手机归属地，如#12345678901，返回结果中国电信 广东广州\n";
	/* + "机器人陪聊:如果用户输入的内容不再关键词范围之内，及使用机器人陪聊接口，实现自动回复。\n"; */
	private ListView lv_chat;
	private EditText et_chat_content;
	private Button btn_chat_send;
	private List<Chat> list;

	private OperateChatDB operateChatDB;

	public static final int FLAG_SEND = 1;
	public static final int FLAG_RECEIVE = 2;

	private static final String URI_TRANSLATE = "http://fanyi.youdao.com/openapi.do?";
	private static final String URI_PHONEADD = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi";
	private static final String URI_CHEPIAO = "http://www.chepiao100.com/api/checi?";
	private static final String URI_CHEYUPIAO =  "http://www.chepiao100.com/api/yupiao";

	private static final int OK = 200;

	private SharedPreferences preferences;
	private Editor editor;
	private LinearLayout linear_title;
	private TextView cloud_title;

	private ChatAdapter adapter;

	private static final int SUCCESS = 1;
	private static final int FAIL = -1;

	private CustomProgressDialog dialog;

	public static String function;

	public static final String TRANSLATE = "translate";
	public static final String PHONEADD = "phoneAdd";
	public static final String TRAINBYCODE = "chePiaoCode";
	private static final String TRAINBYCITY = "chePiaoCity";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case FLAG_RECEIVE:
				dialog.dismiss();
				String responseContent = (String) msg.obj;
				notifyData(new Chat(responseContent, FLAG_RECEIVE));
				break;
			case FLAG_SEND:
				// 将请求内容存入数据库
				String requestContent = (String) msg.obj;
				notifyData(new Chat(requestContent, FLAG_SEND));
				break;
			case FAIL:
				dialog.dismiss();
				ShowCustomToast.show(CloudActivity.this, "访问失败");
				break;
			}
		}

	};

	/**
	 * 翻译的json数据的解析
	 * 
	 * @param responseContent
	 *            返回的数据
	 * @return 解析结果
	 */
	private String processTranslateResponse(String responseContent) {

		// 解析结果为错误的处理
		if (responseContent.equals("no query")) {
			return "error";
		}

		JSONObject object;
		try {
			object = new JSONObject(responseContent);
			responseContent = object.getString("translation");
			System.out.println("re1" + responseContent);
			responseContent = responseContent.substring(
					responseContent.indexOf("[") + 2,
					responseContent.indexOf("\"]"));

		} catch (Exception e) {
			System.out.println("解析失败");
			e.printStackTrace();
		}
		return responseContent;
	}

	/**
	 * 截取xml文件字符串，获得手机号码的有效信息
	 * 
	 * @param responseContent
	 *            解析数据
	 * @return 解析结果
	 */
	private String processPhoneAddResponse(String responseContent) {

		// 解析结果为错误的处理
		if (responseContent.contains("error")) {
			return "error";
		}

		String city = responseContent.substring(
				responseContent.indexOf("<city>") + "city".length() + 2,
				responseContent.indexOf("</city>"));

		String province = responseContent
				.substring(
						responseContent.indexOf("<province>")
								+ "province".length() + 2,
						responseContent.indexOf("</province>"));

		String supplier = responseContent
				.substring(
						responseContent.indexOf("<supplier>")
								+ "supplier".length() + 2,
						responseContent.indexOf("</supplier>"));

		responseContent = "中国" + supplier + "\n" + province + city;
		return responseContent;
	}

	/**
	 * 获取列车班次经过地点及时间
	 * 
	 * @param responseContent
	 * @return
	 */
	private String ProcessTrainByCodeResponse(String responseContent) {
		System.out.println("respon" + responseContent);
		String heade = null;
		String item = null;
		String title = null;

		StringBuffer buffer = new StringBuffer();

		if (responseContent.contains("服务器忙")) {
			return "error";
		}

		try {
			JSONObject json = new JSONObject(responseContent);

			heade = json.getString("heade");
			// 返回["站次","站名","到时","发时","停留","里程","硬座","硬卧上/中/下","软卧上/下"]
			item = json.getString("item");
			title = json.getString("title");
			System.out.println(json.getString("title"));
			// System.out.println("head:"+heade);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 返回：站次,站名,到时,发时,停留,里程,硬座,硬卧上/中/下,软卧上/下
		heade = heade.substring(1, heade.length() - 1).replace("\"", "");
		// 返回：1,长沙,-,21:30,-,-,-,-,-],[2,株洲,22:10,22:16,6分,52公里,11元,57/62/65元,86.5/92.5元
		item = item.substring(2, item.length() - 2).replace("\"", "");

		String[] heades = heade.split(",");
		String[] items = item.split("\\],\\[");
		for (int i = 0; i < items.length; i++) {
			// System.out.println("item" + i + items[i]);

			String msg = "";
			String[] msgs = items[i].split(",");

			/*
			 * "站次", "站名", "到时", "发时", "停留", "里程", "硬座", "硬卧上/中/下", "软卧上/下"
			 */
			String id = "第" + msgs[0] + "站  ";// 站次

			String startStation = processMsg(msgs[1]);// 站名

			String arriveTime = processMsg(msgs[2]);// 到时
			String startTime = processMsg(msgs[3]);// 发时
			if (arriveTime.equals(""))
				startTime = "发时" + msgs[3];
			if (!arriveTime.equals("") && !startTime.equals(""))
				startTime = startTime + "——";

			if (startTime.equals(""))
				arriveTime = "到时" + msgs[2];
			String hard_seat = "";
			String hard_sleeper = "";
			String soft_sleeper = "";
			if (!processMsg(msgs[6]).equals(""))
				hard_seat = ("硬座:" + msgs[6]).replace("\\", "") + "\n";// 硬座
			if (!processMsg(msgs[7]).equals(""))
				hard_sleeper = ("硬卧上/中/下:" + msgs[7]).replace("\\", "") + "\n";
			if (!processMsg(msgs[8]).equals(""))
				soft_sleeper = ("软卧上/下:" + msgs[8]).replace("\\", "") + "\n";

			msg = id + startStation + "\n" + startTime + arriveTime + "\n"
					+ hard_seat + hard_sleeper + soft_sleeper;
			buffer.append(msg);

		}
		return buffer.toString();
	}

	private String processMsg(String msg) {
		if (msg.equals("-"))
			return "";
		return msg + " ";
	}

	private String ProcessTrainByCityResponse(String responseContent) {
		
		if (responseContent.contains("服务器忙") || responseContent.contains("</B>")) {
			return "error";
		}

		String msg = "";
		try {
			JSONObject object1 = new JSONObject(responseContent);

			JSONArray item = object1.getJSONArray("item");
			JSONObject object = (JSONObject) item.get(0);

			String trainCode = object.getString("trainCode");// 列车编号
			String startStation = object.getString("startStation");// 起始站
			String arriveStation = object.getString("arriveStation");// 终点站
			String startTime = object.getString("startTime");// 开出时间
			String endTime = object.getString("endTime");// 抵达时间

			String business = processChePiaoCount(object.getString("business"));// 商务座
			String best_seat = processChePiaoCount(object
					.getString("best-seat"));// 特等座
			String one_seat = processChePiaoCount(object.getString("one-seat"));// 一等座
			String two_seat = processChePiaoCount(object.getString("two-seat"));// 二等座

			String vag_sleeper = processChePiaoCount(object
					.getString("vag-sleeper"));// 特等卧铺
			String soft_sleeper = processChePiaoCount(object
					.getString("soft-sleeper"));// 软卧
			String hard_sleeper = processChePiaoCount(object
					.getString("hard-sleeper"));// 硬卧

			String soft_seat = processChePiaoCount(object
					.getString("soft-seat"));// 软座
			String hard_seat = processChePiaoCount(object
					.getString("hard-seat"));// 硬座
			String none_seat = processChePiaoCount(object
					.getString("none-seat"));// 站票

			msg = "列车编号:" + trainCode + "\n" + "出发终止站:" + startStation + "——"
					+ arriveStation + "\n" + "时间:" + startTime + "——" + endTime
					+ "\n" + "商务座:" + business + "\n" + "特等座:" + best_seat
					+ "\n" + "一等座:" + one_seat + "\n" + "二等座:" + two_seat
					+ "\n" + "特等卧铺" + vag_sleeper + "\n" + "软卧" + soft_sleeper
					+ "\n" + "硬卧" + hard_sleeper + "\n" + "软座" + soft_seat
					+ "\n" + "硬座" + hard_seat + "\n" + "站票" + none_seat + "\n";

			System.out.println("msg***********  " + msg);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;

	}

	private String processChePiaoCount(String str) {
		if (str.equals("--"))
			return "0张";
		return str + "张";
	}

	@Override
	protected void onStart() {
		int drawable = preferences.getInt("theme", 0);
		if (drawable != 0)
			SetTheme.setBackground(linear_title, drawable, null);

		operateChatDB.deleteAll();

		operateChatDB.insert(new Chat(HINT, FLAG_SEND));

		super.onStart();
		function = TRAINBYCITY;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		linear_title = (LinearLayout) this.findViewById(R.id.linear_title);

		// function = TRANSLATE;

		cloud_title = (TextView) this.findViewById(R.id.cloud_title);

		et_chat_content = (EditText) this.findViewById(R.id.et_chat_content);

		btn_chat_send = (Button) this.findViewById(R.id.btn_chat_send);
		btn_chat_send.setOnClickListener(this);

		operateChatDB = new OperateChatDB(this);
		list = new ArrayList<Chat>();

		lv_chat = (ListView) this.findViewById(R.id.lv_chat);
		adapter = new ChatAdapter();
		lv_chat.setAdapter(adapter);

		preferences = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE);
		editor = preferences.edit();

		dialog = CreateCustomProgressDialog.createCustomProgressDialog(this);

		dialog = CreateCustomProgressDialog
				.createCustomProgressDialog(CloudActivity.this);

	}

	@Override
	public void onClick(View v) {

		if (v == btn_chat_send) {

			dialog = CreateCustomProgressDialog
					.createCustomProgressDialog(CloudActivity.this);

			dialog.show();

			final StringBuffer requestContent1 = new StringBuffer(
					et_chat_content.getText().toString().trim());

			if (TextUtils.isEmpty(requestContent1)) {
				ShowCustomToast.show(this, "查询内容不能为空");
				dialog.dismiss();
				return;
			}

			setFunction(requestContent1.toString());

			if (function.equals("error")) {
				dialog.dismiss();
				ShowCustomToast.show(this, "请输入正确的信息");
				et_chat_content.setText("");
				return;
			}

			et_chat_content.setText("");

			Message message = handler.obtainMessage();
			message.what = FLAG_SEND;
			message.obj = requestContent1.toString();
			handler.sendMessage(message);

			new Thread() {
				public void run() {
					try {
						String requestContent = requestContent1.toString();

						HttpClient client = new DefaultHttpClient();
						HttpPost post = new HttpPost();
						List<NameValuePair> pairs = new ArrayList<NameValuePair>();

						// 根据不同的功能请求，将标志去掉，得到真正的请求内容;根据不同的功能请求，得到不同的post请求对象
						if (function.equals(TRANSLATE)) {
							requestContent = requestContent.substring(1);
							post = getTranslatePost(requestContent, pairs);
						} else if (function.equals(PHONEADD)) {
							requestContent = requestContent.substring(1);
							post = getPhoneAddPost(requestContent, pairs);
						} else if (function.equals(TRAINBYCODE)) {
							requestContent = requestContent.substring("班次 "
									.length());
							post = getTrainByDatePost(requestContent, pairs);
						} else if (function.equals(TRAINBYCITY)) {
							requestContent = requestContent.substring("列车,"
									.length());
							post = getTrainByCityPost(requestContent, pairs);
						}

						System.out.println("pairs success");

						post.setEntity(new UrlEncodedFormEntity(pairs,
								HTTP.UTF_8));

						HttpResponse response = client.execute(post);
						String responseContent = "";

						if (response.getStatusLine().getStatusCode() == OK) {

							InputStream ins = response.getEntity().getContent();
							if (ins == null) {
								return;
							}

							// 得到返回的内容字符串
							responseContent = InputStreamToString.transform(
									CloudActivity.this, ins);

							if(function.equals(TRAINBYCODE) && responseContent.contains("服务器忙")){
								fail();
								return;
							}
							
							if(function.equals(TRAINBYCITY)&& responseContent.contains("服务器忙")){
								fail();
								return;
							}
							
							// 根据不同的功能请求，处理处理流后得到的字符串，得到最终的请求结果
							if (function.equals(TRANSLATE)) {
								responseContent = processTranslateResponse(responseContent);
							} else if (function.equals(PHONEADD)) {
								responseContent = processPhoneAddResponse(responseContent);
							} else if (function.equals(TRAINBYCODE)) {

								responseContent = responseContent.substring(0,
										responseContent.indexOf("title") - 2)
										+ "}";
								responseContent = ProcessTrainByCodeResponse(responseContent);
							} else if (function.equals(TRAINBYCITY)) {
								responseContent = responseContent.substring(0,
										responseContent.indexOf("title") - 2)
										+ "}";
								responseContent = ProcessTrainByCityResponse(responseContent);
							}

							if (responseContent.equals("error")) {
								fail();
								return;
							}

							System.out.println("responseContent"
									+ responseContent);

							// handler发送message
							Message msg1 = handler.obtainMessage();
							msg1.what = FLAG_RECEIVE;
							msg1.obj = responseContent;
							handler.sendMessage(msg1);

						}
					} catch (Exception e) {
						fail();
						e.printStackTrace();
					}
				}

				private void fail() {
					Message msg = handler.obtainMessage();
					msg.what = FAIL;
					handler.sendMessage(msg);
					System.out.println("fail");
				}
			}.start();
		}
	}

	/**
	 * 根据用户输入的内容，确定请求功能的类型
	 * 
	 * @param requestContent
	 *            用户输入的内容
	 */
	private void setFunction(String requestContent) {
		if (requestContent.startsWith("$")) {
			function = TRANSLATE;
		} else if (requestContent.startsWith("#")) {
			function = PHONEADD;
		} /*else if (requestContent.startsWith("班次")) {
			function = TRAINBYCODE;
		} else if (requestContent.startsWith("列车")) {
			function = TRAINBYCITY;
		}*/ else
			function = "error";
	}

	private HttpPost getTrainByDatePost(String requestContent,
			List<NameValuePair> pairs) {
		// act=code&trainCode=K407
		HttpPost post = new HttpPost(CloudActivity.URI_CHEPIAO);
		pairs.add(new BasicNameValuePair("act", "code"));
		pairs.add(new BasicNameValuePair("trainCode", requestContent));
		return post;
	}

	/**
	 * 创建翻译的请求
	 * 
	 * @param requestContent
	 *            翻译的单词
	 * @param pairs
	 *            参数集合
	 * @return 翻译的请求
	 */
	private HttpPost getTranslatePost(String requestContent,
			List<NameValuePair> pairs) {
		HttpPost post = new HttpPost(CloudActivity.URI_TRANSLATE);
		pairs.add(new BasicNameValuePair("keyfrom", "translate4youdao"));
		pairs.add(new BasicNameValuePair("key", "559224007"));
		pairs.add(new BasicNameValuePair("type", "data"));
		pairs.add(new BasicNameValuePair("doctype", "json"));
		pairs.add(new BasicNameValuePair("version", "1.1"));
		pairs.add(new BasicNameValuePair("q", requestContent));
		return post;
	}

	/**
	 * 得到获取手机归属的请求
	 * 
	 * @param requestContent
	 *            手机号码
	 * @param pairs
	 *            参数集合
	 * @return 获取手机归属的请求
	 */
	private HttpPost getPhoneAddPost(String requestContent,
			List<NameValuePair> pairs) {
		// ?phone=13892101111&mode=txt
		HttpPost post = new HttpPost(CloudActivity.URI_PHONEADD);
		pairs.add(new BasicNameValuePair("chgmobile", requestContent));
		return post;
	}

	/**
	 * 由目的城市和出发城市获取post请求
	 * 
	 * @param requestContent
	 * @param pairs
	 * @return
	 */
	private HttpPost getTrainByCityPost(String requestContent,
			List<NameValuePair> pairs) {
		// act=remain&startStation=广州&arriveStation=雷州&date=2014-05-30
		String msgs[] = requestContent.split(",");

		System.out.println("new request  " + requestContent);
		for (int i = 0; i < msgs.length; i++) {
			System.out.println("msg" + i + "  " + msgs[i]);
		}

		HttpPost post = new HttpPost(CloudActivity.URI_CHEYUPIAO);
		pairs.add(new BasicNameValuePair("userid", "1209667740@qq.com"));
		pairs.add(new BasicNameValuePair("seckey", "2b0877ffe977485d03486860086cb2d7"));
		pairs.add(new BasicNameValuePair("startStation", msgs[0]));
		pairs.add(new BasicNameValuePair("arriveStation", msgs[1]));
		pairs.add(new BasicNameValuePair("date", msgs[2]));
		return post;
	}

	/**
	 * 更新数据
	 * 
	 * @param responseChat
	 *            插入数据库的数据
	 */
	private void notifyData(Chat responseChat) {
		operateChatDB.insert(responseChat);
		adapter.notifyDataSetChanged();
		lv_chat.setAdapter(adapter);
	}

	/**
	 * ListView的适配器
	 * 
	 */
	private class ChatAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			list = new OperateChatDB(CloudActivity.this).queryAll();
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(CloudActivity.this,
					R.layout.activity_chat_cell, null);

			TextView tv_chat_send = (TextView) view
					.findViewById(R.id.tv_chat_send);
			TextView tv_chat_receive = (TextView) view
					.findViewById(R.id.tv_chat_receive);

			Chat chat = list.get(position);
			int flag = chat.getFlag();
			// System.out.println("content"+chat.getContent()+"***flag"+chat.getFlag());

			/**
			 * 如果是发送的信息，就将内容显示在发送框中，将接受框隐藏 如果是接受的信息，就将内容显示在发送框中，将发送框隐藏
			 */

			if (flag == FLAG_SEND) {
				if (chat.getContent().length() <= 7) {
					tv_chat_send.setGravity(Gravity.CENTER);
				}

				tv_chat_send.setText(chat.getContent());
				tv_chat_receive.setVisibility(View.GONE);
			} else {
				if (chat.getContent().length() <= 7) {
					tv_chat_receive.setGravity(Gravity.CENTER);
				}
				tv_chat_receive.setText(chat.getContent());
				tv_chat_send.setVisibility(View.GONE);
			}
			return view;
		}

	}
}