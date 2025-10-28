package com.android.support;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static com.android.support.Utils.dp;
import static com.android.support.components.Colors.TEXT_COLOR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.support.components.Colors;
import com.android.support.components.IButton;
import com.android.support.components.IButtonLink;
import com.android.support.components.ICategory;
import com.android.support.components.ICheckBox;
import com.android.support.components.ICollapse;
import com.android.support.components.IInputInt;
import com.android.support.components.IInputText;
import com.android.support.components.IRadioButton;
import com.android.support.components.ISlider;
import com.android.support.components.ISpinner;
import com.android.support.components.ISwitch;
import com.android.support.components.ITextView;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.Arrays;
import java.util.List;

@Obfuscate
public class Menu extends BaseMenu{
    public static int MENU_WIDTH = 290;
    public static boolean SHOW_APP_INFO = false;
    public static int ICON_SIZE = 60; //Change both width and height of image
    public static int POS_X = 0;
    public static int POS_Y = 100;
    public static float MENU_CORNER_RADIUS = 0f;
    boolean isExpanded = false;
    boolean overlayRequired;

    // ----------- Menu Layout ------------

    RelativeLayout __mCollapsed, __mRootContainer;
    LinearLayout __mExpanded, __featureContainer;
    LinearLayout.LayoutParams __menuExpanded;
    FrameLayout __frameLayout;
    ScrollView __scrollViewMenu;
    DrawView drawView;
    ImageView __startimage;

    // ----------- Window Manager ------------
    WindowManager mWindowManager;
    WindowManager.LayoutParams vmParams;
    private WindowManager.LayoutParams canvasParams;

    // ----------- Components ------------
    ISwitch iSwitch;
    ISlider iSlider;
    IButton iButton;
    IInputText iInputText;
    IInputInt iInputInt;
    ICheckBox iCheckBox;
    IRadioButton iRadioButton;
    ISpinner iSpinner;
    IButtonLink iButtonLink;
    ICategory iCategory;
    ITextView iTextView;
    ICollapse iCollapse;

    //Here we write the code for our Menu
    // Reference: https://www.androidhive.info/2016/11/android-floating-widget-like-facebook-chat-head/
    public Menu(Context context) {
        super(context);
        drawView    = new DrawView(getContext);
        InitComponent(context);
    }

    private void InitComponent(Context context) {
        // Set Components
        iSwitch     = new ISwitch(context, typeface);
        iSlider     = new ISlider(context, typeface);
        iButton     = new IButton(context, typeface);
        iInputText  = new IInputText(context, typeface);
        iInputInt   = new IInputInt(context, typeface);
        iCheckBox   = new ICheckBox(context, typeface);
        iRadioButton = new IRadioButton(context, typeface);
        iSpinner    = new ISpinner(context, typeface);
        iButtonLink = new IButtonLink(context, typeface);
        iCategory   = new ICategory(context, typeface);
        iTextView   = new ITextView(context, typeface);
        iCollapse   = new ICollapse(context, typeface);

        __frameLayout = new FrameLayout(context); // Global markup
        __frameLayout.setOnTouchListener(onTouchListener());
        __frameLayout.setAlpha(1f);

        __mRootContainer = new RelativeLayout(context); // Markup on which two markups of the icon and the menu itself will be placed
        __mRootContainer.setLayoutParams(new RelativeLayout.LayoutParams(dp(context, MENU_WIDTH), WRAP_CONTENT));


        __mCollapsed = new RelativeLayout(context); // Markup of the icon (when the menu is minimized)
        __mCollapsed.setVisibility(View.VISIBLE);

        //********** The box of the mod menu **********
        __mExpanded = new LinearLayout(context); // Menu markup (when the menu is expanded)
        __mExpanded.setBackgroundColor(Colors.MENU_BG_COLOR);
        __mExpanded.setOrientation(LinearLayout.VERTICAL);
        __mExpanded.setVisibility(View.GONE);
        __mExpanded.setPadding(1, 1, 1, 1); //So borders would be visible
        __mExpanded.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        //********** The box of the mod menu **********
        GradientDrawable gdMenuBody = new GradientDrawable();
        gdMenuBody.setCornerRadius(MENU_CORNER_RADIUS); //Set corner
        gdMenuBody.setColor(Colors.MENU_BG_COLOR); //Set background color
        __mExpanded.setBackground(gdMenuBody); //Apply GradientDrawable to it

        //********** Title **********
        RelativeLayout __titleLayout = new RelativeLayout(context);
        __titleLayout.setPadding(10, 5, 10, 5);
        __titleLayout.setVerticalGravity(16);
        __titleLayout.setGravity(Gravity.START);

        TextView _titleText = new TextView(context);
        _titleText.setTextColor(TEXT_COLOR);
        _titleText.setTextSize(18.0f);
        _titleText.setGravity(Gravity.START);
        _titleText.setTypeface(typeface);


        RelativeLayout.LayoutParams m_titleTextParam = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        m_titleTextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        _titleText.setLayoutParams(m_titleTextParam);

        //********** Bottom Text **********
        TextView _bottomText = new TextView(context);
        _bottomText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        _bottomText.setMarqueeRepeatLimit(-1);
        _bottomText.setSingleLine(true);
        _bottomText.setSelected(true);
        _bottomText.setTextColor(TEXT_COLOR);
        _bottomText.setTypeface(typeface);
        _bottomText.setTextSize(10.0f);
        _bottomText.setGravity(Gravity.CENTER);
        _bottomText.setPadding(0, 15, 0, 15);
        _bottomText.setVisibility(View.VISIBLE);

        __startimage = new ImageView(context);

        int size = Utils.convertDipToPixels(context, ICON_SIZE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.topMargin = Utils.convertDipToPixels(context, 10);
        __startimage.setLayoutParams(params);

        byte[] decode = Base64.decode(Icon(), 0);
        __startimage.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
        __startimage.setScaleType(ImageView.ScaleType.FIT_XY);

        __startimage.setOnTouchListener(onTouchListener());
        __startimage.setOnClickListener(v -> {
            if (isMenuCollapsed()) {
                __mCollapsed.setVisibility(View.GONE);
                __mExpanded.setVisibility(View.VISIBLE);
            }
        });

        //********** Mod menu feature list **********
        __menuExpanded = new LinearLayout.LayoutParams(__mExpanded.getLayoutParams());
        __menuExpanded.weight = 1.0f;

        __scrollViewMenu = new ScrollView(context);
        __scrollViewMenu.setPadding(10, 10, 10, 10);
        __scrollViewMenu.setBackgroundColor(Colors.MENU_FEATURE_BG_COLOR);
        __scrollViewMenu.setLayoutParams(__menuExpanded);
        __scrollViewMenu.setVisibility(View.VISIBLE);


        __featureContainer = new LinearLayout(context);
        __featureContainer.setOrientation(LinearLayout.VERTICAL);

        //********** RelativeLayout for buttons **********
        RelativeLayout btnLayout = new RelativeLayout(context);
        btnLayout.setPadding(10, 3, 10, 3);
        btnLayout.setVerticalGravity(Gravity.CENTER);

        //**********  Hide/Kill button **********
        RelativeLayout.LayoutParams lParamsHideBtn = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lParamsHideBtn.addRule(ALIGN_PARENT_LEFT);

        Button hideBtn = new Button(context);
        hideBtn.setLayoutParams(lParamsHideBtn);
        hideBtn.setBackgroundColor(Color.TRANSPARENT);
        hideBtn.setText("HIDE/KILL (Hold)");
        hideBtn.setTextColor(TEXT_COLOR);
        hideBtn.setTypeface(typeface);
        hideBtn.setOnClickListener(view -> {
            __mCollapsed.setVisibility(View.VISIBLE);
            __mCollapsed.setAlpha(0);
            __mExpanded.setVisibility(View.GONE);
            Toast.makeText(view.getContext(), "Icon hidden. Remember the hidden icon position", Toast.LENGTH_LONG).show();
        });
        hideBtn.setOnLongClickListener(view -> {
            Toast.makeText(view.getContext(), "Menu killed", Toast.LENGTH_LONG).show();
            KillMenu();
            return false;
        });
        btnLayout.addView(hideBtn);

        //********** Close button **********
        RelativeLayout.LayoutParams lParamsCloseBtn = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lParamsCloseBtn.addRule(ALIGN_PARENT_RIGHT);

        Button closeBtn = new Button(context);
        closeBtn.setLayoutParams(lParamsCloseBtn);
        closeBtn.setBackgroundColor(Color.TRANSPARENT);
        closeBtn.setText("MINIMIZE");
        closeBtn.setTypeface(typeface);
        closeBtn.setTextColor(TEXT_COLOR);
        closeBtn.setOnClickListener(view -> {
            if (!isMenuCollapsed()){
                __mCollapsed.setVisibility(View.VISIBLE);
                __mCollapsed.setAlpha(1);
                __mExpanded.setVisibility(View.GONE);
            }
        });
        btnLayout.addView(closeBtn);

        //********** Adding view components **********
        __mRootContainer.addView(__mExpanded);
        __mRootContainer.addView(__mCollapsed);

        __titleLayout.addView(_titleText);

        __mExpanded.addView(__titleLayout);
        __mExpanded.addView(_bottomText);
        __mExpanded.addView(__scrollViewMenu);

        __scrollViewMenu.addView(__featureContainer);
        __mExpanded.addView(btnLayout);
        __mCollapsed.addView(__startimage);
        Init(context, _titleText, _bottomText);
    }

    private native void Init(Context context, TextView title, TextView subTitle);

    private native String Icon();

    @Override
    public void ShowMenu() {
        __frameLayout.removeAllViews();
        __frameLayout.addView(__mRootContainer);
        __featureContainer.removeAllViews();
        featureList(GetFeatureList(), __featureContainer);
    }

    @Override
    public void KillMenu() {
        if (__frameLayout!=null) __frameLayout.removeView(__mRootContainer);
        if (__frameLayout != null && mWindowManager != null && __frameLayout.getParent()!=null) {
            mWindowManager.removeView(__frameLayout);
        }
        if (drawView != null && mWindowManager != null  && drawView.getParent()!=null) {
            mWindowManager.removeView(drawView);
        }
    }

    @SuppressLint("WrongConstant")
    public void SetWindowManagerWindowService() {
        //Variable to check later if the phone supports Draw over other apps permission
        int mType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 2038 : 2002;
        vmParams = new WindowManager.LayoutParams(WRAP_CONTENT,WRAP_CONTENT, mType, 8, -3);
        //params = new WindowManager.LayoutParams(WindowManager.LayoutParams.LAST_APPLICATION_WINDOW, 8, -3);
        vmParams.gravity = 51;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = (WindowManager) getContext.getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                mType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
        );
        canvasParams = layoutParams;
        layoutParams.gravity = Gravity.TOP | Gravity.START;
        canvasParams.x = 0;
        canvasParams.y = 100;
        if (drawView.getParent() == null) mWindowManager.addView(drawView, canvasParams);
        if (__frameLayout.getParent() == null) mWindowManager.addView(__frameLayout, vmParams);
        overlayRequired = true;
    }


    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            private float initialTouchX, initialTouchY;
            private int initialX, initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = vmParams.x;
                        initialY = vmParams.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int rawX = (int) (motionEvent.getRawX() - initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - initialTouchY);
                        __mExpanded.setAlpha(1f);
                        __mCollapsed.setAlpha(1f);
                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (rawX < 10 && rawY < 10 && isMenuCollapsed()) {
                            //When user clicks on the image view of the collapsed layout,
                            //visibility of the collapsed layout will be changed to "View.GONE"
                            //and expanded view will become visible.
                            try {
                                view.performClick();
                                return true;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        __mExpanded.setAlpha(0.3f);
                        __mCollapsed.setAlpha(0.3f);
                        //Calculate the X and Y coordinates of the view.
                        vmParams.x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                        vmParams.y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));
                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(__frameLayout, vmParams);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private void featureList(String feature, LinearLayout linearLayout) {

        List<FeatureEntity> features = FeatureParser.parse(feature);
        // App details
        iTextView.add(linearLayout, "App Package: " + getContext.getPackageName());
        // AppName
        iTextView.add(linearLayout, "App Name: " + getContext.getApplicationInfo().loadLabel(getContext.getPackageManager()));
        // App Version
        try {
            iTextView.add(linearLayout, "App Version: " + getContext.getPackageManager().getPackageInfo(getContext.getPackageName(), 0).versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (FeatureEntity item : features) {
            if (ComponentType.valueOf(item.type) == ComponentType.ICollapse) {// Add the collapse group first
                iCollapse.add(linearLayout, item.name, item.enabled);

                // Then render its children inside the collapseContent
                if (item.children != null && !item.children.isEmpty()) {
                    LinearLayout collapseContent = iCollapse.getCollapseContent();
                    for (FeatureEntity child : item.children) {
                        addFeatureComponent(collapseContent, child);
                    }
                }
            } else {// Non-collapse items
                addFeatureComponent(linearLayout, item);
            }
        }
    }
    private void addFeatureComponent(LinearLayout layout, FeatureEntity item) {
        ComponentType type = ComponentType.valueOf(item.type);
        switch (type) {
            case ICheckBox:
                iCheckBox.add(layout, item.id, item.name, item.description, item.enabled);
                break;
            case ISlider:
                iSlider.add(layout, item.id, item.name, item.min, item.max);
                break;
            case ISpinner:
                if (item.options != null) {
                    iSpinner.add(layout, item.id, item.name, String.join(",", item.options));
                }
                break;
            case ISwitch:
                iSwitch.add(layout, item.id, item.name, item.description, item.enabled);
                break;
            case IButton:
                iButton.add(layout, item.id, item.name);
                break;
            case IInputText:
                iInputText.add(layout, item.id, item.name);
                break;
            case IInputInt:
                iInputInt.add(layout, item.id, item.name, item.max);
                break;
            case IRadioButton:
                if (item.options != null) {
                    iRadioButton.add(layout, item.id, item.name, String.join(",", Arrays.asList(item.options)));
                }
                break;
            case IButtonLink:
                iButtonLink.add(layout, item.name, item.description);
                break;
            case ICategory:
                iCategory.add(layout, item.name);
                break;
            case ITextView:
                iTextView.add(layout, item.name);
                break;
            default:
                Log.e(TAG, "Unknown feature type: " + item.type);
                break;
        }
    }

    private boolean isMenuCollapsed() {
        return __frameLayout == null || __mCollapsed.getVisibility() == View.VISIBLE;
    }

    public void setModMenuVisibility(boolean visible){
        if (__frameLayout != null) {
            __frameLayout.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setEspLayoutVisibility(boolean visible){
        if (drawView != null) {
            drawView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

}
