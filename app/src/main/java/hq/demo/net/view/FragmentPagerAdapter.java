package hq.demo.net.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public static class TabInfo {
        Class<?> clss;
        Bundle bundle;
        String title;

        public TabInfo(Class<?> c, Bundle b, String s) {
            clss = c;
            bundle = b;
            title = s;
        }
    }

    private Context context;
    private List<TabInfo> list;

    public FragmentPagerAdapter(Context c, FragmentManager fm, List<TabInfo> l) {
        super(fm);
        context = c;
        list = l;
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = list.get(position);
        return Fragment.instantiate(context, info.clss.getName(), info.bundle);
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
            return list.get(position).title;
        }


}
