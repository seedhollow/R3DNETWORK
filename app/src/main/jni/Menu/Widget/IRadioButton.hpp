//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_IRADIOBUTTON_HPP
#define IL2CPPANDROID_IRADIOBUTTON_HPP
#include "BaseWidget.hpp"

class IRadioButton : public BaseWidget {
public:
    std::vector<std::string> options;

    /* Constructor
     * pId: Numeric ID of the radio button
     * pName: Name of the radio button
     * pOptions: List of options for the radio button
     */
    IRadioButton(int pId, const std::string& pName, const std::vector<std::string>& pOptions) {
        id = pId;
        type = "IRadioButton";
        name = pName;
        options = pOptions;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"options", options}
        };
    }
};

#endif //IL2CPPANDROID_IRADIOBUTTON_HPP
