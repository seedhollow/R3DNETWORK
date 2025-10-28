//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_ISLIDER_HPP
#define IL2CPPANDROID_ISLIDER_HPP

#include "BaseWidget.hpp"

class ISlider : public BaseWidget {
public:
    int minValue;
    int maxValue;

    /* Constructor
     * pId: Numeric ID of the slider
     * pName: Name of the slider
     * pMin: Minimum value of the slider
     * pMax: Maximum value of the slider
     */
    ISlider(int pId, const std::string& pName, int pMin, int pMax) {
        id = pId;
        type = "ISlider";
        name = pName;
        minValue = pMin;
        maxValue = pMax;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"min", minValue},
                {"max", maxValue}
        };
    }
};

#endif //IL2CPPANDROID_ISLIDER_HPP
