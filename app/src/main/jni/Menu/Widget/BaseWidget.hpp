//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_BASEWIDGET_HPP
#define IL2CPPANDROID_BASEWIDGET_HPP
#include <string>
#include <vector>
#include "Includes/json.hpp"

using json = nlohmann::json;

class BaseWidget {
public:
    int id;
    std::string type;
    std::string name;
    std::string description;
    bool enabled = false;

    virtual json ToJson() const = 0;
    virtual ~BaseWidget() = default;
};


#endif //IL2CPPANDROID_BASEWIDGET_HPP
