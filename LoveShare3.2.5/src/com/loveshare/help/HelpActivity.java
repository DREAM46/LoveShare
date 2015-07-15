package com.loveshare.help;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loveshare.activity.R;

public class HelpActivity extends Activity{
	
	private SharedPreferences preferences;
	
	private LinearLayout title_help;
	private TextView tv_help;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		int drawable = this.getSharedPreferences("sp_LoveShare", MODE_PRIVATE)
				.getInt("theme", 0);
		title_help.setBackgroundResource(drawable);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		title_help = (LinearLayout)this.findViewById(R.id.title_help);
		tv_help = (TextView)this.findViewById(R.id.tv_help);
		
		String html = "<a href='http://www.baidu.com'>°Ù¶È</a>";
		
		CharSequence sequence = Html.fromHtml(html);
		tv_help.setText(sequence);
		
	}
	
}
