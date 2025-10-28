//
// Created by github.com/seedhollow on 17/10/25.
//

#ifndef IL2CPPANDROID_VISUALS_HPP
#define IL2CPPANDROID_VISUALS_HPP

#include "Includes/Draw.hpp"
#include "Hacks/Vars.h"
#include "Includes/Logger.h"
#include "Includes/obfuscate.h"
#include "UnityResolve/UnityResolve.hpp"

class Visuals {
public:
    static void Update(Draw draw, int screenWidth, int screenHeight);
    static void DrawEspCrosshair(Draw draw);

private:
    using Unity = UnityResolve::UnityType;
    int crosshair_scale_size;
};

#endif //IL2CPPANDROID_VISUALS_HPP
