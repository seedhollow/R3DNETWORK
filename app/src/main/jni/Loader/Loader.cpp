//
// Created by rosetta on 16/09/2024.
//

#include <cstdio>
#include <string>
#include <jni.h>
#include <dlfcn.h>
#include <unistd.h>

#include "Includes/obfuscate.h"
#include "Includes/Logger.h"
#include "Includes/RemapTools.h"
#include "Includes/Utils.hpp"

bool remapLoaded = false;

std::string GetNativeLibraryDirectory() {
    char buffer[512];
    FILE *fp = fopen(OBFUSCATE("/proc/self/maps"), OBFUSCATE("re"));
    if (fp != nullptr) {
        while (fgets(buffer, sizeof(buffer), fp)) {
            if (strstr(buffer, OBFUSCATE("libLoader.so"))) {
                RemapTools::ProcMapInfo info{};
                char perms[10];
                char path[255];
                char dev[25];

                sscanf(buffer, OBFUSCATE("%lx-%lx %s %ld %s %ld %s"), &info.start, &info.end, perms,
                       &info.offset, dev, &info.inode, path);
                info.path = path;

                //Remove libLoader.so to get path
                std::string pathStr = std::string(info.path);
                if (!pathStr.empty()) {
                    pathStr.resize(pathStr.size() - 12); //libLoader.so = 12 chars
                }

                return pathStr;
            }
        }
    }
    return "";
}

jboolean isRemapSuccess(JNIEnv *env, jclass clazz) {
    return remapLoaded;
}

int RegisterLoader(JNIEnv* env) {
    jclass clazz = env->FindClass(OBFUSCATE("com/android/support/Loader"));
    if (!clazz) {
        return JNI_ERR; // Class not found
    }

    static const JNINativeMethod methods[] = {
            {OBFUSCATE("isRemapSuccess"), OBFUSCATE("()Z"), reinterpret_cast<void*>(isRemapSuccess)},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) != JNI_OK) {
        return JNI_ERR;
    }

    return JNI_OK;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);

    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // Failed to obtain JNIEnv
    }
    if (RegisterLoader(env)!=0){
        return JNI_ERR;
    }
    LOGI(OBFUSCATE("libLoader.so loaded in %d"), getpid());

    std::string native = GetNativeLibraryDirectory();
    if (native.empty()) {
        LOGE(OBFUSCATE("Error getting native library directory"));
        exit(1);
    }

    LOGI(OBFUSCATE("Found native library directory: %s"), native.c_str());
    std::string path = native + std::string(OBFUSCATE("libGameHelper.so"));

    // Open the library containing the actual code
    void *open = dlopen(path.c_str(), RTLD_NOW);
    if (open == nullptr) {
        LOGE(OBFUSCATE("Error opening libGameHelper.so %s"), dlerror());
        return JNI_ERR;
    }

    RemapTools::RemapLibrary(OBFUSCATE("libGameHelper.so"));
    remapLoaded = true;

    return JNI_VERSION_1_6;
}