package oms.pomelo.itides.ui.daliy;

import oms.pomelo.itides.base.BaseMvpView;
import oms.pomelo.itides.base.BasePresenter;

/**
 * NAME: Sherry
 * DATE: 2019-07-30
 */
public interface DailyContract {

    interface DailyView extends BaseMvpView {

        void getDailySuccess(DailyInfo dailyInfo);

        void getDailyError(String result);

        // ------后续有关更新界面的操作都在此添加相关接口------

    }

    abstract class Presenter extends BasePresenter<DailyView> {

        public abstract void getDaily();

        // ------后续有关功能性的逻辑操作都在此添加相关方法------

    }

}
