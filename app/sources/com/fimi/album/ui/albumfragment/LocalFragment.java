package com.fimi.album.ui.albumfragment;

import android.os.Bundle;
import com.example.album.R;
import com.fimi.album.entity.MediaModel;

public class LocalFragment extends BaseFragment {
    public static LocalFragment obtaion() {
        return obtaion(null);
    }

    public static LocalFragment obtaion(Bundle bundle) {
        LocalFragment mVideoFragment = new LocalFragment();
        if (bundle != null) {
            mVideoFragment.getArguments().putAll(bundle);
        }
        return mVideoFragment;
    }

    /* Access modifiers changed, original: 0000 */
    public int getContentID() {
        return R.layout.album_fragment_local_media;
    }

    public void onStart() {
        super.onStart();
    }

    public void notifyAddCallback(MediaModel mediaModel) {
    }
}
