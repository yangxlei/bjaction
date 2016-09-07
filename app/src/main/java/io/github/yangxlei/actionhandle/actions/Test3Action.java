package io.github.yangxlei.actionhandle.actions;

import android.content.Context;

import java.util.Map;

import io.github.yangxlei.action.BJAction;
import io.github.yangxlei.actionhandle.Test3Activity;
import io.github.yangxlei.annotation.ActionType;

/**
 * Created by yanglei on 16/9/7.
 */
@ActionType("test3")
public class Test3Action implements BJAction.BJActionHandler {

    @Override
    public void doPerform(Context context, String type, Map<String, String> payload) {
        Test3Activity.launch(context);
    }
}
