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
    Widget::Clear();
    RegisterFeatures();

    std::string json = Widget::ToJsonString();
    return env->NewStringUTF(json.c_str());
}


/** Here you can add your features using the Widget system
 * It's better to keep the IDs unique for each widget
 * Some feature are support some HTML tags like <font color=#FF0000>red text</font>
 * Refer to the WidgetExport.hpp for all available widgets, and ITab.hpp for the icon names
 **/
void FeatureModule::RegisterFeatures() {

    Widget::Add(ITab(OBFUSCATE("Player"), OBFUSCATE("grid"), {
            ITab::Child(ITextView(OBFUSCATE("Welcome to <font color=#EC4857>R3D Network ID</font> mod menu! Here you can customize your gameplay experience."))),
            ITab::Child(ISwitch(4, OBFUSCATE("God Mode"), OBFUSCATE("Player will not get any damage except <font color=#FFC542>fall from the map</font>"), Vars::PlayerData.godMode)),
            ITab::Child(IRadioButton(8, OBFUSCATE("Difficulty"), {OBFUSCATE("Easy"), OBFUSCATE("Normal"), OBFUSCATE("Hard")})),
            ITab::Child(IInputFloat(10, OBFUSCATE("Speed Multiplier"), 10.0f)),
            ITab::Child(IButton(9, OBFUSCATE("Print Player Data"))),
    }));

    Widget::Add(ITab(OBFUSCATE("Visuals"), OBFUSCATE("eye"), {
            ITab::Child(ICheckBox(1, OBFUSCATE("ESP Crosshair"), OBFUSCATE("Display a crosshair in the center of the screen, only visible to you"), Vars::PlayerData.ESPCrosshair)),
            ITab::Child(ICollapse(OBFUSCATE("Crosshair Settings"), {
                    ICollapse::Child(ISpinner(2, OBFUSCATE("Crosshair Color"), {OBFUSCATE("Red"), OBFUSCATE("Green"), OBFUSCATE("Blue")})),
                    ICollapse::Child(ISlider(3, OBFUSCATE("Crosshair Size"), 10, 100)),
            })),
    }));

    Widget::Add(ITab(OBFUSCATE("Profile"), OBFUSCATE("user"), {
            ITab::Child(ICategory(OBFUSCATE("Player"))),
            ITab::Child(IInputText(5, OBFUSCATE("Player Name"))),
            ITab::Child(IInputInt(6, OBFUSCATE("Player Age"), 120)),
            ITab::Child(ICategory(OBFUSCATE("Links"))),
            ITab::Child(IButtonLink(7, OBFUSCATE("Visit Website"), OBFUSCATE("https://github.com/seedhollow/R3DNETWORK"))),
    }));
}

void FeatureModule::OnFeatureChanged(JNIEnv *env, jclass clazz, jobject obj, jint featNum, jstring featName, jint value, jfloat floatValue, jboolean boolean, jstring str) {

    const char* name = env->GetStringUTFChars(featName, nullptr);
    const char* strVal = str != nullptr ? env->GetStringUTFChars(str, nullptr) : "";

    LOGD(OBFUSCATE("Feature changed: [%d] %s | value=%d | float=%f | bool=%d | str=%s"),
         featNum, name, value, floatValue, boolean, strVal);

    switch (featNum) {
        case 1: Vars::PlayerData.ESPCrosshair   = boolean;  break;
        case 2: Vars::PlayerData.CrosshairColor = value;    break;
        case 3: Vars::PlayerData.CrosshairSize  = value;    break;
        case 4: Vars::PlayerData.godMode        = boolean;  break;
        case 5: LOGD(OBFUSCATE("Player Name changed to: %s"), strVal); break;
        case 6: LOGD(OBFUSCATE("Player Age changed to: %d"), value); break;
        case 7: LOGD(OBFUSCATE("Button Link clicked: %s"), strVal); break;
        case 8: LOGD(OBFUSCATE("Difficulty changed to: %d"), value); break;
        case 9:
            LOGI(OBFUSCATE("God Mode=%d | ESP Crosshair=%d | Crosshair Color=%d | Crosshair Size=%d | Speed Multiplier=%f"),
                 Vars::PlayerData.godMode, Vars::PlayerData.ESPCrosshair,
                 Vars::PlayerData.CrosshairColor, Vars::PlayerData.CrosshairSize,
                 Vars::PlayerData.SpeedMultiplier);
            break;
        case 10: Vars::PlayerData.SpeedMultiplier = floatValue; break;
        default: break;
    }

    env->ReleaseStringUTFChars(featName, name);
    if (str != nullptr) env->ReleaseStringUTFChars(str, strVal);
}