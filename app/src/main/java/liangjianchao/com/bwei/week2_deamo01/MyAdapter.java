package liangjianchao.com.bwei.week2_deamo01;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by lenovo、 on 2017/8/11.
 */
public class MyAdapter extends BaseAdapter {
      List<DataEntity> list;
          Context context;


    public MyAdapter(List<DataEntity> list,Context context) {
              this.list = list;
              this.context = context;
          }


          @Override
          public int getCount() {
              return list == null ? 0 : list.size();
          }

          @Override
          public Object getItem(int i) {
              return list.get(i);
          }

          @Override
          public long getItemId(int i) {
              return i;
          }

          @Override
          public View getView(int i, View view, ViewGroup viewGroup) {
              ViewHolder holder=null;
              if(view==null){
                view=View.inflate(context,R.layout.item,null);
                  holder=new ViewHolder();


                  x.view().inject(holder,view);
                  view.setTag(holder);
              }else{
                  holder= (ViewHolder) view.getTag();
              }


           DataEntity  data = (DataEntity) getItem(i);
              holder.textView.setText(data.getNews_title());
              x.image().bind(holder.image,data.getPic_url());


              return view;
          }

          class ViewHolder{
              @ViewInject(R.id.textView11)
              TextView textView;
             @ViewInject(R.id.imageView)
             ImageView image;


          }
}
