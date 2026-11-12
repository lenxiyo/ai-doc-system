package com.lvent.doc.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocPO {
    private Integer id;
    private Integer userId;
    private String fileName;
    private String content;
    private LocalDateTime createTime;
}
