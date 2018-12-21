package com.ioter.warehouse.bean;


public class ListLotBean {

            /**
             * Title : 生产日期
             * Type : 2
             * Value :
             * ListOption : null
             * ListData : null
             */

            private String Title;
            private int Type;
            private String Value;
            private Object ListOption;
            private Object ListData;

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }

            public Object getListOption() {
                return ListOption;
            }

            public void setListOption(Object ListOption) {
                this.ListOption = ListOption;
            }

            public Object getListData() {
                return ListData;
            }

            public void setListData(Object ListData) {
                this.ListData = ListData;
            }
}
