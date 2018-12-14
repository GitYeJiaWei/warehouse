package com.ioter.warehouse.common.rx.subscriber;

import android.content.Context;

import com.ioter.warehouse.common.exception.BaseException;

import io.reactivex.disposables.Disposable;


public abstract class AdapterItemSubcriber<T> extends ErrorHandlerSubscriber<T>
{

    public AdapterItemSubcriber(Context context)
    {
        super(context);
    }

    @Override
    public void onSubscribe(Disposable d)
    {

    }

    @Override
    public void onError(Throwable e)
    {
        //e.printStackTrace();
        BaseException baseException = mErrorHandler.handleError(e);
        mErrorHandler.showErrorMessage(baseException);
    }

}
