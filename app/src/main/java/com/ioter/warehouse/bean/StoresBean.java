package com.ioter.warehouse.bean;

import java.io.Serializable;

/**
 * @author YJW
 * @create 2018/7/23
 * @Describe
 */
public class StoresBean implements Serializable {
        /**
         * WhId : 2
         * WhName : 2
         */

        private String WhId;
        private String WhName;

        public String getWhId() {
            return WhId;
        }

        public void setWhId(String WhId) {
            this.WhId = WhId;
        }

        public String getWhName() {
            return WhName;
        }

        public void setWhName(String WhName) {
            this.WhName = WhName;
        }
}
