package com.qf.CJDX_MANAGER.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author:wjp
 * @DATE：2022/7/15 8:51
 * @Description:
 * @Version1.8
 */

//用户实体类

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userCode;
    private String userName;
    private String userPassword;
    private Integer gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private  String phone;
    private  String address;
    private Integer userRole;
    private Integer createdBy;
    private Date creationDate;
    private Integer modifyBy;
    private Date modifyDate;
    private String userRoleName;
    private  Integer age;
    private String idPicPath;
    private String workPicPath;


}
