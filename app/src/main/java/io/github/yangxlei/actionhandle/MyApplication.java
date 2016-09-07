package io.github.yangxlei.actionhandle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.github.yangxlei.action.BJActionUtil;

/**
 * Created by yanglei on 16/9/7.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("App", "Application onCreate");

        //yangxlei://io.github/o.c?action=xx&xx=xx
        BJActionUtil.initialize("yangxlei", "io.github", "o.c", "action", new BJActionUtil.BJActionDefaultHandler() {
            @Override
            public void onHandleDefault(Context context, String schema) {
                //goto webview
            }
        });

        BJActionUtil.loadActions(io.github.yangxlei.actionhandle.actions.ModuleActionTypes.getModuleActionTypes());
        BJActionUtil.loadActions(yangxlei.github.io.app2.actions.ModuleActionTypes.getModuleActionTypes());

    }
}
