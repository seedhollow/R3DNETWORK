//
// Created by github.com/seedhollow on 19/10/26.
//

#ifndef IL2CPPANDROID_IBUTTON_HPP
#define IL2CPPANDROID_IBUTTON_HPP

#include "BaseWidget.hpp"

class IButton : public BaseWidget {
public:
    /* Constructor
     * pId: Numeric ID of the button, OnFeatureChanged receives it on every tap (value is always 0)
     * pName: Label of the button
     */
    IButton(int pId, const std::string& pName) {
        id = pId;
        type = "IButton";
        name = pName;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
        };
    }
};

#endif //IL2CPPANDROID_IBUTTON_HPP
