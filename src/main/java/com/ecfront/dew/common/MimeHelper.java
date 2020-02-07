/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;

import com.ecfront.dew.common.exception.RTException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MIME信息操作.
 *
 * @author gudaoxuri
 */
public class MimeHelper {

    /**
     * Instantiates a new Mime helper.
     */
    MimeHelper() {
    }

    private static Map<String, List<String>> types = new HashMap<>() {
        {
            put("office", new ArrayList<>() {
                {
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
                }
            });
            put("txt", new ArrayList<>() {
                {
                    add(Mime.TXT.toString());
                }
            });
            put("compress", new ArrayList<>() {
                {
                    add(Mime.ZIP1.toString());
                    add(Mime.ZIP2.toString());
                    add(Mime.ZIP3.toString());
                    add(Mime.GZIP.toString());
                    add(Mime.SEVENZ1.toString());
                    add(Mime.SEVENZ2.toString());
                    add(Mime.RAR1.toString());
                    add(Mime.RAR2.toString());
                }
            });
            put("image", new ArrayList<>() {
                {
                    add(Mime.GIF.toString());
                    add(Mime.JPG1.toString());
                    add(Mime.JPG2.toString());
                    add(Mime.PNG.toString());
                    add(Mime.BMP1.toString());
                    add(Mime.BMP2.toString());
                }
            });
            put("audio", new ArrayList<>() {
                {
                    add(Mime.MP3.toString());
                    add(Mime.WAV1.toString());
                    add(Mime.WAV2.toString());
                    add(Mime.WMA.toString());
                }
            });
            put("video", new ArrayList<>() {
                {
                    add(Mime.MP4.toString());
                    add(Mime.MOV.toString());
                    add(Mime.MOVIE.toString());
                    add(Mime.WEBM.toString());
                    add(Mime.RM.toString());
                    add(Mime.RMVB.toString());
                    add(Mime.AVI1.toString());
                    add(Mime.AVI2.toString());
                    add(Mime.AVI3.toString());
                }
            });
        }
    };

    /**
     * The Type office.
     */
    public final List<String> typeOffice = types.get("office");
    /**
     * The Type txt.
     */
    public final List<String> typeTxt = types.get("txt");
    /**
     * The Type compress.
     */
    public final List<String> typeCompress = types.get("compress");
    /**
     * The Type image.
     */
    public final List<String> typeImage = types.get("image");
    /**
     * The Type audio.
     */
    public final List<String> typeAudio = types.get("audio");
    /**
     * The Type video.
     */
    public final List<String> typeVideo = types.get("video");

    /**
     * Gets content type.
     *
     * @param file the file
     * @return the content type
     */
    public String getContentType(File file) {
        String[] nameSplit = file.getName().split("\\.");
        if (nameSplit.length > 1 && mimeTypes.containsKey(nameSplit[nameSplit.length - 1].toLowerCase())) {
            return mimeTypes.get(nameSplit[nameSplit.length - 1].toLowerCase());
        }
        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new RTException(e);
        }
        return contentType;
    }

    private final Map<String, String> mimeTypes = new HashMap<>() {
        {
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
        }
    };

    /**
     * The enum Mime.
     */
    public enum Mime {
        /**
         * Html 1 mime.
         */
        HTML1("text/html"),
        /**
         * Html 2 mime.
         */
        HTML2("application/html"),
        /**
         * Xml 1 mime.
         */
        XML1("text/xml"),
        /**
         * Xml 2 mime.
         */
        XML2("application/xml"),
        /**
         * Txt mime.
         */
        TXT("text/plain"),
        /**
         * Js 1 mime.
         */
        JS1("application/javascript"),
        /**
         * Js 2 mime.
         */
        JS2("application/x-javascript"),
        /**
         * Css mime.
         */
        CSS("text/css"),

        /**
         * Doc mime.
         */
        DOC("application/msword"),
        /**
         * Docx mime.
         */
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        /**
         * Xls 1 mime.
         */
        XLS1("application/x-xls"),
        /**
         * Xls 2 mime.
         */
        XLS2("application/vnd.ms-excel"),
        /**
         * Xlsx mime.
         */
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        /**
         * Ppt 1 mime.
         */
        PPT1("application/x-ppt"),
        /**
         * Ppt 2 mime.
         */
        PPT2("application/vnd.ms-powerpoint"),
        /**
         * Pptx mime.
         */
        PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        /**
         * Pdf mime.
         */
        PDF("application/pdf"),

        /**
         * Zip 1 mime.
         */
        ZIP1("application/zip"),
        /**
         * Zip 2 mime.
         */
        ZIP2("application/x-zip-compressed"),
        /**
         * Zip 3 mime.
         */
        ZIP3("application/x-compressed-zip"),
        /**
         * Gzip mime.
         */
        GZIP("application/gzip"),
        /**
         * Sevenz 1 mime.
         */
        SEVENZ1("application/x-7z-compressed"),
        /**
         * Sevenz 2 mime.
         */
        SEVENZ2("application/octet-stream"),
        /**
         * Rar 1 mime.
         */
        RAR1("application/rar"),
        /**
         * Rar 2 mime.
         */
        RAR2("application/x-rar-compressed"),

        /**
         * Gif mime.
         */
        GIF("image/gif"),
        /**
         * Jpg 1 mime.
         */
        JPG1("image/jpeg"),
        /**
         * Jpg 2 mime.
         */
        JPG2("image/pjpeg"),
        /**
         * Png mime.
         */
        PNG("image/png"),
        /**
         * Bmp 1 mime.
         */
        BMP1("application/x-bmp"),
        /**
         * Bmp 2 mime.
         */
        BMP2("image/bmp"),

        /**
         * Mp 3 mime.
         */
        MP3("audio/mp3"),
        /**
         * Wav 1 mime.
         */
        WAV1("audio/wav"),
        /**
         * Wav 2 mime.
         */
        WAV2("audio/x-wav"),
        /**
         * Wma mime.
         */
        WMA("audio/x-ms-wma"),

        /**
         * Mp 4 mime.
         */
        MP4("video/mpeg4"),
        /**
         * Mov mime.
         */
        MOV("video/quicktime"),
        /**
         * Avi 1 mime.
         */
        AVI1("video/avi"),
        /**
         * Avi 2 mime.
         */
        AVI2("video/x-msvideo"),
        /**
         * Avi 3 mime.
         */
        AVI3("video/msvideo"),
        /**
         * Movie mime.
         */
        MOVIE("video/x-sgi-movie"),
        /**
         * Webm mime.
         */
        WEBM("audio/webm"),
        /**
         * Rm mime.
         */
        RM("audio/x-pn-realaudio"),
        /**
         * Rmvb mime.
         */
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
