package com.fzy.project.model.vo;


import lombok.Data;

@Data
public class NoticeVO {

    private Long id;

    private String noticeContent;

    private String noticeTitle;

    private String createTime;

    private String adminName;
}
