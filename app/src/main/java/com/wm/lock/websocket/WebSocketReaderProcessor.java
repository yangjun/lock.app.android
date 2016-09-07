package com.wm.lock.websocket;

import android.os.Handler;
import android.os.Looper;

import com.wm.lock.core.logger.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WebSocketReaderProcessor {

    private ExecutorService mExecutorService;
    private Handler mHandler;

    private WebSocketReaderProcessor() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newFixedThreadPool(1);
    }

    private static class InstanceHolder {
        static final WebSocketReaderProcessor instance = new WebSocketReaderProcessor();
    }

    public static WebSocketReaderProcessor getInstance() {
        return InstanceHolder.instance;
    }

    public <T> void execute(final WebSocketReaderWork<T> work ) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final T result = work.execute();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            work.onSuccess(result);
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            work.onFail(e);
                        }
                    });
                }
            }
        });
    }

    public void stop() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
    }

    static abstract class WebSocketReaderWork<T> {

        public abstract T execute() throws Exception;

        public abstract void onSuccess(T result);

        public void onFail(Exception e) {
            Logger.p("fail to execute read task", e);
        }

    }

}
