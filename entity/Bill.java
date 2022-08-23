package com.qf.CJDX_MANAGER.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

  private long id;
  private String billCode;
  private String productName;
  private String productDesc;
  private String productUnit;
  private double productCount;
  private double totalPrice;
  private long isPayment;
  private long createdBy;
  private Date creationDate;
  private long modifyBy;
  private Date modifyDate;
  private long providerId;
  private String providerName;



}
