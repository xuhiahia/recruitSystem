package com.fzy.project.model.dto.admin;

import lombok.Data;

@Data
public class AdminAddUserRequest {

    private String userPhone;

    private String userPassword;

    private String checkPassword;

    private String userName;

}
