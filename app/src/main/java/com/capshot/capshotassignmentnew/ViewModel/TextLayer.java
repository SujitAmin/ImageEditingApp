package com.capshot.capshotassignmentnew.ViewModel;

import com.capshot.capshotassignmentnew.ViewModel.Interfaces.LimitsForTextLayer;

// this is made for text data
public class TextLayer extends Layer {

    private String text;
    private Font font;
    public TextLayer() {}

    @Override
    protected void reset() {
        super.reset();
        this.text = "";
        this.font = new Font();
    }

    @Override
    protected float getMaxScale() {
        return LimitsForTextLayer.MAX_SCALE;
    }

    @Override
    protected float getMinScale() {
        return LimitsForTextLayer.MIN_SCALE;
    }

    @Override
    public float initialScale() {
        return LimitsForTextLayer.INITIAL_SCALE;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }


}