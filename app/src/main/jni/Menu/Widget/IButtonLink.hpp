//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_IBUTTONLINK_HPP
#define IL2CPPANDROID_IBUTTONLINK_HPP
#include "BaseWidget.hpp"

class IButtonLink : public BaseWidget {
public:
    /* Constructor
     * pId: Numeric ID of the button link
     * pName: Name of the button link
     * pUrl: URL of the button link (optional)
     * */
    IButtonLink(int pId, const std::string& pName, const std::string& pUrl = "") {
        id = pId;
        type = "IButtonLink";
        name = pName;
        description = pUrl;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"description", description},
        };
    }
};

#endif //IL2CPPANDROID_IBUTTONLINK_HPP
