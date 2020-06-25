package com.capshot.capshotassignmentnew.Utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;
import androidx.annotation.Nullable;

import com.capshot.capshotassignmentnew.Constant.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FontProvider {
    private final Map<String, Typeface> typefaces;
    private final Map<String, String> fontNameToTypefaceFile;
    private final Resources resources;
    private final List<String> fontNames;

    public FontProvider(Resources resources) {
        this.resources = resources;
        typefaces = new HashMap<>();
        fontNameToTypefaceFile = new HashMap<>();
        fontNameToTypefaceFile.put("Arial", "Arial.ttf");
        fontNameToTypefaceFile.put("Eutemia", "Eutemia.ttf");
        fontNameToTypefaceFile.put("GREENPIL", "GreenPil.ttf");
        fontNameToTypefaceFile.put("Grinched", "Grinched.ttf");
        fontNameToTypefaceFile.put("Helvetica", "Helvetica.ttf");
        fontNameToTypefaceFile.put("Libertango", "Libertango.ttf");
        fontNameToTypefaceFile.put("Metal Macabre", "MetalMacabre.ttf");
        fontNameToTypefaceFile.put("Parry Hotter", "ParryHotter.ttf");
        fontNameToTypefaceFile.put("SCRIPTIN", "Scriptin.ttf");
        fontNameToTypefaceFile.put("The Godfather v2", "TheGodfather_v2.ttf");
        fontNameToTypefaceFile.put("Aka Dora", "AkaDora.ttf");
        fontNameToTypefaceFile.put("Waltograph", "Waltograph42.ttf");
        fontNames = new ArrayList<>(fontNameToTypefaceFile.keySet());
    }

     // typefaceName must be one of the font-names
     public Typeface getTypeface(@Nullable String typefaceName) {
        if (TextUtils.isEmpty(typefaceName)) {
            return Typeface.DEFAULT;
        } else {
            if (typefaces.get(typefaceName) == null) {
                typefaces.put(typefaceName,
                        Typeface.createFromAsset(resources.getAssets(), "fonts/" + fontNameToTypefaceFile.get(typefaceName)));
            }
            return typefaces.get(typefaceName);
        }
    }

    public List<String> getFontNames() {
        return fontNames;
    }
    public String getDefaultFontName() {
        return Constant.DEFAULT_FONT_NAME;
    }
}