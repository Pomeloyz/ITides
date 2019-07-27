package oms.pomelo.itides.api;

import oms.pomelo.itides.daliy.DailyInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public interface ApiService {

    @GET("api/v2/quote/quotes/today/")
    Observable<DailyInfo> getDailyInfo();

}
