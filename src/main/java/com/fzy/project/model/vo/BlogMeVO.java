package com.fzy.project.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class BlogMeVO {
    private Long id;

    private String userAvatar;

    private String userName;

    private String blogContent;

    private String blogTitle;

    private List<String> blogImages;

    private Integer blogCollections;

    private Integer blogComments;

    private Integer blogLikes;

    private Integer blogShares;

    private String blogType;

    private String createTime;

}
