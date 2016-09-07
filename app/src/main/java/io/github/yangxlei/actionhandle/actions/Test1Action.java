package io.github.yangxlei.actionhandle.actions;

import android.content.Context;

import java.util.Map;

import io.github.yangxlei.action.BJAction;
import io.github.yangxlei.actionhandle.Test1Activity;
import io.github.yangxlei.annotation.ActionType;

/**
 * Created by yanglei on 16/9/7.
 */
@ActionType("test1")
public class Test1Action implements BJAction.BJActionHandler {

    @Override
    public void doPerform(Context context, String type, Map<String, String> payload) {
        Test1Activity.launch(context);
    }
}
