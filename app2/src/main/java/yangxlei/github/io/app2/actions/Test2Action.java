package yangxlei.github.io.app2.actions;

import android.content.Context;

import java.util.Map;

import io.github.yangxlei.action.BJAction;
import io.github.yangxlei.annotation.ActionType;
import yangxlei.github.io.app2.Test2Activity;

/**
 * Created by yanglei on 16/9/7.
 */
@ActionType("test2")
public class Test2Action implements BJAction.BJActionHandler {

    @Override
    public void doPerform(Context context, String type, Map<String, String> payload) {
        Test2Activity.launch(context);
    }
}
