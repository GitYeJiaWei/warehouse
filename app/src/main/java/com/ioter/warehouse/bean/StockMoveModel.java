package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class StockMoveModel implements Serializable {

    /**
     * LocId : 66
     * ProductId : 3eb29db1-9364-4d08-83eb-72a6924f8f53
     * StockQty : 1
     * AvailQty : 0
     * ListUom : [{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OT","Qty":4,"IsDefault":false},{"Uom":"PL","Qty":2,"IsDefault":false}]
     */

    private String LocId;
    private String ProductId;
    private int StockQty;
    private int AvailQty;
    private List<ListUomBean> ListUom;

    public String getLocId() {
        return LocId;
    }

    public void setLocId(String LocId) {
        this.LocId = LocId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public int getStockQty() {
        return StockQty;
    }

    public void setStockQty(int StockQty) {
        this.StockQty = StockQty;
    }

    public int getAvailQty() {
        return AvailQty;
    }

    public void setAvailQty(int AvailQty) {
        this.AvailQty = AvailQty;
    }

    public List<ListUomBean> getListUom() {
        return ListUom;
    }

    public void setListUom(List<ListUomBean> ListUom) {
        this.ListUom = ListUom;
    }

}
