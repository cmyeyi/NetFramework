package hq.demo.net.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hq.demo.net.MainActivity;
import hq.demo.net.R;
import hq.demo.net.view.FragmentPagerAdapter;
import hq.demo.net.view.SimpleViewPagerIndicator;

public class FragmentMain extends BaseFragment implements View.OnClickListener {

    public static final String TAG = FragmentMain.class.getSimpleName();
    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private Fragment currentFragment;
    private final int PAGE_ONE = 0;
    private final int PAGE_TWO = 1;
    private final int PAGE_THREE = 2;
    private VolleyFragment volleyFragment;
    private OkHttpFragment okHttpFragment;
    private RetrofitFragment retrofitFragment;
    private RxJavaFragment rxJavaFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setActionBarTitle();
        View view = inflater.inflate(R.layout.layout_fragment_main, null);
        mViewPager = (ViewPager) view.findViewById(R.id.main_view_pager);
        mIndicator = (SimpleViewPagerIndicator) view.findViewById(R.id.main_sliding_trip);
        initViewPager();
        return view;
    }

    private void setActionBarTitle() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupBackAsUp(getString(R.string.app_name), false);
        }
    }

    private void initViewPager() {
        List<FragmentPagerAdapter.TabInfo> list = new ArrayList<FragmentPagerAdapter.TabInfo>();
        list.add(new FragmentPagerAdapter.TabInfo(VolleyFragment.class, null, getString(R.string.main_title_text_0)));
        list.add(new FragmentPagerAdapter.TabInfo(OkHttpFragment.class, null, getString(R.string.main_title_text_1)));
        list.add(new FragmentPagerAdapter.TabInfo(RetrofitFragment.class, null, getString(R.string.main_title_text_2)));
        list.add(new FragmentPagerAdapter.TabInfo(RxJavaFragment.class, null, getString(R.string.main_title_text_3)));
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getActivity(), getChildFragmentManager(), list);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(adapter);
        mIndicator.setTexTSizeSP(16);
        mIndicator.setGravityType(Gravity.CENTER);
        mIndicator.setTextColor(getResources().getColor(R.color.white99), getResources().getColor(R.color.white));
        float indicatorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mIndicator.setIndicatorColorAndHeight(getResources().getColor(R.color.white), indicatorHeight);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setTvTitleColor(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
