package io.github.yangxlei.action;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yanglei on 15/11/18.
 *
 * 简单的异步执行分发
 */
class DispatchAsync {

    public interface DispatchRunnable {
        /**
         * 后台执行操作
         */
        void runInBackground();

        /**
         * 主线程执行操作
         */
        void runInMain();
    }

    private static Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(1);

    public static void dispatchAsync(DispatchRunnable runnable) {
        new DispatchAsyncTask().executeOnExecutor(DEFAULT_EXECUTOR, runnable);
    }

    private final static class DispatchAsyncTask extends BJAsyncTask<DispatchRunnable, Void, DispatchRunnable> {

        @Override
        protected DispatchRunnable doInBackground(DispatchRunnable... params) {
            if (params == null || params.length == 0)
                return null;
            DispatchRunnable runnable = params[0];

            runnable.runInBackground();

            return runnable;
        }

        @Override
        protected void onPostExecute(DispatchRunnable disPatchRunnable) {
            if (disPatchRunnable != null) {
                disPatchRunnable.runInMain();
            }
        }
    }
}
