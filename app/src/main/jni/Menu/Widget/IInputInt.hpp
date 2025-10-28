//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_IINPUTINT_HPP
#define IL2CPPANDROID_IINPUTINT_HPP

#include "BaseWidget.hpp"

class IInputInt : public BaseWidget {
public:

    int maxValue;
    /* Constructor
     * pId: Numeric ID of the input int
     * pName: Label of the input int
     * pMax: Maximum value allowed
     */
    IInputInt(int pId, const std::string& pName, int pMax) {
        id = pId;
        type = "IInputInt";
        name = pName;
        maxValue = pMax;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"max", maxValue}
        };
    }
};


#endif //IL2CPPANDROID_IINPUTINT_HPP
