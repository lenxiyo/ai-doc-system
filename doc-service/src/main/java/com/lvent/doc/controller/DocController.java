package com.lvent.doc.controller;

import com.lvent.common.Result;
import com.lvent.doc.service.DocService;
import com.lvent.doc.utils.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/doc")
public class DocController {

    // 如果需要Spring自动装配的类没有实现类那么自动装配时会找不到
    private final DocService docService;
    private final RabbitTemplate rabbitTemplate;

    @RequestMapping("/upload")
    public Result upload(MultipartFile multipartFile) throws Exception {
        if (multipartFile.isEmpty()){
            throw new Exception("文件为空");
        }
        Integer upload = docService.upload(multipartFile);
        return upload == 1 ? Result.success() : Result.error();

        // String originName = "zupload\\" + multipartFile.getOriginalFilename();
        // File file = new File("E:\\Software\\Projects\\ai-doc-system\\"+originName);
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

    @GetMapping("/testRabbitMQ")
    public void testRabbitMQ(){
        rabbitTemplate.convertAndSend("simple.queue","Hello RabbitMQ!");
    }

}
