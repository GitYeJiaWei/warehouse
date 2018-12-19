package com.ioter.warehouse.common.rx;


import android.support.annotation.NonNull;

import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.common.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RxHttpReponseCompat
{
    public static <T> ObservableTransformer<BaseBean<T>, T> compatResult()
    {
        return new ObservableTransformer<BaseBean<T>, T>()
        {
            @Override
            public ObservableSource<T> apply(Observable<BaseBean<T>> baseBeanObservable)
            {

                return baseBeanObservable.flatMap(new Function<BaseBean<T>, ObservableSource<T>>()
                {
                    @Override
                    public ObservableSource<T> apply(@NonNull final BaseBean<T> tBaseBean) throws Exception
                    {

                        if (tBaseBean.success())
                        {

                            return Observable.create(new ObservableOnSubscribe<T>()
                            {
                                @Override
                                public void subscribe(ObservableEmitter<T> subscriber) throws Exception
                                {

                                    try
                                    {
                                        subscriber.onNext(tBaseBean.getData());
                                        subscriber.onComplete();
                                    } catch (Exception e)
                                    {
                                        subscriber.onError(e);
                                    }
                                }
                            });
                        } else
                        {
                            return Observable.error(new ApiException(tBaseBean.success(), tBaseBean.getMessage()));
                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };


    }


}
