package io.github.yangxlei.actionhandle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.yangxlei.action.BJActionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BJActionUtil.sendToTarget(MainActivity.this,
                        BJActionUtil.buildActionUrl("test1", null));
            }
        });
    }
}
