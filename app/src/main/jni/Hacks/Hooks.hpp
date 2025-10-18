//
// Created by rosetta on 05/07/2024.
//



#ifndef HOOKS_H
#define HOOKS_H

#include "Vars.h"
#include "UnityResolve/UnityResolve.hpp"
#include "Includes/ObscuredTypes.hpp"
#include "Includes/obfuscate.h"

class Hooks {
public:
    static void InitHooks();
    static void (*orig_PlayerUpdate)(void *pInstance);
    static void PlayerUpdate(void *pInstance);
};

#endif // HOOKS_H
