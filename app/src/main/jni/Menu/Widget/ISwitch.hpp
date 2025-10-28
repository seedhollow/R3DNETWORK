//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_ISWITCH_HPP
#define IL2CPPANDROID_ISWITCH_HPP
#include "BaseWidget.hpp"
class ISwitch : public BaseWidget {
public:
    /* Constructor
     * pId: Numeric ID of the checkbox
     * pName: Name of the checkbox
     * pDesc: Description of the checkbox (optional)
     * pEnabled: Initial state of the checkbox (optional, default is false)
     */
    ISwitch(int pId, const std::string& pName, const std::string& pDesc = "", bool pEnabled = false) {
        id = pId;
        type = "ISwitch";
        name = pName;
        description = pDesc;
        enabled = pEnabled;
    }

    json ToJson() const override {
        return {
                {"id", id},
                {"type", type},
                {"name", name},
                {"description", description},
                {"enabled", enabled}
        };
    }
};

#endif //IL2CPPANDROID_ISWITCH_HPP
