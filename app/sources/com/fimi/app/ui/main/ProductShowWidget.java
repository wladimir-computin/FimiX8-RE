package com.fimi.app.ui.main;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.fimi.android.app.R;
import com.fimi.app.interfaces.IProductControllers;
import java.util.ArrayList;
import java.util.List;

public class ProductShowWidget extends FrameLayout implements IProductControllers {
    private Context context;
    private List<FrameLayout> frameLayouts;
    private ViewPager pager;
    ChangePositionListener positionListener;

    public interface ChangePositionListener {
        void changePosition(int i);
    }

    class KannerPagerAdapter extends PagerAdapter {
        KannerPagerAdapter() {
        }

        public int getCount() {
            return ProductShowWidget.this.frameLayouts.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView((View) ProductShowWidget.this.frameLayouts.get(position));
            return ProductShowWidget.this.frameLayouts.get(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) ProductShowWidget.this.frameLayouts.get(position));
        }
    }

    private class PageChangeListener implements OnPageChangeListener {
        private PageChangeListener() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            if (ProductShowWidget.this.positionListener != null) {
                ProductShowWidget.this.positionListener.changePosition(position);
            }
        }

        public void onPageScrollStateChanged(int state) {
            switch (state) {
            }
        }
    }

    public void setPositionListener(ChangePositionListener positionListener) {
        this.positionListener = positionListener;
    }

    public ProductShowWidget(@NonNull Context context) {
        this(context, null);
    }

    public ProductShowWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductShowWidget(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initWidget();
    }

    private void initWidget() {
        this.pager = (ViewPager) LayoutInflater.from(this.context).inflate(R.layout.layout_cardslide, this, true).findViewById(R.id.vp);
        this.frameLayouts = new ArrayList();
        for (int i = 0; i < HostNewMainActivity.PRODUCTCLASS.length; i++) {
            if (HostNewMainActivity.PRODUCTCLASS[i] == HostX9ProductView.class) {
                this.frameLayouts.add(new HostX9ProductView(this.context, null));
            } else if (HostNewMainActivity.PRODUCTCLASS[i] == HostGh2ProductView.class) {
                this.frameLayouts.add(new HostGh2ProductView(this.context, null));
            } else if (HostNewMainActivity.PRODUCTCLASS[i] == HostX8sProductView.class) {
                this.frameLayouts.add(new HostX8sProductView(this.context, null));
            }
        }
        this.pager.setAdapter(new KannerPagerAdapter());
        this.pager.setFocusable(true);
        this.pager.setCurrentItem(0);
        this.pager.setOnPageChangeListener(new PageChangeListener());
    }

    public void stopAnimation() {
        if (((FrameLayout) this.frameLayouts.get(0)).getClass() == HostX9ProductView.class) {
            ((HostX9ProductView) this.frameLayouts.get(0)).stopnAnimation();
        }
    }

    public void startAnimation() {
        if (((FrameLayout) this.frameLayouts.get(0)).getClass() == HostX9ProductView.class) {
            ((HostX9ProductView) this.frameLayouts.get(0)).startAnimation();
        }
    }
}
