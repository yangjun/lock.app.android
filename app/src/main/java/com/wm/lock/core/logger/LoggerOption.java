package com.wm.lock.core.logger;

/**
 * Created by Administrator on 2016/4/14.
 */
public final class LoggerOption {

    private boolean debug;
    private DataProvider<String> userDataProvider;
    private ReportProvider ReportProvider;

    private LoggerOption(Builder builder) {
        userDataProvider = builder.userDataProvider;
        ReportProvider = builder.ReportProvider;
        debug = builder.debug;
    }

    public DataProvider<String> getUserDataProvider() {
        return userDataProvider;
    }

    public ReportProvider getReportProvider() {
        return ReportProvider;
    }

    public boolean isDebug() {
        return debug;
    }

    public static class Builder {

        private DataProvider<String> userDataProvider;
        private ReportProvider ReportProvider;

        private boolean debug;

        public Builder setUserDataProvider(DataProvider<String> userDataProvider) {
            this.userDataProvider = userDataProvider;
            return this;
        }

        public Builder setReportProvider(ReportProvider ReportProvider) {
            this.ReportProvider = ReportProvider;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public LoggerOption build() {
            return new LoggerOption(this);
        }
    }

    public static interface DataProvider<T> {
        public T getData();
    }

    public static interface ReportProvider {
        public void report(String description);
        public void report(String description, Throwable t);
    }

}
