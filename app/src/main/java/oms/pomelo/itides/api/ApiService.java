package oms.pomelo.itides.api;

import androidx.annotation.NonNull;

import oms.pomelo.itides.ui.daliy.DailyInfo;
import oms.pomelo.itides.base.BaseModel;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public interface ApiService {

    @GET("v1/dailypics")
    Observable<BaseModel<Object>> getDailyPics(@Query("from") @NonNull String from,
                                               @Query("to") @NonNull String to);

    @GET("v1/dailypics/{date}")
    Observable<DailyInfo> getDailyPics(@Path("date") @NonNull String date);
}
