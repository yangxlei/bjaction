package io.github.yangxlei.action;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类 Jockey 管理 url 跳转
 * @author yanglei
 *
 */
public class BJAction {
	
	private static final String TAG = BJAction.class.getName();
	
	public interface BJActionHandler {
		void doPerform(Context context, String type, Map<String, String> payload);
	}

	private String mSchema;
	private String mHost;
	private String mPath;
	private String mAction;
	
	private ConcurrentHashMap<String, ArrayList<BJActionHandler>> mListeners =
			new ConcurrentHashMap<String, ArrayList<BJActionHandler>>();

	/**
	 *  schema://host/path?actionKey=aaa&xx=xx
	 * @param schema
	 * @param host
	 * @param path
	 * @param actionKey
	 */
	public BJAction(String schema, String host, String path, String actionKey) {
		this.mSchema = schema;
		this.mHost = host;
		this.mPath = path;
		this.mAction = actionKey;
	}

	public String getSchema() {
		return mSchema;
	}

	public String getHost() {
		return mHost;
	}

	public String getPath() {
		return mPath;
	}

	public String getAction() {
		return mAction;
	}

	private boolean isInitialized() {
		return (!TextUtils.isEmpty(mSchema));
	}
	
	public void on(String type, BJActionHandler handler) {
		if (TextUtils.isEmpty(type)) throw new NullPointerException("type 不能为 null");
		
		ArrayList<BJActionHandler> list = mListeners.get(type);
		if (list == null) {
			list = new ArrayList<BJActionHandler>();
			mListeners.put(type, list);
		}
		
		list.add(handler);
	}
	
	public void off(String type) {
		if (TextUtils.isEmpty(type)) throw new NullPointerException("type 不能为 null");
		
		mListeners.remove(type);
	}

	public void offAll() {
		mListeners.clear();
	}
	
	/*
	 * <p>schema 匹配失败直接返回 false。</br>
	 * schema 匹配成功; host， path 匹配失败，属于能够处理但是没有处理的类型，返回 true，同时打印 Log</p>
	 * @param context
	 * @param url
	 * @return false 表示未处理. 外部根据返回值再自行处理
	 */
	public boolean sendToTarget(Context context, String url, OnTriggerEvent triggerEvent) {
		if (! isInitialized()) throw new RuntimeException("还未初始化");
		
		if (TextUtils.isEmpty(mSchema))
			return false;
		BJUrl bjUrl = BJUrl.parse(url, false);
		
		if (mSchema.equals(bjUrl.getProtocol())) {
			if (TextUtils.isEmpty(mHost)) {
				
				if (! TextUtils.isEmpty(bjUrl.getHost())) {
					// host 为空，以 url 中的 host 为 action
					triggerEvent(context, bjUrl.getHost(), bjUrl.getParameters(), triggerEvent);
					return true;
				}
			} else {
				if (!mHost.equals(bjUrl.getHost())) {
					Log.e(TAG, "schema 相同， host 不同。匹配失败");
					return true;
				}
			}
			
			if (TextUtils.isEmpty(mPath)) {
				if (! TextUtils.isEmpty(bjUrl.getPath())) {
					Log.e(TAG, "schema 相同，host 相同， path 不同。 匹配失败");
					return true;
				}
			} else {
				if (! mPath.equals(bjUrl.getPath())) {
					Log.e(TAG, "schema 相同，host 相同， path 不同。 匹配失败");
					return true;
				}
			}
			
			if (bjUrl.getParameters() == null || bjUrl.getParameters().isEmpty()) {
				Log.e(TAG, "url action 不存在。 匹配失败");
				return true;
			}
			
			String type = bjUrl.getParameters().get(mAction);
			if (TextUtils.isEmpty(type)) {
				Log.e(TAG, "type 匹配失败");
				return true;
			}
			
			triggerEvent(context, type, bjUrl.getParameters(), triggerEvent);
			
		} else {
			return false;
		}

		return true;
	}
	
	private void triggerEvent(Context context, String type, Map<String, String> payload, OnTriggerEvent triggerEvent) {
		List<BJActionHandler> list = mListeners.get(type);

		ArrayList<BJActionHandler> _list = new ArrayList<>(list);
		triggerEvent.onTriggerEvent(_list, type, payload);
//		if (list == null || list.size() == 0) return;
//
//		for (Iterator<BJActionHandler> iterator = list.iterator(); iterator.hasNext(); ) {
//			iterator.next().doPerform(context, type, payload);
//		}
	}

	interface OnTriggerEvent {
		void onTriggerEvent(List<BJActionHandler> handlers, String type, Map<String, String> payload);
	}
}
