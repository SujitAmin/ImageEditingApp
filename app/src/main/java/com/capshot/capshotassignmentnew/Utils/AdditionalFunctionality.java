package com.capshot.capshotassignmentnew.Utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AdditionalFunctionality {
    public static void setSnackBar(View root, String textToDisplay) {
        final Snackbar snackbar = Snackbar.make(root, textToDisplay, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                //your other action after user hit the "OK" button
            }
        });
        snackbar.show();
    }
}
