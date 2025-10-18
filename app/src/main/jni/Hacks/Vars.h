//
// Created by rosetta on 01/06/2024.
//

// Here you can store the variables of your features

#pragma once
namespace Vars {
    struct PlayerData_t {
        bool godMode        = false;
        bool ESP            = false;
        bool ESPCrosshair   = false;
        int CrosshairSize   = 1;
        int CrosshairColor  = 0;
    };

    inline PlayerData_t PlayerData;
}
