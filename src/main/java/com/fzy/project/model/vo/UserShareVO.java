package com.fzy.project.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserShareVO extends BlogVO{

    private String blogTitle;

    private String blogUserAvatar;

    private String blogContent;

    private String createTime;

    private List<String> blogImages;
}
