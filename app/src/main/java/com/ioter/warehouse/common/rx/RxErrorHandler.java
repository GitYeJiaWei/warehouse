package com.ioter.warehouse.common.rx;

import android.content.Context;
import android.widget.Toast;

import com.ioter.warehouse.common.exception.ApiException;
import com.ioter.warehouse.common.exception.BaseException;
import com.ioter.warehouse.common.exception.ErrorMessageFactory;

import java.net.ConnectException;


public class RxErrorHandler
{


    private Context mContext;

    public RxErrorHandler(Context context)
    {

        this.mContext = context;
    }

    public BaseException handleError(Throwable e)
    {

        BaseException exception = new BaseException();

        if (e instanceof ConnectException)
        {
            //exception.setCode(BaseException.SOCKET_TIMEOUT_ERROR);
            exception.setDisplayMessage(ErrorMessageFactory.create(mContext, 0x7));
        } else if (e instanceof ApiException)
        {
            //exception.setCode(BaseException.API_ERROR);
            exception.setDisplayMessage(((ApiException) e).getDisplayMessage());
        } else
        {
            //exception.setCode(BaseException.UNKNOWN_ERROR);
            exception.setDisplayMessage(ErrorMessageFactory.create(mContext, 0x4));
        }

        return exception;
    }

    public void showErrorMessage(BaseException e)
    {
        Toast.makeText(mContext, e.getDisplayMessage(), Toast.LENGTH_LONG).show();
    }
}
