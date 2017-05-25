package com.jiangwei.mtxxexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mt.mtxx.image.JNI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private ImageView mIv;
    private MyHandler mHandler = new MyHandler();
    private static final int SEND_BITMAP = 1;
    private JNI jni = new JNI();
    private Bitmap mBitmap;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                xxImage(mBitmap, jni, "StyleLomoHDR");
                Toast.makeText(MainActivity.this, "正在后台转换,请稍候...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_2:
                xxImage(mBitmap, jni, "StyleLomoC");
                Toast.makeText(MainActivity.this, "正在后台转换,请稍候...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_3:
                xxImage(mBitmap, jni, "StyleLomoB");
                Toast.makeText(MainActivity.this, "正在后台转换,请稍候...", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_BITMAP:
                    Bitmap b = (Bitmap) msg.obj;
                    mIv.setImageBitmap(b);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        mBtn1 = (Button) findViewById(R.id.btn_1);
        mBtn2 = (Button) findViewById(R.id.btn_2);
        mBtn3 = (Button) findViewById(R.id.btn_3);
        mIv = (ImageView) findViewById(R.id.iv);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        Report report = new Report();
        report.report("/data/data/" + getPackageName(), Build.VERSION.SDK_INT);
        report.callDialog(this);
    }

    private void xxImage(final Bitmap bitmap, final JNI jni, final String method) {
        final int[] pixels = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (method.equals("StyleLomoHDR")) {
                    jni.StyleLomoHDR(pixels, bitmap.getWidth(), bitmap.getHeight());
                } else if (method.equals("StyleLomoC")) {
                    jni.StyleLomoC(pixels, bitmap.getWidth(), bitmap.getHeight());
                } else if (method.equals("StyleLomoB")) {
                    jni.StyleLomoB(pixels, bitmap.getWidth(), bitmap.getHeight());
                }
                Bitmap bitmap1 =
                        Bitmap.createBitmap(pixels, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Message message = Message.obtain();
                message.what = SEND_BITMAP;
                message.obj = bitmap1;
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
