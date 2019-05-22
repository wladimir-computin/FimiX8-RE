package com.fimi.app.x8s.ui.album.x8s;

import android.os.Bundle;
import com.fimi.app.x8s.R;

public class X8CameraFragment extends X8MediaBaseFragment {
    public static X8CameraFragment obtaion() {
        return obtaion(null);
    }

    public static X8CameraFragment obtaion(Bundle bundle) {
        X8CameraFragment x8CameraFragment = new X8CameraFragment();
        if (bundle != null) {
            x8CameraFragment.getArguments().putAll(bundle);
        }
        return x8CameraFragment;
    }

    /* Access modifiers changed, original: 0000 */
    public int getContentID() {
        return R.layout.x8_fragment_camera;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean judgeTypeCurrentFragment() {
        return true;
    }

    public void onResume() {
        super.onResume();
        this.mBaseFragmentPresenter.registerDownloadListerner();
    }

    public void unRegisterReciver() {
        if (this.mBaseFragmentPresenter != null) {
            this.mBaseFragmentPresenter.unRegisterReciver();
        }
    }

    public void onDisConnect() {
        this.rlMediaSelectBottom.setVisibility(8);
        this.mBaseFragmentPresenter.setEnterSelectMode(false);
    }
}
