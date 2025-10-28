//
// Created by github.com/seedhollow on 28/10/25.
//

#ifndef IL2CPPANDROID_WIDGET_HPP
#define IL2CPPANDROID_WIDGET_HPP

#include <vector>
#include <memory>
#include "BaseWidget.hpp"
class Widget {
private:
    inline static std::vector<std::shared_ptr<BaseWidget>> m_Features;

public:
    template<typename T>
    static void Add(const T& feature) {
        m_Features.push_back(std::make_shared<T>(feature));
    }

    static std::string ToJsonString() {
        json array = json::array();
        for (auto& feat : m_Features) array.push_back(feat->ToJson());
        return array.dump();
    }
};


#endif //IL2CPPANDROID_WIDGET_HPP
