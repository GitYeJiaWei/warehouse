package com.ioter.warehouse.bean;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SkuQty
{
    private String StyleNo;
    private String Color;
    private String Size;
    private double Qty;//计划数量

    public String getStyleNo()
    {
        return StyleNo;
    }

    public void setStyleNo(String styleNo)
    {
        StyleNo = styleNo;
    }

    public String getColor()
    {
        return Color;
    }

    public void setColor(String color)
    {
        Color = color;
    }

    public String getSize()
    {
        return Size;
    }

    public void setSize(String size)
    {
        Size = size;
    }

    public double getQty()
    {
        return Qty;
    }

    public void setQty(double qty)
    {
        Qty = qty;
    }


}
