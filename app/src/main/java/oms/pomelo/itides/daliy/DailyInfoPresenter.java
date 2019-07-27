package oms.pomelo.itides.daliy;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import oms.pomelo.itides.base.BasePresenter;
import oms.pomelo.itides.model.ShanBayResponse;
import oms.pomelo.itides.presenter.BaseContract;
import oms.pomelo.itides.utils.DataManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 */
public class DailyInfoPresenter extends BasePresenter {

    private Context mContext;
    private DailyInfo mDailyInfo;
    private DailyInfoContract mDailyInfoContract;

    public DailyInfoPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void BindPresenterView(BaseContract baseContract) {
        mDailyInfoContract = (DailyInfoContract) baseContract;
    }

    public void getDailyInfo() {
        super.mCompositeSubscription.add(DataManager.getInstance(mContext).getDailyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShanBayResponse<DailyInfo>>() {
                    @Override
                    public void onCompleted() {
                        if (mDailyInfo != null) {
                            mDailyInfoContract.onSuccess(mDailyInfo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShanBayResponse<DailyInfo> dailyInfoShanBayResponse) {
                        if (dailyInfoShanBayResponse.getMsg().equals("SUCCESS")) {
                            mDailyInfo = dailyInfoShanBayResponse.getData();
                        }
                    }
                }));

    }
}
