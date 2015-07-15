package com.loveshare.xml;

import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.loveshare.domin.PhoneNumInfo;

public class XMLHandler1 extends DefaultHandler {

	private PhoneNumInfo info;
	private String tag;

	@Override
	public void startDocument() throws SAXException {
		System.out.println("startDocument");
		info = new PhoneNumInfo();
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("endDocument");
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		System.out.println("startElement\t" + localName);

		if (localName.equals("city")) {
			tag = "city";
		} else if (localName.equals("province")) {
			tag = "province";
		} else if (localName.equals("supplier")) {
			tag = "supplier";
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("endElement");
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		System.out.println("characters");
		if (tag != null) {
			String text = "";
			try {
				text = new String(new String(ch,start,length).getBytes(), "utf-8");
				System.out.println("text\t" + text);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println("text" + text);
			if (tag.equals("city")) {
				System.out.println("tag city");
				info.setCity(text);
			} else if (tag.equals("province")) {
				System.out.println("tag province");
				info.setProvince(text);
			} else if (tag.equals("supplier")) {
				System.out.println("tag supplier");
				info.setSupplier(text);
			}
		}

	}

	public PhoneNumInfo getPhoneNumInfo() {
		if (info == null) {
			System.out.println("info is null");
		} else {
			System.out.println("info is not null");
		}
		
		if(info.getCity() == null){
			System.out.println("city is null");
		}else System.out.println("city"+info.getCity());
		
		if(info.getProvince() == null){
			System.out.println("Province is null");
		}else System.out.println("Province"+info.getCity());
		
		if(info.getSupplier() == null){
			System.out.println("su is null");
		}else System.out.println("su"+info.getSupplier());
		

		System.out.println("info" + info.toString());
		return this.info;
	}

}
