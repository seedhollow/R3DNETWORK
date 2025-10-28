//
// Created by rosetta on 13/09/2024.
//

#include "FeatureModule.hpp"


void FeatureModule::OnDraw(JNIEnv *env, jclass clazz, jobject draw_view, jobject canvas) {
    Draw draw(env, draw_view, canvas);
    if (draw.isValid()) {
        Visuals::Update(draw, draw.getWidth(), draw.getHeight());
    } else {
        LOGE(OBFUSCATE("Draw is not valid in OnDraw"));
    }
}

jstring FeatureModule::GetFeatureList(JNIEnv *env, jobject context) {
    RegisterFeatures();

    std::string json = Widget::ToJsonString();
    return env->NewStringUTF(json.c_str());
}


/** Here you can add your features using the Widget system
 * It's better to keep the IDs unique for each widget
 * And now we have various widgets available like ICheckBox, ISwitch, ISlider, ISpinner, ICollapse, ICategory, IRadioButton, IInputText, IInputInt, IButtonLink
 * Some feature are support some HTML tags like <font color=#FF0000>red text</font>
 *Refer to the WidgetExport.hpp for all available widgets
 **/
void FeatureModule::RegisterFeatures() {

    Widget::Add(ICategory(OBFUSCATE("Player Settings")));
    Widget::Add(ISwitch(4, OBFUSCATE("God Mode"), OBFUSCATE("Player will not get any damage except <font color=#FFFF00>fall from the map </font>"), Vars::PlayerData.godMode));
    Widget::Add(ICollapse(OBFUSCATE("Visual Settings"), {
            ICollapse::Child(ICheckBox(1, OBFUSCATE("ESP Crosshair"), OBFUSCATE("Display a crosshair in the center of the screen, <font color=#00FF00>only visible to you</font>"), Vars::PlayerData.ESPCrosshair)),
            ICollapse::Child(ISpinner(2, OBFUSCATE("Crosshair Color"), {OBFUSCATE("Red"), OBFUSCATE("Green"), OBFUSCATE("Blue")})),
            ICollapse::Child(ISlider(3, OBFUSCATE("Crosshair Size"), 10, 100)),
    }));

    Widget::Add(IInputText(5, "Player Name"));
    Widget::Add(IInputInt(6, "Player Age", 120));
    Widget::Add(IButtonLink(7, "Visit Website", "https://github.com/seedhollow/R3DNETWORK"));
}

void FeatureModule::OnFeatureChanged(JNIEnv *env, jclass clazz, jobject obj, jint featNum, jstring featName, jint value, jboolean boolean, jstring str) {

    const char* name = env->GetStringUTFChars(featName, nullptr);
    const char* strVal = str != nullptr ? env->GetStringUTFChars(str, nullptr) : "";

    LOGD(OBFUSCATE("Feature changed: [%d] %s | value=%d | bool=%d | str=%s"),
         featNum, name, value, boolean, strVal);

    switch (featNum) {
        case 1: Vars::PlayerData.ESPCrosshair   = boolean;  break;
        case 2: Vars::PlayerData.CrosshairColor = value;    break;
        case 3: Vars::PlayerData.CrosshairSize  = value;    break;
        case 4: Vars::PlayerData.godMode        = boolean;  break;
        default: break;
    }

    env->ReleaseStringUTFChars(featName, name);
    if (str != nullptr) env->ReleaseStringUTFChars(str, strVal);
}