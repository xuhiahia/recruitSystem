package com.fzy.project.model.dto.notice;


import lombok.Data;

import java.util.List;

@Data
public class NoticeDeleteRequest {

    private List<Long> ids;

}
