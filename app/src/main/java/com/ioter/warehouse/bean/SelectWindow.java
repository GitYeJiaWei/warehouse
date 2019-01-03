package com.ioter.warehouse.bean;

import java.util.List;

public class SelectWindow {

        /**
         * ListData : [["0011","山山活性炭"],["0022","键盘套件"],["0103100106","0103"],["010310112","0103"],["0103102105","0103"],["0103104105","0103"],["01031112","0103"],["010361106","0103"],["1888100105","时尚No1"],["1888100106","时尚No1"]]
         * TotalCount : 24
         */

        private int TotalCount;
        private List<List<String>> ListData;

        public int getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(int TotalCount) {
            this.TotalCount = TotalCount;
        }

        public List<List<String>> getListData() {
            return ListData;
        }

        public void setListData(List<List<String>> ListData) {
            this.ListData = ListData;
        }
}
