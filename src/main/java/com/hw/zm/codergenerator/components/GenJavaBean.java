package com.hw.zm.codergenerator.components;

import com.hw.zm.codergenerator.util.ConvertToUtf8;
import com.hw.zm.codergenerator.util.FileUtil;
import com.hw.zm.codergenerator.util.FreeMarkerTemplateUtil;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Describle This Class Is 生成JavaBean
 * @Author ZengMin
 * @Date 2018/7/7 14:10
 */
@Service
public class GenJavaBean {
    @Value("${AUTHOR}")
    private String AUTHOR;
    @Value("${COMPANY}")
    private String COMPANY;
    @Value("${REALPATH}")
    private String REALPATH;
    @Value("${FILENAME}")
    private String FILENAME;
    private Map<String,Object> beanMsg;

    static Logger m_logger = LoggerFactory.getLogger(FileUtil.class);

    private static String BEANNAME;
    private static String CHINESETABLENAME;
    private static List<Map<String, String>> lastList;

    public Map<String, Object> GenBean() throws Exception {
        String realPath = REALPATH;
        String path = REALPATH + FILENAME;
        String s = this.readFile(path).toLowerCase();
        s = s.replaceAll("\t"," ");
        String tableName = s.substring(s.indexOf("table") + 6, s.indexOf("(")).trim();
        if(tableName.trim().indexOf(";")!=-1){
            tableName = tableName.substring(0,tableName.trim().indexOf(";"));
        }
        String chineseTablename = s.substring(s.indexOf("--")+2,s.indexOf("create"));
        if(chineseTablename.indexOf("drop")!=-1){
            chineseTablename = chineseTablename.substring(0,chineseTablename.trim().indexOf("drop"));
        }
        if (tableName.indexOf(".") != -1) {
            tableName = tableName.substring(tableName.indexOf(".")+1,tableName.length());
        }
        String beanName = tableName.substring(0,1).toUpperCase()+tableName.substring(1,tableName.length());
        BEANNAME = beanName;
        CHINESETABLENAME = chineseTablename;

        File beanDir =  new File(realPath + "输出目录\\");
        if(!beanDir.exists()){
            beanDir.mkdirs();
        }
        File beanFile = new File(realPath + "输出目录\\" + beanName+".java");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        //取所有字段
        List<Map<String,String>> list = new ArrayList<>();
        String temps = s.substring(s.indexOf("(")+1,s.lastIndexOf(")"));
        temps = temps.trim();
        String[] split = temps.split("--");
        for (int i=0;i<split.length;i++){
            Map<String,String> alone = new HashMap<>();
            String t = split[i];
            if(i == split.length-1){
                break;
            }
            String n = split[i+1];
            t = t.trim();
            if(i==0){
                String en =t.substring(0,t.indexOf(" "));
                alone.put("en",en);
                alone.put("upper",en.substring(0,1).toUpperCase() + en.substring(1,en.length()));
                int index2 = n.indexOf('\t');
                if(index2 == -1){
                    index2 = n.indexOf(' ');
                }
                alone.put("zh",n.substring(0,index2));
            }else{
                int index1 = t.indexOf('\t');
                if(index1 == -1){
                    index1 = t.indexOf(' ');
                }
                t = t.substring(index1,t.length());
                t = t.trim();
                String en = t.substring(0,t.indexOf(" "));
                alone.put("en",en);
                alone.put("upper",en.substring(0,1).toUpperCase() + en.substring(1,en.length()));
                int index2 = n.indexOf('\t');
                if(index2 == -1){
                    index2 = n.indexOf(' ');
                }
                alone.put("zh",n.substring(0,index2));
            }
            list.add(alone);
        }
        lastList = list;
        //执行生成
        Map<String,Object> map = new HashMap<>() ;
        this.generateFileByTemplate("JavaBean.ftl",beanFile,map);
        System.out.println("生成POJO成功：" + realPath + "输出目录\\" + beanName+".java");

        //删除临时文件
        File tempdel = new File(REALPATH + FILENAME + "2");
        tempdel.deleteOnExit();
        return beanMsg;
    }
    @PreDestroy
    public void dostory(){
        //删除临时文件
        File tempdel = new File(REALPATH + FILENAME + "2");
        tempdel.deleteOnExit();
    }
    public String readFile(String path) throws IOException {
        //取sql.txt文件
        String temp = REALPATH + FILENAME;
        String nowsql = temp + "2";
        File file = new File(temp);
        FileUtil fileUtil = new FileUtil();
        ConvertToUtf8 convertToUtf8 = new ConvertToUtf8();
        //取文件编码
        String fileCoding = fileUtil.getFileCoding(file);
        //gbk转utf-8
        convertToUtf8.exchange(fileCoding, "UTF-8", temp, nowsql);  //执行转码
        String s = fileUtil.readFile(nowsql);
        return s;
    }

    private void generateFileByTemplate(final String templateName,File file,Map<String,Object> dataMap) throws Exception{
        Template template = FreeMarkerTemplateUtil.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataMap.put("describle",CHINESETABLENAME);
        dataMap.put("chineseTablename",CHINESETABLENAME);
        dataMap.put("author",AUTHOR);
        dataMap.put("company",COMPANY);
        dataMap.put("title",BEANNAME);
        dataMap.put("lower",BEANNAME.toLowerCase());
        dataMap.put("date",format.format(date));
        dataMap.put("zds",lastList);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
        template.process(dataMap,out);
        beanMsg = dataMap;
    }
}
