//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_ICATEGORY_HPP
#define IL2CPPANDROID_ICATEGORY_HPP

#include "BaseWidget.hpp"

class ICategory : public BaseWidget {
public:
    /* Constructor
     * pId: Numeric ID of the category
     * pName: Name of the category
     */
    ICategory(const std::string& pName) {
        type = "ICategory";
        name = pName;
    }

    json ToJson() const override {
        return {
                {"type", type},
                {"name", name},
        };
    }
};
#endif //IL2CPPANDROID_ICATEGORY_HPP
