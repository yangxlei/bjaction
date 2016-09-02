package io.github.yangxlei.action;

import java.net.URLDecoder;
import java.util.HashMap;

/*
 * 自己实现的 url 解析类
 * 
 * @author yanglei
 * 
 */
public class BJUrl {
	private String mProtocol;
	private String mHost;
	private String mPath;
	private HashMap<String, String> mParameter;

	private static boolean isEmpty(String text) {
		if (text == null || text.length() == 0) {
			return true;
		}
		return false;
	}
	
	public static BJUrl parse(String urlString) {
		return parse(urlString, false);
	}

	/*
	 * 解析 url 格式字符串。 将 protocol, host, path, parameters 全部存入 map 中返回
	 * 
	 * @param urlString
	 * @return
	 * @throws Exception
	 */
	public static BJUrl parse(String urlString, boolean needDecode) {
		if (isEmpty(urlString))
			return null;

		int protocol_index = urlString.indexOf("://");
		if (protocol_index <= 0) {
			throw new IllegalArgumentException();
		}

		BJUrl url = new BJUrl();

		String protocol = urlString.substring(0, protocol_index);
		url.mProtocol = protocol;

		int host_index = urlString.indexOf("/", protocol_index + 3);
		String host;
		if (host_index < 0) {
			int param_index = urlString.indexOf("?", protocol_index + 3);
			if (param_index < 0) {
				host = urlString.substring(protocol_index + 3);
			} else {
				host = urlString.substring(protocol_index + 3, param_index);
			}
		} else {
			host = urlString.substring(protocol_index + 3, host_index);
		}
		host_index = protocol_index + 3 + host.length() - 1;
		url.mHost = host;

		int path_index = urlString.indexOf("?");
		if (path_index < 0 || path_index < host_index) {
			path_index = urlString.length();
		}

		String path = urlString.substring(host_index + 1, path_index);
		url.mPath = path;

		if (path_index < 0 || path_index >= urlString.length())
			return url;
		url.mParameter = anaylizeUrlParameters(urlString
				.substring(path_index + 1), needDecode);

		return url;
	}

	private static HashMap<String, String> anaylizeUrlParameters(
			String parameterString, boolean needDecode) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (isEmpty(parameterString) || parameterString.length() < 1) {
			return parameters;
		}
		
		if (needDecode) {
			parameterString = URLDecoder.decode(parameterString);
		}

		if (parameterString.startsWith("&")) {
			parameterString = parameterString.substring(1);
		}

		int start_index = 0;
		int end_index = parameterString.indexOf("=");
		while (end_index > 0 && end_index < parameterString.length()) {
			String key = parameterString.substring(start_index, end_index);
			start_index = end_index + 1;
			end_index = parameterString.indexOf("&", start_index);
			String value;
			if (end_index < 0) {
				value = parameterString.substring(start_index);
			} else {
				value = parameterString.substring(start_index, end_index);

				start_index = end_index + 1;
				end_index = parameterString.indexOf("=", start_index);
			}

			parameters.put(key, value);
		}

		return parameters;
	}
	
	public static BJUrl parseQuery(String query) {
		return parseQuery(query, false);
	}

	/*
	 * 解析 query: app=3&version=4
	 * 
	 * @param query
	 * @return
	 */
	public static BJUrl parseQuery(String query, boolean needDecode) {
		BJUrl url = new BJUrl();
		url.mParameter = anaylizeUrlParameters(query, needDecode);
		return url;
	}

	public String getProtocol() {
		return mProtocol;
	}

	public String getHost() {
		return mHost;
	}

	public String getPath() {
		return mPath;
	}

	public HashMap<String, String> getParameters() {
		return mParameter;
	}

	@Override
	public String toString() {
		return "[PROTOCOL:" + mProtocol + " HOST:" + mHost + " PATH:" + mPath + " PARAMETERS:" + mParameter
				+ "]";
	}

}