package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class GetStock implements Serializable {

    /**
     * LocId : 1
     * ProductId : 3eb29db1-9364-4d08-83eb-72a6924f8f53
     * ProductName : fashion衣服
     * StockQty : 3
     * Uom : EA
     * TrackCode : 111
     * DistributeQty : 2
     * AvailableQty : 1
     */

    private String LocId;
    private String ProductId;
    private String ProductName;
    private int StockQty;
    private String Uom;
    private String TrackCode;
    private int DistributeQty;
    private int AvailableQty;
    private String OwnerName;

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public int getStockQty() {
        return StockQty;
    }

    public void setStockQty(int StockQty) {
        this.StockQty = StockQty;
    }

    public String getUom() {
        return Uom;
    }

    public void setUom(String Uom) {
        this.Uom = Uom;
    }

    public String getTrackCode() {
        return TrackCode;
    }

    public void setTrackCode(String TrackCode) {
        this.TrackCode = TrackCode;
    }

    public int getDistributeQty() {
        return DistributeQty;
    }

    public void setDistributeQty(int DistributeQty) {
        this.DistributeQty = DistributeQty;
    }

    public int getAvailableQty() {
        return AvailableQty;
    }

    public void setAvailableQty(int AvailableQty) {
        this.AvailableQty = AvailableQty;
    }
}
