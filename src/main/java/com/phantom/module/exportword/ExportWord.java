package com.phantom.module.exportword;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 张志凯 https://github.com/Law-God/phantom-module-exportword
 * phantom-module-exportword
 * com.phantom.module.exportword.ExportWord
 * 2017-02-09 9:28
 */
@Controller
public class ExportWord {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/exportWord")
    public void exportWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> data = new HashMap<String, String>();
        data.put("title","个人简历");
        String basePath = request.getSession().getServletContext().getRealPath("/");//绝对路径;
        ByteArrayOutputStream baos = WordUtil.createWord(data,basePath,"test.ftl","qjx.jpg");
        WordUtil.downloadWord(response,baos.toByteArray(),"测 . 试.docx");
        //return "";
    }

}
