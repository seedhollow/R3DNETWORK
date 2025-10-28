//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_ISPINNER_HPP
#define IL2CPPANDROID_ISPINNER_HPP
#include "BaseWidget.hpp"

class ISpinner : public BaseWidget {
public:
    std::vector<std::string> options;

    /* Constructor
     * pId: Numeric ID of the spinner
     * pName: Name of the spinner
     * pOptions: List of options for the spinner
     */
    ISpinner(int pId, const std::string& pName, const std::vector<std::string>& pOptions) {
        id = pId;
        type = "ISpinner";
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

#endif //IL2CPPANDROID_ISPINNER_HPP
