package com.storyshu.storyshu.widget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.bean.ImageItem;
import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.EditLineData;
import com.storyshu.storyshu.utils.DipPxConversion;
import com.storyshu.storyshu.utils.KeyBordUtil;
import com.storyshu.storyshu.utils.net.QiniuUploadManager;
import com.storyshu.storyshu.utils.sharepreference.ISharePreference;
import com.storyshu.storyshu.widget.imageview.DataImageView;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * 富文本编辑器
 * Created by bear on 2016/12/28.
 */

public class RichTextEditor extends ScrollView {
    private static final String TAG = "RichTextEditor";
    private static int EDIT_PADDING = 10; // edittext常规padding是10dp
    private static int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;

    private String mTmpImagePath; //临时存储图片地址

    private OnTextFocusListener onTextFoucsListener;

    /**
     * 标记
     */
    public static String TAG_TITLE = "tit";
    public static String TAG_TITLE_START = "<tit>";
    public static String TAG_TITLE_END = "</tit>";
    public static String TAG_P = "p";
    public static String TAG_P_START = "<p>";
    public static String TAG_P_END = "</p>";
    public static String TAG_IMAGE = "img";
    public static String TAG_IMAGE_START = "<img>";
    public static String TAG_IMAGE_END = "</img>";
    public static String TAG_IMAGE_PATH = "path";
    public static String TAG_IMAGE_PATH_START = "<path>";
    public static String TAG_IMAGE_PATH_END = "</path>";
    public static String TAG_IMAGE_DESCRIBE = "des";
    public static String TAG_IMAGE_DESCRIBE_START = "<des>";
    public static String TAG_IMAGE_DESCRIBE_END = "</des>";

    public interface OnTextFocusListener {
        void onFocusChange(View v, boolean hasFocus);
    }

    public void setOnTextFocusListener(OnTextFocusListener onTextFoucsListener) {
        this.onTextFoucsListener = onTextFoucsListener;
    }

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onTextFoucsListener != null) {
                    onTextFoucsListener.onFocusChange(v, hasFocus);
                }

                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editNormalPadding = (int) getResources().getDimension(R.dimen.margin_normal);
        EditText firstEdit = createEditText(getResources().getString(R.string.content_hint),
                dip2px(EDIT_FIRST_PADDING_TOP));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    allLayout.setLayoutTransition(null);
                    allLayout.removeView(editTxt);

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        disappearingImageIndex = allLayout.indexOfChild(view);
        allLayout.removeView(view);

    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint, int paddingTop) {
        //
        EditText editText = new EditText(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, 0);
        //
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        editText.setTextColor(getResources().getColor(R.color.colorBlackLight));
        editText.setBackgroundResource(0);
        editText.setLineSpacing(getResources().getDimension(R.dimen.line_space_normal), 1);
        editText.setId(R.id.edit_content_paragraph);
        return editText;
    }

    private TextView createTextView(String content) {
        //
        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setPadding(editNormalPadding, getResources().getDimensionPixelSize(R.dimen.font_small),
                editNormalPadding, 0);
        //
        textView.setOnKeyListener(keyListener);
        textView.setTag(viewTagIndex++);
        textView.setText(content);
        textView.setOnFocusChangeListener(focusListener);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_normal));
        textView.setTextColor(getResources().getColor(R.color.colorBlackLight));
        textView.setBackgroundResource(0);
        textView.setLineSpacing(getResources().getDimension(R.dimen.line_space_normal), 1);
        textView.setId(R.id.edit_content_paragraph);
        return textView;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(p);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        layout.setPadding(0, margin, 0, margin);
        layout.setTag(viewTagIndex++);

        DataImageView imageView = new DataImageView(getContext());
        imageView.setLayoutParams(p);
        imageView.setId(R.id.edit_image_view);
        layout.addView(imageView);

        /**
         * 删除图片的按钮
         */
        View closeView = new ImageView(getContext());
        int iconSize = (int) getResources().getDimension(R.dimen.icon_small);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iconSize, iconSize);
        params.setMargins(0, DipPxConversion.dip2px(getContext(), 2), DipPxConversion.dip2px(getContext(), 2), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeView.setLayoutParams(params);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        closeView.setId(R.id.image_close);
        closeView.setBackgroundResource(R.drawable.close);
        layout.addView(closeView);

        /**
         * 底部的描述控件
         */
        EditText hint = new EditText(getContext());
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pp.addRule(RelativeLayout.BELOW, R.id.edit_image_view);
        hint.setLayoutParams(pp);
        hint.setPadding(editNormalPadding, (int) getResources().getDimension(R.dimen.margin_small), editNormalPadding, 0);
        //设置默认文本
        hint.setText(getResources().getString(R.string.picture_hint,
                ISharePreference.getUserData(getContext()).getNickname()));
        hint.setGravity(Gravity.CENTER);
        hint.setTag(viewTagIndex++);
        hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
        hint.setTextColor(getResources().getColor(R.color.colorGrayDeep));
        hint.setBackgroundResource(0);
        hint.setId(R.id.edit_image_describe);
        layout.addView(hint);

        return layout;
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePathList
     */
    public void insertImage(List<ImageItem> imagePathList) {
        if (imagePathList != null && imagePathList.size() > 0) {
            for (ImageItem item : imagePathList) {
                insertImage(item.path);
            }
        }
    }

    /**
     * 插入一张图片
     */
    private void insertImage(String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (allLayout.getChildCount() - 1 == lastEditIndex
                    || editStr2.length() > 0) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1, imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        }

        //隐藏键盘
        KeyBordUtil.hideKeyboard(getContext(), lastFocusEdit);

    }


    /**
     * 生成图片，仅限用于根据数据转化为view
     *
     * @param path     图片地址
     * @param describe 图片描述
     */
    private void newImageLayout(String path, String describe) {
        RelativeLayout layout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(p);
        int margin = (int) getResources().getDimension(R.dimen.margin_normal);
        layout.setPadding(0, margin, 0, margin);

        DataImageView imageView = new DataImageView(getContext());
        imageView.setLayoutParams(p);
        imageView.setId(R.id.edit_image_view);
        layout.addView(imageView);
        Glide.with(getContext()).load(path).into(imageView);

        /**
         * 底部的描述控件
         */
        TextView hint = new TextView(getContext());
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pp.addRule(RelativeLayout.BELOW, R.id.edit_image_view);
        hint.setLayoutParams(pp);
        hint.setPadding(editNormalPadding, (int) getResources().getDimension(R.dimen.margin_small),
                editNormalPadding, 0);
        //设置默认文本
        hint.setText(describe);
        hint.setGravity(Gravity.CENTER);
        hint.setTag(viewTagIndex++);
        hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
        hint.setTextColor(getResources().getColor(R.color.colorGrayDeep));
        hint.setBackgroundResource(0);
        layout.addView(hint);

        allLayout.addView(layout);
    }


    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("", getResources()
                .getDimensionPixelSize(R.dimen.font_small));
        editText2.setText(editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout
                .findViewById(R.id.edit_image_view);
        imageView.setAbsolutePath(imagePath);
        Glide.with(getContext()).load(imagePath).into(imageView);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
        allLayout.post(new Runnable() {
            @Override
            public void run() {
                allLayout.addView(imageLayout, index);
            }
        });

        //上传图片
        uploadImage(imageView);

    }


    private QiniuUploadManager qiniuUploadManager;  //七牛云上传管家

    /**
     * 上传图片
     */
    private void uploadImage(final DataImageView dataImageView) {
        qiniuUploadManager = new QiniuUploadManager(getContext());
        qiniuUploadManager.uploadFile(dataImageView.getAbsolutePath());
        qiniuUploadManager.setQiniuUploadInterface(new QiniuUploadManager.QiniuUploadInterface() {
            @Override
            public void onSucceed(List<String> pathList) {
                dataImageView.setAbsolutePath(pathList.get(0));
            }

            @Override
            public void onFailed(List<String> errorPathList) {

            }
        });
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);
        if (preView != null && preView instanceof EditText && null != nextView
                && nextView instanceof EditText) {
            Log.d("LeiTest", "合并EditText");
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();

            allLayout.setLayoutTransition(null);
            allLayout.removeView(nextEdit);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
        }
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue dp值
     * @return 像素值
     */
    public int dip2px(float dipValue) {
        float m = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public String getEditData() {
        StringBuffer data = new StringBuffer();
//        data.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");//xml格式／
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditLineData itemData = new EditLineData();
            if (itemView instanceof EditText) {
                if (itemView.getId() == R.id.edit_content_paragraph) {
                    data.append(TAG_P_START);
                    data.append(((EditText) itemView).getText());
                    data.append(TAG_P_END);
                }
                EditText item = (EditText) itemView;
                itemData.setInputStr(item.getText().toString());
            } else if (itemView instanceof RelativeLayout) {
                //图片
                DataImageView image = (DataImageView) itemView.findViewById(R.id.edit_image_view);
                //图片描述
                EditText describe = (EditText) itemView.findViewById(R.id.edit_image_describe);
                data.append(TAG_IMAGE_START);

                data.append(TAG_IMAGE_PATH_START);
                data.append(image.getAbsolutePath());
                data.append(TAG_IMAGE_PATH_END);

                data.append(TAG_IMAGE_DESCRIBE_START);
                data.append(describe.getText());
                data.append(TAG_IMAGE_DESCRIBE_END);

                data.append(TAG_IMAGE_END);
            }
        }

        return data.toString();
    }

    /**
     * 获取摘要文件
     *
     * @return 摘要
     */
    public String getExtract() {
        String extract = "";
        for (int index = 0; index < allLayout.getChildCount(); index++) {
            View itemView = allLayout.getChildAt(index);
            EditLineData itemData = new EditLineData();
            if (itemView instanceof EditText) {
                return ((EditText) itemView).getText().toString();
            }
        }
        return extract;
    }


    public void parseXml(String xml) {
        allLayout.removeAllViews();

        //获取源码
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(new ByteArrayInputStream(xml.getBytes()), "UTF-8");

            int eventType = parser.getEventType();
            String path, describe;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        Log.d(TAG, "parseXml: START_DOCUMENT");
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        if (name.equals(TAG_P)) {
                            String s = parser.nextText();
                            Log.i(TAG, "parseXml: TAG_P+++++" + s);
                            allLayout.addView(createTextView(s));
                        } else if (name.equals(TAG_IMAGE_PATH)) {
                            path = parser.nextText();
                            mTmpImagePath = path;
                            Log.i(TAG, "parseXml: TAG_IMAGE_PATH+++++" + path);
                        } else if (name.equals(TAG_IMAGE_DESCRIBE)) {
                            describe = parser.nextText();
                            newImageLayout(mTmpImagePath, describe);
                            Log.i(TAG, "parseXml: TAG_IMAGE_DESCRIBE+++++" + describe);
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        Log.d(TAG, "parseXml: END_TAG");
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

