package com.fimi.kernel.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;

    public abstract void doTrans();

    public abstract int getLayoutId();

    public abstract void initData(View view);

    public abstract void initMVP();

    public void onAttach(Context context) {
        this.mActivity = (Activity) context;
        this.mContext = context;
        super.onAttach(context);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutView() != null) {
            return getLayoutView();
        }
        View view = inflater.inflate(getLayoutId(), null);
        initData(view);
        initMVP();
        doTrans();
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBundle(getArguments());
        super.onViewCreated(view, savedInstanceState);
    }

    public void getBundle(Bundle bundle) {
    }

    public View getLayoutView() {
        return null;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDetach() {
        super.onDetach();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}
