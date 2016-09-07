package io.github.yangxlei.action;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by yanglei on 16/9/7.
 */
class SendActionTask implements DispatchAsync.DispatchRunnable {

    List<BJAction.BJActionHandler> handlerList;
    boolean result;
    String type;
    Map<String, String> payload;

    BJAction bjAction;
    String schema;
    Context context;
    SendActionTask(BJAction bjAction, Context context, String schema) {
        this.bjAction = bjAction;
        this.schema = schema;
        this.context = context;
    }

    @Override
    public void runInBackground() {
         result = bjAction.sendToTarget(context, schema, new BJAction.OnTriggerEvent() {
            @Override
            public void onTriggerEvent(List<BJAction.BJActionHandler> handlers, String type, Map<String, String> payload) {
                handlerList = handlers;
                SendActionTask.this.type = type;
                SendActionTask.this.payload = payload;
            }
        });
    }

    @Override
    public void runInMain() {
        if (handlerList == null) {
            if (! result) {
                // 匹配失败
                BJActionUtil.doActionDefaultHandler(context, schema);
            }
        } else {
            for (BJAction.BJActionHandler handler : handlerList) {
                handler.doPerform(context, type, payload);
            }
        }
    }
}
