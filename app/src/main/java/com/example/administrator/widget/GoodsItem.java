package com.example.administrator.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.tongcheng.R;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/4/12.
 */

public class GoodsItem extends LinearLayout {

    private LinearLayout LlGoodsItem;   //item容器
    private ImageView IvGoodsPic;
    private TextView TvGoodsTitle;
    private TextView TvGoodsPrice;

    private Boolean isSelected = false;  //是否被选中
    private String Title;
    private String Price;
    private String Pic;
    private MyItemClicked myItemClicked;

    public GoodsItem(Context context) {
        super(context,null);
    }

    public GoodsItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.item_goodsdetail,this);
        LlGoodsItem = (LinearLayout)findViewById(R.id.ll_goods_item);
        TvGoodsPrice = (TextView)findViewById(R.id.tv_item_goodsprice);
        TvGoodsTitle = (TextView)findViewById(R.id.tv_item_goodstitle);
        IvGoodsPic = (ImageView)findViewById(R.id.iv_item_goodspic);
        LlGoodsItem.setOnClickListener(new MyOnClick());
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
        TvGoodsTitle.setText(title);
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
        TvGoodsPrice.setText("￥"+price);
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic,Context context) {
        this.Pic = pic;
        Glide.with(context).load(getUrl()+"images/"+pic).into(IvGoodsPic);
    }

    private class MyOnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            myItemClicked.myItemClicked();
        }
    }

    public interface MyItemClicked {
        public void myItemClicked();
    }

    public void setMyItemClickedListener(MyItemClicked myItemClicked) {
        this.myItemClicked = myItemClicked;
    }
}
