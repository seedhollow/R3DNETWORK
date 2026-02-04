//
// Created by github.com/seedhollow on 04/02/26.
//

#ifndef IL2CPPANDROID_ITEXTVIEW_HPP
#define IL2CPPANDROID_ITEXTVIEW_HPP

#include "BaseWidget.hpp"
class ITextView : public BaseWidget {
public:
  /* Constructor
   * pText: Initial text of the text view (optional, default is empty)
   */
  ITextView(const std::string &pText) {
    type = "ITextView";
    description = pText;
  }

  json ToJson() const override {
    return {
        {"type", type},
        {"name", name},
        {"description", description},
    };
  }

  std::string text;
};

#endif // IL2CPPANDROID_ITEXTVIEW_HPP
