package com.example.tabloproject.blablachat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;

public class MessageListActivity extends AppCompatActivity {

    //private final String mChannelUrl = "sendbird_open_channel_tutorial";
    private final static String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_CHAT";

    private String channelUrl;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private LinearLayoutManager mLayoutManager;

    private Button mSendButton;
    private EditText mMessageEditText;

    private OpenChannel mChannel;

    private ProgressBar mProgressBar3;


    private boolean isMessagesLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_messages);
        setSupportActionBar(myToolbar);

        channelUrl = getIntent().getExtras().getString(GroupsAdapter.EXTRA_CHANNEL);

        showMessage("MessagesActivity: ChanelUrl" + channelUrl);

        mSendButton = (Button) findViewById(R.id.button_chatbox_send);
        mMessageEditText = (EditText) findViewById(R.id.edittext_chatbox);


        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mMessageRecycler.setLayoutManager(mLayoutManager);
        mProgressBar3 = findViewById(R.id.progressBar3);


        mProgressBar3.setVisibility(View.VISIBLE);

        setNotTouchInterface();
        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    e.printStackTrace();
                    mProgressBar3.setVisibility(View.INVISIBLE);
                    clearNotTouchInterface();
                    isMessagesLoading = false;
                    return;

                }

                mProgressBar3.setVisibility(View.VISIBLE);
                setNotTouchInterface();
                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            e.printStackTrace();
                            mProgressBar3.setVisibility(View.INVISIBLE);
                            clearNotTouchInterface();
                            isMessagesLoading = false;
                            return;
                        };
                        mProgressBar3.setVisibility(View.INVISIBLE);
                        clearNotTouchInterface();
                        mMessageAdapter = new MessageListAdapter(getContext(),openChannel);
                        mMessageRecycler.setAdapter(mMessageAdapter);
                        isMessagesLoading = true;
                    }
                });
            }
        });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageAdapter.sendMessage(mMessageEditText.getText().toString());
                mMessageEditText.setText("");
            }
        });

        mMessageRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isMessagesLoading && mLayoutManager.findLastVisibleItemPosition() == mMessageAdapter.getItemCount() - 1) {
                    mMessageAdapter.loadPreviousMessages();
                }
            }
        });


    }

    private void setNotTouchInterface(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
    private void clearNotTouchInterface(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }



    @Override
    protected void onResume() {
        super.onResume();


        // Receives messages from SendBird servers
        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(channelUrl) && baseMessage instanceof UserMessage) {
                    mMessageAdapter.appendMessage((UserMessage) baseMessage);
                }
            }
        });


    }



    Context getContext(){
        return this;
    }
    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);

        super.onPause();
    }

    private class MyTextWatcher implements TextWatcher {

        private View viewInput;


        private MyTextWatcher(View viewInp) {
            this.viewInput = viewInp;

        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (viewInput.getId()) {
                case R.id.input_id:
                    validateView(mMessageEditText);
                    break;

            }
        }
    }

    private void validateView(EditText text) {
        if(text.getText().toString().intern().length() == 0){
            mSendButton.setEnabled(false);
            mSendButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            mSendButton.setEnabled(true);
            mSendButton.setVisibility(View.VISIBLE);
        }
    }
}
