package com.ioter.warehouse.bean;

public class ListUomBean {
    /**
     * Uom : EA
     * Qty : 9.0
     * IsDefault : true
     */

    private String Uom;
    private double Qty;
    private boolean IsDefault;

    public String getUom() {
        return Uom;
    }

    public void setUom(String Uom) {
        this.Uom = Uom;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double Qty) {
        this.Qty = Qty;
    }

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean IsDefault) {
        this.IsDefault = IsDefault;
    }
}
