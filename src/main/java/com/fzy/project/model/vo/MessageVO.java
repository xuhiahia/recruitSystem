package com.fzy.project.model.vo;


import lombok.Data;

@Data
public class MessageVO {

    private String sendName;

    private String receiveName;

    private String sendAvatar;

    private String receiveAvatar;

//    private Boolean isMy=false;

    private String chatContent;

    private String createTime;

    private Long id;


}
