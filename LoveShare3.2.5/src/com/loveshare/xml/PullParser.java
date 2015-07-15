package com.loveshare.xml;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.loveshare.domin.PhoneNumInfo;

public class PullParser {

	private static PhoneNumInfo info;

	private static final String CITY = "city";
	private static final String PROVINCE = "province";
	private static final String SUPPLIER = "supplier";

	public static PhoneNumInfo pullParser(InputStream ins) {
		XmlPullParser parser = Xml.newPullParser();
		int type = parser.START_DOCUMENT;

		String tag = null;
		try {
			parser.setInput(ins,"gb2312");

			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_DOCUMENT:
					info = new PhoneNumInfo();
					break;
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.equals(CITY)) {
						System.out.println("tag city");
						info.setCity(parser.nextText());
					} else if (tag.equals(PROVINCE)) {
						System.out.println("tag province");
						info.setProvince(parser.nextText());
					} else if (tag.equals(SUPPLIER)) {
						System.out.println("tag supplier");
						info.setSupplier(parser.nextText());
					}

					break;

				}
				type = parser.next();
			}
			return info;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
