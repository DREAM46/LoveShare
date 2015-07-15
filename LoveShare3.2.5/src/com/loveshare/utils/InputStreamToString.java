package com.loveshare.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.loveshare.UI.CloudActivity;
import com.loveshare.view.ShowCustomToast;

public class InputStreamToString {

	public static String transform(Context context, InputStream ins) {

		byte[] data = new byte[1024];
		StringBuffer content = new StringBuffer();
		String content1 = "";
		try {
			while (ins.read(data) != -1) {
				if (CloudActivity.function.equals(CloudActivity.PHONEADD))
					content.append(new String(data, 0, data.length, "gb2312").trim());
				else
					content.append(new String(data, 0, data.length).trim());
			}

			content1 = content.toString().trim();
			

			if (content1.contains("gb2312")) {
			/*	content1 = content1.replace("gb2312", "utf-8");
				content1 = new String(content1.getBytes(), "utf-8");*/
			}
			System.out.println("instream to string   " + content1);

		} catch (IOException e) {
			System.out.println("Ω‚Œˆ ß∞‹");
			e.printStackTrace();
		} finally {
		/*	try {
				ins.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		return content1;
	}

}
