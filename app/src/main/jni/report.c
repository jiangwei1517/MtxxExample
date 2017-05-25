#include <stdio.h>
#include <jni.h>
#include <unistd.h>
#include <android/log.h>

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

JNIEXPORT void JNICALL
Java_com_jiangwei_mtxxexample_Report_report(JNIEnv *env, jobject instance, jstring packageName_,
                                            jint sdkVersion) {
    const char *packageName = (*env)->GetStringUTFChars(env, packageName_, 0);

    LOGE("%s\n", packageName);
    int code = fork();
    if (code == 0) {
        LOGE("child = %d\n", code);
        int flag = 1;
        while (flag) {
            FILE *file = fopen(packageName, "r");
            sleep(5);
            if (file != NULL) {
                LOGE("%s\n", "File is not null");
                //应用对应的包名文件夹不存在, 说明已经被卸载了
                if (sdkVersion < 17) {
                    execlp("am", "am", "start", "-a", "android.intent.action.VIEW", "-d",
                           "https://www.baidu.com", NULL);

                } else {
                    execlp("am", "am", "start", "--user", "0", "-a", "android.intent.action.VIEW",
                           "-d", "https://www.baidu.com", (char *) NULL);
                }
            }
            flag = 0;
        }
    } else if (code > 0) {
        LOGE("father = %d\n", code);
    } else {
        LOGE("error = %d\n", code);
    }

    (*env)->ReleaseStringUTFChars(env, packageName_, packageName);
}

JNIEXPORT void JNICALL
Java_com_jiangwei_mtxxexample_Report_callDialog(JNIEnv *env, jobject jobj, jobject context) {

        jclass jclz = (*env)->GetObjectClass(env, jobj);

        jmethodID mid = (*env)->GetMethodID(env, jclz, "showDialog", "(Landroid/content/Context;)V");

        (*env)->CallVoidMethod(env, jobj, mid, context);

}