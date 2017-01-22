package com.ecfront.dew.common;

public enum Mime {
    HTML1("text/html"),
    HTML2("application/html"),
    XML1("text/xml"),
    XML2("application/xml"),
    TXT("text/plain"),
    JS1("application/javascript"),
    JS2("application/x-javascript"),
    CSS("text/css"),

    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLS1("application/x-xls"),
    XLS2("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT1("application/x-ppt"),
    PPT2("application/vnd.ms-powerpoint"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PDF("application/pdf"),

    ZIP1("application/zip"),
    ZIP2("application/x-zip-compressed"),
    ZIP3("application/x-compressed-zip"),
    GZIP("application/gzip"),
    SEVENZ1("application/x-7z-compressed"),
    SEVENZ2("application/octet-stream"),
    RAR1("application/rar"),
    RAR2("application/x-rar-compressed"),

    GIF("image/gif"),
    JPG1("image/jpeg"),
    JPG2("image/pjpeg"),
    PNG("image/png"),
    BMP1("application/x-bmp"),
    BMP2("image/bmp"),

    MP3("audio/mp3"),
    WAV1("audio/wav"),
    WAV2("audio/x-wav"),
    WMA("audio/x-ms-wma"),

    MP4("video/mpeg4"),
    MOV("video/quicktime"),
    AVI1("video/avi"),
    AVI2("video/x-msvideo"),
    AVI3("video/msvideo"),
    MOVIE("video/x-sgi-movie"),
    WEBM("audio/webm"),
    RM("audio/x-pn-realaudio"),
    RMVB("application/vnd.rn-realmedia-vbr");

    private String flag;
    Mime(String flag){
        this.flag=flag;
    }

    @Override
    public String toString() {
        return this.flag;
    }
}
