package com.james.ultimaterecyclerview.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * 通用的recylerview的工具类 实现自动下拉刷新和上拉加载,当数据为0的时候自动显示emptyview,当数据<pagecount时候(自动禁用上拉加载更多)
 */
public class RecyclerviewHelper implements UltimateRecyclerView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    /**
     * recylerview
     */
    private UltimateRecyclerView mRecyclerView;
    /**
     * context
     */
    private Context mContext;
    /**
     * adapter
     */
    private UltimateViewAdapter mAdapter;
    /**
     * 每页多少行
     */
    private int pageCount;
    /**
     * 当前adapter 对应的 List 数据
     */
    private List<?> mList;
    /**
     * 当前的页码
     */
    private int currentPageNumber;
    /**
     * 下拉刷新和上拉加载更多的回调
     */
    private RecyclerviewHelper.RecyclerViewCallBack callBack;

    public RecyclerviewHelper(@NonNull UltimateRecyclerView recyclerView, @NonNull Context context, @NonNull UltimateViewAdapter adapter, @NonNull int pageCount, @NonNull List<?> list, @NonNull RecyclerviewHelper.RecyclerViewCallBack callBack) {
        mRecyclerView = recyclerView;
        mContext = context;
        mAdapter = adapter;
        this.pageCount = pageCount;
        mList = list;
        LinearLayoutManager manager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.enableLoadmore();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setDefaultOnRefreshListener(this);
        this.callBack = callBack;
    }

    public RecyclerviewHelper(@NonNull UltimateRecyclerView recyclerView, @NonNull Context context, @NonNull UltimateViewAdapter adapter, @NonNull int pageCount, @NonNull List<?> list, @NonNull int gridCount, @NonNull RecyclerviewHelper.RecyclerViewCallBack callBack) {
        mRecyclerView = recyclerView;
        mContext = context;
        mAdapter = adapter;
        this.pageCount = pageCount;
        mList = list;
        GridLayoutManager manager = new GridLayoutManager(context, gridCount);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.enableLoadmore();
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setDefaultOnRefreshListener(this);
        this.callBack = callBack;
    }

    @Override
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        //计算出下一页 加载的页码
        if (mAdapter.getAdapterItemCount() % pageCount == 0) {
            currentPageNumber = (mAdapter.getAdapterItemCount() / pageCount) + 1;
        }
        callBack.onLoadMoreData();
    }

    /**
     * 刷新的时候
     * 1.重置下拉刷新
     * 2.重置当前的页码
     * 3.清空list数据
     * 4.回调刷新
     */
    @Override
    public void onRefresh() {
        mRecyclerView.reenableLoadmore();
        currentPageNumber = 0;
        mAdapter.clearInternal(mList);
        callBack.onRefreshData();
    }

    /**
     * 下拉加载之后,获得数据之后调用此方法
     * 1.遍历数据,加载adapter 尾部
     * 2.如果数据<pagecount 禁用上拉加载更多
     * 3.如果数据==0,显示emptyview
     *
     * @param data
     */
    public void insertData(List<?> data) {
        if (data == null)
            return;
        for (int i = 0; i < data.size(); i++) {
            mAdapter.insertLastInternal(mList, data.get(i));
        }
        if (data.size() < pageCount) {
            mRecyclerView.disableLoadmore();
        }
        //没有任何数据,显示空布局
        else if (mAdapter.getAdapterItemCount() < 1) {
            View emptyView = mRecyclerView.getEmptyView();
            if (emptyView != null) {
                mRecyclerView.showEmptyView();
            }
        }
        mRecyclerView.setRefreshing(false);
        data = null;
    }

    /**
     * 接口回调
     */
    public interface RecyclerViewCallBack {
        public void onLoadMoreData();

        public void onRefreshData();
    }

    /**
     * 返回当前的页码
     *
     * @return
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

}
