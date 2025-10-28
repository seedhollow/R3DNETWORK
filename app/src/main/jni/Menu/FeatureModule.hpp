//
// Created by rosetta on 13/09/2024.
//

#ifndef IL2CPPANDROID_FEATUREMODULE_HPP
#define IL2CPPANDROID_FEATUREMODULE_HPP
#include <jni.h>
#include "dobby.h"
#include "Widget/Widget.hpp"
#include "Widget/WidgetExport.hpp"

#include "../UnityResolve/UnityResolve.hpp"
#include "Includes/Draw.hpp"
#include "Hacks/Visuals.hpp"
#include "Hacks/Hooks.hpp"
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "KittyMemory/MemoryPatch.h"
#include "Hacks/Vars.h"
#include "Hacks/Hooks.hpp"

class FeatureModule {
public:
    // Initialize or build the feature list
    static jstring GetFeatureList(JNIEnv *env, jobject context);

    // Called when a feature value changes
    static void OnFeatureChanged(JNIEnv *env, jclass clazz, jobject obj,
                                 jint featNum, jstring featName,
                                 jint value, jboolean boolean, jstring str);

    // Called when rendering happens
    static void OnDraw(JNIEnv *env, jclass clazz, jobject draw_view, jobject canvas);

private:
    static void RegisterFeatures();
};

#endif //IL2CPPANDROID_FEATUREMODULE_HPP
