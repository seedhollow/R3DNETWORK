//
// Created by rosetta on 13/09/2024.
//

#pragma once

#include <jni.h>

void OnDrawLoad(JNIEnv *env, jclass clazz, jobject draw_view, jobject canvas);

void LoadNativeLibPath(JNIEnv *env, jclass clazz, jstring native_lib_dir);

jobjectArray GetFeatureList(JNIEnv *env, jobject context);

void Changes(JNIEnv *env, jclass clazz, jobject obj,jint featNum, jstring featName, jint value,jboolean boolean, jstring str);