package com.viking.ci.exception

class NotifyException extends RuntimeException {

    private static final String defaultInfo = 'DinkTalk 通知失败'

    NotifyException() {
        super(defaultInfo)
    }

    NotifyException(String extra) {
        super(defaultInfo + extra)
    }
}
