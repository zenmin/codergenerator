package com.hw.zm.codergenerator.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2018/7/7 15:06
 */
@Component
public class ConvertToUtf8 {

    private static  String SOURCE_ENCODING;
    private static  String TARGET_ENCODING;
    private static String SOURCE_DIR;
    private static String TARGET_DIR;

    /**
     * exchange the character encoding from srcDir to targetDir
     * 　　*
     * 　　* @param srcDir
     * 　　* @param targetDir
     *
     */
    public static void exchange(String source_encoding,String target_encoding,String srcDir,String target) {
        SOURCE_ENCODING = source_encoding;
        TARGET_ENCODING = target_encoding;
        SOURCE_DIR = srcDir;
        TARGET_DIR = target;
        String absPath = "";
        if (!srcDir.equals(SOURCE_DIR)) {
            absPath = srcDir.substring(SOURCE_DIR.length());
            String targetDir = TARGET_DIR + absPath;
            File targetDirectory = new File(targetDir);
            if (targetDirectory.isDirectory() && !targetDirectory.exists()) {
                targetDirectory.mkdirs();
            }
        }

        File sourceDirectory = new File(srcDir);
        if (sourceDirectory.exists()) {
            if (sourceDirectory.isFile()) {
                String targetFilePath = TARGET_DIR + absPath;
                try {
                    fileEncodingExchange(sourceDirectory, targetFilePath);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                File[] childs = sourceDirectory.listFiles();
                for (File child : childs)
                    exchange(source_encoding,target_encoding,child.getPath(),target);
            }
        }
    }

    private static void fileEncodingExchange(File infile, String targetAbsFilePath) throws IOException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        String tmpTargetPath = targetAbsFilePath.substring(0, targetAbsFilePath.lastIndexOf(File.separator));
        File tmpTargetDir = new File(tmpTargetPath);
        if (!tmpTargetDir.exists())
            tmpTargetDir.mkdirs();
        try {
            fin = new FileInputStream(infile);
            fout = new FileOutputStream(targetAbsFilePath);
            fcin = fin.getChannel();
            fcout = fout.getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (true) {
                buffer.clear();
                int r = fcin.read(buffer);
                if (r == -1) {
                    break;
                }
                buffer.flip();
                String encoding = SOURCE_ENCODING; //System.getProperty("file.encoding");
                fcout.write(ByteBuffer.wrap(Charset.forName(encoding).decode(buffer).toString().getBytes(TARGET_ENCODING)));
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (fcin != null) {
                fcin.close();
            }
            if (fout != null)
                fout.close();
            if (fcout != null)
                fcout.close();
        }
    }
}
