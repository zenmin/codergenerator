package com.hw.zm.codergenerator;

import com.hw.zm.codergenerator.main.MainHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertySourceFactory;

import javax.annotation.PostConstruct;

/**
 * @Describle 和为代码逆向工程
 * @Author ZengMin
 * @Date 2018/7/7 14:10
 */
@SpringBootApplication
@PropertySource(value = {"file:config.properties"})
//@PropertySource(value = {"classpath:config.properties"})
public class CodergeneratorApplication {
    @Value("${debug}")
    private static String debug;
    @Autowired
    ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException {
        try{
            SpringApplication.run(CodergeneratorApplication.class, args);
        }catch (Exception e){
            if(debug.equals("true"))
            e.printStackTrace();
            System.out.println("请检查你的配置，路径不要带中文！SQL.txt文件请规范！");
            Thread.sleep(300000);
        }
        System.out.println("\n***********所有任务执行成功*********");
        System.out.println("\n********请连续按两次Ctrl+C结束程序*********");
        System.out.println("\n");
        Thread.sleep(600000);
    }

    @PostConstruct
    public void getValue() throws Exception {
        MainHandle mainHandle = (MainHandle) applicationContext.getBean("mainHandle");
        mainHandle.genAll();    //生成实体类
    }

}
