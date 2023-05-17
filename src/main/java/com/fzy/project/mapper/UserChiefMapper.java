package com.fzy.project.mapper;

import com.fzy.project.model.entity.UserChief;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 徐小帅
* @description 针对表【user_chief(用户-岗位对应表)】的数据库操作Mapper
* @createDate 2023-04-18 21:57:09
* @Entity com.fzy.project.model.entity.UserChief
*/
public interface UserChiefMapper extends BaseMapper<UserChief> {

    public List<UserChief> queryUC(@Param("chiefName")String chiefName,Long companyId);

}




