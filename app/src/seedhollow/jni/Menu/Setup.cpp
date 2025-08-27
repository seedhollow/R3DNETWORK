#include "Menu/Setup.hpp"
#include <sstream>
#include "../Includes/obfuscate.h"
#include "../Includes/Logger.h"
#include "../Includes/Utils.hpp"
#include "Menu/Menu.hpp"

bool iconValid, settingsValid, initValid;

void Init(JNIEnv *env, jobject thiz, jobject ctx, jobject title, jobject bottomTitle){
    setText(env, title, OBFUSCATE("<b>-=[ R3D Network ID ]=-</b>"));
    setText(env, bottomTitle, OBFUSCATE("<b>- Modded by rrosetta | Platinmods.com -</b>"));

    // Set Colored Text
    // setText(env, title, OBFUSCATE("<b><p style=\"color:green\">Modded by Nik2143</p></b>"));

    showToast(env,ctx,OBFUSCATE("R3D Network ID"),ToastLength::LENGTH_LONG);

    // Set Colored Toast
    //Toast(env,ctx,OBFUSCATE("<b><font color=lime>Modded by Nik2143 - Exclusive on www.Platinmods.com</font></b>"),ToastLength::LENGTH_LONG);

    initValid = true;
}

int RegisterMenu(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/Menu"));
    if (!clazz) {
        LOGE(OBFUSCATE("Menu class not found"));
        return JNI_ERR; // Class not found
    }

    static const JNINativeMethod methods[] = {
            {OBFUSCATE("Init")              , OBFUSCATE("(Landroid/content/Context;Landroid/widget/TextView;Landroid/widget/TextView;)V")   , reinterpret_cast<void *>(Init)},
    };

    jint ret = env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(JNINativeMethod));
    if (ret != JNI_OK) {
        return ret; // Failed to register natives
    }

    LOGI(OBFUSCATE("Menu registered"));
    return JNI_OK;
}


