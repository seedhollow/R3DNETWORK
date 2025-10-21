//
// Created by rosetta on 13/09/2024.
//

#include "dobby.h"
#include "Features.hpp"
#include "../UnityResolve/UnityResolve.hpp"
#include "Includes/Draw.hpp"
#include "Hacks/Visuals.hpp"
#include "Hacks/Hooks.hpp"
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "KittyMemory/MemoryPatch.h"
#include "Hacks/Vars.h"
#include "Hacks/Offsets.h"
#include "Hacks/Hooks.hpp"

void OnDrawLoad(JNIEnv *env, jclass clazz, jobject draw_view, jobject canvas) {
    Draw draw = Draw(env, draw_view, canvas);
    if (draw.isValid()) {
        Visuals::Update(draw, draw.getWidth(), draw.getHeight());
    } else {
        LOGE(OBFUSCATE("Draw is not valid in OnDrawLoad"));
    }
}

// Do not change or translate the first text unless you know what you are doing
// Assigning feature numbers is optional. Without it, it will automatically count for you, starting from 0
// Assigned feature numbers can be like any numbers 1,3,200,10... instead in order 0,1,2,3,4,5...
// ButtonLink, Category, RichTextView and RichWebView is not counted. They can't have feature number assigned
// Toggle, ButtonOnOff and Checkbox can be switched on by default, if you add True_. Example: CheckBox_True_The Check Box
// To learn HTML, go to this page: https://www.w3schools.com/

jobjectArray GetFeatureList(JNIEnv *env, jobject context) {
    jobjectArray ret;

    const char *features[] = {
            OBFUSCATE("ICollapse_ESP Hack"),
            OBFUSCATE("1_CollapseAdd_ICheckBox_Crosshair"),
            OBFUSCATE("2_CollapseAdd_ISpinner_Crosshair Color_Red,Green,Blue"),
            OBFUSCATE("3_CollapseAdd_ISlider_Crosshair Size_1_5"),
            OBFUSCATE("4_CollapseAdd_ISwitch_God Mode"),
            OBFUSCATE("5_CollapseAdd_IRadioButton_Box Type_2D,3D"),
            OBFUSCATE("6_CollapseAdd_IInputText_Player Name"),
            OBFUSCATE("7_CollapseAdd_IInputInt_Player Health"),
            OBFUSCATE("8_CollapseAdd_IButton_Player ESP"),
            OBFUSCATE("9_CollapseAdd_IButtonLink_Website_https://github.com/seedhollow/IL2CppAndroid"),
            OBFUSCATE("10_CollapseAdd_ICategory_Player Info"),
            OBFUSCATE("11_CollapseAdd_ITextView_<b>Player Info</b> <br> <i>Player Name: </i> <u>Unknown</u> <br> <i>Player Health: </i> <u>100</u>"),
            };

    //Now you dont have to manually update the number everytime;
    int Total_Feature = (sizeof features / sizeof features[0]);
    ret = (jobjectArray)
            env->NewObjectArray(Total_Feature, env->FindClass(OBFUSCATE("java/lang/String")),
                                env->NewStringUTF(""));

    for (int i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));

    return (ret);
}

void Changes(JNIEnv *env, jclass clazz, jobject obj,jint featNum, jstring featName, jint value,jboolean boolean, jstring str) {

    LOGD(OBFUSCATE("Feature name: %d - %s | Value: = %d | Bool: = %d | Text: = %s"), featNum,env->GetStringUTFChars(featName, 0), value,boolean, str != NULL ? env->GetStringUTFChars(str, 0) : "");

    //BE CAREFUL NOT TO ACCIDENTLY REMOVE break;

    switch (featNum) {
        case 1:
            Vars::PlayerData.ESPCrosshair = boolean;
            break;
        case 2:
            Vars::PlayerData.CrosshairColor = value;
            break;
        case 3:
            Vars::PlayerData.CrosshairSize = value;
            break;
        case 4:
            Vars::PlayerData.godMode = boolean;
            break;
        default:
            break;
    }
}