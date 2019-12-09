package oms.pomelo.itides.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * NAME: Lay
 * DATE: 2019-08-03
 */
public final class BreathPagerAdapter<T extends View> extends PagerAdapter {

    private final Context mContext;
    private final List<T> mViewList;

    public BreathPagerAdapter(Context context, List<T> viewList) {
        this.mContext = context;
        this.mViewList = viewList;
    }

    @Override
    public int getCount() {
        return mViewList != null ? mViewList.size() : 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
