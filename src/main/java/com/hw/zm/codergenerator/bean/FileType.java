package com.hw.zm.codergenerator.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件类型
 * 
 */
public class FileType {
	public static final List<String> FILETYPES = new ArrayList<String>(Arrays
			.asList(new String[] { "7z", "ai", "ain", "asp", "avi", "bin",
					"bmp", "cab", "cad", "cat", "cdr", "chm", "com", "css",
					"csv", "cur", "dat", "db", "dll", "doc", "docx", "dot",
					"dps", "dpt", "dwg", "dxf", "emf", "eml", "eps", "esp",
					"et", "ett", "exe", "fla", "flash", "gif", "hdd", "help",
					"html", "icl", "ico", "inf", "ini", "iso", "jpg", "js",
					"m3u", "max", "mdb", "mde", "mht", "mid", "mov", "mp3",
					"msi", "pdf", "php", "pl", "png", "pot", "ppt", "pptx",
					"psd", "pub", "flv", "ram", "rar", "reg", "rtf", "tif", "tiff",
					"torrent", "txt", "vbs", "vsd", "vss", "vst", "wav", "wmf",
					"wmv", "wps", "wpt", "xls", "xlsx", "xlt", "xml", "zip", "pak" }));
	
	public static final List<String> DOCTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"doc", "docx", "pdf", "ppt", "pptx", "odt", 
					"ods", "odp", "odg", "odf", "txt", "wpd", "wps", "wpt", "xls",
					"xlsx", "sxc", "sxi", "sxw", "sxd", "rtf", "wiki",
					"csv", "tsv", "svg", "stw"}));
	
	public static final List<String> PICTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"bmp", "gif", "ico", "jpg", "png" ,"jpeg"}));
	
	public static final List<String> VIDEOTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"avi", "mp4", "mpg", "3gp", "flv", "rm", 
					"rmvb", "wmv", "mov" }));

	public static final List<String> RADIOTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"mp3", "wav"}));
	
	public static final List<String> FLASHTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"swf"}));
	
	public static final List<String> CODETYPES = new ArrayList<String>(Arrays
			.asList(new String[] { "js", "jsp", "java", "cs", "aspx", "xml",
					"mht", "htm", "html", "xhtml", "css", "cpp", "sql", "ini",
					"vbs", "log", "config", "dat" }));
	
	public static final List<String> ZIPTYPES = new ArrayList<String>(Arrays
			.asList(new String[] {"zip", "rar", "tar"}));
	
}