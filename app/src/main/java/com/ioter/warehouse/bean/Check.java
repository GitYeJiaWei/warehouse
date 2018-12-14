package com.ioter.warehouse.bean;


import java.util.List;

/**
 * @author YJW
 * @create 2018/7/19
 * @Describe
 */
public class Check {

        /**
         * Epc : 1808020002000001
         * ProductName : 上衣
         * BoxId : ZXD1808020006
         * Style : XAT888
         * Color : B
         * Size : X
         * Price : 200
         * ListFilePath : []
         */

        private String Epc;
        private String ProductName;
        private String BoxId;
        private String Style;
        private String Color;
        private String Size;
        private int Price;
        private List<?> ListFilePath;

        public String getEpc() {
            return Epc;
        }

        public void setEpc(String Epc) {
            this.Epc = Epc;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getBoxId() {
            return BoxId;
        }

        public void setBoxId(String BoxId) {
            this.BoxId = BoxId;
        }

        public String getStyle() {
            return Style;
        }

        public void setStyle(String Style) {
            this.Style = Style;
        }

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            this.Color = Color;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }

        public int getPrice() {
            return Price;
        }

        public void setPrice(int Price) {
            this.Price = Price;
        }

        public List<?> getListFilePath() {
            return ListFilePath;
        }

        public void setListFilePath(List<?> ListFilePath) {
            this.ListFilePath = ListFilePath;
        }
}
