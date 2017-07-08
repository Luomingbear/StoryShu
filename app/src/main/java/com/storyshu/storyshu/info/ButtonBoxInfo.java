package com.storyshu.storyshu.info;


/**
 * 按钮的信息结构
 */
public class ButtonBoxInfo

{
    private int drawableResDef;
    private int drawableResSel;
    private int colorDef;
    private int colorSel;
    private int titleRes;
    private boolean isChecked = false;

    public ButtonBoxInfo() {

    }

    public ButtonBoxInfo(int drawableResDef, int drawableResSel, int colorDef,
                         int colorSel, int titleRes) {
        this.drawableResDef = drawableResDef;
        this.drawableResSel = drawableResSel;
        this.colorDef = colorDef;
        this.colorSel = colorSel;
        this.titleRes = titleRes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getDrawableResDef() {
        return drawableResDef;
    }

    public void setDrawableResDef(int drawableResDef) {
        this.drawableResDef = drawableResDef;
    }

    public int getDrawableResSel() {
        return drawableResSel;
    }

    public void setDrawableResSel(int drawableResSel) {
        this.drawableResSel = drawableResSel;
    }

    public int getColorDef() {
        return colorDef;
    }

    public void setColorDef(int colorDef) {
        this.colorDef = colorDef;
    }

    public int getColorSel() {
        return colorSel;
    }

    public void setColorSel(int colorSel) {
        this.colorSel = colorSel;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }


}
