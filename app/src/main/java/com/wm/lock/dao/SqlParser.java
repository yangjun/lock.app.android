package com.wm.lock.dao;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
	
	/** 当前版本号 **/
	private int curId = -1;

	private static SqlParser instance;
	
	/** 节点 **/
	private static final String SQL = "sql";
	private static final String CONTENT = "content";
	
	/** 编码方式 **/
	private static final String ENCODING = "UTF-8";

	private Context mCtx;
	private String mFileName;

	public SqlParser(Context ctx, String fileName) {
		this.mCtx = ctx;
		this.mFileName = fileName;
	}

	/**
	 * 获取对应的sql语句列表
	 * 
	 * @param id
	 *            版本标识；
	 * @return
	 */
	public List<String> parse(int id) throws Exception {
		List<String> list = null;
		XmlPullParser pullParser = Xml.newPullParser();
		// 为解析器设置要解析的XML数据
		pullParser.setInput(mCtx.getAssets().open(mFileName), ENCODING);
		int event = pullParser.getEventType();
		// 循环遍历每个元素只到文档末尾
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<String>();
				break;
			case XmlPullParser.START_TAG:// 标签开始
				if (pullParser.getName().equals(SQL) && pullParser.getAttributeCount() > 0) {
					curId = Integer.valueOf(pullParser.getAttributeValue(0));
				}
				if (curId == id && pullParser.getName().equals(CONTENT)) {
					String content = pullParser.nextText();
					list.add(content);
				}
				break;
			}
			event = pullParser.next();
		}
		return list;
	}
	
}
