package com.fzy.project.model.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserCheckChiefRequest {

    private String chiefAddress;

    private String chiefSalary;

    private String chiefName;

    private String companyName;
}
