
#pragma once
#include <cstdint>

union IntFloat {
    int i;
    float f;
};


class ObscuredTypes {
public:
    // Int
    static int GetObscuredInt(uint64_t location);
    static void SetObscuredInt(uint64_t location, int value);
    
    static int GetObscuredIntAB(uint64_t location);
    static void SetObscuredIntAB(uint64_t location, int value);

    // Float
    static float GetObscuredFloat(uint64_t location);
    static void SetObscuredFloat(uint64_t location, float value);
};