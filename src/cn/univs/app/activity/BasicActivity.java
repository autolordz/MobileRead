package cn.univs.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class BasicActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBmob();
    }

    protected void initBmob() {
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("0d6aa979f3a238f57f7425e8235f70ee")////aa1b96905091917ec4e7ede1a752199d
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(2)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

    }
}
