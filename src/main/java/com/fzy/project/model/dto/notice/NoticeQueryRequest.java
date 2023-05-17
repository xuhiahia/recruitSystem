package com.fzy.project.model.dto.notice;


import lombok.Data;

@Data
public class NoticeQueryRequest {

    private String noticeTitle;

    private String adminId;
}
