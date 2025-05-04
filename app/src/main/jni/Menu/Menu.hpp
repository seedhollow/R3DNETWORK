#include <jni.h>

//Big letter cause crash
void setText(JNIEnv *env, jobject obj, const char *text) ;

void showDialog(jobject ctx, JNIEnv *env, const char *title, const char *msg);

void showToast(JNIEnv *env, jobject thiz, const char *text, int length);
