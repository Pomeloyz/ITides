package oms.pomelo.itides.daliy;

import android.content.Context;

import oms.pomelo.itides.base.BasePresenter;
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
//        super.BindPresenterView(baseContract);
        mDailyInfoContract = (DailyInfoContract) baseContract;
    }

    public void getDailyInfo() {
        super.mCompositeSubscription.add(DataManager.getInstance(mContext).getDailyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailyInfo>() {
                    @Override
                    public void onCompleted() {
                        if (mDailyInfo != null) {
                            mDailyInfoContract.onSuccess(mDailyInfo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDailyInfoContract.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(DailyInfo dailyInfo) {
                        mDailyInfo = dailyInfo;
                    }
                }));
    }
}
