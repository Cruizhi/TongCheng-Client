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

import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.UUIDUtil;
import com.example.administrator.utils.UploadTask;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/3/30.
 */

public class BusinessRelease extends Activity{

    private String url = "Business";

    private final int IMAGE_OPEN = 1;  //打开图片标记
    private String pathImage;  //选择图片路径
    private Bitmap bmp;  //导入临时缩略图
    private ArrayList<HashMap<String,Object>> imageItem;
    private SimpleAdapter simpleAdapter;  //图片适配器

    @BindView(R.id.gv_business_addpic)
    GridView GvAddPic;  //网格显示缩略图
    @BindView(R.id.et_business_content)
    EditText EtContent;
    @BindView(R.id.et_business_title)
    EditText EtTitle;
    @BindView(R.id.sp_business_assortment)
    Spinner SpAssortment;
    @BindView(R.id.et_business_price)
    EditText EtPrice;
    @BindView(R.id.et_business_linkphone)
    EditText EtLinkPhone;
    @BindView(R.id.btn_business_back)
    Button BtBack;
    @BindView(R.id.btn_business_submit)
    Button BtSubmit;

    private List<String> list;
    private String uploadFile = "";
    private String[] newfilename = {"none","none","none","none","none","none","none"};
    private String randomnum = UUIDUtil.getUUID();  //随机生成数字
    private String city;
    private String userid;
    private String usertoken;
    private String username;
    private File[] file = new File[8];
    private List<File> lfile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_business);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        list = new ArrayList<>();
        ButterKnife.bind(this);
        getUserInfo();
        Addpic();  //添加图片
        initClick();
    }

    private void init(){
        GvAddPic = (GridView)findViewById(R.id.gv_business_addpic);
        SpAssortment = (Spinner)findViewById(R.id.sp_business_assortment);
        EtTitle = (EditText)findViewById(R.id.et_business_title);
        EtContent = (EditText)findViewById(R.id.et_business_content);
        EtPrice = (EditText)findViewById(R.id.et_business_price);
        SpAssortment = (Spinner)findViewById(R.id.sp_business_assortment);
        EtLinkPhone = (EditText)findViewById(R.id.et_business_linkphone);
        BtBack = (Button)findViewById(R.id.btn_business_back);
        BtSubmit = (Button)findViewById(R.id.btn_business_submit);
    }

    private void getUserInfo(){
        SharedPreferences shareP = getSharedPreferences("userinfo",MODE_PRIVATE);
        userid = shareP.getString("phone","");
        usertoken= shareP.getString("token","");
        username = shareP.getString("username","");
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
                    Toast.makeText(BusinessRelease.this,"添加图片",Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //通过onResume刷新数据
                    startActivityForResult(intent,IMAGE_OPEN);
                }else{
                    //删除图片
                    dialog(position);
                    Toast.makeText(BusinessRelease.this,"点击第"+1+position+"号图片",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

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

    private void UpLoadPics(){
        for(int i = 0;i < list.size();i++){
            uploadFile = list.get(i);
            String temp[] = {};
            temp = uploadFile.replaceAll("\\\\","/").split("/");  //保留路径中最后一个/后面的内容
            newfilename[i] = randomnum + "_" + temp[temp.length-1];
            L.i_crz("Business--UploadPics:"+uploadFile);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessRelease.this);
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

//    @Override
    @OnClick({R.id.btn_business_submit,R.id.btn_business_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_business_submit:
//                UpLoadPics();

                city = getCity();
                final String assortment = SpAssortment.getSelectedItem().toString();
                for(int i = 0;i < list.size();i++){
                    uploadFile = list.get(i);
                    newfilename[i] = "img"+i+"_"+randomnum+".jpg";
                    L.i_crz("Personal--UploadPics:"+uploadFile+"newfilename:"+newfilename[i]);
                    if(uploadFile != null && uploadFile.length() > 0){
                        file[i+1] = new File(uploadFile);
                        L.i_crz("Personal--file:"+file[i+1]);
                        lfile.add(file[i+1]);
                    }
                }
                Map<String, String> params = new HashMap<>();
                params.put("userid",userid);
                params.put("token",usertoken);
                params.put("username",username);
                params.put("title",EtTitle.getText().toString());
                params.put("content",EtContent.getText().toString());
                params.put("price",EtPrice.getText().toString());
                params.put("assortment",assortment);
                params.put("city",city);
                params.put("pic0",newfilename[0]);
                params.put("pic1",newfilename[1]);
                params.put("pic2",newfilename[2]);
                params.put("pic3",newfilename[3]);
                params.put("pic4",newfilename[4]);
                params.put("pic5",newfilename[5]);
                params.put("pic6",newfilename[6]);

                PostFormBuilder post = OkHttpUtils.post();
                post.url(getUrl()+"ReceiveImages");
                for (int i = 0;i<lfile.size();i++){
                    post.addFile("mFile", newfilename[i], file[i+1]);
                }
                RequestCall build1 = post.build();
                build1.execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        L.e_crz("Business--onError:"+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        L.i_crz("success upload"+response);
                    }
                });

                OkHttpUtils
                        .post()
                        .params(params)
                        .url(getUrl()+url)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                L.e_crz("Business--params--onError:"+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")){
                                    Toast.makeText(BusinessRelease.this,"发布成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(BusinessRelease.this,"发布失败，请检查网络或是否登录",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


//                ThreadPoolManager.getInstance().addTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        BasicNameValuePair bnvp0 = new BasicNameValuePair("userid",userid);
//                        BasicNameValuePair bnvp1 = new BasicNameValuePair("token",usertoken);
//                        BasicNameValuePair bnvp2 = new BasicNameValuePair("title",EtTitle.getText().toString());
//                        BasicNameValuePair bnvp3 = new BasicNameValuePair("content",EtContent.getText().toString());
//                        BasicNameValuePair bnvp4 = new BasicNameValuePair("price",EtPrice.getText().toString());
//                        BasicNameValuePair bnvp5 = new BasicNameValuePair("assortment",assortment);
//                        BasicNameValuePair bnvp6 = new BasicNameValuePair("linkphone",EtLinkPhone.getText().toString());
//                        BasicNameValuePair bnvp7 = new BasicNameValuePair("city",city);
//                        BasicNameValuePair bnvp8 = new BasicNameValuePair("pic0",newfilename[0]);
//                        BasicNameValuePair bnvp9 = new BasicNameValuePair("pic1",newfilename[1]);
//                        BasicNameValuePair bnvp10 = new BasicNameValuePair("pic2",newfilename[2]);
//                        BasicNameValuePair bnvp11 = new BasicNameValuePair("pic3",newfilename[3]);
//                        BasicNameValuePair bnvp12 = new BasicNameValuePair("pic4",newfilename[4]);
//                        BasicNameValuePair bnvp13 = new BasicNameValuePair("pic5",newfilename[5]);
//                        BasicNameValuePair bnvp14 = new BasicNameValuePair("pic6",newfilename[6]);
//                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,
//                                bnvp6,bnvp7,bnvp8,bnvp9,bnvp10,bnvp11,bnvp12,bnvp13,bnvp14);
//                        Message msg = new Message();
//                        msg.what = 2;
//                        msg.obj = response;
//                        handler.sendMessage(msg);
//                        L.i_crz("Business--ThreadPoolManager--reponse:"+response);
//                    }
//                });
                break;
            case R.id.btn_business_back:
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
                        Toast.makeText(BusinessRelease.this,"发布成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(BusinessRelease.this,"发布失败，请检查网络或是否登录",Toast.LENGTH_SHORT).show();
                    }
                    L.i_crz("Business--handle--2:"+response);
                    break;
                case 1:
                    String b = (String) msg.obj;
                    Toast.makeText(BusinessRelease.this, b, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    String string = (String) msg.obj;
                    Toast.makeText(BusinessRelease.this, string, Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
}
