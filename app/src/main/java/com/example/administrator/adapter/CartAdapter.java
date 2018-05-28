package com.example.administrator.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.bean.Goods;
import com.example.administrator.bean.StoreInfo;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ViewUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CartAdapter extends BaseExpandableListAdapter {

    private List<StoreInfo> groups;
    private Map<String, List<Goods>> childrens;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEditorListener groupEditorListener;
    private int count = 0;
    private boolean flag = true;  //组的编辑按钮是否可见

    public CartAdapter(List<StoreInfo> groups,Map<String,List<Goods>> childrens,Context context){
        this.groups = groups;
        this.childrens = childrens;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return childrens.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Goods> childs = childrens.get(String.valueOf(groups.get(groupPosition).getId()));
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_cart_group,null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        //设置店铺名字
        groupViewHolder.storeName.setText(group.getName());
        //设置选中用户时的监听事件
        groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //店铺状态设置为选中时，所有商品都是选中
                group.setChoosed(((CheckBox)v).isChecked());
                checkInterface.checkGroup(groupPosition,((CheckBox)v).isChecked());
            }
        });
        groupViewHolder.storeCheckBox.setChecked(group.isChoosed());

        //当我们按下ActionBar的 "编辑"按钮， 应该把所有组的文字显示"编辑",并且设置按钮为不可见
        // 当我们完成编辑后，再把组的编辑按钮设置为可见
        if(group.isActionBarEditor()){
            group.setEditor(false);
            groupViewHolder.storeEdit.setVisibility(View.GONE);
            flag = false;
        }else{
            flag = true;
            groupViewHolder.storeEdit.setVisibility(View.VISIBLE);
        }

        //当我们按下组的"编辑"按钮后，组处于编辑状态，文字显示"完成"
        // 当我们点击“完成”按钮后，文字显示"编辑“,组处于未编辑状态
        if(group.isEditor()){
            groupViewHolder.storeEdit.setText("完成");
        }else{
            groupViewHolder.storeEdit.setText("编辑");
        }

        groupViewHolder.storeEdit.setOnClickListener(new GroupViewClick(group,groupPosition,groupViewHolder.storeEdit));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_cart_product,null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        /*
        //根据组的编辑按钮的可见与不可见，去判断是组对下辖的子元素编辑  还是ActionBar(top_cart)对组的下瞎元素的编辑
        //如果组的编辑按钮可见，那么肯定是组对自己下辖元素的编辑
        // 如果组的编辑按钮不可见，那么肯定是ActionBar对组下辖元素的编辑
         */
        if(flag){//ActionBar完成编辑或者没有点击
            if(groups.get(groupPosition).isEditor()){ //自己对改组的编辑
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            }else{
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        }else{//ActionBar点击的时候
            if(groups.get(groupPosition).isActionBarEditor()){
                childViewHolder.delGoods.setVisibility(View.GONE);
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            }else{
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        }

        final Goods child = (Goods) getChild(groupPosition,childPosition);
        if(child != null){
            childViewHolder.goodsName.setText(child.getTitle());
            childViewHolder.goodsPrice.setText("￥"+child.getPrice());
            childViewHolder.goodsNum.setText(String.valueOf(child.getCount()));
            //网络加载图片
            childViewHolder.setImageURL(context,getUrl()+child.getPic0());
            childViewHolder.goods_size.setText("类型"+child.getType());
            //设置打折前原价
//            SpannableString spannableString = new SpannableString("￥9999")
//            StrikethroughSpan span = new StrikethroughSpan();
//            spannableString.setSpan(span,0,spannableString.length()-1+1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //避免无限次append
            if(childViewHolder.goodsPrimePrice.length() > 0){
                childViewHolder.goodsPrimePrice.setText("");
            }
//            childViewHolder.goodsPrimePrice.setText(spannableString);
            childViewHolder.goodsBuyNum.setText("x1");
            childViewHolder.singleCheckBox.setChecked(child.isChoosed());
            childViewHolder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.setChoosed(((CheckBox)v).isChecked());
                    childViewHolder.singleCheckBox.setChecked(((CheckBox)v).isChecked());
                    checkInterface.checkChild(groupPosition,childPosition,((CheckBox)v).isChecked());
                }
            });
            childViewHolder.increaseGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition,childPosition,childViewHolder.goodsNum,childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.reduceGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition,childPosition,childViewHolder.goodsNum,childViewHolder.singleCheckBox.isChecked());
                }
            });
            childViewHolder.goodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(groupPosition,childPosition,childViewHolder.goodsNum,childViewHolder.singleCheckBox.isChecked(),child);
                }
            });
            childViewHolder.delGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("确定要删除该商品吗")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(groupPosition,childPosition);
                                }
                            })
                            .create()
                            .show();
                }
            });
        }

        return convertView;
    }

    private void showDialog(final int groupPosition,final int childPosition,final View showCountView,
                            final boolean isChecked,final Goods child){
        final AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_change,null);
        final AlertDialog dialog = alertDialog_Builder.create();
        dialog.setView(view);
        count = child.getCount();
        final EditText num = (EditText) view.findViewById(R.id.et_cart_change_num);
        num.setText(count+"");
        //自动弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ViewUtil.showKeyBoard(context,showCountView);
            }
        });
        final TextView increase = (TextView)view.findViewById(R.id.tv_cart_change_increaseNum);
        final TextView DeIncrease = (TextView)view.findViewById(R.id.tv_cart_change_reduceNum);
        final TextView pButton = (TextView)view.findViewById(R.id.tv_cart_change_sure);
        final TextView nButton = (TextView)view.findViewById(R.id.tv_cart_change_cancel);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(num.getText().toString().trim());
                if(number == 0){
                    dialog.dismiss();
                }else{
                    L.i_crz("数量="+number);
                    num.setText(String.valueOf(number));
                    child.setCount(number);
                    modifyCountInterface.doUpdate(groupPosition,childPosition,showCountView,isChecked);
                    dialog.dismiss();
                }
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                num.setText(String.valueOf(count));
            }
        });
        DeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 1){
                    count--;
                    num.setText(String.valueOf(count));
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public GroupEditorListener getGroupEditorListener(){
        return groupEditorListener;
    }

    public void setGroupEditorListener(GroupEditorListener groupEditorListener) {
        this.groupEditorListener = groupEditorListener;
    }

    public CheckInterface getCheckInterface() {
        return checkInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    static class GroupViewHolder{
        @BindView(R.id.cb_cart_group_choose)
        CheckBox storeCheckBox;
        @BindView(R.id.tv_cart_group_name)
        TextView storeName;
        @BindView(R.id.btn_cart_group_edit)
        TextView storeEdit;

        public GroupViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    //个人(店铺)的复选框
    public interface  CheckInterface{
        //组选框状态改变触发事件
        void checkGroup(int groupPosition,boolean isChecked);

        //子选框状态改变触发的事件
        void checkChild(int groupPosition,int childPosition,boolean isChecked);
    }

    //改变数量接口
    public interface ModifyCountInterface{
        /*
        **增加操作
        * @param groupPositon组元素的位置
        * @param childPosition子元素的位置
        * @param showCountView 用于展示变化后数量的view
        * @param isChecked 子元素是否选中
        *
         */
        void doIncrease(int groupPosition,int childPosition,View showCountView,boolean isChecked);

        void doDecrease(int groupPosition,int childPosition,View showCountView,boolean isChecked);

        void doUpdate(int groupPosition,int childPosition,View showCountView,boolean isChecked);

        //删除Item
        void childDelete(int groupPosition,int childPosition);
    }

    //监听编辑状态
    public interface GroupEditorListener{
        void groupEditor(int groupPosition);
    }

    //使某个小组处于编辑状态
    private class GroupViewClick implements View.OnClickListener{

        private StoreInfo group;
        private int groupPosition;
        private TextView editor;

        public GroupViewClick(StoreInfo group,int groupPosition,TextView editor){
            this.group = group;
            this.groupPosition = groupPosition;
            this.editor = editor;
        }

        @Override
        public void onClick(View v) {
            if(editor.getId() == v.getId()){
                groupEditorListener.groupEditor(groupPosition);
                if(group.isEditor()){
                    group.setEditor(false);
                }else{
                    group.setEditor(true);
                }
                notifyDataSetChanged();
            }
        }
    }

    static class ChildViewHolder{
        @BindView(R.id.cb_cart_product_choose)
        CheckBox singleCheckBox;
        @BindView(R.id.iv_cart_product_image)
        ImageView goodsImage;
        @BindView(R.id.tv_cart_product_name)
        TextView goodsName;
        @BindView(R.id.tv_cart_product_type)
        TextView goods_size;
        @BindView(R.id.tv_cart_product_price)
        TextView goodsPrice;
        @BindView(R.id.tv_cart_product_prime_price)
        TextView goodsPrimePrice;
        @BindView(R.id.tv_cart_product_buyNum)
        TextView goodsBuyNum;
        @BindView(R.id.rl_cart_product_data)
        RelativeLayout goodsData;
        @BindView(R.id.tv_cart_product_reduceNum)
        TextView reduceGoodsNum;
        @BindView(R.id.tv_cart_product_num)
        TextView goodsNum;
        @BindView(R.id.tv_cart_product_increaseNum)
        TextView increaseGoodsNum;
        @BindView(R.id.tv_cart_product_type1)
        TextView goodsSize;
        @BindView(R.id.tv_cart_product_del)
        TextView delGoods;
        @BindView(R.id.ll_cart_product_editData)
        LinearLayout editGoodsData;

        public ChildViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void setImageURL(Context context,String url){
            Glide.with(context).load(url).into(goodsImage);
            L.i_crz("url:"+url);
        }

    }

}
