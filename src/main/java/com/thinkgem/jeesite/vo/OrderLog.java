package com.thinkgem.jeesite.vo;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 订单统计实体类
 */
public class OrderLog {
    private Integer id;

    private String orderNo; //订单号

    private String areaId;
    @ExcelField(title = "区域", type = 1, align = 2, sort = 1)
    private String areaName; //

    private String cabinetNo;

    private String cabinetName;

    private String productId;
    @ExcelField(title = "商品名称", type = 1, align = 2, sort = 2)
    private String productName;
    @ExcelField(title = "商品数量", type = 1, align = 2, sort = 3)
    private Integer productNum;

    private Integer submitOrderType;

    private Integer productPrice;
    @ExcelField(title = "商品实际金额", type = 1, align = 2, sort = 4)
    private Integer productActualPrice;

    private Date paymentTime;

    private Integer paymentType;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo == null ? null : cabinetNo.trim();
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName == null ? null : cabinetName.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductActualPrice() {
        return productActualPrice;
    }

    public void setProductActualPrice(Integer productActualPrice) {
        this.productActualPrice = productActualPrice;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSubmitOrderType() {
        return submitOrderType;
    }

    public void setSubmitOrderType(Integer submitOrderType) {
        this.submitOrderType = submitOrderType;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}