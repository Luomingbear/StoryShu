package com.storyshu.storyshu.widget.text;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.storyshu.storyshu.R;
import com.storyshu.storyshu.info.EditLineData;
import com.storyshu.storyshu.widget.imageview.DataImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本编辑器
 * Created by bear on 2016/12/28.
 */

public class RichTextEditor extends ScrollView {
    private static final String TAG = "RichTextEditor";
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
    private static final int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;


    private ArrayList<TextData> oldStringList = new ArrayList<>(); //上一次的文本信息

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
        setupLayoutTransitions();
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
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editNormalPadding = dip2px(EDIT_PADDING);
        EditText firstEdit = createEditText(getResources().getString(R.string.edit_content));
        firstEdit.addTextChangedListener(mTextWatcher);
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;

    }

    /**
     * 添加文本变化监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, "beforeTextChanged: " + s);

            if (oldStringList.size() == 0) {
                oldStringList.add(new TextData(s.toString(), start));
            } else if (!oldStringList.contains(new TextData(s.toString(), start))) {
                oldStringList.add(new TextData(s.toString(), start));
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


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
                    allLayout.setLayoutTransition(mTransitioner); // 恢复transition动画

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
        if (!mTransitioner.isRunning()) {
            disappearingImageIndex = allLayout.indexOfChild(view);
            allLayout.removeView(view);
        }
    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint) {
        //
        EditText editText = new EditText(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        //
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
        editText.setBackgroundResource(0);
        editText.setLineSpacing(getResources().getDimension(R.dimen.line_space), 1);

        editText.addTextChangedListener(mTextWatcher);
        return editText;
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
        imageView.setId(R.id.edit_imageView);
        layout.addView(imageView);

        View closeView = new ImageView(getContext());
        int iconSize = (int) getResources().getDimension(R.dimen.icon_small);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(iconSize, iconSize);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.topMargin = margin;
        closeView.setLayoutParams(params);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        closeView.setId(R.id.image_close);
        closeView.setBackgroundResource(R.drawable.close);
        layout.addView(closeView);
        return layout;
    }

    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath) {
        Log.i(TAG, "insertImage: imagePath:" + imagePath);
//        int width = getWidth() == 0 ? 1024 : getWidth();
        int width = 1024;
        Bitmap bmp = getScaledBitmap(imagePath, width);
        insertImage(bmp, imagePath);
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, String imagePath) {
        if (bitmap == null)
            return;
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, bitmap, imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (allLayout.getChildCount() - 1 == lastEditIndex
                    || editStr2.length() > 0) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        }
        hideKeyBoard();
    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("");
        editText2.setText(editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setAbsolutePath(imagePath);

        // 调整imageView的高度
        int width = getWidth() == 0 ? 1024 : getWidth();
        int imageHeight = width * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, imageHeight);
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
        allLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                allLayout.addView(imageLayout, index);
            }
        }, 200);
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.edit_imageView);
        Bitmap bmp = getScaledBitmap(imagePath, 1024);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setAbsolutePath(imagePath);

        // 调整imageView的高度
        int width = 1024;
        int imageHeight = width * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, imageHeight);
        imageView.setLayoutParams(lp);
        allLayout.addView(imageLayout, index);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);

        if (preView != null && preView instanceof EditText && null != nextView
                && nextView instanceof EditText) {
            Log.d(TAG, "mergeEditText:合并EditText ");
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();

            preEdit.setText(str1 + "\n" + str2);
            allLayout.setLayoutTransition(null);
            allLayout.removeView(nextEdit);
            preEdit.requestFocus();
            preEdit.setSelection((str1 + str2).length(), (str1 + str2).length());
            allLayout.setLayoutTransition(mTransitioner);
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

    //类型标签
    public static String TAG_TXT = "txt";
    public static String TAG_IMG = "img";

    /**
     * 设置故事的数据
     *
     * @param data
     */
    public void setDataContent(String data) {
        Log.i(TAG, "setDataContent: 开始匹配！！！");
        String regxp = "<[^>]+>"; //匹配文本

        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(data);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();

        int index = 0;
        while (result1) {
            StringBuffer s = new StringBuffer();
            matcher.appendReplacement(s, "");
            result1 = matcher.find();

            if (TextUtils.isEmpty(s))
                continue;
            createViewByData(index, s.toString());
            index++;
        }
        matcher.appendTail(sb);
        //
        mergeEditText();

    }

    /**
     * 根据类型添加文本或者图片
     *
     * @param index
     * @param s
     */
    private void createViewByData(int index, String s) {
        if (index == 0)
            allLayout.removeAllViews();

        if (s.substring(0, 3).equals(TAG_TXT)) {
            addEditTextAtIndex(index, s.substring(TAG_TXT.length()));
        } else if (s.substring(0, 3).equals(TAG_IMG)) {
            String path = s.substring(TAG_IMG.length());
            addImageViewAtIndex(index, path);
        }
    }

    /**
     * 返回上一次的编辑内容
     */
    public void undoText() {
        if (oldStringList.size() > 0) {
            TextData old = oldStringList.get(oldStringList.size() - 1);
            lastFocusEdit.removeTextChangedListener(mTextWatcher);
            lastFocusEdit.setText(old.editText);
            lastFocusEdit.setSelection(old.selectionIndex);
            oldStringList.remove(old);
            Log.d(TAG, "undoText: list:" + oldStringList);
        } else
            lastFocusEdit.setText("");

        lastFocusEdit.addTextChangedListener(mTextWatcher);
    }


    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditLineData> getEditData() {
        List<EditLineData> dataList = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditLineData itemData = new EditLineData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.setInputStr(item.getText().toString());
//                if (!TextUtils.isEmpty(item.getText().toString()))
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView
                        .findViewById(R.id.edit_imageView);
                itemData.setIamgePath(item.getAbsolutePath());
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public String getEditString() {
        StringBuffer data = new StringBuffer();
        List<EditLineData> dataList = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditLineData itemData = new EditLineData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                data.append("<text>" + TAG_TXT + item.getText().toString() + "</text>");
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView
                        .findViewById(R.id.edit_imageView);
                data.append("<image>" + TAG_IMG + item.getAbsolutePath() + "</image>");
            }
            dataList.add(itemData);
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
        for (EditLineData data : getEditData()) {
            if (!TextUtils.isEmpty(data.getInputStr()))
                return extract = data.getInputStr();
        }
        return extract;
    }

    /**
     * 获取当前编辑的editText
     *
     * @return
     */
    public EditText getLastFocusEdit() {
        return lastFocusEdit;
    }

    /**
     * 获取封面
     *
     * @return
     */
    public String getCoverPic() {
        String cover = "";
        for (EditLineData data : getEditData()) {
            if (!TextUtils.isEmpty(data.getIamgePath()))
                return cover = data.getIamgePath();
        }
        return cover;
    }

    private class TextData {
        String editText;
        int selectionIndex;

        TextData() {
            this.editText = "";
            this.selectionIndex = 0;
        }

        TextData(String editText, int selectionIndex) {
            this.editText = editText;
            this.selectionIndex = selectionIndex;
        }

        @Override
        public String toString() {
            return "text:" + editText + " ++ index:" + selectionIndex;
        }
    }
}