package com.ioter.warehouse.data.http;

import com.ioter.warehouse.bean.BaseBean;
import com.ioter.warehouse.bean.GetStock;
import com.ioter.warehouse.bean.LoginBean;
import com.ioter.warehouse.bean.PickModel;
import com.ioter.warehouse.bean.StockBean;
import com.ioter.warehouse.bean.StockMoveModel;
import com.ioter.warehouse.bean.TrackBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService
{
    //String BASE_URL = "http://mall.ioter-e.com:8097/api/";
    String BASE_URL =  "http://192.168.66.3:8107/api/";
    //最后要加斜杠

    //http://192.168.66.3:8107/api/user/Login?userName=admin&password=123
    @GET("user/Login")//登录
    Observable<BaseBean<LoginBean>> login(@QueryMap Map<String ,String> params);

    //http://192.168.66.3:8107/api/StockIn/QueryAsn?stockInId=ASN201812130002&orderNo=""
    @GET("StockIn/QueryAsn")//查询预收货通知单
    Observable<BaseBean<List<StockBean>>> QueryAsn(@QueryMap Map<String,String> params);

    @FormUrlEncoded
    @POST("stockin/StockIn")//收货
    Observable<BaseBean> StockIn(@FieldMap Map<String ,String> params);

    //http://192.168.66.3:8107//api/stock/GetStock?locid=1&productId=3eb29db1-9364-4d08-83eb-72a6924f8f53&isIgnoreLot=true
    @GET("stock/GetStock")//获取库存
    Observable<BaseBean<List<GetStock>>> GetStock(@QueryMap Map<String,String> params);

    //http://192.168.66.3:8107/api/shelf/GetProductByTrackCode?trackCode=*
    @GET("shelf/GetProductByTrackCode")//通过跟踪号获取上架商品列表
    Observable<BaseBean<List<TrackBean>>> GetProductByTrackCode(@QueryMap Map<String,String> params);

    @FormUrlEncoded
    @POST("shelf/Shelf")//上架
    Observable<BaseBean> Shelf(@FieldMap Map<String,String> params);

    //
    @GET("Pick/GetPickTask")//获取拣货任务列表
    Observable<BaseBean<List<PickModel>>> GetPickTask(@QueryMap Map<String,String> params);

    //http://192.168.66.3:8107/api/stockmove/GetStockMoveInfo?locid=66&trackcode=*&productid=
    @GET("stockmove/GetStockMoveInfo")//获取移库信息
    Observable<BaseBean<StockMoveModel>> GetStockMoveInfo(@QueryMap Map<String,String> params);

}
