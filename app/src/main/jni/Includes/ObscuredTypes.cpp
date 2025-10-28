//
// Created by github.com/seedhollow on 17/10/25.
//

#include "ObscuredTypes.hpp"

int ObscuredTypes::GetObscuredInt(uint64_t location) {
    int cryptoKey = *(int*)location;
    int encryptedValue = *(int*)(location + 0x4);
    int realValue = encryptedValue ^ cryptoKey;
    return realValue;
}

void ObscuredTypes::SetObscuredInt(uint64_t location, int value) {
    int cryptoKey = *(int*)location;
    int encryptedValue = value ^ cryptoKey;
    *(int*)(location + 0x4) = encryptedValue;
}

int ObscuredTypes::GetObscuredIntAB(uint64_t location) {
    // Identical logic — kept separate for compatibility
    int cryptoKey = *(int*)location;
    int encryptedValue = *(int*)(location + 0x4);
    int realValue = encryptedValue ^ cryptoKey;
    return realValue;
}

void ObscuredTypes::SetObscuredIntAB(uint64_t location, int value) {
    int cryptoKey = *(int*)location;
    int encryptedValue = value ^ cryptoKey;
    *(int*)(location + 0x4) = encryptedValue;
}

float ObscuredTypes::GetObscuredFloat(uint64_t location) {
    int cryptoKey = *(int*)location;
    IntFloat encryptedValue;
    encryptedValue.f = *(float*)(location + 0x4);

    IntFloat realValue;
    realValue.i = encryptedValue.i ^ cryptoKey;
    return realValue.f;
}

void ObscuredTypes::SetObscuredFloat(uint64_t location, float value) {
    int cryptoKey = *(int*)location;
    IntFloat realValue;
    realValue.f = value;

    IntFloat encryptedValue;
    encryptedValue.i = realValue.i ^ cryptoKey;
    *(float*)(location + 0x4) = encryptedValue.f;
}
