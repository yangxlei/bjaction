package io.github.yangxlei.actionhandle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.yangxlei.action.BJActionUtil;

/**
 * Created by yanglei on 16/9/7.
 */
public class Test1Activity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent launcher = new Intent(context, Test1Activity.class);
        context.startActivity(launcher);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        findViewById(R.id.button_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BJActionUtil.sendToTarget(Test1Activity.this, BJActionUtil.buildActionUrl("test2", null));
            }
        });
    }
}
