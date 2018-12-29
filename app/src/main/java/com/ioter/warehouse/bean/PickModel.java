package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class PickModel implements Serializable {
    /**
     * TaskId : 1
     * SourceLoc : a
     * ProductId : 02783c4c-d595-44c7-a911-78ee7f77c36d
     * ProductName : WSKJ
     * PickQty : 2
     * ListUom : []
     */

    private String TaskId;
    private String SourceLoc;
    private String ProductId;
    private String ProductName;
    private String Packing;
    private int PickQty;
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

    public String getSourceLoc() {
        return SourceLoc;
    }

    public void setSourceLoc(String SourceLoc) {
        this.SourceLoc = SourceLoc;
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

    public int getPickQty() {
        return PickQty;
    }

    public void setPickQty(int PickQty) {
        this.PickQty = PickQty;
    }

    public List<ListUomBean> getListUom() {
        return ListUom;
    }

    public void setListUom(List<ListUomBean> listUom) {
        ListUom = listUom;
    }
}
