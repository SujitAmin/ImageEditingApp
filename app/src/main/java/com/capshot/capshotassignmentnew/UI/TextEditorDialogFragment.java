package com.capshot.capshotassignmentnew.UI;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.capshot.capshotassignmentnew.Constant.Constant;
import com.capshot.capshotassignmentnew.R;

public class TextEditorDialogFragment extends DialogFragment {
    protected EditText editText;
    private OnTextLayerCallback callback;

    @Deprecated
    public TextEditorDialogFragment() {}

    public static TextEditorDialogFragment getInstance(String textValue) {
        @SuppressWarnings("deprecation")
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ARG_TEXT, textValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTextLayerCallback) {
            this.callback = (OnTextLayerCallback) activity;
        } else {
            throw new IllegalStateException(activity.getClass().getName()
                    + " must implement " + OnTextLayerCallback.class.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_editor_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String text = "";
        if (args != null) {
            text = args.getString(Constant.ARG_TEXT);
        }

        editText = view.findViewById(R.id.edit_text_view);

        initWithTextEntity(text);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (callback != null) {
                    callback.textChanged(s.toString());
                }
            }
        });

        view.findViewById(R.id.text_editor_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initWithTextEntity(String text) {
        editText.setText(text);
        editText.post(new Runnable() {
            @Override
            public void run() {
                if (editText != null) {
                    Selection.setSelection(editText.getText(), editText.length());
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onDetach() {
        // release links
        this.callback = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                // remove background
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                WindowManager.LayoutParams windowParams = window.getAttributes();
                window.setDimAmount(0.0F);
                window.setAttributes(windowParams);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.post(new Runnable() {
            @Override
            public void run() {
                setEditText(true);
                editText.requestFocus();
                InputMethodManager ims = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                ims.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void setEditText(boolean gainFocus) {
        if (!gainFocus) {
            editText.clearFocus();
            editText.clearComposingText();
        }
        editText.setFocusableInTouchMode(gainFocus);
        editText.setFocusable(gainFocus);
    }


    //Callback that passes all user input through the method
    public interface OnTextLayerCallback {
        void textChanged(@NonNull String text);
    }
}