package io.clickhandler.gwtLumberjack.client;

import io.clickhandler.logglyGwt.client.Loggly;

public class Logger {
    private static Level LOG_LEVEL = Level.ERROR;
    private static boolean LOGGLY_ENABLED = false;

    public static void setLogLevel(Level logLevel) {
        if (logLevel == null) {
            logLevel = Level.ERROR;
        }
        LOG_LEVEL = logLevel;
    }

    public static void setLogglyEnabled(boolean logglyEnabled) {
        LOGGLY_ENABLED = logglyEnabled;
    }

    public enum Level {
        TRACE(4, "trace"),
        DEBUG(3, "debug"),
        INFO(2, "info"),
        WARN(1, "warn"),
        ERROR(0, "error");

        private String level;
        private int value;

        Level(int value, String level) {
            this.value = value;
            this.level = level;
        }

        public int getValue() {
            return value;
        }

        public String getLevel() {
            return level;
        }
    }

    /*
     * Logger Instance
     */

    private String clazzName;

    public static Logger get(Class clazz) {
        return new Logger(clazz);
    }

    public Logger(Class clazz) {
        this.clazzName = clazz != null ? clazz.getSimpleName() : "Null Class";
    }

    private void log(Level level, String message, Object... data) {
        if (level == null || LOG_LEVEL.getValue() < level.getValue()) {
            return;
        }

        logToBrowserConsole(level.toString(), clazzName, message, data);

        if (LOGGLY_ENABLED) {
            Loggly.push(level.toString(), clazzName + ": " + message, data);
        }
    }

    private static native void logToBrowserConsole(String level, String clazzName, String message, Object... data) /*-{
        if (level === 'trace') {
            console.trace("[" + level.toUpperCase() + "] " + clazzName + ": " + message, data);
        } else if (level === 'debug') {
            console.debug("[" + level.toUpperCase() + "] " + clazzName + ": " + message, data);
        } else if (level === 'info') {
            console.info("[" + level.toUpperCase() + "] " + clazzName + ": " + message, data);
        } else if (level === 'warn') {
            console.warn("[" + level.toUpperCase() + "] " + clazzName + ": " + message, data);
        } else {
            console.error("[" + level.toUpperCase() + "] " + clazzName + ": " + message, data);
        }
    }-*/;

    // Logging Methods

    public void trace(String msg, Object... data) {
        log(Level.TRACE, msg, data);
    }

    public void debug(String msg, Object... data) {
        log(Level.DEBUG, msg, data);
    }

    public void info(String msg, Object... data) {
        log(Level.INFO, msg, data);
    }

    public void warn(String msg, Object... data) {
        log(Level.WARN, msg, data);
    }

    public void error(String msg, Object... data) {
        log(Level.ERROR, msg, data);
    }

    public void error(Throwable exception) {
        log(Level.ERROR, null, exception);
    }
}
