package oms.pomelo.itides.ui.daliy;

import android.content.Context;

import oms.pomelo.itides.base.BasePresenter;
import oms.pomelo.itides.model.DailyInfo;
import oms.pomelo.itides.model.ShanBayResponse;
import oms.pomelo.itides.utils.DataManager;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 */
public class DailyInfoPresenter extends BasePresenter<DailyInfoContract> {

    private Context mContext;
    private DailyInfo mDailyInfo;
    private DailyInfoContract mDailyInfoContract;

    public DailyInfoPresenter(Context mContext) {
        this.mContext = mContext;
        init();
    }

    @Override
    public void bindPresenterView(DailyInfoContract baseContract) {
        mDailyInfoContract = baseContract;
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
