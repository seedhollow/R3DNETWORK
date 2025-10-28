//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_IINPUTTEXT_HPP
#define IL2CPPANDROID_IINPUTTEXT_HPP

#include "BaseWidget.hpp"

class IInputText : public BaseWidget {
public:
    /* Constructor
     * pId: Numeric ID of the input text
     * pName: Name of the input text
     *
     */
    IInputText(int pId, const std::string& pName) {
        id = pId;
        type = "IInputText";
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


#endif //IL2CPPANDROID_IINPUTTEXT_HPP
