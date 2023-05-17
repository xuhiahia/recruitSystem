package com.fzy.project.model.dto.notice;


import lombok.Data;

@Data
public class NoticeAddRequest {

    private Long adminId;

    private String noticeContent;

    private String noticeTitle;

}
