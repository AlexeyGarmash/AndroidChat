package com.example.tabloproject.blablachat;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupsActivity extends AppCompatActivity {



    private RecyclerView mRecyclerChanels;

    private List<BaseChannel> listChanels;

    private ProgressBar progressBar;

    private GroupsAdapter mGroupsAdapter;

    private LinearLayoutManager mLayoutManager;

    private OpenChannelListQuery channelListQuery;

    private GroupChannelListQuery channelListQueryGr;

    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setTitle("All chanels");


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mRecyclerChanels = findViewById(R.id.my_recycler_view);


        listChanels = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerChanels.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerChanels.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerChanels.addItemDecoration(dividerItemDecoration);

        setAdapter(listChanels);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("GroupsActivity", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        updateOperation();
                    }
                }
        );

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupsActivity.this, AddOpenChannelActivity.class);

                startActivity(intent);
            }
        });







    }

    private void updateOperation() {
        listChanels.clear();
        progressBar.setVisibility(View.VISIBLE);

        getOpenChannels();

        progressBar.setVisibility(View.VISIBLE);

        getGroupChannels();
    }

    void setAdapter(List<BaseChannel> listChanel){
        mGroupsAdapter = new GroupsAdapter(listChanel, this);
        mRecyclerChanels.setAdapter(mGroupsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOperation();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_chanels:
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Select open chanels", Toast.LENGTH_SHORT).show();
                listChanels.clear();
                getOpenChannels();
                getSupportActionBar().setTitle("Open channels");
                return true;

            case R.id.group_chanels:
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Select group chanels", Toast.LENGTH_SHORT).show();
                listChanels.clear();
                getGroupChannels();
                getSupportActionBar().setTitle("Group channels");
                return true;

            case R.id.all_chanels:
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Select all chanels", Toast.LENGTH_SHORT).show();
                listChanels.clear();
                getOpenChannels();
                getGroupChannels();
                getSupportActionBar().setTitle("All channels");
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_toolbar, menu);
        return true;
    }

    private void getOpenChannels(){
        channelListQuery = OpenChannel.createOpenChannelListQuery();
        channelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(List<OpenChannel> channels, SendBirdException e) {
                if (e != null) {
                    // Error.
                    progressBar.setVisibility(View.INVISIBLE);
                    mySwipeRefreshLayout.setRefreshing(false);
                    return;

                }


                listChanels.addAll(channels);
                mGroupsAdapter.refresh();
                progressBar.setVisibility(View.INVISIBLE);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getGroupChannels(){
        channelListQueryGr = GroupChannel.createMyGroupChannelListQuery();
        channelListQueryGr.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler(){
            @Override
            public void onResult(List<GroupChannel> channels, SendBirdException e) {
                if (e != null) {
                    // Error.
                    progressBar.setVisibility(View.INVISIBLE);
                    mySwipeRefreshLayout.setRefreshing(false);
                    return;
                }

                listChanels.addAll(channels);
                mGroupsAdapter.refresh();
                progressBar.setVisibility(View.INVISIBLE);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    /*private List<BaseChannel> filterOpenGroup(String filter){
        List<BaseChannel> ogChanels = new ArrayList<>();
        for(BaseChannel chanel: listChanels){
            if(filter.equals("Group"))
            {
            if(chanel.isGroupChannel()){
                ogChanels.add(chanel);
            }
            }
            else
            {
                if(chanel.isOpenChannel()){
                    ogChanels.add(chanel);
                }
            }
        }

        return ogChanels;

    }*/
}
