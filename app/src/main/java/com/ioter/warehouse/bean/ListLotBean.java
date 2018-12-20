package com.ioter.warehouse.bean;

import java.util.Map;

public class ListLotBean {
    /**
     * Title : 生产日期
     * Type : 2
     * Value :
     * listOption : null
     * listData : null
     */

    private String Title;
    private int Type;
    private String Value;
    private Map<String, String> listOption;
    private Object listData;

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

    public Map<String, String> getListOption() {
        return listOption;
    }

    public void setListOption(Map<String, String> listOption) {
        this.listOption = listOption;
    }

    public Object getListData() {
        return listData;
    }

    public void setListData(Object listData) {
        this.listData = listData;
    }

}
