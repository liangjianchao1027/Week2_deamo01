package liangjianchao.com.bwei.week2_deamo01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener{

    @ViewInject(R.id.xlistview1)
    private XListView xlv;
    private List<DataEntity> list;
    private MyAdapter adapter;
    private DbManager dbManager;
    int page =1;
    private App application;
    private ImageView imagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x.view().inject(this);

        list = new ArrayList<DataEntity>();
        adapter = new MyAdapter(list,this);
        xlv.setAdapter(adapter);
        xlv.setPullLoadEnable(true);
        xlv.setPullRefreshEnable(true);
        xlv.setXListViewListener(this);

        //存入数据库
        App app = (App) getApplication();
        dbManager = app.getDbManager();

        application = (App) getApplication();

        loadadd();

        initMeanu();
    }



    private void initMeanu() {

        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.shadow_width);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);

        View view = View.inflate(this,R.layout.item_left,null);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        imagebutton = view.findViewById(R.id.imageViewbutton);
        //为侧滑菜单设置布局
        menu.setMenu(view);
    }

    public void QQLongin(View view){

        application.getUmshareAPI().getPlatformInfo(this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {


                if(share_media==SHARE_MEDIA.QQ){
                   String url = map.get("iconurl");
                    if(url!=null){

                        Glide.with(MainActivity.this).load(url).into(imagebutton);
                    }
                }

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    public void mulu(View view){

          SharedPreferences pre = getSharedPreferences("user_setting", MODE_PRIVATE);
           boolean isNight = pre.getBoolean("night",false);

           if(isNight){
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
               pre.edit().putBoolean("night",false).commit();
           }else{
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
               pre.edit().putBoolean("night",true).commit();
           }

           recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        application.getUmshareAPI().onActivityResult(requestCode,resultCode,data);
    }

    private void loadadd() {



        try {
            List<DataEntity> lists  = dbManager.selector(DataEntity.class).where("page","=",page).findAll();

            if(lists!=null&&lists.size()>0){

                list.addAll(lists);
                adapter.notifyDataSetChanged();
                return;
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams("http://api.expoon.com/AppNews/getNewsList/type/1/p/"+page);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

               Gson gson = new Gson();
             Data data =   gson.fromJson(result,Data.class);

                list.addAll(data.getData());
                adapter.notifyDataSetChanged();



                for (DataEntity dates : data.getData()){
                    dates.setPage(page);
                }

                try {
                    dbManager.save(data.getData());
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onRefresh() {
        list.clear();
        page=1;
        loadadd();
        xlv.stopLoadMore();
        xlv.stopRefresh();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        xlv.setRefreshTime(format.format(date));

    }

    @Override
    public void onLoadMore() {

        page+=1;
        loadadd();
        xlv.stopLoadMore();
        xlv.stopRefresh();

    }
}
