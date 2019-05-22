package com.fimi.widget.sticklistview.util;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.apache.mina.proxy.handlers.http.ntlm.NTLMConstants;

public class StickyListHeadersListView extends ListView {
    public OnLoadingMoreLinstener loadMoreListener;
    public AdapterWrapper mAdapter;
    private com.fimi.widget.sticklistview.util.AdapterWrapper.OnHeaderClickListener mAdapterHeaderClickListener;
    private boolean mAreHeadersSticky;
    private final Rect mClippingRect;
    private Boolean mClippingToPadding;
    private Long mCurrentHeaderId;
    private DataSetObserver mDataSetChangedObserver;
    private Drawable mDivider;
    private int mDividerHeight;
    private boolean mDrawingListUnderStickyHeader;
    private ArrayList<View> mFooterViews;
    private View mHeader;
    private boolean mHeaderBeingPressed;
    private int mHeaderBottomPosition;
    private float mHeaderDownY;
    private Integer mHeaderPosition;
    private OnHeaderClickListener mOnHeaderClickListener;
    public OnScrollListener mOnScrollListener;
    public OnScrollListener mOnScrollListenerDelegate;
    private Field mSelectorPositionField;
    private Rect mSelectorRect;
    private ViewConfiguration mViewConfig;

    public interface OnHeaderClickListener {
        void onHeaderClick(StickyListHeadersListView stickyListHeadersListView, View view, int i, long j, boolean z);
    }

    public interface OnLoadingMoreLinstener {
        void OnLoadingMore();
    }

    public void setLoadingMoreListener(OnLoadingMoreLinstener listener) {
        this.loadMoreListener = listener;
    }

    public StickyListHeadersListView(Context context) {
        this(context, null);
    }

    public StickyListHeadersListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842868);
    }

    public StickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAreHeadersSticky = true;
        this.mClippingRect = new Rect();
        this.mCurrentHeaderId = null;
        this.mHeaderDownY = -1.0f;
        this.mHeaderBeingPressed = false;
        this.mDrawingListUnderStickyHeader = false;
        this.mSelectorRect = new Rect();
        this.mAdapterHeaderClickListener = new com.fimi.widget.sticklistview.util.AdapterWrapper.OnHeaderClickListener() {
            public void onHeaderClick(View header, int itemPosition, long headerId) {
                if (StickyListHeadersListView.this.mOnHeaderClickListener != null) {
                    StickyListHeadersListView.this.mOnHeaderClickListener.onHeaderClick(StickyListHeadersListView.this, header, itemPosition, headerId, false);
                }
            }
        };
        this.mDataSetChangedObserver = new DataSetObserver() {
            public void onChanged() {
                StickyListHeadersListView.this.reset();
            }

            public void onInvalidated() {
                StickyListHeadersListView.this.reset();
            }
        };
        this.mOnScrollListener = new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (StickyListHeadersListView.this.mOnScrollListenerDelegate != null) {
                    StickyListHeadersListView.this.mOnScrollListenerDelegate.onScrollStateChanged(view, scrollState);
                }
                if (scrollState != 2) {
                    if ((scrollState == 1 || scrollState == 0) && StickyListHeadersListView.this.getLastVisiblePosition() == StickyListHeadersListView.this.getCount() - 1 && StickyListHeadersListView.this.loadMoreListener != null) {
                        StickyListHeadersListView.this.loadMoreListener.OnLoadingMore();
                    }
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (StickyListHeadersListView.this.mOnScrollListenerDelegate != null) {
                    StickyListHeadersListView.this.mOnScrollListenerDelegate.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (VERSION.SDK_INT >= 8) {
                    StickyListHeadersListView.this.scrollChanged(firstVisibleItem);
                }
            }
        };
        super.setOnScrollListener(this.mOnScrollListener);
        super.setDivider(null);
        super.setDividerHeight(0);
        this.mViewConfig = ViewConfiguration.get(context);
        if (this.mClippingToPadding == null) {
            this.mClippingToPadding = Boolean.valueOf(true);
        }
        try {
            Field selectorRectField = AbsListView.class.getDeclaredField("mSelectorRect");
            selectorRectField.setAccessible(true);
            this.mSelectorRect = (Rect) selectorRectField.get(this);
            this.mSelectorPositionField = AbsListView.class.getDeclaredField("mSelectorPosition");
            this.mSelectorPositionField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            reset();
            scrollChanged(getFirstVisiblePosition());
        }
    }

    private void reset() {
        this.mHeader = null;
        this.mCurrentHeaderId = null;
        this.mHeaderPosition = null;
        this.mHeaderBottomPosition = -1;
    }

    public boolean performItemClick(View view, int position, long id) {
        if (view instanceof WrapperView) {
            view = ((WrapperView) view).mItem;
        }
        return super.performItemClick(view, position, id);
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
        if (divider != null) {
            int dividerDrawableHeight = divider.getIntrinsicHeight();
            if (dividerDrawableHeight >= 0) {
                setDividerHeight(dividerDrawableHeight);
            }
        }
        if (this.mAdapter != null) {
            this.mAdapter.setDivider(divider);
            requestLayout();
            invalidate();
        }
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        if (this.mAdapter != null) {
            this.mAdapter.setDividerHeight(height);
            requestLayout();
            invalidate();
        }
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListenerDelegate = l;
    }

    public void setAreHeadersSticky(boolean areHeadersSticky) {
        if (this.mAreHeadersSticky != areHeadersSticky) {
            this.mAreHeadersSticky = areHeadersSticky;
            requestLayout();
        }
    }

    public boolean getAreHeadersSticky() {
        return this.mAreHeadersSticky;
    }

    public void setAdapter(ListAdapter adapter) {
        if (isInEditMode()) {
            super.setAdapter(adapter);
        } else if (adapter == null) {
            this.mAdapter = null;
            reset();
            super.setAdapter(null);
        } else if (adapter instanceof StickyListHeadersAdapter) {
            this.mAdapter = wrapAdapter(adapter);
            reset();
            super.setAdapter(this.mAdapter);
        } else {
            throw new IllegalArgumentException("Adapter must implement StickyListHeadersAdapter");
        }
    }

    private AdapterWrapper wrapAdapter(ListAdapter adapter) {
        AdapterWrapper wrapper;
        if (adapter instanceof SectionIndexer) {
            wrapper = new SectionIndexerAdapterWrapper(getContext(), (StickyListHeadersAdapter) adapter);
        } else {
            wrapper = new AdapterWrapper(getContext(), (StickyListHeadersAdapter) adapter);
        }
        wrapper.setDivider(this.mDivider);
        wrapper.setDividerHeight(this.mDividerHeight);
        wrapper.registerDataSetObserver(this.mDataSetChangedObserver);
        wrapper.setOnHeaderClickListener(this.mAdapterHeaderClickListener);
        return wrapper;
    }

    public StickyListHeadersAdapter getWrappedAdapter() {
        return this.mAdapter == null ? null : this.mAdapter.mDelegate;
    }

    public View getWrappedView(int position) {
        View childAt = getChildAt(position);
        if (childAt instanceof WrapperView) {
            return ((WrapperView) childAt).mItem;
        }
        return childAt;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        if (VERSION.SDK_INT < 8) {
            scrollChanged(getFirstVisiblePosition());
        }
        positionSelectorRect();
        if (!this.mAreHeadersSticky || this.mHeader == null) {
            super.dispatchDraw(canvas);
            return;
        }
        if (!this.mDrawingListUnderStickyHeader) {
            this.mClippingRect.set(0, this.mHeaderBottomPosition, getWidth(), getHeight());
            canvas.save();
            canvas.clipRect(this.mClippingRect);
        }
        super.dispatchDraw(canvas);
        if (!this.mDrawingListUnderStickyHeader) {
            canvas.restore();
        }
        drawStickyHeader(canvas);
    }

    private void positionSelectorRect() {
        if (!this.mSelectorRect.isEmpty()) {
            int selectorPosition = getSelectorPosition();
            if (selectorPosition >= 0) {
                View v = getChildAt(selectorPosition - fixedFirstVisibleItem(getFirstVisiblePosition()));
                if (v instanceof WrapperView) {
                    WrapperView wrapper = (WrapperView) v;
                    this.mSelectorRect.top = wrapper.getTop() + wrapper.mItemTop;
                }
            }
        }
    }

    private int getSelectorPosition() {
        if (this.mSelectorPositionField == null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i).getBottom() == this.mSelectorRect.bottom) {
                    return fixedFirstVisibleItem(getFirstVisiblePosition()) + i;
                }
            }
        } else {
            try {
                return this.mSelectorPositionField.getInt(this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return -1;
    }

    private void drawStickyHeader(Canvas canvas) {
        int headerHeight = getHeaderHeight();
        int top = this.mHeaderBottomPosition - headerHeight;
        this.mClippingRect.left = getPaddingLeft();
        this.mClippingRect.right = getWidth() - getPaddingRight();
        this.mClippingRect.bottom = top + headerHeight;
        this.mClippingRect.top = this.mClippingToPadding.booleanValue() ? getPaddingTop() : 0;
        canvas.save();
        canvas.clipRect(this.mClippingRect);
        canvas.translate((float) getPaddingLeft(), (float) top);
        this.mHeader.draw(canvas);
        canvas.restore();
    }

    private void measureHeader() {
        int heightMeasureSpec;
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(((getWidth() - getPaddingLeft()) - getPaddingRight()) - (isScrollBarOverlay() ? 0 : getVerticalScrollbarWidth()), NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        LayoutParams params = this.mHeader.getLayoutParams();
        if (params == null || params.height <= 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, NTLMConstants.FLAG_NEGOTIATE_KEY_EXCHANGE);
        }
        this.mHeader.measure(widthMeasureSpec, heightMeasureSpec);
        this.mHeader.layout(getPaddingLeft(), 0, getWidth() - getPaddingRight(), this.mHeader.getMeasuredHeight());
    }

    private boolean isScrollBarOverlay() {
        int scrollBarStyle = getScrollBarStyle();
        return scrollBarStyle == 0 || scrollBarStyle == NTLMConstants.FLAG_UNIDENTIFIED_8;
    }

    private int getHeaderHeight() {
        return this.mHeader == null ? 0 : this.mHeader.getMeasuredHeight();
    }

    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        this.mClippingToPadding = Boolean.valueOf(clipToPadding);
    }

    private void scrollChanged(int reportedFirstVisibleItem) {
        Log.e("parent", "scrollChanged");
        int adapterCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (adapterCount != 0 && this.mAreHeadersSticky) {
            int listViewHeaderCount = getHeaderViewsCount();
            int firstVisibleItem = fixedFirstVisibleItem(reportedFirstVisibleItem) - listViewHeaderCount;
            if (firstVisibleItem < 0 || firstVisibleItem > adapterCount - 1) {
                reset();
                updateHeaderVisibilities();
                invalidate();
                return;
            }
            if (this.mHeaderPosition == null || this.mHeaderPosition.intValue() != firstVisibleItem) {
                this.mHeaderPosition = Integer.valueOf(firstVisibleItem);
                this.mCurrentHeaderId = Long.valueOf(this.mAdapter.getHeaderId(firstVisibleItem));
                Log.i("zhej", "scrollChanged: " + this.mHeaderPosition);
                this.mHeader = this.mAdapter.getHeaderView(this.mHeaderPosition.intValue(), this.mHeader, this, true);
                measureHeader();
            }
            int childCount = getChildCount();
            if (childCount != 0) {
                View viewToWatch = null;
                int watchingChildDistance = Integer.MAX_VALUE;
                boolean viewToWatchIsFooter = false;
                for (int i = 0; i < childCount; i++) {
                    View child = super.getChildAt(i);
                    boolean childIsFooter = this.mFooterViews != null && this.mFooterViews.contains(child);
                    int childDistance = child.getTop() - (this.mClippingToPadding.booleanValue() ? getPaddingTop() : 0);
                    if (childDistance >= 0 && (viewToWatch == null || (!(viewToWatchIsFooter || ((WrapperView) viewToWatch).hasHeader()) || ((childIsFooter || ((WrapperView) child).hasHeader()) && childDistance < watchingChildDistance)))) {
                        viewToWatch = child;
                        viewToWatchIsFooter = childIsFooter;
                        watchingChildDistance = childDistance;
                    }
                }
                int headerHeight = getHeaderHeight();
                if (viewToWatch == null || !(viewToWatchIsFooter || ((WrapperView) viewToWatch).hasHeader())) {
                    this.mHeaderBottomPosition = (this.mClippingToPadding.booleanValue() ? getPaddingTop() : 0) + headerHeight;
                } else if (firstVisibleItem != listViewHeaderCount || super.getChildAt(0).getTop() <= 0 || this.mClippingToPadding.booleanValue()) {
                    int paddingTop = this.mClippingToPadding.booleanValue() ? getPaddingTop() : 0;
                    this.mHeaderBottomPosition = Math.min(viewToWatch.getTop(), headerHeight + paddingTop);
                    this.mHeaderBottomPosition = this.mHeaderBottomPosition < paddingTop ? headerHeight + paddingTop : this.mHeaderBottomPosition;
                } else {
                    this.mHeaderBottomPosition = 0;
                }
            }
            updateHeaderVisibilities();
            invalidate();
        }
    }

    public void addFooterView(View v) {
        super.addFooterView(v);
        if (this.mFooterViews == null) {
            this.mFooterViews = new ArrayList();
        }
        this.mFooterViews.add(v);
    }

    public boolean removeFooterView(View v) {
        if (!super.removeFooterView(v)) {
            return false;
        }
        this.mFooterViews.remove(v);
        return true;
    }

    private void updateHeaderVisibilities() {
        int top;
        if (this.mClippingToPadding.booleanValue()) {
            top = getPaddingTop();
        } else {
            top = 0;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = super.getChildAt(i);
            if (child instanceof WrapperView) {
                WrapperView wrapperViewChild = (WrapperView) child;
                if (wrapperViewChild.hasHeader()) {
                    View childHeader = wrapperViewChild.mHeader;
                    if (wrapperViewChild.getTop() < top) {
                        childHeader.setVisibility(4);
                    } else {
                        childHeader.setVisibility(0);
                    }
                }
            }
        }
    }

    private int fixedFirstVisibleItem(int firstVisibleItem) {
        if (VERSION.SDK_INT >= 11) {
            return firstVisibleItem;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getBottom() >= 0) {
                firstVisibleItem += i;
                break;
            }
        }
        if (!this.mClippingToPadding.booleanValue() && getPaddingTop() > 0 && super.getChildAt(0).getTop() > 0 && firstVisibleItem > 0) {
            firstVisibleItem--;
        }
        return firstVisibleItem;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.mOnHeaderClickListener = onHeaderClickListener;
    }

    public void setDrawingListUnderStickyHeader(boolean drawingListUnderStickyHeader) {
        this.mDrawingListUnderStickyHeader = drawingListUnderStickyHeader;
    }

    public boolean isDrawingListUnderStickyHeader() {
        return this.mDrawingListUnderStickyHeader;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action != 0 || ev.getY() > ((float) this.mHeaderBottomPosition)) {
            if (this.mHeaderBeingPressed) {
                if (Math.abs(ev.getY() - this.mHeaderDownY) >= ((float) this.mViewConfig.getScaledTouchSlop())) {
                    this.mHeaderDownY = -1.0f;
                    this.mHeaderBeingPressed = false;
                    this.mHeader.setPressed(false);
                    this.mHeader.invalidate();
                    invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
                } else if (action != 1 && action != 3) {
                    return true;
                } else {
                    this.mHeaderDownY = -1.0f;
                    this.mHeaderBeingPressed = false;
                    this.mHeader.setPressed(false);
                    this.mHeader.invalidate();
                    invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
                    if (this.mOnHeaderClickListener == null) {
                        return true;
                    }
                    this.mOnHeaderClickListener.onHeaderClick(this, this.mHeader, this.mHeaderPosition.intValue(), this.mCurrentHeaderId.longValue(), true);
                    return true;
                }
            }
            return super.onTouchEvent(ev);
        }
        this.mHeaderDownY = ev.getY();
        this.mHeaderBeingPressed = true;
        this.mHeader.setPressed(true);
        this.mHeader.invalidate();
        invalidate(0, 0, getWidth(), this.mHeaderBottomPosition);
        return true;
    }
}
