package com.buaa.tezlikai.smartsh.impl.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.tezlikai.smartsh.R;
import com.buaa.tezlikai.smartsh.base.BaseMenuDetailPager;
import com.buaa.tezlikai.smartsh.domain.PhotoBean;
import com.buaa.tezlikai.smartsh.domain.PhotoBean.PhotoNewsData;
import com.buaa.tezlikai.smartsh.global.Constants;
import com.buaa.tezlikai.smartsh.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 菜单详情页-组图
 * Created by Administrator on 2016/4/14.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    @ViewInject(R.id.gv_list)
    private GridView gvList;

    //组图集合
    private ArrayList<PhotoBean.PhotoNewsData> mPhotoList;

    private boolean isList = true;//当前页面默认是List

    private ImageButton btnDisplay;
    public PhotosMenuDetailPager(Activity mActivity, ImageButton btnDisplay) {
        super(mActivity);
        this.btnDisplay = btnDisplay;
        //给按钮设置一个点击事件
        btnDisplay.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_photo, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //做一个缓存
        String cache = CacheUtils.getCache(Constants.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){
            processResult(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processResult(responseInfo.result);
                //加入缓存
                CacheUtils.setCache(Constants.PHOTOS_URL,responseInfo.result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析数据
     * @param result
     */
    private void processResult(String result) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);

        mPhotoList = photoBean.data.news;
        lvList.setAdapter(new PhotoAdapter());
        gvList.setAdapter(new PhotoAdapter());

    }



    class PhotoAdapter extends BaseAdapter{

        //通过Xutil加载图片
        private BitmapUtils mBitmapUtils;

        public PhotoAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }


        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotoBean.PhotoNewsData getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null ){
                convertView = View.inflate(mActivity,R.layout.list_item_photo,null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotoNewsData item = getItem(position);

            holder.tvTitle.setText(item.title);
            mBitmapUtils.display(holder.ivIcon,item.listimage);

            return convertView ;
        }
    }

    static class ViewHolder{
        public TextView tvTitle;
        public ImageView ivIcon;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_display:
                if (isList){
                    isList = false;
                    lvList.setVisibility(View.GONE);
                    gvList.setVisibility(View.VISIBLE);
                    btnDisplay.setImageResource(R.mipmap.icon_pic_list_type);
                }else {
                    isList = true;
                    lvList.setVisibility(View.VISIBLE);
                    gvList.setVisibility(View.GONE);
                    btnDisplay.setImageResource(R.mipmap.icon_pic_grid_type);
                }
                break;
            default:
                break;
        }
    }
}
