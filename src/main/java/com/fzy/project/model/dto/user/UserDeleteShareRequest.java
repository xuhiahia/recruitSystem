package com.fzy.project.model.dto.user;

import lombok.Data;

@Data
public class UserDeleteShareRequest {
    private Long blogId;

    private Long userId;
}
