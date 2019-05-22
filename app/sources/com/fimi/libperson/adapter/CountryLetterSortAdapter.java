package com.fimi.libperson.adapter;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.fimi.libperson.R;
import com.fimi.widget.sticklistview.SortModel;
import com.fimi.widget.sticklistview.util.StickyListHeadersAdapter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;

public class CountryLetterSortAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer, Filterable {
    private static final String TAG = CountryLetterSortAdapter.class.getSimpleName();
    private Context mContext;
    private List<SortModel> mList = null;
    private List<SortModel> mListAll = new ArrayList();
    private OnShowLetterChangedListener mOnShowLetterChangedListener;

    static final class HeaderViewHolder {
        TextView tvLetter;

        HeaderViewHolder() {
        }
    }

    public interface OnShowLetterChangedListener {
        void onShowLetterChanged(String str);
    }

    static final class ViewHolder {
        TextView tvTitle;

        ViewHolder() {
        }
    }

    public CountryLetterSortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.mList = list;
        this.mListAll.clear();
        this.mListAll.addAll(list);
    }

    public void updateListView(List<SortModel> list) {
        this.mList = list;
        this.mListAll = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return this.mList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.country_select_item_country_letter_sort, viewGroup, false);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (position < this.mList.size()) {
            viewHolder.tvTitle.setText(((SortModel) this.mList.get(position)).getName().substring(0, ((SortModel) this.mList.get(position)).getName().lastIndexOf(Marker.ANY_MARKER)));
        }
        return view;
    }

    public Filter getFilter() {
        return new Filter() {
            /* Access modifiers changed, original: protected */
            public FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList<SortModel> filterArrayName = new ArrayList();
                String search = charSequence.toString().toLowerCase();
                int n = CountryLetterSortAdapter.this.mListAll.size();
                for (int i = 0; i < n; i++) {
                    if (((SortModel) CountryLetterSortAdapter.this.mListAll.get(i)).getPinyin().toLowerCase().startsWith(search)) {
                        filterArrayName.add(CountryLetterSortAdapter.this.mListAll.get(i));
                    }
                }
                filterResults.count = filterArrayName.size();
                filterResults.values = filterArrayName;
                return filterResults;
            }

            /* Access modifiers changed, original: protected */
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                CountryLetterSortAdapter.this.mList.clear();
                CountryLetterSortAdapter.this.mList.addAll((List) filterResults.values);
                CountryLetterSortAdapter.this.notifyDataSetChanged();
            }
        };
    }

    @RequiresApi(api = 23)
    public View getHeaderView(int position, View view, ViewGroup viewGroup, boolean isScroll) {
        HeaderViewHolder headerViewHolder;
        if (view == null) {
            headerViewHolder = new HeaderViewHolder();
            view = LayoutInflater.from(this.mContext).inflate(R.layout.country_select_item_sticky_header, viewGroup, false);
            headerViewHolder.tvLetter = (TextView) view.findViewById(R.id.sticky_header_letter_tv);
            view.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }
        headerViewHolder.tvLetter.setBackgroundColor(this.mContext.getColor(R.color.country_select_bg));
        if (isScroll) {
            this.mOnShowLetterChangedListener.onShowLetterChanged(((SortModel) this.mList.get(position)).getSortLetter());
        }
        if (!((SortModel) this.mList.get(position)).getSortLetter().equals("#")) {
            headerViewHolder.tvLetter.setText(((SortModel) this.mList.get(position)).getSortLetter());
        }
        return view;
    }

    public long getHeaderId(int position) {
        return (long) ((SortModel) this.mList.get(position)).getSortLetter().subSequence(0, 1).charAt(0);
    }

    public int getSectionForPosition(int position) {
        return ((SortModel) this.mList.get(position)).getSortLetter().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            if (((SortModel) this.mList.get(i)).getSortLetter().toUpperCase().charAt(0) == section) {
                return i;
            }
        }
        return -1;
    }

    private String getAlpha(String letter) {
        String sortStr = letter.trim().substring(0, 1).toUpperCase();
        return sortStr.matches("[A-Z]") ? sortStr : "#";
    }

    public Object[] getSections() {
        return null;
    }

    public void setOnShowLetterChangedListener(OnShowLetterChangedListener onShowLetterChangedListener) {
        this.mOnShowLetterChangedListener = onShowLetterChangedListener;
    }
}
