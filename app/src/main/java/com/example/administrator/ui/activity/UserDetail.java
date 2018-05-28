package com.example.administrator.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ThreadPoolManager;
import com.example.administrator.utils.UUIDUtil;
import com.example.administrator.utils.UploadTask;
import com.example.administrator.widget.RoundImageView;

import org.apache.http.message.BasicNameValuePair;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/9.
 */

public class UserDetail extends Activity{

    private String url = "UserHead";  //修改后端用户的头像

    private final int IMAGE_OPEN = 1;   //打开图片标记
    private String Imagepath;  //选择图片的路径
    private String uploadimage;   //修改的头像
    private String randomnum = UUIDUtil.getUUID();  //随机生成数字
    private String newfilename;  //上传图片的新名字

    @BindView(R.id.rl_userdetail_head)
    RelativeLayout RlHeadPhoto;
    @BindView(R.id.riv_userdetail_head)
    RoundImageView IvHeadPhoto;  //显示用户头象
    @BindView(R.id.btn_userdetail_back)
    Button BtBack;
    @BindView(R.id.btn_userdetail_username)
    Button BtUsername;
    @BindView(R.id.btn_userdetail_mygoodsaddress)
    Button BtmyDelieryAddress;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_userdetail);

        ButterKnife.bind(this);

    }

//    @Override
    @OnClick({R.id.rl_userdetail_head,R.id.btn_userdetail_back,R.id.btn_userdetail_mygoodsaddress,
            R.id.btn_userdetail_username})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_userdetail_head:
                Toast.makeText(this,"修改头像",Toast.LENGTH_SHORT).show();
                //从手机本地中获取图片
                Intent intent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,IMAGE_OPEN);
                break;
            case R.id.btn_userdetail_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    //获取图片路径
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //打开图片
        if(resultCode == RESULT_OK && requestCode == IMAGE_OPEN){
            Uri uri = data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())){
                //查询选择图片
                Cursor cursor = getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},
                        null,null,null);
                if( null == cursor ){
                    return;
                }
                //移动光标至开头，获取图片路径
                cursor.moveToFirst();
                Imagepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                displayImage(Imagepath);   //根据路径显示图片
                dialog();  //提示是否修改头像
            }
        }
    }

    private void displayImage(String Imagepath){
        String temp[] = Imagepath.replaceAll("\\\\","/").split("/");  //保留路径中最后一个/后面的内容
        newfilename = randomnum + "_" + temp[temp.length-1];
        L.i_crz("display--"+temp[temp.length-1]);
        if(Imagepath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(Imagepath);
            IvHeadPhoto.setImageBitmap(bitmap);   //显示图片
        }else{
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }

    //dialog用户提示用户是否修改图片
    protected void dialog(){
        String string = "是否修改图片";
        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetail.this);
        builder.setMessage(string);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                uploadimage = Imagepath;
                if(uploadimage != null && uploadimage.length() > 0){
                    //异步上传图片
                    UploadTask uploadTask = new UploadTask(UserDetail.this,newfilename);
                    uploadTask.execute(uploadimage);
                    ThreadPoolManager.getInstance().addTask(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp = new BasicNameValuePair("Imagepath",newfilename);
                            String response = UploadByServlet.post(url,bnvp);
                            Message msg = Message.obtain();
                            msg.obj = response;
                            handler.sendMessage(msg);
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            if(response.equals("true")){
                Toast.makeText(UserDetail.this,"Success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UserDetail.this,"请重新上传",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
