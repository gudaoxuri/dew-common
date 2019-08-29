package com.ecfront.dew.common;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MIME信息操作
 */
public class MimeHelper {

    MimeHelper() {
    }

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

    public final List<String> TYPE_OFFICE = types.get("office");
    public final List<String> TYPE_TXT = types.get("txt");
    public final List<String> TYPE_COMPRESS = types.get("compress");
    public final List<String> TYPE_IMAGE = types.get("image");
    public final List<String> TYPE_AUDIO = types.get("audio");
    public final List<String> TYPE_VIDEO = types.get("video");

    public String getContentType(File file) {
        String[] nameSplit = file.getName().split("\\.");
        if (nameSplit.length > 1 && mimeTypes.containsKey(nameSplit[nameSplit.length - 1].toLowerCase())) {
            return mimeTypes.get(nameSplit[nameSplit.length - 1].toLowerCase());
        }
        String content_type = null;
        try {
            content_type = Files.probeContentType(file.toPath());
        } catch (IOException ignore) {
        }
        if (content_type == null) {
            content_type = new MimetypesFileTypeMap().getContentType(file);
        }
        return content_type;
    }

    private final Map<String, String> mimeTypes = new HashMap<String, String>() {{
        put("kar", "audio/midi");
        put("mid", "audio/midi");
        put("midi", "audio/midi");

        put("aac", "audio/mp4");
        put("f4a", "audio/mp4");
        put("f4b", "audio/mp4");
        put("m4a", "audio/mp4");

        put("mp3", "audio/mpeg");
        put("oga", "audio/ogg");
        put("ogg", "audio/ogg");
        put("opus", "audio/ogg");

        put("ra", "audio/x-realaudio");
        put("wav", "audio/x-wav");

        put("bmp", "image/bmp");
        put("gif", "image/gif");
        put("jpeg", "image/jpeg");
        put("jpg", "image/jpeg");

        put("png", "image/png");
        put("svg", "image/svg+xml");
        put("svgz", "image/svg+xml");

        put("tif", "image/tiff");
        put("tiff", "image/tiff");
        put("wbmp", "image/vnd.wap.wbmp");
        put("webp", "image/webp");
        put("ico", "image/x-icon");
        put("cur", "image/x-icon");
        put("jng", "image/x-jng");

        put("js", "application/javascript");
        put("json", "application/json");

        put("webapp", "application/x-web-app-manifest+json");
        put("manifest", "text/cache-manifest");
        put("appcache", "text/cache-manifest");

        put("doc", "application/msword");
        put("xls", "application/vnd.ms-excel");
        put("ppt", "application/vnd.ms-powerpoint");
        put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        put("3gpp", "video/3gpp");
        put("3gp", "video/3gpp");

        put("mp4", "video/mp4");
        put("m4v", "video/mp4");
        put("f4v", "video/mp4");
        put("f4p", "video/mp4");

        put("mpeg", "video/mpeg");
        put("mpg", "video/mpeg");

        put("ogv", "video/ogg");
        put("mov", "video/quicktime");
        put("webm", "video/webm");
        put("flv", "video/x-flv");
        put("mng", "video/x-mng");
        put("asx", "video/x-ms-asf");
        put("asf", "video/x-ms-asf");

        put("wmv", "video/x-ms-wmv");
        put("avi", "video/x-msvideo");

        put("atom", "application/xml");
        put("rdf", "application/xml");
        put("rss", "application/xml");
        put("xml", "application/xml");
        put("yaml", "application/xml");
        put("yml", "application/xml");

        put("woff", "application/font-woff");
        put("woff2", "application/font-woff2");
        put("eot", "application/vnd.ms-fontobject");
        put("ttc", "application/x-font-ttf");
        put("ttf", "application/x-font-ttf");

        put("otf", "font/opentype");

        put("jar", "application/java-archive");
        put("war", "application/java-archive");
        put("ear", "application/java-archive");

        put("hqx", "application/mac-binhex40");
        put("pdf", "application/pdf");
        put("ps", "application/postscript");
        put("eps", "application/postscript");
        put("ai", "application/postscript");

        put("rtf", "application/rtf");
        put("wmlc", "application/vnd.wap.wmlc");
        put("xhtml", "application/xhtml+xml");
        put("kml", "application/vnd.google-earth.kml+xml");
        put("kmz", "application/vnd.google-earth.kmz");
        put("7z", "application/x-7z-compressed");
        put("crx", "application/x-chrome-extension");
        put("oex", "application/x-opera-extension");
        put("xpi", "application/x-xpinstall");
        put("cco", "application/x-cocoa");
        put("jardiff", "application/x-java-archive-diff");
        put("jnlp", "application/x-java-jnlp-file");
        put("run", "application/x-makeself");

        put("pl", "application/x-perl");
        put("pm", "application/x-perl");

        put("prc", "application/x-pilot");
        put("pdb", "application/x-pilot");

        put("rar", "application/x-rar-compressed");
        put("rpm", "application/x-redhat-package-manager");
        put("sea", "application/x-sea");
        put("swf", "application/x-shockwave-flash");
        put("sit", "application/x-stuffit");
        put("tcl", "application/x-tcl");
        put("tk", "application/x-tcl");

        put("der", "application/x-x509-ca-cert");
        put("pem", "application/x-x509-ca-cert");
        put("crt", "application/x-x509-ca-cert");

        put("torrent", "application/x-bittorrent");
        put("zip", "application/zip");

        put("bin", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("dll", "application/octet-stream");

        put("deb", "application/octet-stream");
        put("dmg", "application/octet-stream");
        put("iso", "application/octet-stream");
        put("img", "application/octet-stream");

        put("msi", "application/octet-stream");
        put("msp", "application/octet-stream");
        put("msm", "application/octet-stream");

        put("safariextz", "application/octet-stream");

        put("css", "text/css");
        put("html", "text/html");
        put("htm", "text/html");
        put("shtml", "text/html");

        put("mml", "text/mathml");
        put("txt", "text/plain");
        put("jad", "text/vnd.sun.j2me.app-descriptor");
        put("wml", "text/vnd.wap.wml");
        put("vtt", "text/vtt");
        put("htc", "text/x-component");
        put("vcf", "text/x-vcard");
    }};

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
