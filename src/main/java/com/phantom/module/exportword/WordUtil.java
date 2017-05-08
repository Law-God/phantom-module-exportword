package com.phantom.module.exportword;

import freemarker.template.Configuration;
import freemarker.template.Template;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @Author 张志凯 https://github.com/Law-God/phantom-module-exportword
 * phantom-module-exportword
 * com.phantom.module.exportword.WordUtil
 * 2017-02-09 9:41
 */
public class WordUtil {
    public static ByteArrayOutputStream createWord(Map data,String basePath,String templateName,String imageName) throws Exception {
        //创建freemarker配置类
        Configuration configuration = new Configuration();
        //设置编码
        configuration.setDefaultEncoding("UTF-8");

        try {
            //ftl模板文件统一配置到ftl目录下
            configuration.setDirectoryForTemplateLoading(new File(basePath+"\\ftl"));
            //获取模板并设置模板编码
            Template template = configuration.getTemplate(templateName);
            template.setEncoding("UTF-8");

            if(imageName != null && !"".equals(imageName)){
                String imagePath = basePath + "/images/" + imageName;
                //将图片转换为
                InputStream in = null;
                byte[] bytes = null;
                try{
                    in = new FileInputStream(imagePath);
                    bytes = new byte[in.available()];
                    in.read(bytes);
                    in.close();
                }catch (Exception e){
                    throw new Exception(e);
                }finally {
                    if(in != null){
                        in.close();
                    }
                }
                //进行base64位编码
                BASE64Encoder encoder = new BASE64Encoder();
                //图片数据
                data.put("image",encoder.encode(bytes));
            }

           /* //输出文件
            File outFile = new File(basePath + File.separator + fileName);
            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }*/
            //将模板和数据模型合并生成文件
            // Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
            Writer out = new StringWriter();
            //生成文件
            template.process(data,out);
            String str = out.toString();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //需要增加utf-8编码，不然请求下载的文档为xml代码
            baos.write(str.getBytes("UTF-8"));
            baos.flush();
            //关闭流
            out.close();
            baos.close();
            return baos;

        } catch (IOException e) {
            throw new Exception();
        }
    }

    /**
     * 下载word
     * @param response
     * @param bytes
     * @param filename
     */
    public static void downloadWord(HttpServletResponse response, final byte[] bytes, final String filename){
        //设置让浏览器弹出下载对话框的Header
       setFileDownloadHeader(response,filename);
        if(bytes != null){
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setFileDownloadHeader(HttpServletResponse response,String filename){
        //中文文件名支持
        try {
            String encodedFileName = new String(filename.getBytes("GBK"),"ISO8859-1");
            //response.setContentType("application/msword");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

