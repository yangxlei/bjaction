package io.github.yangxlei.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yanglei on 16/8/29.
 */
public class BJActionUtil {

    public interface BJActionDefaultHandler {
        void onHandleDefault(Context context, String schema);
    }

    private static BJAction sBJAction;
    private static BJActionDefaultHandler sDefaultHandler;

    public static void initialize(String schema, String host, String path, String actionKey, BJActionDefaultHandler defaultHandler) {
        String _path = path;
        if (!TextUtils.isEmpty(path)) {
            _path = "/"+_path;
        }
        sBJAction = new BJAction(schema, host, _path, actionKey);
        sDefaultHandler = defaultHandler;

    }

    private static void checkIfInit() {
        if (sBJAction == null) {
            throw new IllegalStateException("ActionUtil is not initialize.");
        }
    }

    public static String buildActionUrl(String actionType, Map<String, String> payload) {
        checkIfInit();
        StringBuilder url = new StringBuilder();
        if (!TextUtils.isEmpty(sBJAction.getSchema())) {
            url.append(sBJAction.getSchema()).append("://");
        }

        if (!TextUtils.isEmpty(sBJAction.getHost())) {
            url.append(sBJAction.getHost());
        }

        if (!TextUtils.isEmpty(sBJAction.getPath())) {
            url.append(sBJAction.getPath());
        }

        if (!TextUtils.isEmpty(sBJAction.getAction())) {
            url.append("?").append(sBJAction.getAction()).append("=").append(actionType);
        }

        if (payload != null && !payload.isEmpty()) {
            String keys[] = new String[payload.size()];
            payload.keySet().toArray(keys);

            for (int i = 0, len = keys.length; i < len; ++ i) {
                url.append("&").append(keys[i]).append("=").append(payload.get(keys[i]));
            }
        }

        return url.toString();
    }

    public static void sendToTarget(Context context, String url) {
        checkIfInit();
        DispatchAsync.dispatchAsync(new SendActionTask(sBJAction, context, url));
    }

    public static void removeAllActions() {
        checkIfInit();
        sBJAction.offAll();
    }

    public static void loadAction(String actionType, BJAction.BJActionHandler handler) {
        checkIfInit();
        sBJAction.on(actionType, handler);
    }

    public static void loadActions(Map actionTypes) {
        checkIfInit();
        final Map _actionTypes = new HashMap(actionTypes);

        if (_actionTypes.isEmpty()) return;

        DispatchAsync.dispatchAsync(new DispatchAsync.DispatchRunnable() {
            @Override
            public void runInBackground() {
                Iterator iterator = _actionTypes.keySet().iterator();

                while (iterator.hasNext()) {
                    Object key = iterator.next();
                    if (!(key instanceof String)) continue;
                    Object value = _actionTypes.get(key);
                    if (!(value instanceof BJAction.BJActionHandler)) continue;

                    sBJAction.on((String)key, (BJAction.BJActionHandler) value);
                }
            }

            @Override
            public void runInMain() {
            }
        });
    }

    static void doActionDefaultHandler(Context context, String schema) {
        if (sDefaultHandler != null) {
            sDefaultHandler.onHandleDefault(context, schema);
        }
    }
}
