package io.github.yangxlei.actionhandle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yanglei on 16/9/7.
 */
public class Test3Activity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent launcher = new Intent(context, Test3Activity.class);
        context.startActivity(launcher);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
    }
}
