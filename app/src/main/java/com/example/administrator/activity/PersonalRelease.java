package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ThreadPoolManager;
import com.example.administrator.utils.UUIDUtil;
import com.example.administrator.utils.UploadTask;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class PersonalRelease extends Activity implements View.OnClickListener{

    private String url = "Personal";

    private GridView GvAddPic;  //网格显示缩略图
    private Button BtSubmit;  //提交按钮
    private final int IMAGE_OPEN = 1;  //打开图片标记
    private String pathImage;  //选择图片路径
    private Bitmap bmp;  //导入临时缩略图
    private ArrayList<HashMap<String,Object>> imageItem;
    private SimpleAdapter simpleAdapter;  //图片适配器

    private EditText EtTitle;  //标题
    private EditText EtContent;  //内容
    private EditText EtPrice;  //价格
    private EditText EtCarriage;  //运费
    private Spinner SpAssortment;  //分类
    private Button BtBack;  //取消按钮

    private List<String> list;
    private String uploadFile = "";
    private String[] newfilename = {"none","none","none","none","none","none","none"};
    private String randomnum = UUIDUtil.getUUID();  //随机生成数字
    private String city;
    private String userid;
    private String usertoken;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_personal);

        /*
         * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        list = new ArrayList<>();
        init();
        getUserInfo();
        Addpic();  //添加图片
        initClick();
    }

    private void init(){
        GvAddPic = (GridView)findViewById(R.id.gv_personal_addpic);
        EtTitle = (EditText)findViewById(R.id.et_personal_title);
        EtContent = (EditText)findViewById(R.id.et_personal_content);
        EtPrice = (EditText)findViewById(R.id.et_personal_price);
        EtCarriage = (EditText)findViewById(R.id.et_personal_carriage);
        SpAssortment = (Spinner)findViewById(R.id.sp_personal_assortment);
        BtBack = (Button)findViewById(R.id.btn_personal_back);
        BtSubmit = (Button)findViewById(R.id.btn_personal_submit);
        BtBack.setOnClickListener(this);
        BtSubmit.setOnClickListener(this);
    }

    private void getUserInfo(){
        SharedPreferences shareP = getSharedPreferences("userinfo",MODE_PRIVATE);
        userid = shareP.getString("phone","");
        usertoken= shareP.getString("token","");
    }

    private void Addpic(){
        //载入添加图片的加号
        bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.gmacs_ic_more_normal);
        imageItem = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("itemImage",bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,imageItem,R.layout.item_addpic_grid,new String[]{"itemImage"},
                new int[]{R.id.iv_addpic_item});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如 map.put("itemImage",
         * R.drawable.img); 解决方法: 1.自定义继承BaseAdapter实现 2.ViewBinder()接口实现
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        GvAddPic.setAdapter(simpleAdapter);
    }

    private void initClick(){
        GvAddPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //添加图片
                    Toast.makeText(PersonalRelease.this,"添加图片",Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //通过onResume刷新数据
                    startActivityForResult(intent,IMAGE_OPEN);
                }else{
                    //删除图片
                    dialog(position);
                    Toast.makeText(PersonalRelease.this,"点击第"+1+position+"号图片",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    //获取图片路径
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //打开图片
        if(resultCode == RESULT_OK && requestCode == IMAGE_OPEN){
            Uri uri = data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())){
                //查询选择图片
                Cursor cursor = getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},
                        null,null,null);
                //若无图片
                if(null == cursor){
                    return ;
                }
                //光标移动至开头，获取图骗路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                list.add(pathImage);
            }
        }
    }

    //上传图片
    private void UpLoadPics(){
        for(int i = 0;i < list.size();i++){
            uploadFile = list.get(i);
            String temp[] = {};
            temp = uploadFile.replaceAll("\\\\","/").split("/");  //保留路径中最后一个/后面的内容
//            newfilename[i] = randomnum + "_" + temp[temp.length-1];
            newfilename[i] = "img"+i+"_"+randomnum;
            L.i_crz("Personal--UploadPics:"+uploadFile+"newfilename:"+newfilename[i]);
            if(uploadFile != null && uploadFile.length() > 0){
                UploadTask uploadTask = new UploadTask(this,newfilename[i]);
                uploadTask.execute(uploadFile);
            }
        }
    }

    //刷新图片
    @Override
    protected void onResume(){
        super.onResume();

        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem,
                    R.layout.item_addpic_grid, new String[] { "itemImage" },
                    new int[] { R.id.iv_addpic_item });
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            GvAddPic.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            // 刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    protected void dialog(final int position){
        String str = "是否删除图片";
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalRelease.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                list.remove(position - 1);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_personal_submit:
                UpLoadPics();
                city = getCity();
                final String assortment = SpAssortment.getSelectedItem().toString();
                ThreadPoolManager.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("userid",userid);
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("token",usertoken);
                        BasicNameValuePair bnvp2 = new BasicNameValuePair("title",EtTitle.getText().toString());
                        BasicNameValuePair bnvp3 = new BasicNameValuePair("content",EtContent.getText().toString());
                        BasicNameValuePair bnvp4 = new BasicNameValuePair("price",EtPrice.getText().toString());
                        BasicNameValuePair bnvp5 = new BasicNameValuePair("assortment",assortment);
                        BasicNameValuePair bnvp6 = new BasicNameValuePair("city",city);
                        BasicNameValuePair bnvp7 = new BasicNameValuePair("pic0",newfilename[0]);
                        BasicNameValuePair bnvp8 = new BasicNameValuePair("pic1",newfilename[1]);
                        BasicNameValuePair bnvp9 = new BasicNameValuePair("pic2",newfilename[2]);
                        BasicNameValuePair bnvp10 = new BasicNameValuePair("pic3",newfilename[3]);
                        BasicNameValuePair bnvp11 = new BasicNameValuePair("pic4",newfilename[4]);
                        BasicNameValuePair bnvp12 = new BasicNameValuePair("pic5",newfilename[5]);
                        BasicNameValuePair bnvp13 = new BasicNameValuePair("pic6",newfilename[6]);
                        BasicNameValuePair bnvp14 = new BasicNameValuePair("carriage",EtCarriage.getText().toString());
                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,
                                bnvp6,bnvp7,bnvp8,bnvp9,bnvp10,bnvp11,bnvp12,bnvp13,bnvp14);
//                                bnvp6,bnvp14);
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                });
                break;
            case R.id.btn_personal_back:
                finish();
                break;
            default:
                break;
        }
    }

    private String getCity(){
        SharedPreferences shareP = getSharedPreferences("localcity",MODE_PRIVATE);
        String city = shareP.getString("localcity","");
        return city;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    String response = (String)msg.obj;
                    if(response.equals("true")){
                        Toast.makeText(PersonalRelease.this,"发布成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PersonalRelease.this,"发布失败，请检查网络或是否登录",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    String b = (String) msg.obj;
                    Toast.makeText(PersonalRelease.this, b, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    String string = (String) msg.obj;
                    Toast.makeText(PersonalRelease.this, string, Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
}
