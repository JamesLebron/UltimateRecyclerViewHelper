package com.james.ultimaterecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.james.ultimaterecyclerview.recyclerview.BaseAdapterHelper;
import com.james.ultimaterecyclerview.recyclerview.QuickAdapter;
import com.james.ultimaterecyclerview.recyclerview.RecyclerviewHelper;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerviewHelper.RecyclerViewCallBack {

    private UltimateRecyclerView mRecyclerview;
    private List<Bean> mList;
    private RecyclerviewHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        mRecyclerview = (UltimateRecyclerView) findViewById(R.id.RecyclerView);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new Bean("title" + i, "http://www.iyi8.com/uploadfile/2014/0422/20140422123756205.jpg"));
        }
        mRecyclerview.setEmptyView(R.layout.empty_view);
        QuickAdapter<Bean> adapter = new QuickAdapter<Bean>(this, R.layout.list_item_layout, mList) {
            @Override
            protected void convert(BaseAdapterHelper helper, Bean item) {
                helper.getTextView(R.id.tv_title).setText(item.getTitle());
                Glide.with(MainActivity.this).load(item.getImageUrl()).into(helper.getImageView(R.id.iv_image));
            }
        };
        mHelper = new RecyclerviewHelper(mRecyclerview, this, adapter, 10, mList, 2, this);
    }

    @Override
    public void onLoadMoreData() {
        //getdata from network
        doNetWork();
    }


    @Override
    public void onRefreshData() {
        //getdatafrom net work
        doNetWork();
        mRecyclerview.setRefreshing(false);
    }

    private void doNetWork() {
        //get data and parse to List
        List<Bean> tempList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tempList.add(new Bean("new data"+i, "http://img1.fjtv.net/material/news/img/640x/2015/03/201503031636424U42.jpg?u1To"));
        }
        mHelper.insertData(tempList);
    }

    public class Bean {
        String title;
        String ImageUrl;

        public Bean(String title, String imageUrl) {
            this.title = title;
            ImageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }
    }
}
