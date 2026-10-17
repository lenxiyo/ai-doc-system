package com.lvent.doc.controller;

import com.lvent.common.Result;
import com.lvent.doc.utils.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/doc")
public class DocController {

    @RequestMapping("/upload")
    public Result upload(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()){
            throw new Exception("文件为空");
        }
        String originName = "zupload\\" + multipartFile.getOriginalFilename();

        multipartFile.transferTo(new File("E:\\Software\\Projects\\ai-doc-system\\"+originName));
        return Result.success();
    }

    @PostMapping("/extract")
    public Result extractText(MultipartFile file) throws Exception {
        if (file.isEmpty()){
            throw new Exception("文件为空");
        }
        File text = new File("E:\\Software\\Projects\\ai-doc-system\\zpdf" + file.getOriginalFilename());
        file.transferTo(text);
        // PdfUtil.extractText(text);
        PdfUtil.textSplitter(text);
        return Result.success();
    }

}
