package com.example.unistud.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.unistud.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.unistud.Fragments.StudentTutorialFragment.TUTORIAL_ID;
import static com.example.unistud.Fragments.StudentTutorialFragment.TUTORIAL_LINK;
import static com.example.unistud.Fragments.StudentTutorialFragment.TUTORIAL_STATUS;

public class PlayVideo extends AppCompatActivity {

    private static final String APP_NAME = MainActivity.class.getSimpleName();
    private static final java.lang.String POS_KEY = "pos";
    private SimpleExoPlayer player;
    private String tutorialLINK;
    private String tutorialStatus;
    private Intent intent;
    private Uri videoUri;
    private String link;
    private MediaSource videoSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        intent = getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    private void initializePlayer(){
        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //Initialize simpleExoPlayerView
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.player_view);
        if(player != null && simpleExoPlayerView != null) {
            simpleExoPlayerView.setPlayer(player);
        }

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this,"UniStud"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        tutorialLINK = intent.getStringExtra(TUTORIAL_LINK);
        tutorialStatus = intent.getStringExtra(TUTORIAL_STATUS);



        if(tutorialStatus.equals("live")){
            link = "http://" + getResources().getString(R.string.server_ip) + ":1935/live/myStream/playlist.m3u8";
            videoUri = Uri.parse(link);
            videoSource = new HlsMediaSource(videoUri,
                    dataSourceFactory, 3, null, null);
        }
        else {
            link = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
            if(!link.equals("")) {
                videoUri = Uri.parse(link);
                videoSource = new ExtractorMediaSource(videoUri,
                        dataSourceFactory, extractorsFactory, null, null);
            }
        }

        if(videoSource != null) {
            player.prepare(videoSource);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}


