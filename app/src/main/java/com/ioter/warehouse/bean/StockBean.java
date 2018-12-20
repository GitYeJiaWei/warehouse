package com.ioter.warehouse.bean;

import java.util.List;

public class StockBean {


    /**
     * Success : true
     * Message :
     * ResultObj : [{"StockInId":"ASN201812130002","LineNo":"00001","ProductId":"3eb29db1-9364-4d08-83eb-72a6924f8f53","ProductName":"7","PreQty":1,"StockQty":0,"Packing":[{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}],"PlanLoc":"1","ListLot":[{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"属性2","Type":6,"Value":"4","listOption":null,"listData":null},{"Title":"属性1","Type":1,"Value":"3","listOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"listData":null},{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"属性3","Type":6,"Value":"5","listOption":null,"listData":null},{"Title":"入库日期","Type":2,"Value":"","listOption":null,"listData":null}],"ListEpc":["epc1","epc2"]},{"StockInId":"ASN201812130002","LineNo":"00001","ProductId":"3eb29db1-9364-4d08-83eb-72a6924f8f53","ProductName":"7","PreQty":1,"StockQty":0,"Packing":[{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}],"PlanLoc":"1","ListLot":[{"Title":"属性2","Type":6,"Value":"","listOption":null,"listData":null},{"Title":"属性1","Type":1,"Value":"","listOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"listData":null},{"Title":"入库日期","Type":2,"Value":"2018-12-13","listOption":null,"listData":null},{"Title":"状态","Type":6,"Value":"","listOption":null,"listData":null},{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"属性3","Type":6,"Value":"3","listOption":null,"listData":null}],"ListEpc":["epc1","epc2"]},{"StockInId":"ASN201812130002","LineNo":"0101","ProductId":"3eb29db1-9364-4d08-83eb-72a6924f8f53","ProductName":"7","PreQty":2,"StockQty":0,"Packing":[{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}],"PlanLoc":"1","ListLot":[{"Title":"属性3","Type":6,"Value":"3","listOption":null,"listData":null},{"Title":"属性2","Type":6,"Value":"2","listOption":null,"listData":null},{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"状态","Type":6,"Value":"启用","listOption":null,"listData":null},{"Title":"属性1","Type":1,"Value":"111","listOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"listData":null},{"Title":"入库日期","Type":2,"Value":"2018-12-12","listOption":null,"listData":null}],"ListEpc":["epc1","epc2"]},{"StockInId":"ASN201812130002","LineNo":"0102","ProductId":"3eb29db1-9364-4d08-83eb-72a6924f8f53","ProductName":"7","PreQty":1,"StockQty":0,"Packing":[{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}],"PlanLoc":"1","ListLot":[{"Title":"属性3","Type":6,"Value":"","listOption":null,"listData":null},{"Title":"状态","Type":6,"Value":"","listOption":null,"listData":null},{"Title":"属性1","Type":1,"Value":"","listOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"listData":null},{"Title":"属性2","Type":6,"Value":"","listOption":null,"listData":null},{"Title":"生产日期","Type":2,"Value":"2018-12-20","listOption":null,"listData":null},{"Title":"入库日期","Type":2,"Value":"","listOption":null,"listData":null}],"ListEpc":["epc1","epc2"]}]
     */

    /**
     * StockInId : ASN201812130002
     * LineNo : 00001
     * ProductId : 3eb29db1-9364-4d08-83eb-72a6924f8f53
     * ProductName : 7
     * PreQty : 1.0
     * StockQty : 0.0
     * Packing : [{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}]
     * PlanLoc : 1
     * ListLot : [{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"属性2","Type":6,"Value":"4","listOption":null,"listData":null},{"Title":"属性1","Type":1,"Value":"3","listOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"listData":null},{"Title":"生产日期","Type":2,"Value":"","listOption":null,"listData":null},{"Title":"属性3","Type":6,"Value":"5","listOption":null,"listData":null},{"Title":"入库日期","Type":2,"Value":"","listOption":null,"listData":null}]
     * ListEpc : ["epc1","epc2"]
     */

    private String AsnDetailId;
    private String ProductId;
    private String ProductName;
    private double PreQty;
    private double StockQty;
    private String PlanLoc;
    private List<PackingBean> Packing;
    private List<ListLotBean> ListLot;
    private List<String> ListEpc;

    public String getAsnDetailId() {
        return AsnDetailId;
    }

    public void setAsnDetailId(String asnDetailId) {
        AsnDetailId = asnDetailId;
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

    public double getPreQty() {
        return PreQty;
    }

    public void setPreQty(double PreQty) {
        this.PreQty = PreQty;
    }

    public double getStockQty() {
        return StockQty;
    }

    public void setStockQty(double StockQty) {
        this.StockQty = StockQty;
    }

    public String getPlanLoc() {
        return PlanLoc;
    }

    public void setPlanLoc(String PlanLoc) {
        this.PlanLoc = PlanLoc;
    }

    public List<PackingBean> getPacking() {
        return Packing;
    }

    public void setPacking(List<PackingBean> Packing) {
        this.Packing = Packing;
    }

    public List<ListLotBean> getListLot() {
        return ListLot;
    }

    public void setListLot(List<ListLotBean> ListLot) {
        this.ListLot = ListLot;
    }

    public List<String> getListEpc() {
        return ListEpc;
    }

    public void setListEpc(List<String> ListEpc) {
        this.ListEpc = ListEpc;
    }

}
