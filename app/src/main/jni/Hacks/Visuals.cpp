//
// Created by github.com/seedhollow on 17/10/25.
//

#include "Visuals.hpp"


void Visuals::Update(Draw draw, int screenWidth, int screenHeight) {
    //Example to get camera
    //UnityResolve::UnityType::Camera *camera = UnityResolve::UnityType::Camera::GetMain();
    // Full documentation of UnityResolve.hpp
    // https://github.com/issuimo/UnityResolve.hpp
    if (Vars::PlayerData.ESPCrosshair) {
        DrawEspCrosshair(draw);
    }
}

void Visuals::DrawEspCrosshair(Draw draw) {
    LOGD(OBFUSCATE("Drawing Crosshair"));
    // Draw ESP here
    Unity::Color crosshair_color {0, 0, 0, 255};

    if (Vars::PlayerData.CrosshairColor == 0) {
        crosshair_color = Unity::Color(255, 0, 0, 255);
    } else if (Vars::PlayerData.CrosshairColor == 1) {
        crosshair_color = Unity::Color(0, 255, 0, 255);
    } else {
        crosshair_color = Unity::Color(0, 0, 255, 255);
    }

    draw.DrawCrosshair(crosshair_color,Unity::Vector2(draw.getWidth() / 2.0f,draw.getHeight() / 2.0f),Vars::PlayerData.CrosshairSize * 10);
}