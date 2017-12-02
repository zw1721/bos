package com.taotao.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.vo.PicUploadResult;
import com.taotao.manage.service.PropertieService;

@Controller
@RequestMapping("/pic/upload")
public class PicUploadController {

    private static final List<String> SUFFIXES = Arrays.asList(".jpg", ".png", ".bmp", ".jpeg");

    @Autowired
    private PropertieService propertieService;
    
    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public PicUploadResult upload(@RequestParam("uploadFile") MultipartFile file) {
        // 创建图片上传的结果对象
        PicUploadResult result = new PicUploadResult();
        // 初始化状态为1，代表失败
        result.setError(1);

        // 1）校验类型
        String filename = file.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(filename, ".");
        if (!SUFFIXES.contains("." + suffix)) {
            return result;
        }

        // 2）校验图片内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return result;
            }
        } catch (IOException e) {
            return result;
        }

        // 获取新的路径名
        String filePath = getFilePath(filename);// D:\\heima07\\taotao-upload\\images\\2017\\09\\16\\213131231.jpg
        // 把图片保存到本地
        try {
            file.transferTo(new File(filePath));
        } catch (IllegalStateException | IOException e) {
            return result;
        }

        // 生成图片的绝对引用地址（URL地址）
        String picUrl = StringUtils.replace(StringUtils.substringAfter(filePath, propertieService.REPOSITORY_PATH), "\\",
                        "/");
        // 将URL地址放到result对象中
        result.setUrl(propertieService.IMAGE_BASE_URL + picUrl);// http://image.taotao.com/images/2017/09/16/213131231.jpg

        // 设置状态为0
        result.setError(0);
        // 返回
        return result;
    }

    // 给上传的文件生成以年月日为目录的新路径
    private String getFilePath(String sourceFileName) {
        String baseFolder = propertieService.REPOSITORY_PATH + File.separator + "images";
        Date nowDate = new Date();
        // 根据年月日：生成yyyy/MM/dd的目录结构  2017/09/16
        String fileFolder = baseFolder + File.separator
                + new DateTime(nowDate).toString("yyyy" + File.separator + "MM" + File.separator + "dd");// yyyy/MM/dd
        File file = new File(fileFolder);
        // 如果目录不存在，则创建目录
        if (!file.exists()) {
            file.mkdirs();
        }
        // 生成新的文件名
        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
                + RandomUtils.nextInt(100, 9999) + "." + StringUtils.substringAfterLast(sourceFileName, ".");
        // 返回文件的全路径
        return fileFolder + File.separator + fileName;
    }
}
