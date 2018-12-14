package com.ioter.warehouse.data.http;

import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.Check;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.bean.Pack;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService
{

    //String BASE_URL = "http://mall.ioter-e.com:8097/api/";
    String BASE_URL = "http://192.168.1.100:8097/api/";
    //String BASE_URL = "http://192.168.31.67:8097/api/";
    //最后要加斜杠

    @FormUrlEncoded
    @POST("Center/Login")//登录
    Observable<BaseBean<LoginBean>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Center/GetWhStockOutEpcList")//获取发货单所有标签
    Observable<BaseBean<List<Pack>>> getInboundDeliveryPack(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("Center/StoreIn")//门店收货
    Observable<BaseBean> stockIn(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Center/GetAllEpcList")//获取盘点列表
    Observable<BaseBean<List<Check>>> getStockTakeList(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("EPC/InitEpc")//初始化标签
    Observable<BaseBean> initEpc(@Field("epc") String jsonParam);
}
