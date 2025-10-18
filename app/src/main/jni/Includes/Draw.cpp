//
// Created by github.com/seedhollow on 17/10/25.
//

#include "Draw.hpp"

Draw::Draw() {
    _env = nullptr;
    _cvsView = nullptr;
    _cvs = nullptr;
}

Draw::Draw(JNIEnv* env, jobject cvsView, jobject cvs) {
    this->_env = env;
    this->_cvsView = cvsView;
    this->_cvs = cvs;
}

bool Draw::isValid() const {
    return (_env != nullptr && _cvsView != nullptr && _cvs != nullptr);
}

int Draw::getWidth() const {
    if (isValid()) {
        jclass canvas = _env->GetObjectClass(_cvs);
        jmethodID width = _env->GetMethodID(canvas, "getWidth", "()I");
        return _env->CallIntMethod(_cvs, width);
    }
    return 0;
}

int Draw::getHeight() const {
    if (isValid()) {
        jclass canvas = _env->GetObjectClass(_cvs);
        jmethodID height = _env->GetMethodID(canvas, "getHeight", "()I");
        return _env->CallIntMethod(_cvs, height);
    }
    return 0;
}

void Draw::DrawLine(Unity::Color color, float thickness,
                    Unity::Vector2 start, Unity::Vector2 end) {
    if (isValid()) {
        jclass canvasView = _env->GetObjectClass(_cvsView);
        jmethodID drawline = _env->GetMethodID(canvasView, "DrawLine",
                                               "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        _env->CallVoidMethod(_cvsView, drawline, _cvs,
                             (int)color.a, (int)color.r, (int)color.g, (int)color.b,
                             thickness, start.x, start.y, end.x, end.y);
    }
}

void Draw::DrawText(Unity::Color color, const char* txt,
                    Unity::Vector2 pos, float size) {
    if (isValid()) {
        jclass canvasView = _env->GetObjectClass(_cvsView);
        jmethodID drawtext = _env->GetMethodID(canvasView, "DrawText",
                                               "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
        _env->CallVoidMethod(_cvsView, drawtext, _cvs,
                             (int)color.a, (int)color.r, (int)color.g, (int)color.b,
                             _env->NewStringUTF(txt), pos.x, pos.y, size);
    }
}

void Draw::DrawCircle(Unity::Color color, float stroke,
                      Unity::Vector2 pos, float radius) {
    if (isValid()) {
        jclass canvasView = _env->GetObjectClass(_cvsView);
        jmethodID drawcircle = _env->GetMethodID(canvasView, "DrawCircle",
                                                 "(Landroid/graphics/Canvas;IIIIFFFF)V");
        _env->CallVoidMethod(_cvsView, drawcircle, _cvs,
                             (int)color.a, (int)color.r, (int)color.g, (int)color.b,
                             stroke, pos.x, pos.y, radius);
    }
}

void Draw::DrawFilledCircle(Unity::Color color,
                            Unity::Vector2 pos, float radius) {
    if (isValid()) {
        jclass canvasView = _env->GetObjectClass(_cvsView);
        jmethodID drawfilledcircle = _env->GetMethodID(canvasView, "DrawFilledCircle",
                                                       "(Landroid/graphics/Canvas;IIIIFFF)V");
        _env->CallVoidMethod(_cvsView, drawfilledcircle, _cvs,
                             (int)color.a, (int)color.r, (int)color.g, (int)color.b,
                             pos.x, pos.y, radius);
    }
}

void Draw::DrawBox(Unity::Color color, float stroke, Unity::Rect rect) {
    Unity::Vector2 v1(rect.fX, rect.fY);
    Unity::Vector2 v2(rect.fX + rect.fWidth, rect.fY);
    Unity::Vector2 v3(rect.fX + rect.fWidth, rect.fY + rect.fHeight);
    Unity::Vector2 v4(rect.fX, rect.fY + rect.fHeight);

    DrawLine(color, stroke, v1, v2);
    DrawLine(color, stroke, v2, v3);
    DrawLine(color, stroke, v3, v4);
    DrawLine(color, stroke, v4, v1);
}

void Draw::DrawHorizontalHealthBar(Unity::Vector2 screenPos,
                                   float width, float maxHealth, float currentHealth) {
    screenPos -= Unity::Vector2(0.0f, 8.0f);
    DrawBox(Unity::Color(0, 0, 0, 255), 3,
            Unity::Rect(screenPos.x, screenPos.y, width + 2, 5.0f));
    screenPos += Unity::Vector2(1.0f, 1.0f);

    Unity::Color clr = Unity::Color(0, 255, 0, 255);
    float hpWidth = (currentHealth * width) / maxHealth;

    if (currentHealth <= (maxHealth * 0.6))
        clr = Unity::Color(255, 255, 0, 255);
    if (currentHealth < (maxHealth * 0.3))
        clr = Unity::Color(255, 0, 0, 255);

    DrawBox(clr, 3, Unity::Rect(screenPos.x, screenPos.y, hpWidth, 3.0f));
}

void Draw::DrawCrosshair(Unity::Color clr,
                         Unity::Vector2 center, int size) {
    float x = center.x - (size / 2.0f);
    float y = center.y - (size / 2.0f);
    DrawLine(clr, 3,
             Unity::Vector2(x, center.y),
             Unity::Vector2(x + size, center.y));
    DrawLine(clr, 3,
             Unity::Vector2(center.x, y),
             Unity::Vector2(center.x, y + size));
}
