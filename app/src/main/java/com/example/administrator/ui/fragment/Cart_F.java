package com.example.administrator.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.adapter.CartAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.bean.StoreInfo;
import com.example.administrator.http.ListGoodsCallback;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ViewUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;
import static com.example.administrator.http.UploadByServlet.getUrl;
import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

/**
 * Created by Administrator on 2018/2/2.
 */

public class Cart_F extends Fragment implements View.OnClickListener,CartAdapter.CheckInterface,CartAdapter.ModifyCountInterface,CartAdapter.GroupEditorListener{

    private String url="CartServlet";

    @BindView(R.id.lv_cart_listview)
    ExpandableListView listView;
    @BindView(R.id.cb_cart_all)
    CheckBox allCheckBox;
    @BindView(R.id.tv_cart_total_price)
    TextView totalPrice;
    @BindView(R.id.tv_cart_pay)
    TextView goPay;
    @BindView(R.id.ll_cart_order_info)
    LinearLayout orderInfo;
    @BindView(R.id.tv_cart_share_goods)
    TextView shareGoods;
    @BindView(R.id.tv_cart_collect_goods)
    TextView collectGoods;
    @BindView(R.id.tv_cart_del)
    TextView delGoods;
    @BindView(R.id.ll_cart_share_info)
    LinearLayout shareInfo;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.ptr_cart_ptrframe)
    PtrFrameLayout mPtrFrame;

    @BindView(R.id.tv_cart_top_cartnum)
    TextView shoppingcatNum;
    @BindView(R.id.tv_cart_top_edit)
    TextView actionBarEdit;
//    @BindView(R.id.rl_cart_empty)
//    LinearLayout empty_shopcart;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false是编辑，true是完成
    private boolean flag = false;
    private CartAdapter myAdapter;
    private List<StoreInfo> groups; //组元素列表
    private Map<String,List<Goods>> childs;  //子元素的列表
    private List<Goods> goods ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cart,null);
        //设置全屏
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);

        ButterKnife.bind(this,view);

        L.i_crz("Cart_F--onCreateView");

        initPtrFrame();

        initData();

        return view;
    }

    private void initPtrFrame(){
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        header.setPadding(dp2px(20),dp2px(20),0,0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                },2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,View content,View header){
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
    }

    private void initEvent(){
        actionBarEdit.setOnClickListener(this);
        myAdapter = new CartAdapter(groups,childs,mcontext);
        myAdapter.setCheckInterface(this);  //步骤1：设置复选框接口
        myAdapter.setModifyCountInterface(this);  //步骤2：设置增删的接口
        myAdapter.setGroupEditorListener(this);  //步骤3：监听组列表的编辑状态
        listView.setGroupIndicator(null);
        listView.setAdapter(myAdapter);
        for(int i = 0;i < myAdapter.getGroupCount();i++){
            listView.expandGroup(i);  //步骤4：初始化，将ExpandableListView以展开的方式显示
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiablePosition = view.getFirstVisiblePosition();
                int top = -1;
                View firstView = view.getChildAt(firstVisibleItem);
                L.i_crz("childCount="+view.getChildCount());//返回的是显示层面上的所包含的子view的个数
                if(firstView != null){
                    top = firstView.getTop();
                }
                L.i_crz("firstVisiableItem="+firstVisibleItem+",fistVisiablePosition="+firstVisiablePosition+",firstView="+firstView+",top="+top);
                if(firstVisibleItem == 0&& top ==0){
                    mPtrFrame.setEnabled(true);
                }else{
                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    //设置购物车的数量
    private void setCartNum(){
        int count = 0;
        for(int i = 0;i < groups.size();i++){
            StoreInfo group = groups.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<Goods> Childs = childs.get(group.getId());
            for(Goods childs :Childs){
                count++;
            }
        }

        //购物车已经清空
        if(count == 0){
            clearCart();
        }else{
            shoppingcatNum.setText("购物车（"+count+")");
        }
    }

    private void clearCart(){
        shoppingcatNum.setText("购物车(0)");
        actionBarEdit.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
//        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
     * 其Key是组元素的Id
     */
    //TODO
    private void initData(){
        mcontext = getActivity();
//        groups = new ArrayList<StoreInfo>();
//        childs = new HashMap<String, List<Goods>>();
//        for (int i = 0; i < 5; i++) {
//            groups.add(new StoreInfo(i + "", "小马的第" + (i + 1) + "号当铺"));
//            List<Goods> goods = new ArrayList<>();
//            for (int j = 0; j <= i; j++) {
//                int[] img = {R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz};
//                //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
//                goods.add(new Goods(i + "-" + j, "商品", groups.get(i).getName() + "的第" + (j + 1) + "个商品"));
//            }
//            childs.put(groups.get(i).getId(), goods);
//        }

        groups = new ArrayList<StoreInfo>();
        childs = new HashMap<String, List<Goods>>();

        SharedPreferences ShareP = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);

        OkHttpUtils
                .post()
                .url(getUrl()+url)
                .addParams("userid",ShareP.getString("userid",""))
                .build()
                .execute(new ListGoodsCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        L.e_crz("GoodsList--onError"+e);
                    }

                    @Override
                    public void onResponse(List<Goods> response, int id) {
                        goods = new ArrayList<Goods>(response);
                        L.i_crz("response:"+response.size()+",goods:"+goods.size());


                        for(int i = 0 ;i < goods.size() - 1;i++){
                            for(int j = i + 1;j < goods.size(); j++){
                                if(goods.get(i).getUserid().equals(goods.get(j).getUserid())){
                                    goods.remove(j);
                                    j--;
                                }
                            }
                        }
                        L.i_crz("goods:"+goods.size()+response);
                        for(int x = 0; x < goods.size();x++){
                            groups.add(new StoreInfo(goods.get(x).getUserid(),goods.get(x).getUsername()));
                        }
                        for(int y = 0; y < groups.size(); y++){
                            List<Goods> child = new ArrayList<Goods>();
                            for(int z = 0; z < response.size();z++){
                                if(groups.get(y).getId().equals(response.get(z).getUserid())){
                                    child.add(response.get(z));
                                }
                            }
                            childs.put(groups.get(y).getId(),child);
                            L.i_crz("goods:"+goods+response.size()+",child--:"+child.size()+",groups--:"+groups.size());
                        }
                        initEvent();
                    }
                });

    }

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete(){
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();  //待删除的组元素
        for(int i = 0;i < groups.size(); i++){
            StoreInfo group = groups.get(i);
            if(group.isChoosed()){
                toBeDeleteGroups.add(group);
            }
            List<Goods> toBeDeleteChilds = new ArrayList<Goods>();  //待删除的子元素
            List<Goods> child = childs.get(group.getId());
            for(int j = 0;j < child.size();j++){
                if(child.get(j).isChoosed()){
                    toBeDeleteChilds.add(child.get(j));
                }
            }
            child.removeAll(toBeDeleteChilds);
        }
        groups.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        myAdapter.notifyDataSetChanged();
    }

//    private void initActionBar() {
//        //隐藏标题栏
//        if (getSupportActionBar() != null) {
//            //去掉阴影
//            getSupportActionBar().setElevation(0);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowCustomEnabled(true);
//            View view = getLayoutInflater().inflate(R.layout.acitonbar, null);
//            findView(view);
//            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            ActionBar.LayoutParams lp = (ActionBar.LayoutParams) view.getLayoutParams();
//            lp.gravity = Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
//            getSupportActionBar().setCustomView(view, lp);
//        }
//
//
//    }

    @OnClick({R.id.cb_cart_all,R.id.tv_cart_pay,R.id.tv_cart_share_goods,R.id.tv_cart_collect_goods,
            R.id.tv_cart_del,R.id.tv_cart_top_edit})
    public void onClick(View v) {
        AlertDialog dialog;
        switch(v.getId()){
            case R.id.cb_cart_all:
                doCheckAll();
                break;
            case R.id.tv_cart_pay:
                if(mtotalCount == 0){
                    ViewUtil.toast(mcontext,"请选择要支付的商品");
                    return ;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("总计："+mtotalCount+"种商品"+mtotalPrice+"元");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        return ;
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.tv_cart_share_goods:
                if(mtotalCount == 0){
                    ViewUtil.toast(mcontext,"请选择要分享的商品");
                    return ;
                }
                ViewUtil.toast(mcontext,"分享成功");
                break;
            case R.id.tv_cart_collect_goods:
                if(mtotalCount == 0){
                    ViewUtil.toast(mcontext,"请选择要收藏的商品");
                    return ;
                }
                ViewUtil.toast(mcontext,"收藏成功");
                break;
            case R.id.tv_cart_del:
                if(mtotalCount == 0){
                    ViewUtil.toast(mcontext,"请选择要删除的商品");
                    return ;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("确定要删除该商品吗?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return ;
                    }
                });
                dialog.show();
                break;
            case R.id.tv_cart_top_edit:
                flag = !flag;
                setActionBarEditor();
                setVisiable();
                break;
            default:
                break;
        }
    }

    /**
     * @param groupPosition 组元素的位置
     * @param isChecked     组元素的选中与否
     *        思路:组元素被选中了，那么下辖全部的子元素也被选中
     */
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<Goods> child = childs.get(group.getId());
        for(int i = 0;i < child.size(); i++){
            child.get(i).setChoosed(isChecked);
        }
        if(isCheckAll()){
            allCheckBox.setChecked(true);  //全选
        }else{
            allCheckBox.setChecked(false);  //反选
        }
        myAdapter.notifyDataSetChanged();
        calulate();
    }

    //判断组元素是否全选
    private boolean isCheckAll(){
        for(StoreInfo group:groups){
            if(!group.isChoosed()){
                return false;
            }
        }
        return true;
    }

    /**
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param isChecked     子元素的选中与否
     */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;   //判断该组下面的所有子元素是否处于同一状态
        StoreInfo group = groups.get(groupPosition);
        List<Goods> child = childs.get(group.getId());
        for(int i = 0;i < child.size(); i++){
            //不选全中
            if(child.get(i).isChoosed() != isChecked){
                allChildSameState =false;
                break;
            }
        }
        if(allChildSameState){
            group.setChoosed(isChecked);  //如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        }else{
            group.setChoosed(false);  //否则一律视为为选中
        }

        myAdapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        Goods goods = (Goods) myAdapter.getChild(groupPosition,childPosition);
        int count = goods.getCount();
        count++;
        goods.setCount(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        myAdapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        Goods goods = (Goods) myAdapter.getChild(groupPosition,childPosition);
        int count = goods.getCount();
        if(count == 1){
            return;
        }
        count--;
        goods.setCount(count);
        ((TextView)showCountView).setText(""+count);
        myAdapter.notifyDataSetInvalidated();
        calulate();
    }

    @Override
    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        Goods goods = (Goods) myAdapter.getChild(groupPosition,childPosition);
        int count = goods.getCount();
        L.i_crz("进行数据更新，数量："+count+"");
        ((TextView)showCountView).setText(String.valueOf(count));
        myAdapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition) {
        StoreInfo group = groups.get(groupPosition);
        List<Goods> child = childs.get(group.getId());
        child.remove(childPosition);
        if(child.size() == 0){
            //TODO  删除服务端数据
            groups.remove(groupPosition);
        }
        myAdapter.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void groupEditor(int groupPosition) {

    }

    /**
     * ActionBar标题上点编辑的时候，只显示每一个店铺的商品修改界面
     * ActionBar标题上点完成的时候，只显示每一个店铺的商品信息界面
     */
    private void setActionBarEditor(){
        for(int i = 0;i <groups.size(); i++){
            StoreInfo group = groups.get(i);
            if(group.isActionBarEditor()){
                group.setActionBarEditor(false);
            }else{
                group.setActionBarEditor(true);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll(){
        for(int i = 0;i < groups.size(); i++){
            StoreInfo group = groups.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<Goods> child = childs.get(group.getId());
            for(int j = 0; j < child.size();j++){
                child.get(j).setChoosed(allCheckBox.isChecked()); //出现过错误
            }
        }
        myAdapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate(){
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for(int i = 0;i < groups.size(); i++){
            StoreInfo group = groups.get(i);
            List<Goods> child = childs.get(group.getId());
            for(int j = 0;j < child.size(); j++){
                Goods goods = child.get(j);
                if(goods.isChoosed()){
                    mtotalCount++;
                    mtotalPrice += Integer.valueOf(goods.getPrice()) * goods.getCount();
                }
            }
        }
        totalPrice.setText("￥"+mtotalPrice+"");
        goPay.setText("去支付("+mtotalCount+")");
        if(mtotalCount == 0){
            setCartNum();
        }else{
            shoppingcatNum.setText("购物车("+mtotalCount+")");
        }
    }

    private void setVisiable(){
        if(flag){
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            actionBarEdit.setText("完成");
        }else{
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            actionBarEdit.setText("编辑");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        myAdapter = null;
        childs.clear();
        groups.clear();
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }

}
