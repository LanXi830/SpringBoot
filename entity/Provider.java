package com.qf.CJDX_MANAGER.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {

  private long id;
  private String proCode;
  private String proName;
  private String proDesc;
  private String proContact;
  private String proPhone;
  private String proAddress;
  private String proFax;
  private long createdBy;
  private Date creationDate;
  private Date modifyDate;
  private long modifyBy;
  private String companyLicPicPath;
  private String orgCodePicPath;


}
