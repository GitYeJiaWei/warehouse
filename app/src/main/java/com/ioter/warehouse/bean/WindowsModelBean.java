package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class WindowsModelBean implements Serializable{
    private int WindowsType;
    private String DefaultText;
    private String ValueField;
    private String TextField;
    private List<String> ListTitle;
    private List<String> ListField;
    private List<Boolean> ListTitleVisable;

    public List<Boolean> getListTitleVisable() {
        return ListTitleVisable;
    }

    public void setListTitleVisable(List<Boolean> listTitleVisable) {
        ListTitleVisable = listTitleVisable;
    }

    public List<String> getListField() {
        return ListField;
    }

    public void setListField(List<String> listField) {
        ListField = listField;
    }

    public String getValueField() {
        return ValueField;
    }

    public void setValueField(String valueField) {
        ValueField = valueField;
    }

    public String getTextField() {
        return TextField;
    }

    public void setTextField(String textField) {
        TextField = textField;
    }

    public int getWindowsType() {
        return WindowsType;
    }

    public void setWindowsType(int windowsType) {
        WindowsType = windowsType;
    }

    public String getDefaultText() {
        return DefaultText;
    }

    public void setDefaultText(String defaultText) {
        DefaultText = defaultText;
    }

    public List<String> getListTitle() {
        return ListTitle;
    }

    public void setListTitle(List<String> listTitle) {
        ListTitle = listTitle;
    }
}
