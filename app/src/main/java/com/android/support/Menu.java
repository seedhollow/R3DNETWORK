package com.android.support;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.support.components.BubbleView;
import com.android.support.components.IButton;
import com.android.support.components.IButtonLink;
import com.android.support.components.ICategory;
import com.android.support.components.ICheckBox;
import com.android.support.components.ICollapse;
import com.android.support.components.IInputFloat;
import com.android.support.components.IInputInt;
import com.android.support.components.IInputText;
import com.android.support.components.IRadioButton;
import com.android.support.components.ISlider;
import com.android.support.components.ISpinner;
import com.android.support.components.ISwitch;
import com.android.support.components.ITextView;
import com.android.support.components.IconView;
import com.android.support.components.StatusView;
import com.android.support.components.Theme;
import com.android.support.components.Ui;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Floating menu: a draggable bubble that opens a panel with a tab sidebar on the left
 * and the selected tab's cards on the right. Everything is built in code.
 */
@Obfuscate
public class Menu extends BaseMenu implements Ui.Host {

    public static int POS_X = 0;
    public static int POS_Y = 100;

    private static final String GENERAL_TAB = "General";
    private static final String SETTINGS_TAB = "Settings";

    // Preferences treats feature -1 as the "load saved values" flag
    private static final int SAVE_SETTINGS_ID = -1;
    private static final String SAVE_SETTINGS = "Save settings";

    // ----------- Window Manager ------------
    WindowManager mWindowManager;
    WindowManager.LayoutParams vmParams;
    private WindowManager.LayoutParams canvasParams;

    // ----------- Views ------------
    DrawView drawView;
    private final Ui ui;
    private FrameLayout root;
    private BubbleView bubble;
    private LinearLayout panel;
    private ScrollView sidebarScroll, contentScroll;
    private LinearLayout sidebar, content;
    private TextView titleText, creditText;

    // ----------- Components ------------
    private ISwitch iSwitch;
    private ICheckBox iCheckBox;
    private ISlider iSlider;
    private ISpinner iSpinner;
    private IRadioButton iRadioButton;
    private IButton iButton;
    private IButtonLink iButtonLink;
    private IInputText iInputText;
    private IInputInt iInputInt;
    private IInputFloat iInputFloat;
    private ITextView iTextView;
    private ICategory iCategory;
    private ICollapse iCollapse;

    // ----------- State ------------
    private final List<Tab> tabs = new ArrayList<>();
    private final Deque<Page> pages = new ArrayDeque<>();
    private final Map<Object, LinearLayout> pageCache = new IdentityHashMap<>();
    private int activeTab = -1;
    private boolean expanded;
    private int bubbleX, bubbleY;
    private EditText editing;

    private static class Tab {
        String name, icon;
        List<FeatureEntity> items;
        boolean settings;
        IconView iconView;
        TextView label;
    }

    private static class Page {
        final String title;
        final List<FeatureEntity> items;

        Page(String title, List<FeatureEntity> items) {
            this.title = title;
            this.items = items;
        }
    }

    public Menu(Context context) {
        super(context);
        drawView = new DrawView(getContext);
        ui = new Ui(context, typeface, this);
        InitComponent(context);
    }

    private void InitComponent(Context context) {
        iSwitch = new ISwitch(ui);
        iCheckBox = new ICheckBox(ui);
        iSlider = new ISlider(ui);
        iSpinner = new ISpinner(ui);
        iRadioButton = new IRadioButton(ui);
        iButton = new IButton(ui);
        iButtonLink = new IButtonLink(ui);
        iInputText = new IInputText(ui);
        iInputInt = new IInputInt(iInputText);
        iInputFloat = new IInputFloat(iInputText);
        iTextView = new ITextView(ui);
        iCategory = new ICategory(ui);
        iCollapse = new ICollapse(ui);

        // Root of the overlay window, also receives ACTION_OUTSIDE to minimize the panel
        root = new FrameLayout(context) {
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_OUTSIDE) {
                    // while typing, touches on the keyboard also arrive as outside touches
                    if (expanded && editing == null) collapse();
                    return true;
                }
                return super.dispatchTouchEvent(event);
            }
        };

        bubble = new BubbleView(context, ui.bold, Theme.BUBBLE_TEXT, decodeIcon(Icon()));
        bubble.setOnTouchListener(bubbleTouchListener());
        root.addView(bubble, new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        panel = buildPanel(context);
        panel.setVisibility(View.GONE);
        root.addView(panel);

        creditText = ui.text("", Theme.TEXT_BODY, Theme.TEXT, ui.regular);
        creditText.setLineSpacing(ui.dp(2), 1f);

        Init(context, titleText, creditText);
    }


    // -------------------------- Menu UI --------------------------
    private LinearLayout buildPanel(Context context) {
        LinearLayout panel = new LinearLayout(context);
        panel.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable background = ui.round(Theme.PANEL_BG, Theme.PANEL_RADIUS);
        background.setStroke(Math.max(1, ui.dp(0.8f)), Theme.PANEL_STROKE);
        panel.setBackground(background);
        // swallow taps on empty panel areas so they don't reach the game window below
        panel.setClickable(true);

        panel.addView(buildHeader(context), new LinearLayout.LayoutParams(MATCH_PARENT, ui.dp(Theme.HEADER_HEIGHT + 6)));

        LinearLayout body = new LinearLayout(context);
        body.setOrientation(LinearLayout.HORIZONTAL);
        panel.addView(body, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f));

        sidebar = new LinearLayout(context);
        sidebar.setOrientation(LinearLayout.VERTICAL);
        sidebar.setPadding(0, ui.dp(2), 0, ui.dp(10));
        sidebarScroll = new ScrollView(context);
        sidebarScroll.setVerticalScrollBarEnabled(false);
        sidebarScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        sidebarScroll.addView(sidebar, new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        body.addView(sidebarScroll, new LinearLayout.LayoutParams(ui.dp(85), MATCH_PARENT));

        content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        contentScroll = new ScrollView(context);
        contentScroll.setVerticalScrollBarEnabled(false);
        contentScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        contentScroll.setPadding(0, 0, ui.dp(14), ui.dp(6));
        contentScroll.setClipToPadding(false);
        contentScroll.addView(content, new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        body.addView(contentScroll, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1f));

        return panel;
    }

    private View buildHeader(Context context) {
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(ui.dp(18), ui.dp(4), ui.dp(18), 0);

        // slanted accent mark in front of the title
        View mark = new View(context) {
            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            final Path path = new Path();

            @Override
            protected void onDraw(Canvas canvas) {
                float w = getWidth(), h = getHeight(), slant = w * 0.55f;
                paint.setColor(Theme.ACCENT);
                path.reset();
                path.moveTo(slant, 0);
                path.lineTo(w, 0);
                path.lineTo(w - slant, h);
                path.lineTo(0, h);
                path.close();
                canvas.drawPath(path, paint);
            }
        };
        header.addView(mark, new LinearLayout.LayoutParams(ui.dp(7), ui.dp(15)));

        titleText = ui.text("", Theme.TEXT_TITLE, Theme.TEXT, Typeface.create(ui.bold, Typeface.BOLD_ITALIC));
        titleText.setLetterSpacing(0.08f);
        titleText.setSingleLine(true);
        titleText.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f);
        titleParams.leftMargin = ui.dp(8);
        header.addView(titleText, titleParams);

        header.addView(new StatusView(ui));

        header.setOnTouchListener(dragListener());
        return header;
    }

    /**
     * Size of the region the overlay window can occupy. Window coordinates start below the
     * status bar, so the raw display size would push the panel under the navigation bar.
     */
    private Rect usableArea() {
        Rect area = new Rect();
        root.getWindowVisibleDisplayFrame(area);
        if (area.isEmpty()) {
            area.set(0, 0, getContext.getResources().getDisplayMetrics().widthPixels,
                    getContext.getResources().getDisplayMetrics().heightPixels);
        }
        return area;
    }

    /** Sizes the panel for the current screen, so rotation and small screens are handled. */
    private void layoutPanel() {
        Rect area = usableArea();
        int margin = ui.dp(Theme.PANEL_MARGIN);
        int width = Math.min(ui.dp(Theme.PANEL_MAX_WIDTH), area.width() - margin * 2);
        int height = Math.min(ui.dp(Theme.PANEL_MAX_HEIGHT), area.height() - margin * 2);
        panel.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        sidebarScroll.getLayoutParams().width = Math.round(width * Theme.SIDEBAR_RATIO);
        sidebarScroll.requestLayout();
    }

    @Override
    public void ShowMenu() {
        // read the flag before any widget is built, every widget loads its saved value only when it is on
        Preferences.loadPrefBool(SAVE_SETTINGS, SAVE_SETTINGS_ID, false);

        List<FeatureEntity> features = FeatureParser.parse(GetFeatureList());
        buildTabs(features);
        // build every tab and sub-page now, so saved values reach native at startup
        // instead of the first time a tab is opened
        for (Tab tab : tabs) {
            column(tab, null, tab.items, tab.settings);
            prebuildPages(tab.items);
        }
        if (!tabs.isEmpty()) selectTab(0);
    }

    private void buildTabs(List<FeatureEntity> features) {
        tabs.clear();
        pageCache.clear();
        sidebar.removeAllViews();

        List<FeatureEntity> loose = new ArrayList<>();
        for (FeatureEntity item : features) {
            if ("ITab".equals(item.type)) {
                tabs.add(tab(item.name, item.icon, item.children != null ? item.children : new ArrayList<>(), false));
            } else {
                loose.add(item);
            }
        }
        // widgets registered outside of an ITab go into a leading "General" tab
        if (!loose.isEmpty()) tabs.add(0, tab(GENERAL_TAB, "grid", loose, false));
        tabs.add(tab(SETTINGS_TAB, "gear", null, true));

        for (int i = 0; i < tabs.size(); i++) {
            sidebar.addView(buildTabView(tabs.get(i), i));
        }
    }

    private static Tab tab(String name, String icon, List<FeatureEntity> items, boolean settings) {
        Tab tab = new Tab();
        tab.name = name;
        tab.icon = TextUtils.isEmpty(icon) ? "grid" : icon;
        tab.items = items;
        tab.settings = settings;
        return tab;
    }

    private View buildTabView(Tab tab, int index) {
        LinearLayout item = new LinearLayout(getContext);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER_HORIZONTAL);
        item.setPadding(ui.dp(4), ui.dp(12), ui.dp(4), ui.dp(12));
        item.setClickable(true);

        tab.iconView = ui.icon(tab.icon, 22, Theme.TEXT_DIM);
        item.addView(tab.iconView);

        tab.label = ui.text(Ui.html(tab.name), Theme.TEXT_TAB, Theme.TEXT_DIM, ui.regular);
        tab.label.setGravity(Gravity.CENTER);
        tab.label.setMaxLines(2);
        tab.label.setEllipsize(TextUtils.TruncateAt.END);
        tab.label.setLineSpacing(ui.dp(1), 1f);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        labelParams.topMargin = ui.dp(6);
        item.addView(tab.label, labelParams);

        item.setOnClickListener(v -> selectTab(index));
        return item;
    }

    private void selectTab(int index) {
        endEdit();
        boolean same = index == activeTab && pages.isEmpty();
        activeTab = index;
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            boolean active = i == index;
            tab.iconView.setColor(active ? Theme.ACCENT : Theme.TEXT_DIM);
            tab.label.setTextColor(active ? Theme.TEXT : Theme.TEXT_DIM);
            tab.label.setTypeface(active ? ui.medium : ui.regular);
        }
        if (same) return;
        pages.clear();
        render(0);
    }

    @Override
    public void openPage(String title, List<FeatureEntity> children) {
        endEdit();
        pages.push(new Page(title, children));
        render(1);
    }

    private void closePage() {
        endEdit();
        if (pages.isEmpty()) return;
        pages.pop();
        render(-1);
    }

    /** Shows the current tab or sub-page. direction: 0 fade, 1 slide in from the right, -1 from the left. */
    private void render(int direction) {
        Tab tab = tabs.get(activeTab);
        Page page = pages.peek();
        LinearLayout column = page != null
                ? column(page.items, page.title, page.items, false)
                : column(tab, null, tab.items, tab.settings);

        content.removeAllViews();
        content.addView(column, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        contentScroll.scrollTo(0, 0);
        content.setAlpha(0f);
        content.setTranslationX(direction * ui.dp(24));
        content.animate().alpha(1f).translationX(0f).setDuration(direction == 0 ? 140 : 200)
                .setInterpolator(new DecelerateInterpolator()).start();
    }

    /**
     * Returns the cached card column for a tab (key = Tab) or sub-page (key = its children list),
     * building it on first use. title is set for sub-pages only.
     */
    private LinearLayout column(Object key, String title, List<FeatureEntity> items, boolean settings) {
        LinearLayout column = pageCache.get(key);
        if (column != null) return column;

        column = new LinearLayout(getContext);
        column.setOrientation(LinearLayout.VERTICAL);
        if (title != null) column.addView(buildPageHeader(title));
        if (settings) renderSettings(column);
        else renderItems(column, items);
        pageCache.put(key, column);
        return column;
    }

    private void prebuildPages(List<FeatureEntity> items) {
        if (items == null) return;
        for (FeatureEntity item : items) {
            // ICollapse.build has run by now, so children is the instance openPage() will use
            if ("ICollapse".equals(item.type) && item.children != null) {
                column(item.children, item.name, item.children, false);
                prebuildPages(item.children);
            }
        }
    }

    private void renderItems(LinearLayout column, List<FeatureEntity> items) {
        if (items == null) return;
        for (FeatureEntity item : items) {
            View view = build(item);
            if (view != null) column.addView(view);
        }
    }

    private View build(FeatureEntity item) {
        ComponentType type;
        try {
            type = ComponentType.valueOf(item.type);
        } catch (IllegalArgumentException | NullPointerException e) {
            Log.e(TAG, "Unknown feature type: " + item.type);
            return null;
        }
        switch (type) {
            case ISwitch:      return iSwitch.build(item);
            case ICheckBox:    return iCheckBox.build(item);
            case ISlider:      return iSlider.build(item);
            case ISpinner:     return iSpinner.build(item);
            case IRadioButton: return iRadioButton.build(item);
            case IButton:      return iButton.build(item);
            case IButtonLink:  return iButtonLink.build(item);
            case IInputText:   return iInputText.build(item);
            case IInputInt:    return iInputInt.build(item);
            case IInputFloat:  return iInputFloat.build(item);
            case ITextView:    return iTextView.build(item);
            case ICategory:    return iCategory.build(item);
            case ICollapse:    return iCollapse.build(item);
            case ITab:
                Log.e(TAG, "ITab is only allowed at the top level: " + item.name);
                return null;
            default:
                return null;
        }
    }

    private View buildPageHeader(String title) {
        LinearLayout header = new LinearLayout(getContext);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setMinimumHeight(ui.dp(36));
        header.setClickable(true);

        IconView back = ui.icon("chevron-left", 18, Theme.TEXT);
        header.addView(back);

        TextView text = ui.text(Ui.html(title), Theme.TEXT_TITLE, Theme.TEXT, ui.medium);
        text.setSingleLine(true);
        text.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f);
        textParams.leftMargin = ui.dp(6);
        header.addView(text, textParams);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.bottomMargin = ui.dp(6);
        header.setLayoutParams(params);
        header.setOnClickListener(v -> closePage());
        return header;
    }

    private void renderSettings(LinearLayout column) {
        FeatureEntity save = new FeatureEntity();
        save.id = SAVE_SETTINGS_ID;
        save.type = "ISwitch";
        save.name = SAVE_SETTINGS;
        save.description = "Restore your last values the next time the menu starts";
        column.addView(iCategory.build("Menu"));
        column.addView(iSwitch.build(save));

        if (creditText.getText().length() > 0) {
            column.addView(iCategory.build("Credits"));
            LinearLayout card = ui.card(true, false);
            card.setPadding(card.getPaddingLeft(), ui.dp(12), card.getPaddingRight(), ui.dp(12));
            if (creditText.getParent() != null) ((ViewGroup) creditText.getParent()).removeView(creditText);
            card.addView(creditText, ui.weight());
            column.addView(card);
        }

        column.addView(iCategory.build("Application"));
        column.addView(infoRow("Name", String.valueOf(getContext.getApplicationInfo().loadLabel(getContext.getPackageManager()))));
        column.addView(infoRow("Package", getContext.getPackageName()));
        try {
            column.addView(infoRow("Version", getContext.getPackageManager().getPackageInfo(getContext.getPackageName(), 0).versionName));
        } catch (Exception e) {
            Log.e(TAG, "Cannot read app version", e);
        }
    }

    private View infoRow(String label, String value) {
        LinearLayout card = ui.card(true, false);
        card.addView(ui.text(label, Theme.TEXT_BODY, Theme.TEXT, ui.regular));
        TextView valueText = ui.text(value, Theme.TEXT_BODY, Theme.TEXT_DIM, ui.regular);
        valueText.setGravity(Gravity.END);
        valueText.setSingleLine(true);
        valueText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        LinearLayout.LayoutParams params = ui.weight();
        params.leftMargin = ui.dp(16);
        card.addView(valueText, params);
        return card;
    }


    // -------------------------- Bubble & Panel --------------------------
    private void expand() {
        if (expanded) return;
        expanded = true;
        bubbleX = vmParams.x;
        bubbleY = vmParams.y;

        layoutPanel();
        Rect area = usableArea();
        int margin = ui.dp(Theme.PANEL_MARGIN);
        int width = panel.getLayoutParams().width;
        int height = panel.getLayoutParams().height;
        vmParams.x = clamp(bubbleX, margin, area.width() - width - margin);
        vmParams.y = clamp(bubbleY, margin, area.height() - height - margin);

        bubble.setVisibility(View.GONE);
        panel.setVisibility(View.VISIBLE);
        updateWindow();

        // grow out of the bubble's position
        int bubbleSize = ui.dp(Theme.BUBBLE_SIZE);
        panel.setPivotX(clamp(bubbleX - vmParams.x + bubbleSize / 2, 0, width));
        panel.setPivotY(clamp(bubbleY - vmParams.y + bubbleSize / 2, 0, height));
        panel.setAlpha(0f);
        panel.setScaleX(0.92f);
        panel.setScaleY(0.92f);
        panel.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(180)
                .setInterpolator(new DecelerateInterpolator()).start();
    }

    private void collapse() {
        if (!expanded) return;
        endEdit();
        expanded = false;
        panel.animate().cancel();
        panel.setVisibility(View.GONE);
        bubble.setVisibility(View.VISIBLE);
        vmParams.x = bubbleX;
        vmParams.y = bubbleY;
        updateWindow();
    }

    /** Leaves the bubble where it was dropped, only pulling it back if it is partly off-screen. */
    private void keepBubbleOnScreen() {
        Rect area = usableArea();
        int size = ui.dp(Theme.BUBBLE_SIZE);
        int x = clamp(vmParams.x, 0, area.width() - size);
        int y = clamp(vmParams.y, 0, area.height() - size);
        if (x == vmParams.x && y == vmParams.y) return;
        vmParams.x = x;
        vmParams.y = y;
        updateWindow();
    }

    private View.OnTouchListener bubbleTouchListener() {
        int slop = ViewConfiguration.get(getContext).getScaledTouchSlop();
        return new View.OnTouchListener() {
            private float touchX, touchY;
            private int startX, startY;
            private boolean moved;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                        startX = vmParams.x;
                        startY = vmParams.y;
                        moved = false;
                        view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(90).start();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getRawX() - touchX, dy = event.getRawY() - touchY;
                        if (!moved && Math.hypot(dx, dy) > slop) moved = true;
                        if (moved) {
                            vmParams.x = startX + (int) dx;
                            vmParams.y = startY + (int) dy;
                            updateWindow();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.animate().scaleX(1f).scaleY(1f).setDuration(90).start();
                        if (moved) keepBubbleOnScreen();
                        else expand();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        // e.g. the system back gesture took over the drag
                        view.animate().scaleX(1f).scaleY(1f).setDuration(90).start();
                        if (moved) keepBubbleOnScreen();
                        return true;
                }
                return false;
            }
        };
    }

    /** Drags the whole panel by its header. */
    private View.OnTouchListener dragListener() {
        return new View.OnTouchListener() {
            private float touchX, touchY;
            private int startX, startY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                        startX = vmParams.x;
                        startY = vmParams.y;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        vmParams.x = startX + (int) (event.getRawX() - touchX);
                        vmParams.y = startY + (int) (event.getRawY() - touchY);
                        updateWindow();
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
                return false;
            }
        };
    }


    // -------------------------- Input --------------------------
    // The overlay window is not focusable by default, so the game keeps input. When an EditText is focused, we lift that restriction so the keyboard can be used.
    // When the EditText loses focus, we hide the keyboard and restore the window to not focusable.
    // This is a bit of a hack, but it works. The alternative is to make the window focusable all the time, but then the game loses input when the menu is open.
    @Override
    public void beginEdit(EditText field) {
        if (editing == field) return;
        endEdit();
        editing = field;
        // the overlay is not focusable by default so the game keeps input, lift that while typing
        vmParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        updateWindow();

        field.setFocusableInTouchMode(true);
        field.requestFocus();
        field.setSelection(field.length());
        field.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && editing == field) imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
        }, 120);
    }

    @Override
    public void endEdit() {
        if (editing == null) return;
        EditText field = editing;
        editing = null;
        InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
        field.clearFocus();
        field.setFocusable(false);
        vmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        updateWindow();
    }

    private native void Init(Context context, TextView title, TextView subTitle);

    /** Base64 image for the bubble, defined in Setup.cpp. Empty means no image. */
    private native String Icon();

    private static Bitmap decodeIcon(String base64) {
        if (TextUtils.isEmpty(base64)) return null;
        int comma = base64.indexOf(',');
        if (base64.startsWith("data:") && comma >= 0) base64 = base64.substring(comma + 1);
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) Log.e(TAG, "Bubble icon is not a valid image, using text instead");
            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Bubble icon is not valid base64, using text instead", e);
            return null;
        }
    }

    private static int overlayFlags() {
        return WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    }

    @SuppressLint("WrongConstant")
    public void SetWindowManagerWindowService() {
        int mType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
        vmParams = new WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, mType, overlayFlags(), PixelFormat.TRANSLUCENT);
        vmParams.gravity = Gravity.TOP | Gravity.START;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = (WindowManager) getContext.getSystemService(Context.WINDOW_SERVICE);

        canvasParams = new WindowManager.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT,
                mType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );
        canvasParams.gravity = Gravity.TOP | Gravity.START;
        canvasParams.x = 0;
        canvasParams.y = 100;
        if (drawView.getParent() == null) mWindowManager.addView(drawView, canvasParams);
        if (root.getParent() == null) mWindowManager.addView(root, vmParams);
    }

    @SuppressLint("WrongConstant")
    public void SetWindowManagerActivity() {
        vmParams = new WindowManager.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                overlayFlags() |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        vmParams.gravity = Gravity.TOP | Gravity.START;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = ((Activity) getContext).getWindowManager();
        mWindowManager.addView(root, vmParams);
    }

    private void updateWindow() {
        if (mWindowManager != null && root.getParent() != null) {
            mWindowManager.updateViewLayout(root, vmParams);
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(Math.max(min, max), value));
    }

    public void setModMenuVisibility(boolean visible) {
        if (root != null) {
            root.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setEspLayoutVisibility(boolean visible) {
        if (drawView != null) {
            drawView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void KillMenu() {
        endEdit();
        if (root != null && mWindowManager != null && root.getParent() != null) {
            mWindowManager.removeView(root);
        }
        if (drawView != null && mWindowManager != null && drawView.getParent() != null) {
            mWindowManager.removeView(drawView);
        }
    }
}
