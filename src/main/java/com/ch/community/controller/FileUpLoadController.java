package com.ch.community.controller;


import com.ch.community.dto.FileDTO;
import com.ch.community.provider.UcloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUpLoadController {

    @Autowired
    private UcloudProvider ucloudProvider;

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO fileDTO(HttpServletRequest request){
        MultipartRequest multipartRequest= (MultipartRequest) request;
        MultipartFile file=multipartRequest.getFile("editormd-image-file");
        try {
            ucloudProvider.upload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String fileName = ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileName);
            return fileDTO;
        } catch (Exception e) {
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }
}
