package com.android.bakingapp;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;



public class RecipeDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "RecipeStepDetails";


    // TODO: Rename and change types of parameters
    private com.android.bakingapp.Step mStepDetail = new com.android.bakingapp.Step();
    private TextView mTextDetailsRecipeSteps;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Uri progressiveUri;
    private long playbackPosition = 0;
    private View rootview;




    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(com.android.bakingapp.Step step) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, step);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
             mStepDetail.setVideoURL(savedInstanceState.get("VIDEO_URL").toString());
             playbackPosition = savedInstanceState.getLong("PLAY_BACK_POSITION");
             mStepDetail = savedInstanceState.getParcelable("STEP");




        }

        rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mTextDetailsRecipeSteps = (TextView) rootview.findViewById(R.id.tv_DetailRecipeSteps);
        mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.view_exoplayer);

        mTextDetailsRecipeSteps.setText(mStepDetail.getDescription());

        if (mStepDetail.getVideoURL() != null) {
            progressiveUri = Uri.parse(mStepDetail.getVideoURL());
            initializePlayer(progressiveUri);
        }

        if (mStepDetail.getVideoURL() == null || mStepDetail.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
            mTextDetailsRecipeSteps.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "There is no video associated with this step.", Toast.LENGTH_SHORT).show();
        }

        if (isHandsetAndLandscape() && mStepDetail.getVideoURL()!=null){
            mTextDetailsRecipeSteps.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

        }


    // Inflate the layout for this fragment
    return rootview;
}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initializePlayer(Uri mediaUri) {
        // Create an instance of the ExoPlayer.
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.seekTo(playbackPosition);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(false);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    public void setRecipeDescription (String recipeDescription){
        mStepDetail.setDescription(recipeDescription);
    }

    public void setRecipeUri (String recipeUri){
        mStepDetail.setVideoURL(recipeUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressiveUri != null)
            initializePlayer(progressiveUri);
        if(isHandsetAndLandscape()) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putLong("PLAY_BACK_POSITION", playbackPosition);
        outState.putParcelable("STEP", mStepDetail);
        outState.putString("VIDEO_URL", progressiveUri.toString());

    }

    private boolean isHandsetAndLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && getResources().getConfiguration().screenWidthDp <= 900;
    }

}
