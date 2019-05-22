package com.fimi.app.x8s.adapter.section;

import com.fimi.app.x8s.entity.X8B2oxFile;
import java.util.List;

public class X8B2oxSection extends AbsSection {
    private List<X8B2oxFile> list;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<X8B2oxFile> getList() {
        return this.list;
    }

    public void setList(List<X8B2oxFile> list) {
        this.list = list;
    }

    public X8B2oxSection(String title, List<X8B2oxFile> list, boolean hasHeader) {
        super(hasHeader);
        this.title = title;
        this.list = list;
    }

    public int getContentItemsTotal() {
        return this.list.size();
    }
}
