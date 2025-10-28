//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_ICOLLAPSE_HPP
#define IL2CPPANDROID_ICOLLAPSE_HPP

#include "BaseWidget.hpp"

#pragma once
#include "BaseWidget.hpp"
#include <vector>
#include <memory>
#include <initializer_list>

class ICollapse : public BaseWidget {
public:
    std::vector<std::shared_ptr<BaseWidget>> children;

    /* Constructor
     * pName: Name of the collapse
     */
    explicit ICollapse(const std::string& pName) {
        id = -1;
        type = "ICollapse";
        name = pName;
    }

    /* Constructor (with children)
     * pName: Name of the collapse
     * pChildren: Initializer list of child widgets
     */
    explicit ICollapse(const std::string& pName, std::initializer_list<std::shared_ptr<BaseWidget>> pChildren) {
        id = -1;
        type = "ICollapse";
        name = pName;
        children = pChildren;
    }

    /* Add a child widget
     * feature: The child widget to add
     */
    template<typename T>
    void AddChild(const T& feature) {
        children.push_back(std::make_shared<T>(feature));
    }

    // Static helper for cleaner syntax
    template<typename T>
    static std::shared_ptr<BaseWidget> Child(const T& feature) {
        return std::make_shared<T>(feature);
    }

    json ToJson() const override {
        json jChildren = json::array();
        for (const auto& child : children) {
            jChildren.push_back(child->ToJson());
        }

        return {
                {"type", type},
                {"name", name},
                {"children", jChildren}
        };
    }
};


#endif //IL2CPPANDROID_ICOLLAPSE_HPP
