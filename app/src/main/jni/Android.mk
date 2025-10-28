LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libdobby
LOCAL_SRC_FILES := dobby/libraries/$(TARGET_ARCH_ABI)/libdobby.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/dobby/
include $(PREBUILT_STATIC_LIBRARY)


include $(CLEAR_VARS)

# Here is the name of your lib.
# When you change the lib name, change also on System.loadLibrary("") under OnCreate method on StaticActivity.java
# Both must have same name
LOCAL_MODULE    := GameHelper

# -std=c++17 is required to support AIDE app with NDK
LOCAL_CFLAGS := -w -s -Wno-error=format-security -fvisibility=hidden -fpermissive -fexceptions
LOCAL_CPPFLAGS := -w -s -Wno-error=format-security -fvisibility=hidden -Werror -std=c++20
LOCAL_CPPFLAGS += -Wno-error=c++11-narrowing -fpermissive -Wall -fexceptions
LOCAL_LDFLAGS += -Wl,--gc-sections,--strip-all,-llog
LOCAL_LDLIBS := -llog -landroid -lEGL -lGLESv2
LOCAL_ARM_MODE := arm

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_STATIC_LIBRARIES := libdobby
# Here you add the cpp file to compile
LOCAL_SRC_FILES := $(LOCAL_PATH)/Main.cpp \
	$(LOCAL_PATH)/Includes/Utils.cpp \
	$(LOCAL_PATH)/Includes/Draw.cpp \
	$(LOCAL_PATH)/Includes/ObscuredTypes.cpp \
	$(LOCAL_PATH)/Menu/JNILoader.cpp \
	$(LOCAL_PATH)/Menu/FeatureModule.cpp \
	$(LOCAL_PATH)/Menu/Menu.cpp \
	$(LOCAL_PATH)/KittyMemory/KittyMemory.cpp \
	$(LOCAL_PATH)/KittyMemory/MemoryPatch.cpp \
    $(LOCAL_PATH)/KittyMemory/MemoryBackup.cpp \
    $(LOCAL_PATH)/KittyMemory/KittyUtils.cpp \
    $(LOCAL_PATH)/Hacks/Hooks.cpp \
    $(LOCAL_PATH)/Hacks/Visuals.cpp \

# Convert comma-separated string to space-separated list
FLAVOR_DIMENSION_VARS := LAYOUT

# Dynamically include Android.mk from each flavor path
$(foreach FLAVOR_DIMENSION,$(FLAVOR_DIMENSION_VARS),\
    $(eval FLAVOR := $($(FLAVOR_DIMENSION))) \
    $(if $(FLAVOR),\
        $(info Including JNI for $(FLAVOR_DIMENSION)=$(FLAVOR))\
        $(if $(wildcard $(LOCAL_PATH)/../../$(FLAVOR)/jni/Android.mk),\
            $(eval include $(LOCAL_PATH)/../../$(FLAVOR)/jni/Android.mk),\
            $(warning Missing Android.mk for flavor: $(FLAVOR))\
        ),\
        $(warning No value set for dimension $(FLAVOR_DIMENSION))\
    )\
)

include $(BUILD_SHARED_LIBRARY)


# Loader
include $(CLEAR_VARS)

LOCAL_MODULE    := Loader
LOCAL_CFLAGS := -w -s -Wno-error=format-security -fvisibility=hidden -fpermissive -fexceptions
LOCAL_LDLIBS := -llog -landroid

LOCAL_SRC_FILES := Loader/Loader.cpp

include $(BUILD_SHARED_LIBRARY)
