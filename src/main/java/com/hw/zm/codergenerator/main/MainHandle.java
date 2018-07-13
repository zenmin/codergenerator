package com.hw.zm.codergenerator.main;

import com.hw.zm.codergenerator.components.GenActionAndService;
import com.hw.zm.codergenerator.components.GenJavaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Describle This Class Is
 * @Author ZengMin
 * @Date 2018/7/7 13:20
 */
@Repository
public class MainHandle{

    @Value("${AUTHOR}")
    private String  COMPONY;
    @Autowired
    GenJavaBean genJavaBean;
    @Autowired
    GenActionAndService genActionAndService;

    public String genAll() throws Exception {
        Map<String, Object> map = genJavaBean.GenBean();
        genActionAndService.genAll(map);
        return COMPONY;
    }



}
