//
// Created by github.com/seedhollow on 19/10/26.
//

#ifndef IL2CPPANDROID_ITAB_HPP
#define IL2CPPANDROID_ITAB_HPP

#include "BaseWidget.hpp"
#include <vector>
#include <memory>
#include <initializer_list>

/* A sidebar tab. Every top-level widget should live inside one.
 * Widgets added outside of any ITab are grouped into an implicit "General" tab.
 *
 * Built-in icons:
 *   grid, hex, bell-off, wand, shield, eye, target, user,
 *   bolt, gear, info, star, gamepad, sword
 * Unknown names fall back to "grid".
 */
class ITab : public BaseWidget {
public:
    std::string icon;
    std::vector<std::shared_ptr<BaseWidget>> children;

    /* Constructor
     * pName: Label shown under the icon in the sidebar
     * pIcon: Built-in icon name (see list above)
     */
    explicit ITab(const std::string& pName, const std::string& pIcon = "grid") {
        id = -1;
        type = "ITab";
        name = pName;
        icon = pIcon;
    }

    /* Constructor (with children)
     * pName: Label shown under the icon in the sidebar
     * pIcon: Built-in icon name (see list above)
     * pChildren: Initializer list of child widgets
     */
    ITab(const std::string& pName, const std::string& pIcon, std::initializer_list<std::shared_ptr<BaseWidget>> pChildren) {
        id = -1;
        type = "ITab";
        name = pName;
        icon = pIcon;
        children = pChildren;
    }

    template<typename T>
    void AddChild(const T& feature) {
        children.push_back(std::make_shared<T>(feature));
    }

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
                {"icon", icon},
                {"children", jChildren}
        };
    }
};

#endif //IL2CPPANDROID_ITAB_HPP
