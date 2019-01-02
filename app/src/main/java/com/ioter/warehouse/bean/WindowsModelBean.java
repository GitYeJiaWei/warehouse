package com.ioter.warehouse.bean;

import java.io.Serializable;
import java.util.List;

public class WindowsModelBean implements Serializable{
    private int WindowsType;
    private String DefaultText;
    private List<String> ListTitle;

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
