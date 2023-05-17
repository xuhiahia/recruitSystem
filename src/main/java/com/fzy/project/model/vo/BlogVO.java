package com.fzy.project.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class BlogVO {

    private Long id;

    private Long userId;

    private String userAvatar;

    private String userName;

    private String blogTitle;

    private String blogContent;

    private List<String> blogImages;

    private Integer blogCollections;

    private Integer blogComments;

    private Integer blogLikes;

    private Integer blogShares;

    private String blogType;

    private Boolean isLiked;

    private Boolean isCollected;
}
