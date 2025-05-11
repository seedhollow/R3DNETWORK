#include <list>
#include <vector>
#include <cstring>
#include <pthread.h>
#include <thread>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include <numbers>

#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "dobby/dobby.h"
#include "Menu/Features.hpp"
#include "UnityResolve/UnityResolve.hpp"
#include "Includes/Utils.hpp"
#include "Includes/RemapTools.h"
#include "KittyMemory/MemoryPatch.h"
#include "Includes/ObscuredTypes.h"
#include "Includes/ESPManager.h"
#include "Menu/BaseSetup.hpp"
#include "Menu/Setup.hpp"

//Target lib here
#define IL2CPP_MODULE OBFUSCATE("libil2cpp.so")

void LoadNativeLibPath(JNIEnv *env, jclass clazz, jstring native_lib_dir) {
    LOGE(OBFUSCATE("Load injector in pid %d"), getpid());
    const char* dir = env->GetStringUTFChars(native_lib_dir, nullptr);
    std::string path = std::string(dir) + std::string (OBFUSCATE("/libLoader.so"));

    //Open the library containing the actual code
    void *open = dlopen(path.c_str(), RTLD_NOW);
    if (open == nullptr) {
        LOGE(OBFUSCATE("Error opening %s %s"), path.c_str(), dlerror());
    }
    RemapTools::RemapLibrary(OBFUSCATE("libLoader.so"));
}

void hook_thread();

void *hack_thread(void *) {
    LOGI(OBFUSCATE("pthread created"));

    espManager = new ESPManager();

    //Check if target lib is loaded
    do {
        sleep(1);
    } while (!KittyMemory::getLibraryMap(IL2CPP_MODULE).isValid());

    LOGI(OBFUSCATE("%s has been loaded"), (const char *) IL2CPP_MODULE);
    LOGI(OBFUSCATE("Trying to hook in il2cpp now..."));

    //sleep(5); // this is optional depending on your game if it takes time to load all the symbols

    UnityResolve::Init(dlopen(IL2CPP_MODULE, RTLD_NOW));

    // Start hooking
    LOGI(OBFUSCATE("Starting hooks"));
    hook_thread();
    return NULL;
}

__attribute__((constructor))
void init() {
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}

extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv((void **) &env, JNI_VERSION_1_6);

    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // Failed to obtain JNIEnv
    }

    if (RegisterDrawView(env) != 0)
        return JNI_ERR;
    if (RegisterMain(env) != 0)
        return JNI_ERR;
    if (RegisterBaseMenu(env) != 0)
        return JNI_ERR;
    if (RegisterMenu(env) != 0)
        return JNI_ERR;
    if (RegisterPreferences(env) != 0)
        return JNI_ERR;

    return JNI_VERSION_1_6;
}
