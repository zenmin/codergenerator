package com.hw.zm.codergenerator.util;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Encoder;

/**
 * 文件辅助类
 */
@Component
public class FileUtil {
	
	/** 默认文件后缀名(系统生成文件后缀名，如解压缩文件等) */
	public static final String DEFAULT_FILE_SUFFIX = ".sysauto";
	
	/** size of buffer to use for byte[] operations - defaults to 1024 */
	protected static int BUFFER_SIZE = 1024 * 10;
	
	static Logger m_logger = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 格式化文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSzie = null;
		if (size < 1024) {
			fileSzie = df.format((double) size) + "byte";
		} else if (size < 1048576) {
			fileSzie = df.format((double) size / 1024) + "KB";
		} else if (size < 1073741824) {
			fileSzie = df.format((double) size / 1048576) + "MB";
		} else {
			fileSzie = df.format((double) size / 1073741824) + "GB";
		}
		return fileSzie;
	}
	
	/**
	 * 获取文件大小
	 * @param Path 文件路径
	 * @return
	 */
	public static long getFileSize(String Path) {
		File file = new File(Path);
		long size = 0;
		// 文件存在时
		if(file.exists()) {
			if(file.isFile()) { // 当前文件为单个文件时,直接返回当前文件大小
				size = file.length();
			} else { // 当前文件为文件夹时,统计文件夹及所有子级文件、文件夹大小
				size = getChildFileSize(file.listFiles(), size);
			}
		}
		return size;
	}
	
	/**
	 * 循环获取文件夹内所有文件、文件夹大小
	 * 
	 * @param childFiles
	 *            文件列表
	 * @param size
	 *            统计大小
	 * @return
	 */
	private static long getChildFileSize(File[] childFiles, long size) {
		for (int i = 0; i <childFiles.length; i++) {
			// 文件存在时统计
			if(childFiles[i].exists()) {
				// 当前文件为系统产出文件不统计
				if(childFiles[i].getName().endsWith(FileUtil.DEFAULT_FILE_SUFFIX)) {
					continue;
				}
				// 文件为单个文件时，直接累加当前文件大小
				if(childFiles[i].isFile()) {
					size += childFiles[i].length();
					continue;
				}
				// 当前为文件夹时统计子级文件大小
				size = getChildFileSize(childFiles[i].listFiles(), size);
			}
		}
		return size;
	}
	
	/**
	 *            jarPath E:\a.jar or /opt/a.jar
	 *            dirName 文件夹名称 (可以传空字符串得到整个jar包或者zip包大小)如：sources文件夹
	 *            fileName 文件名称 (可以传空字符串得到整个jar包或者zip包中指定dirName目录的大小)
	 * @return long 该工程总大小
	 * */
	public static long getFileSizeInJarOrZip(String jarPath, String dirName, String fileName) {
		long projectSize = 0;
		File currentArchive = new File(jarPath);
		ZipFile zf = null;
		try {
			zf = new ZipFile(currentArchive);
			int size = zf.size();
			Enumeration<?> entries = zf.entries();

			String product = dirName + "/" + fileName;
			for (int i = 0; i < size; i++) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.getName().startsWith(product)) {
					projectSize += entry.getSize();
				}
			}
			zf.close();
		} catch (Exception e) {
			System.out.println(e);
			if (zf != null) {
				try {
					zf.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return projectSize;
	}

	/**
	 * 读文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		StringBuilder b = new StringBuilder();
		String t;
		while ((t = br.readLine()) != null) {
			b.append(t);
		}
		return b.toString();
	}
	
	/**
	 * 读文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(File file) throws IOException {
		byte[] contents = {0};
		if(file == null || !file.exists())
			return contents;
		if(!file.isFile() || !file.canRead())
			return contents;
		FileInputStream fstream = new FileInputStream(file);
		contents = new byte[(int) file.length()];
		fstream.read(contents);
		fstream.close();
		return contents;
	}

	/**
	 * 读取二进制文件用Base64编码并以字符串格式返回
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readBinaryFile(File file) throws IOException {
		String contents = null;
		byte[] b = null;
		InputStream in = null;
		try {
		    in = new FileInputStream(file);
		    b = new byte[in.available()];
		    in.read(b);
		    in.close();
		    if(null != b)
		    {
		    	 BASE64Encoder encode = new BASE64Encoder();
		    	 contents = encode.encode(b);
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return contents;
	}
	
	/**
	 * 判断一个目录是否有写的权限
	 * @param path
	 * @return
	 */
	public static boolean canWritAble(String path){
		File f = new File(path.trim());
		if(!f.isDirectory() || !f.canWrite())
			return false;
		return true;
	}
	
	/**
	 * 判断一个文件是否有读的权限
	 * @param path
	 * @return
	 */
	public static boolean canReadAble(String path){
		File f = new File(path.trim());
		if(!f.isFile() || !f.canRead())
			return false;
		return true;
	}
	
	/**
	 * 判断一个文件是否有执行（运行）的权限
	 * @param path
	 * @return
	 */
	public static boolean canExecuteAble(String path){
		File f = new File(path.trim());
		if(!f.isFile() || !f.canExecute())
			return false;
		return true;
	}
	
	/**
	 * 写文件(带缓存区的写文件方式)
	 * @param //filePath
	 * 			文件路径，
	 * @param data
	 * 			源数据，字符串
	 * @return
	 */
	public static File writFile(File file, String data, String encode) throws IOException
	{
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try
        {
			file = FileUtil.makeDirFile(file);
			fos = new FileOutputStream(file);
			dos = new DataOutputStream(fos);
			byte[] bytes = data.getBytes(encode);
	        int length = bytes.length;
	        int pos = 0;
	        while (pos < length) {
	          int count = length - pos;
	          if (count > BUFFER_SIZE) {
	            count = BUFFER_SIZE;
	          }
	          dos.write(bytes, pos, count);
	          dos.flush();
	          pos += count;
	        }
        }finally
        {
        	if (null != dos) 
        		dos.close();
        }
		return file;
	}
	
	/**
	 * 写文件(带缓存区的写文件方式)
	 * @param //filePath
	 * 			文件路径，
	 * @param data
	 * 			源数据，字符串
	 * @return
	 */
	public static File writFile(File file, String data) throws IOException
	{
		return writFile(file, data, "UTF-8");
	}
	
	/**
	 * 写文件(带缓存区的写文件方式)
	 * @param filePath	
	 * 			文件路径，
	 * @param data
	 * 			源数据，字符串
	 * @return
	 */
	public static File writFile(String filePath, String data) throws IOException
	{
		File file = new File(filePath);
		return writFile(file, data);
	}
	
	/**
	 * 根据filePath创建相应的文件
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File makeFile(String filePath) throws IOException{
		File file = new File(filePath);
		return makeFile(file);
	}
	
	/**
	 * 根据file创建相应的文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File makeFile(File file) throws IOException{
		if(!file.exists())
		{
			file.createNewFile();
		}
		return file;
	}
	
	/**
	 * 根据filePath创建相应的目录和文件(带递归)
	 * @param //filePath
	 * @return
	 * @throws IOException
	 */
	public static File makeDirFile(File file) throws IOException{
		return makeDirFile(file, null);
	}
	
	/**
	 * 根据filePath创建相应的目录和文件(带递归)
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File makeDirFile(String filePath) throws IOException{
		File file = new File(filePath);
		return makeDirFile(file, null);
	}
	
	/**
	 * 根据filePath创建相应的目录和文件(带递归)
	 * @param //filePath
	 * @param isFile 
	 * 			如果要强制创建成文件，有些文件没有后缀名，但他是文件格式，而不是文件夹
	 * @return
	 * @throws IOException
	 */
	public static File makeDirFile(File file, String isFile) throws IOException{
		//首先递归创建目录
		if( file!=null ) { 
		   if( !file.exists() ) {
			   if( file.getParent()!=null ) {
			      File parentDir = new File(file.getParent());
			      if( !parentDir.exists() ) {
			    	  makeDirFile(parentDir, isFile);//递归
			      }
		    	}
		      if(file.getPath().indexOf(".") != -1 && !(file.getPath().endsWith("/") || file.getPath().endsWith("\\")))
		      {//创建文件如果有.且最后一个字符不是路径分隔符的,统一视为文件
		    	  file.createNewFile();
		      }else
		      {//默认创建目录
		    	  if(null != isFile && "true".equals(isFile.toLowerCase()))
		    	  {//如果要强制创建成文件，有些文件没有后缀名，但他是文件格式，而不是文件夹
		    		  file.createNewFile();
		    	  }else
		    	  {
		    		  file.mkdir();
		    	  }
		      }
		   }
		}
		return file;
	}
	
	/**
	 * 只创建目录(带递归)
	 * @param dir
	 */
	public static void makeDir(File dir) {
	  if( dir!=null ) { 
	    if( !dir.exists() ) {
	    	if( dir.getParent()!=null ) {
		      File parentDir = new File(dir.getParent());
		      if( !parentDir.exists() ) {
		      	makeDir(parentDir);
		      }
	    	}
	      dir.mkdir();
	    }
	  }
	}

	/**
	 * 删除文件或文件件
	 * @param filePath
	 */
	  public static void  deleteFileOrDir(String filePath) {
			try {
			    File file = new File(filePath);
			    if (file.exists()) {
			    	if(file.isFile()){
				    	file.delete();	
			    	}else if(file.isDirectory()){
			    		deleteFolder(filePath);
			    	}
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			}
	    }

	
	
	/**
     * 删除文件
     * @param //fileName 文件名称
     */
    public static void deleteFile(String filePath) {
		try {
		    File file = new File(filePath);
		    if (file.isFile() && file.exists()) {
			file.delete();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
    }

	/**
	 * 删除文件夹 param folderPath 文件夹完整绝对路径
	 */
	public static void deleteFolder(String folderPath) {
		try {
			deleteAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件 param path 文件夹完整绝对路径
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				deleteAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				deleteFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获取指定目录下的所有文件列表(无递归)
	  * @param path
	 *            文件或者文件夹路径
	 * @return
	 */
	public final static List<File> getFiles(String path) {
		File file = new File(path);
		List<File> list = new ArrayList<File>();
		return getFiles(file, list, false);
	}
	
	/**
	 * 获取指定目录下的所有文件列表(可带递归)
	 * @param path
	 *            文件或者文件夹路径
	 * @param isRecursion
	 *            是否需要递归，true表示要进行递归查找            
	 * @return
	 */
	public final static List<File> getFiles(String path, boolean isRecursion) {
		File file = new File(path);
		List<File> list = new ArrayList<File>();
		return getFiles(file, list, true);
	}
	
	/**
	 * 递归获取指定目录下的所有文件列表
	  * @param file
	 *            文件或者文件夹
	 * @param isRecursion
	 *            是否需要递归，true表示要进行递归查找                 
	 * @return
	 */
	private final static List<File> getFiles(File file, List<File> list, boolean isRecursion) {
		if(!file.exists())
			return list;
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if(null != childFiles)
				for (File childFile : childFiles) {
					if(!isRecursion)
					{//不递归查找
						list.add(childFile);
					}else
					{//递归查找
						if (childFile.isFile()) {
							list.add(childFile);
						} else {
							getFiles(childFile, list, isRecursion);//递归调用
						}
					}
				}
		} else
			list.add(file);
		return list;
	}
	
	/**
	 * 获取文件扩展名
	 * @param file
	 */
    public static String getExtName(File file) { 
    	return getExtName(file.getAbsolutePath());
    }
    
    /**
	 * 获取文件名（不带扩展名）
	 * @param //file
	 */
    public static String getFileName(String path) { 
    	File file = new File(path);
    	return file.getName();
    }
	
	/**
	 * 获取文件扩展名
	 * @param //fileName
	 */
    public static String getExtName(String fileName) { 
    	if(isDir(fileName))
    		return "dir";
        if ((fileName != null) && (fileName.length() > 0)) { 
            int dot = fileName.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (fileName.length() - 1))) { 
                return fileName.substring(dot + 1); 
            } 
        } 
        return fileName; 
    } 
    
    /**
     * 判断一个路径字符串是否是目录
     * @param //dir
     * @return
     */
    public static boolean isDir(String path) {
		if(path == null || path.length() == 0)
			return false;
		File f = new File(path);
		if(f.isDirectory())
			return true;
		return false;
	}
    
    /**
     * 判断一个路径字符串是否是文件
     * @param //dir
     * @return
     */
    public static boolean isFile(String path) {
		if(path == null || path.length() == 0)
			return false;
		File f = new File(path);
		if(f.isFile())
			return true;
		return false;
	}
    
    /**
     * 判断指定路径的文件是否存在
     * @param path
     * @return
     */
    public static boolean fileExists(String path) {
		if(path == null || path.length() == 0)
			return false;
		File f = new File(path);
		if(f.exists())
			return true;
		return false;
	}
    
    /**
     * 获取文件编码
     * @param file
     * @return "UTF-8" OR "GBK"
     */
    public static String getFileCoding(File file) {
    	byte[] data = new byte[BUFFER_SIZE];
    	FileInputStream input = null;
    	try{
	    	input = new FileInputStream(file);
			int len = 0;
			while ((len = input.read(data)) != -1) {
				if(len > 1000)
					break;//验证编码不用读取全部文件内容
			}
			input.close();
    	}catch (Exception e) {
    		close(input, null);
		}finally
		{
			close(input, null);
		}
    	
        return getByteCoding(data);
    }

    /**
     * 获取一个字节流的编码
     * @param data
     * @return
     */
	public static String getByteCoding(byte[] data) {
		int count_good_utf = 0;
        int count_bad_utf = 0;
        byte current_byte = 0x00;
        byte previous_byte = 0x00;
        for (int i = 1; i < data.length; i++) {
            current_byte = data[i];
            previous_byte = data[i - 1];
            if ((current_byte & 0xC0) == 0x80) {
                if ((previous_byte & 0xC0) == 0xC0) {
                    count_good_utf++;
                } else if ((previous_byte & 0x80) == 0x00) {
                    count_bad_utf++;
                }
            } else if ((previous_byte & 0xC0) == 0xC0) {
                count_bad_utf++;
            }
        }
        return (count_good_utf > count_bad_utf) ? "UTF-8" : "GBK";
	}
	
    /**
     * 拷贝文件从原始路径到目标路径
     * 
     * @param //fromPath
     *            源地址
     * @param //toPath
     *            现地址
     * @param //oldFileName
     *            老文件名
     * @param //newFileName
     *            新文件名
     * @return
     */
    public static Long copeFile(File fromFile, String toFilePath) {
		File file = null;
		InputStream in = null;
		OutputStream out = null;
		try {
		    //源文件
		    in = new BufferedInputStream(new FileInputStream(fromFile), BUFFER_SIZE);
		    
		    //目标文件
		    out = new BufferedOutputStream(new FileOutputStream(toFilePath), BUFFER_SIZE);
			
		    byte[] buffer = new byte[BUFFER_SIZE];
		    while (in.read(buffer) > 0) {
		    	out.write(buffer);
		    }
		    // 判断文件是否存在
		    file = new File(toFilePath);
		    if (file.isFile()) {
		    	return file.length();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		    System.out.println(e.getMessage());
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    close(in, out);
		}
		return 0L;
    }
    
    /**
     * 拷贝文件从原始路径到目标路径
     * 
     * @param fromPath
     *            源地址
     * @param toPath
     *            现地址
     * @param oldFileName
     *            老文件名
     * @param newFileName
     *            新文件名
     * @return
     */
    public static Long copeFile(String fromPath,
	    String toPath, String oldFileName, String newFileName) {
		File file = null;
		InputStream in = null;
		OutputStream out = null;
		try {
		    File filePath = new File(fromPath);
		    if (!filePath.exists()) { // 目标文件夹不存在,则创建文件夹
		    	filePath.mkdirs();
		    }
		    //源文件
		    in = new BufferedInputStream(new FileInputStream(
		    		new StringBuffer(fromPath).append(File.separator).append(oldFileName).toString()), BUFFER_SIZE);
		    
		    //目标文件
		    out = new BufferedOutputStream(
			    new FileOutputStream(new StringBuffer(toPath).append(
				    newFileName).toString()), BUFFER_SIZE);
			
		    byte[] buffer = new byte[BUFFER_SIZE];
		    while (in.read(buffer) > 0) {
		    	out.write(buffer);
		    }
		    // 判断文件是否存在
		    file = new File(new StringBuffer(toPath).append(newFileName).toString());
		    if (file.isFile()) {
		    	return file.length();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		    System.out.println(e.getMessage());
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    close(in, out);
		}
		return 0L;
    }
    
    /**
     * NIO方式拷贝文件从原始路径到目标路径
     * 
     * @param fromPath
     *            源地址
     * @param toPath
     *            现地址
     * @param oldFileName
     *            老文件名
     * @param newFileName
     *            新文件名
     * @return
     */
    public static Long copeFileNIO(String fromPath,
	    String toPath, String oldFileName, String newFileName) {
		try {
		    File filePath = new File(fromPath);
		    if (!filePath.exists()) { // 目标文件夹不存在,则创建文件夹
		    	filePath.mkdirs();
		    }
		    //源文件
		    File srcFile = new File(new StringBuffer(fromPath).append(oldFileName).toString());
		    //目标文件
		    File toFile = new File(new StringBuffer(toPath).append(newFileName).toString());
		    //开始拷贝文件
		    FileChannelCopy(srcFile.getAbsolutePath(), toFile.getAbsolutePath());
		    // 判断文件是否存在
		    if (toFile.isFile()) {
		    	return toFile.length();
		    }
		} catch (FileNotFoundException e) {
		    System.out.println(e.getMessage());
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return 0L;
    }
    
    /**
     * 调整图片文件的尺寸
     * @param srcImgPath 原图路径
     * @param newImgPath 新图路径
     * @param width 指定的宽
     * @param height 指定的高
     * @throws IOException
     */
    public static void resizeImage(String srcImgPath, String newImgPath,
    	    int width, int height) throws IOException {
    	File srcFile = new File(srcImgPath);
    	Image srcImg = ImageIO.read(srcFile);
    	BufferedImage buffImg = null;
    	buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	buffImg.getGraphics().drawImage(
    		srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
    		0, null);

    	ImageIO.write(buffImg, "gif", new File(newImgPath));
    }

    /**
     * 关闭文件输入输出流
     * @param in
     * @param out
     */
	public static void close(InputStream in, OutputStream out) {
		if (out != null) {
		try {
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		}
		if (in != null) {
		try {
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		}
	}
    
    /**
     * 搜索指定目录下，与通配符匹配的文件
     * @param dir
     * @param pattern \n通配符可以为：" "或者"."或者"*"
     * @return
     */
    public static List<String> searchFiles(String dir, String pattern){
		List<String> result = new ArrayList<String>();
		File d = new File(dir);
		if(!d.isDirectory() || null == pattern)
			return result;
		String[] files = d.list();
		pattern = pattern.replace(" ", "*");
		pattern = pattern.replace(".", "[.]");
		pattern = pattern.replace("*", "(.*)");
		Pattern r = Pattern.compile(pattern);
		for(int i = 0; i < files.length; i++){
			Matcher m = r.matcher(files[i]);
			if (m.matches( )) {
				result.add(dir + files[i]);
			}
		}
		return result;
	}

	/**
	 * 遍历文件全路径
	 * @param filePath 文件路径
	 * @return
	 */
	public static List<com.hw.zm.codergenerator.bean.File> getAllPath(String filePath) {
		List<com.hw.zm.codergenerator.bean.File> files = new ArrayList<com.hw.zm.codergenerator.bean.File>();
		File parentFile = new File(filePath).getParentFile();
		// 文件属于压缩文件
		if(filePath.indexOf(FileUtil.DEFAULT_FILE_SUFFIX) != -1) {
			List<File> tmpFiles = new ArrayList<File>();
			// 循环所有级文件名称
			while(true) {
				tmpFiles.add(0, parentFile);
				parentFile = parentFile.getParentFile();
				if(parentFile.getParentFile() == null) {
					break;
				}
			} 
			// 查询出解压缩物理路径层级
			int index = 0;
			for(File f : tmpFiles) {
				if(f.getName().indexOf(FileUtil.DEFAULT_FILE_SUFFIX) != -1) {
					break;
				}
				index ++;
			}
			// 从解压缩文件层级开始,选择解压缩文件的文件路径层级顺序
			for (; index < tmpFiles.size(); index++) {
				files.add(new com.hw.zm.codergenerator.bean.File(tmpFiles.get(index).getName().replaceAll(FileUtil.DEFAULT_FILE_SUFFIX, "")));
			}
		} else {
			files.add(0, new com.hw.zm.codergenerator.bean.File(parentFile.getName()));
		}
		return files;
	}	
	
	/**
     * nio拷贝
     * @param inFile  源文件
     * @param outFile 目标文件
     * @return
     * @throws Exception
     */
    public static void FileChannelCopy(String inFile,String outFile) throws FileNotFoundException, IOException
    {
        File in = new File(inFile);   
        File out = new File(outFile);
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try{
	        fin = new FileInputStream(in);
	        fout = new FileOutputStream(out);
	        FileChannel inc = fin.getChannel();
	        FileChannel outc = fout.getChannel();
	        int bufferLen = 2097152;//2m内存
	        ByteBuffer bb = ByteBuffer.allocateDirect(bufferLen);
	        while (true)
	        {
	            int ret = inc.read(bb);
	            if (ret == -1)
	            {      
	                fin.close();
	                fout.flush();
	                fout.close();
	                break;
	            }
	            bb.flip();
	            outc.write(bb);
	            bb.clear();
	        }
        }finally{
			close(fin, fout);
		}
    }
    /**
     * io拷贝
     * @param inFile 源文件
     * @param outFile 目标文件
     * @return
     * @throws Exception
     */
    public static void FileStraeamCopy(String inFile,String outFile)  throws FileNotFoundException, IOException
    {
        File in = new File(inFile);
        File out = new File(outFile);
        
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try{
	        fin=new FileInputStream(in);
	        fout=new FileOutputStream(out);
	     
	        int length=2097152;//2m内存
	        byte[] buffer=new byte[length];
	         
	        while(true)
	        {
	            int ins=fin.read(buffer);
	            if(ins==-1)
	            {
	                fin.close();
	                fout.flush();
	                fout.close();
	                break;
	                 
	            }else
	                fout.write(buffer,0,ins);
	        }
        }finally{
			close(fin, fout);
		}
    }
	/**
	 * 获取文件的上传路径
	 * @param sPrefixPath
	 * @return
	 */
	public static String getUploadFilePath(String sPrefixPath){
		String sSystem = System.getProperty("os.name");
		String tempDirectory=null;
		if(!new File(sPrefixPath+"/ROOT").exists()){
			tempDirectory = sPrefixPath+"/files/tempfile";
		}else{
			tempDirectory = sPrefixPath+"/ROOT/files/tempfile";//要加ROOT否则会和action的files冲突 
		}
		File dir = new File(tempDirectory);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return tempDirectory;
	}
	

	/**
	 * 下载HTTP文件
	 * @param localFilePath
	 * @param remoteFilePath
	 * @return
	 */
	public static boolean downloadHttpFile(String localFilePath,String remoteFilePath) {
		int bytesum = 0;  
		int byteread = 0;  
		URL url = null;  
		HttpURLConnection conn = null;
		String httpurl = remoteFilePath.replaceAll("\\\\", "/");
	    try {
//	    	remoteFilePath = URLEncoder.encode(remoteFilePath, "utf-8");
	        url = new URL(httpurl);  
	    	String localFileDir = localFilePath.substring(0, localFilePath.lastIndexOf("/"));
	    	File localFile = new File(localFileDir);
	    	if (!localFile.exists()) {
	    		localFile.mkdirs();
	    	}
	        conn = (HttpURLConnection)url.openConnection();
	        conn.connect();
	        if (conn.getResponseCode() >= 400){
	        	throw new Exception("远程文件下载错误_错误码"+conn.getResponseCode()+"错误内容"+conn.getResponseMessage());
	        }
	        System.out.println("当前转换文件:"+httpurl);
	        InputStream inStream = conn.getInputStream();  
	        FileOutputStream fs = new FileOutputStream(localFilePath);  
	  
	        byte[] buffer = new byte[1204];  
	        while ((byteread = inStream.read(buffer)) != -1) {  
               bytesum += byteread;
               fs.write(buffer, 0, byteread);  
	        }  
	        fs.close();
	        return true;  
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	        return false;  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	        return false;  
	    }catch (Exception e) {  
	        e.printStackTrace();  
	        return false;  
	    }
	}
	
}
