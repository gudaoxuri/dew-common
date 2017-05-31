package com.ecfront.dew.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MIME信息操作
 */
public class MimeHelper {

    MimeHelper(){}

    private  static Map<String, List<String>> types = new HashMap<String, List<String>>() {{
        put("office", new ArrayList<String>() {{
            add(Mime.TXT.toString());
            add(Mime.DOC.toString());
            add(Mime.DOCX.toString());
            add(Mime.XLS1.toString());
            add(Mime.XLS2.toString());
            add(Mime.XLSX.toString());
            add(Mime.PPT1.toString());
            add(Mime.PPT2.toString());
            add(Mime.PPTX.toString());
            add(Mime.PDF.toString());
        }});
        put("txt", new ArrayList<String>() {{
            add(Mime.TXT.toString());
        }});
        put("compress", new ArrayList<String>() {{
            add(Mime.ZIP1.toString());
            add(Mime.ZIP2.toString());
            add(Mime.ZIP3.toString());
            add(Mime.GZIP.toString());
            add(Mime.SEVENZ1.toString());
            add(Mime.SEVENZ2.toString());
            add(Mime.RAR1.toString());
            add(Mime.RAR2.toString());
        }});
        put("image", new ArrayList<String>() {{
            add(Mime.GIF.toString());
            add(Mime.JPG1.toString());
            add(Mime.JPG2.toString());
            add(Mime.PNG.toString());
            add(Mime.BMP1.toString());
            add(Mime.BMP2.toString());
        }});
        put("audio", new ArrayList<String>() {{
            add(Mime.MP3.toString());
            add(Mime.WAV1.toString());
            add(Mime.WAV2.toString());
            add(Mime.WMA.toString());
        }});
        put("video", new ArrayList<String>() {{
            add(Mime.MP4.toString());
            add(Mime.MOV.toString());
            add(Mime.MOVIE.toString());
            add(Mime.WEBM.toString());
            add(Mime.RM.toString());
            add(Mime.RMVB.toString());
            add(Mime.AVI1.toString());
            add(Mime.AVI2.toString());
            add(Mime.AVI3.toString());
        }});
    }};

    public final List<String> TYPE_OFFICE = types.get("office");
    public final List<String> TYPE_TXT = types.get("txt");
    public final List<String> TYPE_COMPRESS = types.get("compress");
    public final List<String> TYPE_IMAGE = types.get("image");
    public final List<String> TYPE_AUDIO = types.get("audio");
    public final List<String> TYPE_VIDEO = types.get("video");

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

        Mime(String flag) {
            this.flag = flag;
        }

        @Override
        public String toString() {
            return this.flag;
        }
    }

}
