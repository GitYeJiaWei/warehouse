package com.ioter.warehouse.bean;


import java.io.Serializable;
import java.util.List;

public class ListLotBean implements Serializable {

    /**
     * Title : 属性1
     * Type : 1
     * Value : 3
     * ListOption : {"68":"平方米","86":"平方分米","87":"公顷","88":"5"}
     * ListData : null
     */

    private String Title;
    private int Type;
    private String Value;
    private Object ListOption;
    private WindowsModelBean WindowsModel;

    public Object getListOption() {
        return ListOption;
    }

    public void setListOption(Object listOption) {
        ListOption = listOption;
    }

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

    public WindowsModelBean getWindowsModel() {
        return WindowsModel;
    }

    public void setWindowsModel(WindowsModelBean windowsModel) {
        WindowsModel = windowsModel;
    }
}
