package com.hw.zm.codergenerator.components;

import com.hw.zm.codergenerator.util.FreeMarkerTemplateUtil;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Map;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2018/7/7 21:27
 */
@Service
public class GenActionAndService {

    @Value("${REALPATH}")
    private String REALPATH;

    private String CHINESETABLENAME;
    private String beanName;

    public String genAll(Map<String,Object> map) throws Exception {
        REALPATH = REALPATH + "输出目录\\";
        beanName = map.get("title").toString();
        File beanDir =  new File(REALPATH);
        if(!beanDir.exists()){
            beanDir.mkdirs();
        }
        this.genService(map);
        this.genAction(map);
        this.genBeansXML(map);
        this.genHTML(map);
        this.genJS(map);
        return "true";
    }

    public void genAction(Map<String,Object> map) throws Exception {
        File beanFile = new File(REALPATH + beanName+"Action.java");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        this.generateFileByTemplate("Action.ftl",beanFile,map);
        System.out.println("生成Action成功，文件："+ beanFile);
    }

    public void genService(Map<String,Object> map) throws Exception {
        File beanFile = new File(REALPATH + beanName+"Service.java");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        this.generateFileByTemplate("Service.ftl",beanFile,map);
        System.out.println("生成Service成功，文件："+ beanFile);

    }

    public void genBeansXML(Map<String,Object> map) throws Exception {
        File beanFile = new File(REALPATH +"beans-manager.xml");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        this.generateFileByTemplate("Beans.ftl",beanFile,map);
        System.out.println("生成Beans.xml成功，文件："+ beanFile);
    }

    public void genHTML(Map<String,Object> map) throws Exception {
        File beanFile = new File(REALPATH + map.get("title") + ".html");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        this.generateFileByTemplate("HTML.ftl",beanFile,map);
        System.out.println("生成HTML成功，文件："+ beanFile);
    }

    public void genJS(Map<String,Object> map) throws Exception {
        File beanFile = new File(REALPATH + map.get("title") + ".js");
        if(!beanFile.exists()){
            beanFile.createNewFile();
        }
        this.generateFileByTemplate("JS.ftl",beanFile,map);
        System.out.println("生成JS成功，文件："+ beanFile);
    }

    private void generateFileByTemplate(final String templateName, File file, Map<String,Object> dataMap) throws Exception{
        Template template = FreeMarkerTemplateUtil.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"),10240);
        template.process(dataMap,out);
    }
}
