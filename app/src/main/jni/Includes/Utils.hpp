#ifndef UTILS_HPP
#define UTILS_HPP

#include <cstdint>
#include <jni.h>

// DWORD alias for platform compatibility
using DWORD = unsigned long;

// External global variables
extern bool libLoaded;

namespace ToastLength {
    inline const int LENGTH_LONG = 1;
    inline const int LENGTH_SHORT = 0;
}

// Library/Memory-related functions
DWORD findLibrary(const char *library);
DWORD getAbsoluteAddress(const char *libraryName, DWORD relativeAddr);
bool isLibraryLoaded(const char *libraryName);
uintptr_t string2Offset(const char *c);

// JNI functions
jboolean isGameLibLoaded(JNIEnv *env, jobject thiz);
jbyteArray LoadFontData(JNIEnv *env, jclass thiz, jobject ctx);

#endif // UTILS_HPP
