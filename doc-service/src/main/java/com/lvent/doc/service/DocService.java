package com.lvent.doc.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocService {
    Integer upload(MultipartFile multipartFile) throws IOException;
}
