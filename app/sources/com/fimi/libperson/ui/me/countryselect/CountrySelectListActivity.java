package com.fimi.libperson.ui.me.countryselect;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import com.fimi.kernel.base.BaseActivity;
import com.fimi.kernel.utils.StatusBarUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.adapter.CountryLetterSortAdapter;
import com.fimi.libperson.adapter.CountryLetterSortAdapter.OnShowLetterChangedListener;
import com.fimi.libperson.widget.TitleView;
import com.fimi.widget.sticklistview.CharacterParser;
import com.fimi.widget.sticklistview.LetterSideBar;
import com.fimi.widget.sticklistview.LetterSideBar.OnTouchingLetterChangedListener;
import com.fimi.widget.sticklistview.PinyinComparator;
import com.fimi.widget.sticklistview.SortModel;
import com.fimi.widget.sticklistview.util.StickyListHeadersListView;
import com.fimi.widget.sticklistview.util.StickyListHeadersListView.OnHeaderClickListener;
import com.fimi.widget.sticklistview.util.StickyListHeadersListView.OnLoadingMoreLinstener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountrySelectListActivity extends BaseActivity implements OnHeaderClickListener, OnItemClickListener, OnLoadingMoreLinstener, OnShowLetterChangedListener {
    public static final byte LOGIN_REQUEST_CODE = (byte) 1;
    public static final byte REGISTER_REQUEST_CODE = (byte) 2;
    public static final String SELECT_COUNTRY_AREO_CODE = "select_country_areo_code";
    private static final String TAG = CountrySelectListActivity.class.getSimpleName();
    CountryLetterSortAdapter mAdapter;
    private CharacterParser mCharacterParser;
    private EditText mEtSearch;
    private ImageButton mIbtnDeleteSearch;
    private LetterSideBar mLetterSideBar;
    private List<SortModel> mSourceDateFilterList = new ArrayList();
    private List<SortModel> mSourceDateList;
    StickyListHeadersListView mStickyLV;
    private TitleView mTitleView;

    /* Access modifiers changed, original: protected */
    public void setStatusBarColor() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* Access modifiers changed, original: protected */
    public int getContentViewLayoutID() {
        return R.layout.activity_country_select;
    }

    public void initData() {
        initView();
    }

    public void doTrans() {
        this.mIbtnDeleteSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CountrySelectListActivity.this.mEtSearch.setText(null);
            }
        });
    }

    public void initView() {
        this.mCharacterParser = CharacterParser.getInstance();
        PinyinComparator pinyinComparator = new PinyinComparator();
        this.mSourceDateList = filledData(getResources().getStringArray(R.array.country_code_list));
        String[] ss = getResources().getStringArray(R.array.common_places_list);
        Collections.sort(this.mSourceDateList, pinyinComparator);
        for (int i = ss.length - 1; i >= 0; i--) {
            for (int j = 0; j < this.mSourceDateList.size(); j++) {
                if (((SortModel) this.mSourceDateList.get(j)).getName().contains(ss[i])) {
                    this.mSourceDateList.add(0, this.mSourceDateList.remove(j));
                    ((SortModel) this.mSourceDateList.get(0)).setSortLetter("#");
                    break;
                }
            }
        }
        this.mAdapter = new CountryLetterSortAdapter(this, this.mSourceDateList);
        this.mAdapter.setOnShowLetterChangedListener(this);
        this.mStickyLV = (StickyListHeadersListView) findViewById(R.id.stickyList);
        this.mStickyLV.setAdapter(this.mAdapter);
        this.mStickyLV.setOnItemClickListener(this);
        this.mStickyLV.setOnHeaderClickListener(this);
        this.mStickyLV.setLoadingMoreListener(this);
        this.mLetterSideBar = (LetterSideBar) findViewById(R.id.cs_letter_sb);
        this.mLetterSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
            public void onTouchingLetterChanged(String letter) {
                CountrySelectListActivity.this.mStickyLV.setSelection(CountrySelectListActivity.this.mAdapter.getPositionForSection(letter.charAt(0)));
            }
        });
        this.mEtSearch = (EditText) findViewById(R.id.et_cs_search);
        this.mEtSearch.setVisibility(0);
        this.mEtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    CountrySelectListActivity.this.mAdapter.updateListView(CountrySelectListActivity.this.mSourceDateList);
                    CountrySelectListActivity.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                String searchText = CountrySelectListActivity.this.mEtSearch.getText().toString();
                CountrySelectListActivity.this.mSourceDateFilterList.clear();
                for (int h = 0; h < CountrySelectListActivity.this.mSourceDateList.size(); h++) {
                    if (((SortModel) CountrySelectListActivity.this.mSourceDateList.get(h)).getName().contains(searchText)) {
                        CountrySelectListActivity.this.mSourceDateFilterList.add(CountrySelectListActivity.this.mSourceDateList.get(h));
                    }
                }
                if (CountrySelectListActivity.this.mSourceDateFilterList.size() > 0) {
                    CountrySelectListActivity.this.mAdapter.updateListView(CountrySelectListActivity.this.mSourceDateFilterList);
                    CountrySelectListActivity.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    if (R.id.et_cs_search == CountrySelectListActivity.this.mEtSearch.getId()) {
                        CountrySelectListActivity.this.mIbtnDeleteSearch.setVisibility(0);
                    }
                } else if (R.id.et_cs_search == CountrySelectListActivity.this.mEtSearch.getId()) {
                    CountrySelectListActivity.this.mIbtnDeleteSearch.setVisibility(8);
                }
            }
        });
        this.mTitleView = (TitleView) findViewById(R.id.title_view);
        this.mTitleView.setTvTitle(this.mContext.getResources().getString(R.string.country_select_title));
        this.mIbtnDeleteSearch = (ImageButton) findViewById(R.id.ibtn_delete_search);
    }

    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList();
        int n = date.length;
        for (int i = 0; i < n; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            String pinyin = this.mCharacterParser.getSelling(date[i]);
            sortModel.setPinyin(pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetter(sortString.toUpperCase());
            } else {
                sortModel.setSortLetter("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    public void OnLoadingMore() {
    }

    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        setResult(-1, getIntent().putExtra(SELECT_COUNTRY_AREO_CODE, ((SortModel) this.mAdapter.getItem(position)).getName()));
        finish();
    }

    public void onShowLetterChanged(String letter) {
        this.mLetterSideBar.updateLetter(letter);
    }
}
