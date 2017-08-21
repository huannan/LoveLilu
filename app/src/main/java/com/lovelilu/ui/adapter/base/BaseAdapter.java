package com.lovelilu.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter的简单封装
 */
public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected Context mCtx;

    protected int layoutResId;

    protected List<T> datas;

    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }


    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.datas = (datas == null ? new ArrayList<T>() : datas);
        this.mCtx = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        BaseViewHolder vh = new BaseViewHolder(view, mOnItemClickListener, mOnItemLongClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getItem(position);
        convert((H) holder, item, position);
    }

    @Override
    public int getItemCount() {
        if (datas == null || datas.size() <= 0)
            return 0;

        return datas.size();
    }

    public T getItem(int position) {
        if (position >= datas.size()) return null;
        return datas.get(position);
    }

    /**
     * 删除数据
     *
     * @param position
     */
    public void removeData(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        int size = datas.size();
        datas.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * 下拉刷新，清除原有数据，添加新数据
     *
     * @param newData
     */
    public void refreshData(List<T> newData) {
        clearData();
        datas.addAll(newData);
        notifyItemRangeChanged(0, datas.size());
    }

    /**
     * 插入数据
     *
     * @param data
     */
    public void addData(T data) {
        datas.add(data);
        notifyItemInserted(0);
    }

    /**
     * 在原来数据的末尾追加新数据
     *
     * @param moreData
     */
    public void loadMoreData(List<T> moreData) {
        int lastPosition = datas.size();
        datas.addAll(lastPosition, moreData);
        notifyItemRangeInserted(lastPosition, moreData.size());
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param viewHoder A fully initialized helper.
     * @param item      The item that needs to be displayed.
     * @param position
     */
    protected abstract void convert(H viewHoder, T item, int position);


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;

    }

    public List<T> getDatas() {
        return datas;
    }

    public void setItemLayout(int layoutResId) {
        this.layoutResId = layoutResId;
        notifyItemRangeChanged(0, datas.size());
    }
}