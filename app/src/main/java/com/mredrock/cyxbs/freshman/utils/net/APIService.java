package com.mredrock.cyxbs.freshman.utils.net;

import com.mredrock.cyxbs.freshman.bean.Description;
import com.mredrock.cyxbs.freshman.bean.Entity;
import com.mredrock.cyxbs.freshman.bean.SexRatio;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit 的精髓，正常写Retrofit的接口就好
 */
public interface APIService {
    /**
     * 接口1,获得某一类实体的总数和实体的名称（用实体名称来获取文字描述和图片）
     * @param index index的值应为Consts类中INDEX_开头的常量
     * @return Entity实体类
     * @see Const
     * @see Entity
     */
    @GET("data/describe/getamount")
    Observable<Entity> getEntityName(@Query("index") String index);

    /**
     * 接口2，获得男生与女生的人数
     * @param name 学院名
     * @return SexRatio
     * @see SexRatio
     */
    @GET("search/school/1")
    Observable<SexRatio> getSexRatio(@Query("name") String name);

    /**
     * 接口4,获得学院名称（因为只有name，懒得新建类）
     * @return Entity实体类
     * @see Entity
     */
    @GET("search/school/getname")
    Observable<Entity> getAcademyName();

    @GET("data/get/describe")
    Observable<Description> getDescriptions(@Query("index") String index);
}
