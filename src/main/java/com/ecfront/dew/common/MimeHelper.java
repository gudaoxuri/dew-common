package com.ecfront.dew.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MimeHelper {

    private static Map<String, List<String>> types = new HashMap<String, List<String>>() {{
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

    public static final List<String> TYPE_OFFICE = types.get("office");
    public static final List<String> TYPE_TXT = types.get("txt");
    public static final List<String> TYPE_COMPRESS = types.get("compress");
    public static final List<String> TYPE_IMAGE = types.get("image");
    public static final List<String> TYPE_AUDIO = types.get("audio");
    public static final List<String> TYPE_VIDEO = types.get("video");

}
