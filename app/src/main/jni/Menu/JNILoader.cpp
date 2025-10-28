//
// Created by Nik2143 on 28/04/2025.
//

#include <jni.h>
#include "Includes/obfuscate.h"
#include "Includes/Logger.h"
#include "Includes/Utils.hpp"
#include "FeatureModule.hpp"
#include "JNILoader.hpp"

// ----------------------------
// Register All JNI Bindings
// ----------------------------
int JNILoader::RegisterAll(JNIEnv *env) {
    if (RegisterPreferences(env) != JNI_OK) return JNI_ERR;
    if (RegisterBaseMenu(env) != JNI_OK) return JNI_ERR;
    if (RegisterDrawView(env) != JNI_OK) return JNI_ERR;

    LOGI(OBFUSCATE("All JNI classes registered successfully"));
    return JNI_OK;
}

// ----------------------------
// Preferences
// ----------------------------
int JNILoader::RegisterPreferences(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/Preferences"));
    if (!clazz) {
        LOGE(OBFUSCATE("Preferences class not found"));
        return JNI_ERR;
    }

    static const JNINativeMethod methods[] = {
            {OBFUSCATE("Changes"),
             OBFUSCATE("(Landroid/content/Context;ILjava/lang/String;IZLjava/lang/String;)V"),
             reinterpret_cast<void *>(FeatureModule::OnFeatureChanged)},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0) {
        LOGE(OBFUSCATE("Failed to register Preferences natives"));
        return JNI_ERR;
    }

    LOGI(OBFUSCATE("Preferences registered"));
    return JNI_OK;
}

// ----------------------------
// Base Menu
// ----------------------------
int JNILoader::RegisterBaseMenu(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/BaseMenu"));
    if (!clazz) {
        LOGE(OBFUSCATE("BaseMenu class not found"));
        return JNI_ERR;
    }

    static const JNINativeMethod methods[] = {
            {OBFUSCATE("LoadFontData"),
                    OBFUSCATE("(Landroid/content/Context;)[B"),
                    reinterpret_cast<void *>(LoadFontData)},
            {OBFUSCATE("GetFeatureList"),
                    OBFUSCATE("()Ljava/lang/String;"),
                    reinterpret_cast<void *>(FeatureModule::GetFeatureList)},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0) {
        LOGE(OBFUSCATE("Failed to register BaseMenu natives"));
        return JNI_ERR;
    }

    LOGI(OBFUSCATE("BaseMenu registered"));
    return JNI_OK;
}

// ----------------------------
// DrawView
// ----------------------------
int JNILoader::RegisterDrawView(JNIEnv *env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/DrawView"));
    if (!clazz) {
        LOGE(OBFUSCATE("DrawView class not found"));
        return JNI_ERR;
    }

    static const JNINativeMethod methods[] = {
            {OBFUSCATE("OnDrawLoad"),
             OBFUSCATE("(Lcom/android/support/DrawView;Landroid/graphics/Canvas;)V"),
             reinterpret_cast<void *>(FeatureModule::OnDraw)},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != 0) {
        LOGE(OBFUSCATE("Failed to register DrawView natives"));
        return JNI_ERR;
    }

    LOGI(OBFUSCATE("DrawView registered"));
    return JNI_OK;
}