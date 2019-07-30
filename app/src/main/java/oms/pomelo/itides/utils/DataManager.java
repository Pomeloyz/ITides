package oms.pomelo.itides.utils;

import android.content.Context;

import oms.pomelo.itides.api.ApiService;
import oms.pomelo.itides.ui.daliy.DailyInfo;
import oms.pomelo.itides.base.BaseModel;
import rx.Observable;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 * 该类用来管理ApiService中对应的各种API接口，
 * 当做Retrofit和presenter中的桥梁，Activity就不用直接和retrofit打交道了
 */
public final class DataManager {

    private ApiService apiService;
    private volatile static DataManager sInstance;

    private DataManager(Context context) {
        this.apiService = RetrofitUtil.getInstance(context).getApiService();
    }

    public static DataManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DataManager.class) {
                if (sInstance == null) {
                    sInstance = new DataManager(context);
                }
            }
        }
        return sInstance;
    }

    //将retrofit的业务方法映射到DataManager中，以后统一用该类来调用业务方法
    public Observable<BaseModel<DailyInfo>> getDailyInfo() {
        return apiService.getDailyInfo();
    }

}
