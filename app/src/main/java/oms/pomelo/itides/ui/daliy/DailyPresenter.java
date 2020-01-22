package oms.pomelo.itides.ui.daliy;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;

import oms.pomelo.itides.utils.DataManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * NAME: Sherry
 * DATE: 2019-07-30
 */
public class DailyPresenter extends DailyContract.Presenter {

    private DailyInfo mDailyInfo;
    private Context mContext;

    public DailyPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getDaily() {
        String date = DateFormat.format("yyyy-MM-dd", Calendar.getInstance()).toString();
        super.mCompositeSubscription.add(DataManager.getInstance(mContext).getDailyPics(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyInfo>() {
                    @Override
                    public void onCompleted() {
                        if (mDailyInfo != null && mDailyInfo.getCode() == 0) {
                            getMvpView().getDailySuccess(mDailyInfo);
                        } else if (mDailyInfo != null){
                            getMvpView().getDailyError(mDailyInfo.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getDailyError(e.getMessage());
                    }

                    @Override
                    public void onNext(DailyInfo dailyInfo) {
                        mDailyInfo = dailyInfo;
                    }
                }));
    }
}
