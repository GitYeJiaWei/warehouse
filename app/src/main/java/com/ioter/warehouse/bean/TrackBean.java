package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class TrackBean implements Serializable {

    /**
     * TaskId : 16919d45-057f-45da-9214-bbdbb7e80a22
     * ProductId : 3eb29db1-9364-4d08-83eb-72a6924f8f53
     * ProductName : fashion衣服
     * ShelfQty : 9.0
     * ListUom : [{"Uom":"EA","Qty":9,"IsDefault":true},{"Uom":"OT","Qty":9,"IsDefault":false},{"Uom":"PL","Qty":9,"IsDefault":false},{"Uom":"EA","Qty":9,"IsDefault":true},{"Uom":"OT","Qty":9,"IsDefault":false},{"Uom":"PL","Qty":9,"IsDefault":false}]
     * AlreadyQty : 0.0
     * RecommendLoc : null
     */

    private String TaskId;
    private String ProductId;
    private String ProductName;
    private double ShelfQty;
    private String Packing;
    private double AlreadyQty;
    private String RecommendLoc;
    private List<ListUomBean> ListUom;

    public String getPacking() {
        return Packing;
    }

    public void setPacking(String packing) {
        Packing = packing;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String TaskId) {
        this.TaskId = TaskId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public double getShelfQty() {
        return ShelfQty;
    }

    public void setShelfQty(double ShelfQty) {
        this.ShelfQty = ShelfQty;
    }

    public double getAlreadyQty() {
        return AlreadyQty;
    }

    public void setAlreadyQty(double AlreadyQty) {
        this.AlreadyQty = AlreadyQty;
    }

    public String getRecommendLoc() {
        return RecommendLoc;
    }

    public void setRecommendLoc(String recommendLoc) {
        RecommendLoc = recommendLoc;
    }

    public List<ListUomBean> getListUom() {
        return ListUom;
    }

    public void setListUom(List<ListUomBean> ListUom) {
        this.ListUom = ListUom;
    }


}
