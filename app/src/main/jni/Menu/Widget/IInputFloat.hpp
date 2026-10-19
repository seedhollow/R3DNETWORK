//
// Created by github.com/seedhollow on 19/10/26.
//

#ifndef IL2CPPANDROID_IINPUTFLOAT_HPP
#define IL2CPPANDROID_IINPUTFLOAT_HPP

#include "BaseWidget.hpp"

class IInputFloat : public BaseWidget {
public:
    float maxValue;
    int decimals;

    /* Constructor
     * pId: Numeric ID of the input, OnFeatureChanged receives the value as floatValue
     * pName: Label of the input
     * pMax: Maximum value allowed (optional, 0 means no limit)
     * pDecimals: Digits kept after the decimal point (optional, default is 2)
     */
    IInputFloat(int pId, const std::string& pName, float pMax = 0.0f, int pDecimals = 2) {
        id = pId;
        type = "IInputFloat";
        name = pName;
        maxValue = pMax;
        decimals = pDecimals;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"max", maxValue},
                {"decimals", decimals}
        };
    }
};

#endif //IL2CPPANDROID_IINPUTFLOAT_HPP
