package com.wm.lock.core.security;

import java.io.UnsupportedEncodingException;

/**
 * 字符串处理
 * @author majun
 *
 */
public class StringHexUtil {
	
	/**
	 * 默认编码
	 */
	public static final String DEFAULT_CODE = "UTF-8";
	
	/**
	 * 反格式化byte
	 * 
	 * @param s
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static byte[] hex2byte(String s) throws UnsupportedEncodingException {
		byte[] src = s.toLowerCase().getBytes(DEFAULT_CODE);
		byte[] ret = new byte[src.length / 2];
		for (int i = 0; i < src.length; i += 2) {
			byte hi = src[i];
			byte low = src[i + 1];
			hi = (byte) ((hi >= 'a' && hi <= 'f') ? 0x0a + (hi - 'a')
					: hi - '0');
			low = (byte) ((low >= 'a' && low <= 'f') ? 0x0a + (low - 'a')
					: low - '0');
			ret[i / 2] = (byte) (hi << 4 | low);
		}
		return ret;
	}

	/**
	 * 格式化byte
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] out = new char[b.length * 2];
		for (int i = 0; i < b.length; i++) {
			byte c = b[i];
			out[i * 2] = Digit[(c >>> 4) & 0X0F];
			out[i * 2 + 1] = Digit[c & 0X0F];
		}
		return new String(out);
	}
	
	/**
	 * U编码
	 * @param s
	 * @return
	 */
	public static String UCodeEncode(String s) {
		if (s == null)
			return null;
		StringBuffer result = new StringBuffer();
		int i;
		for (i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= 0x2018) {
				result.append('\\');
				result.append('u');
				String hex = Integer.toHexString(s.charAt(i));
				result.append(hex);
			} else {
				result.append(s.charAt(i));
			}
		}
		return result.toString();
	}
	
	/**
	 * U解码
	 * @param s
	 * @return
	 */
	public static String UCodeDecode(String s) {
		if (s == null)
			return null;
		StringBuffer result = new StringBuffer();
		int start = 0;
		int end = 0;
		while ((end = s.indexOf("\\u", start)) != -1) {
			result.append(s.substring(start, end));
			start = end + 2;
			end = start + 4;
			if (end > s.length()) {
				break;
			}
			char c = (char) Integer.parseInt(s.substring(start, end), 16);
			result.append(c);
			start = end;
		}
		if (start < s.length()) {
			result.append(s.substring(start));
		}
		return result.toString();
	}
	
}
