package com.fzy.project.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminBlogVO {

    private Long id;

    private String userName;

    private String userAvatar;

    private String blogTitle;

    private String blogContent;

    private List<String> blogImages;

    private String blogType;

    private String blogStatus;


}
