//
// Created by Nik2143 on 28/04/2025.
// Modified by rosetta on 28/10/2025.
//

#ifndef IL2CPPANDROID_JNILOADER_HPP
#define IL2CPPANDROID_JNILOADER_HPP

#include <jni.h>

class JNILoader {
public:
    // Register all JNI classes in one go
    static int RegisterAll(JNIEnv *env);

private:
    // Individual registrations
    static int RegisterPreferences(JNIEnv *env);
    static int RegisterBaseMenu(JNIEnv *env);
    static int RegisterDrawView(JNIEnv *env);
};

#endif //IL2CPPANDROID_JNILOADER_HPP