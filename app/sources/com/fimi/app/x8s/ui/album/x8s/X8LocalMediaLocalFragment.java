package com.fimi.app.x8s.ui.album.x8s;

import android.view.View;
import com.fimi.app.x8s.R;

public class X8LocalMediaLocalFragment extends X8MediaBaseFragment {
    /* Access modifiers changed, original: 0000 */
    public int getContentID() {
        return R.layout.x8_fragment_local_media;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean judgeTypeCurrentFragment() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void initData(View view) {
    }

    public int getLayoutId() {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void doTrans() {
    }

    /* Access modifiers changed, original: protected */
    public void initMVP() {
    }

    public void unRegisterReciver() {
        if (this.mBaseFragmentPresenter != null) {
            this.mBaseFragmentPresenter.unRegisterReciver();
        }
    }
}
