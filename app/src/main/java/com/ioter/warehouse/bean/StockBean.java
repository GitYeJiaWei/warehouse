package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class StockBean implements Serializable{
        /**
         * AsnDetailId : 292ba7a9-d334-4b0e-8158-3e1f0c7cc31b
         * ProductId : 3eb29db1-9364-4d08-83eb-72a6924f8f53
         * ProductName : 7
         * PreQty : 1.0
         * StockQty : 0.0
         * Packing : 444
         * ListUom : [{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OL","Qty":1,"IsDefault":false},{"Uom":"IP","Qty":2,"IsDefault":false}]
         * PlanLoc : 1
         * ListLot : [{"Title":"属性1","Type":1,"Value":"3","ListOption":{"68":"平方米","86":"平方分米","87":"公顷","88":"5"},"ListData":null},{"Title":"属性2","Type":6,"Value":"4","ListOption":null,"ListData":null},{"Title":"生产日期","Type":2,"Value":"","ListOption":null,"ListData":null},{"Title":"属性3","Type":6,"Value":"5","ListOption":null,"ListData":null},{"Title":"入库日期","Type":2,"Value":"","ListOption":null,"ListData":null}]
         * ListEpc : ["epc1","epc2"]
         */

        private String AsnDetailId;
        private String ProductId;
        private String ProductName;
        private double PreQty;
        private double StockQty;
        private String Packing;
        private String PlanLoc;
        private List<PackingBean> ListUom;
        private List<ListLotBean> ListLot;
        private List<String> ListEpc;

        public String getAsnDetailId() {
            return AsnDetailId;
        }

        public void setAsnDetailId(String AsnDetailId) {
            this.AsnDetailId = AsnDetailId;
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

        public String getPacking() {
            return Packing;
        }

        public void setPacking(String Packing) {
            this.Packing = Packing;
        }

        public String getPlanLoc() {
            return PlanLoc;
        }

        public void setPlanLoc(String PlanLoc) {
            this.PlanLoc = PlanLoc;
        }

        public List<PackingBean> getListUom() {
            return ListUom;
        }

        public void setListUom(List<PackingBean> ListUom) {
            this.ListUom = ListUom;
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
