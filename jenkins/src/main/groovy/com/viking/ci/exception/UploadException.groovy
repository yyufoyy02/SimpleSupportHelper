package com.viking.ci.exception

class UploadException extends RuntimeException {

    private static final String defaultInfo = '上传：'

    UploadException() {
        super(defaultInfo)
    }

    UploadException(String extra) {
        super(defaultInfo + extra)
    }
}
