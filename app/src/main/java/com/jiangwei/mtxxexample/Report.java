package com.jiangwei.mtxxexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * author: jiangwei18 on 17/5/25 13:54 email: jiangwei18@baidu.com Hi: jwill金牛
 */

public class Report {
    private Man man = new Man();
    static {
        System.loadLibrary("report");
    }

    public native void report(String packageName, int sdkVersion);

    public native void callDialog(Context context);

    public native void callFatherMethod();

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("auther:jiangwei18");
        builder.setPositiveButton("好评", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
}
