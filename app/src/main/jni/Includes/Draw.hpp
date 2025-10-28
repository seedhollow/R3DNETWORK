#ifndef IL2CPPANDROID_DRAW_HPP
#define IL2CPPANDROID_DRAW_HPP
#include <jni.h>
#include "UnityResolve/UnityResolve.hpp" // assuming you use UnityResolve::UnityType

class Draw {
private:
    JNIEnv* _env;
    jobject _cvsView;
    jobject _cvs;

public:
    Draw();
    Draw(JNIEnv* env, jobject cvsView, jobject cvs);

    bool isValid() const;
    int getWidth() const;
    int getHeight() const;

    void DrawLine(UnityResolve::UnityType::Color color, float thickness,
                  UnityResolve::UnityType::Vector2 start, UnityResolve::UnityType::Vector2 end);

    void DrawText(UnityResolve::UnityType::Color color, const char* txt,
                  UnityResolve::UnityType::Vector2 pos, float size);

    void DrawCircle(UnityResolve::UnityType::Color color, float stroke,
                    UnityResolve::UnityType::Vector2 pos, float radius);

    void DrawFilledCircle(UnityResolve::UnityType::Color color,
                          UnityResolve::UnityType::Vector2 pos, float radius);

    void DrawBox(UnityResolve::UnityType::Color color, float stroke,
                 UnityResolve::UnityType::Rect rect);

    void DrawHorizontalHealthBar(UnityResolve::UnityType::Vector2 screenPos,
                                 float width, float maxHealth, float currentHealth);

    void DrawCrosshair(UnityResolve::UnityType::Color clr,
                       UnityResolve::UnityType::Vector2 center, int size = 20);

private:
    using Unity = UnityResolve::UnityType;
};

#endif //IL2CPPANDROID_DRAW_HPP