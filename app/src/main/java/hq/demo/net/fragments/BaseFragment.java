package hq.demo.net.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    public static final int REFRESH_LIST = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * todo 异步任务回调必须判断activity状态
     * 当前fragment依附activity是否已销毁
     *
     * @return
     */
    protected boolean isFinishing() {

        if (null == getActivity()) {
            return true;
        }

        return getActivity().isFinishing();
    }
}
