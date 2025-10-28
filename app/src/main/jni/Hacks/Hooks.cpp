//
// Created by github.com/seedhollow on 17/10/25.
//

#include "Hooks.hpp"


void Hooks::InitHooks() {
    // ----------------- Hooks -------------------
    // You can hook your methods here
    // If your class have a namespace, you don't need to add the namespace to the class name
    // Example RedNetwork.PlayerManager -> PlayerManager

    // m_pArgs = {} if the method doesn't have any arguments
    // if you're lazy to input type data on args, you can use "*" depends how many arguments the method have
    // Example: m_pArgs = {"*", "*", "*"} if the method have 3 arguments
    // or you can use {"System.String", "System.Int32"} for more specific type matching
    UnityResolve::Hook(OBFUSCATE("PlayerManager"), OBFUSCATE("Update"), {}, (void *) Hooks::PlayerUpdate, (void **) &Hooks::orig_PlayerUpdate);

    // With DobbyHook
    DobbyHook(UnityResolve::Get(OBFUSCATE("Assembly-CSharp.dll"))->Get(OBFUSCATE("Player"))->Get<UnityResolve::Method>(OBFUSCATE("Update"))->function,(void *) Hooks::PlayerUpdate,(void **) &Hooks::orig_PlayerUpdate);
}

void (*Hooks::orig_PlayerUpdate)(void *pInstance) = nullptr;
void Hooks::PlayerUpdate(void *pInstance) {
    // Call original function

    // Your custom code here
    if (Vars::PlayerData.godMode) {
        uintptr_t healthOffset = 0x12; // Replace with actual offset

        std::int32_t healthOffsetByName = UnityResolve::Get(OBFUSCATE("Assembly-CSharp.dll"))->Get(OBFUSCATE("Player"))->Get<UnityResolve::Field>(OBFUSCATE("health"))->offset;

        // set obscured type value
        ObscuredTypes::SetObscuredFloat(reinterpret_cast<uintptr_t>(pInstance) + healthOffset, 1000.0f);
        // this is how you set a field value using UnityResolve.hpp
        UnityResolve::Get(OBFUSCATE("Assembly-CSharp.dll"))->Get(OBFUSCATE("Player"))->SetValue(pInstance, OBFUSCATE("health"), 9999);

        // void SetHealth(int health)
        UnityResolve::Get(OBFUSCATE("Assembly-CSharp.dll"))->Get(OBFUSCATE("Player"))->Get<UnityResolve::Method>(OBFUSCATE("SetHealth"), {"*"})->Invoke<void>(pInstance, 9999);
    }

    orig_PlayerUpdate(pInstance);
}