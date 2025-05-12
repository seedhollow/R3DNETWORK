//
// Created by Nik2143 on 28/04/2025.
//

#include <jni.h>
#include "Includes/obfuscate.h"
#include "Includes/Logger.h"
#include "Includes/Utils.hpp"
#include "Features.hpp"
#include "BaseSetup.hpp"

int RegisterPreferences(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/Preferences"));
    if (!clazz) {
        LOGE(OBFUSCATE("Preferences class not found"));
        return JNI_ERR; // Class not found
    }
    JNINativeMethod methods[] = {
            {OBFUSCATE("Changes"),
             OBFUSCATE("(Landroid/content/Context;ILjava/lang/String;IZLjava/lang/String;)V"),
             reinterpret_cast<void *>(Changes)},
    };
    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0)
        return JNI_ERR;
    return JNI_OK;
}

int RegisterBaseMenu(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/BaseMenu"));
    if (!clazz) {
        LOGE(OBFUSCATE("BaseMenu class not found"));
        return JNI_ERR; // Class not found
    }

    static const JNINativeMethod methods[] = {
//            {OBFUSCATE("IsGameLibLoaded")   , OBFUSCATE("()Z")                                                                              , reinterpret_cast<void *>(isGameLibLoaded)},
            {OBFUSCATE("LoadFontData")      , OBFUSCATE("(Landroid/content/Context;)[B")                                                    , reinterpret_cast<void *>(LoadFontData)},
            {OBFUSCATE("GetFeatureList")    , OBFUSCATE("()[Ljava/lang/String;")                                                            , reinterpret_cast<void *>(GetFeatureList)},
    };

    jint ret = env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(JNINativeMethod));
    if (ret != JNI_OK) {
        return ret; // Failed to register natives
    }

    LOGI(OBFUSCATE("Menu registered"));
    return JNI_OK;
}

int RegisterDrawView(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/DrawView"));
    if (!clazz) {
        // Failed to find the class
        return JNI_ERR;
    }

    JNINativeMethod methods[] = {
            {OBFUSCATE("OnDrawLoad"), OBFUSCATE("(Lcom/android/support/DrawView;Landroid/graphics/Canvas;)V"), reinterpret_cast<void*>(OnDrawLoad)}
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0) {
        // Failed to register
        return JNI_ERR;
    }

    return JNI_OK;
}
