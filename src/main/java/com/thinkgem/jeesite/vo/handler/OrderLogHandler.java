package com.thinkgem.jeesite.vo.handler;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * Created by yangtao on 2017/9/28.
 */
public class OrderLogHandler {
    @ExcelField(title = "区域", type = 1, align = 2, sort = 1)
    private String areaName;
    @ExcelField(title = "柜子编号", type = 1, align = 2, sort = 2)
    private String cabinetNo;
    @ExcelField(title = "商品金额统计", type = 1, align = 2, sort = 3)
    private String totalPrice;
    @ExcelField(title = "商品数量统计", type = 1, align = 2, sort = 4)
    private String productNum;
    @ExcelField(title = "总金额", type = 1, align = 2, sort = 5)
    private String productTotalPrice;
    @ExcelField(title = "总数量", type = 1, align = 2, sort = 6)
    private String totalProductNum;

    //区域统计总金额和总数量
    @ExcelField(title = "总计数量", type = 1, align = 2, sort = 7)
    private String totalProductNums;
    @ExcelField(title = "总计金额", type = 1, align = 2, sort = 8)
    private String totalProductPrices;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getTotalProductNum() {
        return totalProductNum;
    }

    public void setTotalProductNum(String totalProductNum) {
        this.totalProductNum = totalProductNum;
    }

    public String getCabinetNo() {
        return cabinetNo;
    }

    public void setCabinetNo(String cabinetNo) {
        this.cabinetNo = cabinetNo;
    }

    public String getTotalProductNums() {
        return totalProductNums;
    }

    public void setTotalProductNums(String totalProductNums) {
        this.totalProductNums = totalProductNums;
    }

    public String getTotalProductPrices() {
        return totalProductPrices;
    }

    public void setTotalProductPrices(String totalProductPrices) {
        this.totalProductPrices = totalProductPrices;
    }
}
