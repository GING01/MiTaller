package mta.epn.ginghogam.com.mitaller.model;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 27.11.2017.
 */

public class CellModel implements ISortableModel,IFilterableModel {
    private String mId;
    private Object mData;

    public CellModel(String pId, Object mData) {
        this.mId = pId;
        this.mData = mData;
    }

    public Object getData() {
        return mData;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public Object getContent() {
        return mData;
    }

    @Override
    public String getFilterableKeyword() {
        return null;
    }
}
