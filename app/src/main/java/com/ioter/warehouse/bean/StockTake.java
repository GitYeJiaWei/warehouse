package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class StockTake implements Serializable {

        /**
         * TaskId : 1111111
         * Loc : a
         * ProductId : 01b2cd33-4703-42e9-8b3b-c898b79912ed
         * ProductName : ioter(产品)
         * Qty : 10
         * ListUom : [{"Uom":"EA","Qty":1,"IsDefault":true},{"Uom":"OT","Qty":4,"IsDefault":false},{"Uom":"IP","Qty":null,"IsDefault":false},{"Uom":"CS","Qty":null,"IsDefault":false},{"Uom":"PL","Qty":2,"IsDefault":false}]
         * Packing : 包装（规格）
         */

        private String TaskId;
        private String Loc;
        private String ProductId;
        private String ProductName;
        private int Qty;
        private String Packing;
        private List<ListUomBean> ListUom;

        public String getTaskId() {
            return TaskId;
        }

        public void setTaskId(String TaskId) {
            this.TaskId = TaskId;
        }

        public String getLoc() {
            return Loc;
        }

        public void setLoc(String Loc) {
            this.Loc = Loc;
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

        public int getQty() {
            return Qty;
        }

        public void setQty(int Qty) {
            this.Qty = Qty;
        }

        public String getPacking() {
            return Packing;
        }

        public void setPacking(String Packing) {
            this.Packing = Packing;
        }

        public List<ListUomBean> getListUom() {
            return ListUom;
        }

        public void setListUom(List<ListUomBean> ListUom) {
            this.ListUom = ListUom;
        }

}
