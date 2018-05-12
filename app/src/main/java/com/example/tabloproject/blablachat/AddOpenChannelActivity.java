package com.example.tabloproject.blablachat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;

import java.io.IOException;

public class AddOpenChannelActivity extends AppCompatActivity {


    private Button mButtonUpload;

    private ImageButton mButtonChoosePic;

    private EditText mEditTextNameChannel;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_open_channel);

        mButtonChoosePic = findViewById(R.id.imgChannelChoose);

        mButtonUpload = findViewById(R.id.btnUpload);

        mEditTextNameChannel = findViewById(R.id.input_name_channel);

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadChannel();
            }
        });


        mButtonChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    //NAME, COVER_IMAGE_OR_URL, DATA, CUSTOM_TYPE,
    private void uploadChannel() {
        String nameChannel = mEditTextNameChannel.getText().toString();



        OpenChannel.createChannelWithOperatorUserIds(nameChannel, null, null, null, new OpenChannel.OpenChannelCreateHandler() {
            @Override
            public void onResult(OpenChannel openChannel, SendBirdException e) {
                if(e != null){
                    Snackbar.make(mButtonUpload, "Upload failed. Try again?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", yesOption);
                    return;
                }
                finish();
            }
        });


    }

    View.OnClickListener yesOption = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            uploadChannel();
        }
    };



    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mButtonChoosePic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
