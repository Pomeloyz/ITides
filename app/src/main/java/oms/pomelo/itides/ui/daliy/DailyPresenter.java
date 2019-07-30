package oms.pomelo.itides.ui.daliy;

import android.content.Context;

import oms.pomelo.itides.base.BaseModel;
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
        super.mCompositeSubscription.add(DataManager.getInstance(mContext).getDailyInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseModel<DailyInfo>>() {
                    @Override
                    public void onCompleted() {
                        if (mDailyInfo != null) {
                            getMvpView().getDailySuccess(mDailyInfo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().getDailyError(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseModel<DailyInfo> dailyInfoBaseModel) {
                        if (dailyInfoBaseModel.getMsg().equals("SUCCESS")) {
                            mDailyInfo = dailyInfoBaseModel.getData();
                        }
                    }
                }));
    }
}
