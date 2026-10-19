package com.android.support.components;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public final class Theme {

    // ----------- Colors ------------
    public static int ACCENT        = 0xFFEC4857;
    public static int ACCENT_LIGHT  = 0xFFFF7B87; // gradient start for toggles and sliders
    public static int ACCENT_KNOB   = 0xFFFFFFFF; // knob color of a toggle that is on

    public static int PANEL_BG      = 0xF0161619;
    public static int PANEL_STROKE  = 0x14FFFFFF;
    public static int CARD_BG       = 0xFF27282C;
    public static int CARD_PRESSED  = 0xFF323338;
    public static int DIVIDER       = 0x12FFFFFF;

    public static int TEXT          = 0xFFF1F1F3;
    public static int TEXT_DIM      = 0xFF8E8F95;
    public static int TEXT_FAINT    = 0xFF5E5F65;

    public static int TRACK_OFF     = 0xFF3B3C42;
    public static int KNOB_OFF      = 0xFFF1F1F3;

    // ----------- Sizes ------------
    public static int PANEL_MAX_WIDTH  = 380;
    public static int PANEL_MAX_HEIGHT = 360;
    public static int PANEL_MARGIN     = 12;   // minimum distance from the screen edges
    public static int PANEL_RADIUS     = 16;
    public static float SIDEBAR_RATIO  = 0.25f;

    public static int HEADER_HEIGHT = 40;
    public static int CARD_RADIUS   = 10;
    public static int CARD_HEIGHT   = 46;
    public static int CARD_GAP      = 8;
    public static int CARD_PADDING  = 14;

    public static int BUBBLE_SIZE   = 46;
    public static String BUBBLE_TEXT = "R3D";

    // ----------- Text sizes (sp) ------------
    public static float TEXT_TITLE  = 16f;
    public static float TEXT_BODY   = 14.5f;
    public static float TEXT_SMALL  = 12f;
    public static float TEXT_TAB    = 11f;

    // ----------- Font ------------
    // false: system font, true: custom font (loaded from native LoadFontData)
    public static boolean USE_CUSTOM_FONT = false;

    private Theme() {
    }
}
