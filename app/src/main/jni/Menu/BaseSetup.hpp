//
// Created by Nik2143 on 28/04/2025.
//

#pragma once
#include <jni.h>

// Register native methods for com.android.support.Main
int RegisterMain(JNIEnv *env);

// Register native methods for com.android.support.Preferences
int RegisterPreferences(JNIEnv *env);

// Register native methods for com.android.support.BaseMenu
int RegisterBaseMenu(JNIEnv *env);

// Register native methods for com.android.support.DrawView
int RegisterDrawView(JNIEnv *env);

int RegisterLoader(JNIEnv* env);