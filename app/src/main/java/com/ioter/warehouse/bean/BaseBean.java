package com.ioter.warehouse.bean;

public class BaseBean<T> extends BaseEntity
{
    private boolean Success;

    private String Message;

    private T ResultObj;

    public boolean success() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        this.Message = message;
    }

    public T getData()
    {
        return ResultObj;
    }

    public void setData(T data)
    {
        this.ResultObj = data;
    }


}
