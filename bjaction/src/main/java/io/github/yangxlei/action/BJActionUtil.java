package io.github.yangxlei.action;

import android.content.Context;

import java.util.Map;

/**
 * Created by yanglei on 16/8/29.
 */
public class BJActionUtil {

    private static BJAction sBJAction;
    public static void initialize(String schema, String host, String path, String actionKey) {
        sBJAction = new BJAction(schema, host, path, actionKey);
        //TODO load all actions
    }

    public static String buildActionUrl(String actionType, Map<String, String> payload) {
        return "";
    }

    public static boolean sendToTarget(Context context, String url) {
        if (sBJAction == null) {
            throw new IllegalStateException("ActionUtil is not initialize.");
        }
        return sBJAction.sendToTarget(context, url);
    }
}
