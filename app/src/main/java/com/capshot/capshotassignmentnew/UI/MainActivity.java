package com.capshot.capshotassignmentnew.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.capshot.capshotassignmentnew.Constant.Constant;
import com.capshot.capshotassignmentnew.R;
import com.capshot.capshotassignmentnew.UI.Adapter.FontsAdapter;
import com.capshot.capshotassignmentnew.Utils.AdditionalFunctionality;
import com.capshot.capshotassignmentnew.Utils.FontProvider;
import com.capshot.capshotassignmentnew.ViewModel.Font;
import com.capshot.capshotassignmentnew.ViewModel.Interfaces.LimitsForTextLayer;
import com.capshot.capshotassignmentnew.ViewModel.Layer;
import com.capshot.capshotassignmentnew.ViewModel.TextLayer;
import com.capshot.capshotassignmentnew.Widgets.Entity.ImageEntity;
import com.capshot.capshotassignmentnew.Widgets.Entity.MotionEntity;
import com.capshot.capshotassignmentnew.Widgets.Entity.TextEntity;
import com.capshot.capshotassignmentnew.Widgets.MotionView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TextEditorDialogFragment.OnTextLayerCallback {

    private static final String TAG = "MainActivity";
    protected MotionView motionView;
    protected View textEntityEditPanel;
    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if (entity instanceof TextEntity) {
                textEntityEditPanel.setVisibility(View.VISIBLE);
            } else {
                textEntityEditPanel.setVisibility(View.GONE);
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            startTextEntityEditing();
        }
    };
    private FontProvider fontProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fontProvider = new FontProvider(getResources());
        motionView = findViewById(R.id.main_motion_view);
        textEntityEditPanel = findViewById(R.id.main_motion_text_entity_edit_panel);
        motionView.setMotionViewCallback(motionViewCallback);
        addSticker(R.drawable.sample_image, true);
        initTextEntitiesListeners();
    }

    private void addSticker(final int stickerResId,final boolean isStartOfActivity) {
        motionView.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
                Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);
                ImageEntity entity = new ImageEntity(layer, pica, motionView.getWidth(), motionView.getHeight());
                motionView.addEntityAndPosition(entity, isStartOfActivity);
            }
        });
    }

    private void initTextEntitiesListeners() {
        findViewById(R.id.text_entity_font_size_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseTextEntitySize();
            }
        });
        findViewById(R.id.text_entity_font_size_decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseTextEntitySize();
            }
        });
        findViewById(R.id.text_entity_color_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextEntityColor();
            }
        });
        findViewById(R.id.text_entity_font_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextEntityFont();
            }
        });
        findViewById(R.id.text_entity_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTextEntityEditing();
            }
        });
    }

    private void increaseTextEntitySize() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().increaseSize(LimitsForTextLayer.FONT_SIZE_STEP);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

    private void decreaseTextEntitySize() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().decreaseSize(LimitsForTextLayer.FONT_SIZE_STEP);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

    private void changeTextEntityColor() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity == null) {
            return;
        }

        int initialColor = textEntity.getLayer().getFont().getColor();

        ColorPickerDialogBuilder
                .with(MainActivity.this)
                .setTitle(R.string.select_color)
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(8) // magic number
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setColor(selectedColor);
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void changeTextEntityFont() {
        final List<String> fonts = fontProvider.getFontNames();
        FontsAdapter fontsAdapter = new FontsAdapter(this, fonts, fontProvider);
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_font)
                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .show();
    }

    private void startTextEntityEditing() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
    }

    @Nullable
    private TextEntity currentTextEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_add_sticker) {
            Intent intent = new Intent(this, StickerSelectActivity.class);
            startActivityForResult(intent, Constant.SELECT_STICKER_REQUEST_CODE);
            return true;
        } else if (item.getItemId() == R.id.main_add_text) {
            addTextSticker();
        } else if (item.getItemId() == R.id.save_image) {
            permissionGrantedNowSaveImage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void permissionGrantedNowSaveImage() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, Constant.WRITE_REQUEST_CODE);
            }
        }
    }

    private void saveImage() {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + Constant.rootFolder);
        boolean success = false;
        if(!folder.exists()) {
            success = folder.mkdirs();
        }
        Log.d(TAG, "saveImage: "+success+" folder");
        String uniqueString = UUID.randomUUID().toString();
        File file = new File(Environment.getExternalStorageDirectory().toString()+ Constant.rootFolder+ "/"+uniqueString+Constant.fileType);
        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "saveImage: "+success+" file" );
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            Log.d(TAG, "saveImage: ostream "+outputStream);
            Bitmap well = motionView.getThumbnailImage();
            Bitmap save = Bitmap.createBitmap(motionView.getWidth(), motionView.getHeight(), Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0,0,motionView.getWidth(),motionView.getHeight()),paint);
            now.drawBitmap(well, new Rect(0,0,well.getWidth(),well.getHeight()), new Rect(0,0,motionView.getWidth(),motionView.getHeight()), null);
            save.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            AdditionalFunctionality.setSnackBar(findViewById(android.R.id.content),"Saved" );
        } catch(NullPointerException e) {
            e.printStackTrace();
            AdditionalFunctionality.setSnackBar(findViewById(android.R.id.content),"Null Error" );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            AdditionalFunctionality.setSnackBar(findViewById(android.R.id.content),"File Error" );
        }  catch (Exception e) {
            e.printStackTrace();
            AdditionalFunctionality.setSnackBar(findViewById(android.R.id.content),"Unknown error occured, Contact Support" );
        }
    }

    protected void addTextSticker() {
        TextLayer textLayer = createTextLayer();
        TextEntity textEntity = new TextEntity(textLayer, motionView.getWidth(),
                motionView.getHeight(), fontProvider);
        motionView.addEntityAndPosition(textEntity,false);
        // move text sticker up so that its not hidden under keyboard
        PointF center = textEntity.absoluteCenter();
        center.y = center.y * 0.5F;
        textEntity.moveCenterTo(center);
        // redraw
        motionView.invalidate();

        startTextEntityEditing();
    }

    private TextLayer createTextLayer() {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(LimitsForTextLayer.INITIAL_FONT_COLOR);
        font.setSize(LimitsForTextLayer.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());
        textLayer.setFont(font);
        textLayer.setText(Constant.defaultText);

        return textLayer;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.SELECT_STICKER_REQUEST_CODE) {
                if (data != null) {
                    int stickerId = data.getIntExtra(Constant.EXTRA_STICKER_ID, 0);
                    if (stickerId != 0) {
                        addSticker(stickerId, false);
                    }
                }
            }
        }
    }

    @Override
    public void textChanged(@NonNull String text) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                motionView.invalidate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.WRITE_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Granted.
                    permissionGrantedNowSaveImage();
                }
                else{
                    AdditionalFunctionality.setSnackBar(findViewById(android.R.id.content),"Denied Permission, Grant Permission" );
                }
                break;
        }
    }
}
