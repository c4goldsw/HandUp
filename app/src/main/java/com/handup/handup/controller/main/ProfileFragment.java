package com.handup.handup.controller.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.controller.ContentDisplay;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ImageHandler;
import com.handup.handup.helper.LevelPicker;
import com.handup.handup.model.Content;
import com.handup.handup.model.fbquery.ContentPullTask;
import com.handup.handup.model.Course;
import com.handup.handup.model.fbquery.FbDataChange;
import com.handup.handup.model.User;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ContentPullTask.ContentQueryImplementer {

    private TextView userName;
    private TextView points;
    private TextView level;
    private ImageView profilePicture;
    private ImageView contentPicture;

    private CardView profileCard;
    private CardView contentCard;

    private OnProfileInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.onProfileSelect(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View ui = inflater.inflate(R.layout.fragment_profile, container, false);

        //get UI views
        points = (TextView) ui.findViewById(R.id.rank_card_points);
        userName = (TextView) ui.findViewById(R.id.profile_card_name);
        profilePicture = (ImageView) ui.findViewById(R.id.profile_card_picture);
        contentPicture = (ImageView) ui.findViewById(R.id.content_card_picture);

        profileCard = (CardView) ui.findViewById(R.id.profile_card);
        contentCard = (CardView) ui.findViewById(R.id.content_card);

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Code from: http://bit.ly/1UmSnOi*/
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        Constants.SELECT_IMAGE);
            }
        });

        contentCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ContentDisplay.class);
                i.putExtra(Constants.PUT_EXTRA_UID, MainActivity.getUser().getUid());

                ArrayList<Course> courses = MainActivity.getCourses();
                String[] courseIDs = new String[courses.size()];
                for(int p = 0; p < courses.size(); ++p){
                    courseIDs[p] = Integer.toString(courses.get(p).getId());
                }

                i.putExtra(Constants.PUT_EXTRA_COURSE_IDS,courseIDs);

                startActivity(i);
            }
        });

        level = (TextView) ui.findViewById(R.id.rank_card_level);

        updateUI();

        return ui;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileInteractionListener) {
            mListener = (OnProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onContentQueryFinish(Content c) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProfileInteractionListener {

        void onProfileSelect(ProfileFragment f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //taken from http://bit.ly/1UmSnOi , on SO
        if (requestCode == Constants.SELECT_IMAGE)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                selectProfilePicture(data);
        }
    }

    /*===========================================================================================
    UI Methods
    ===========================================================================================*/

    public void updateUI() {

        User u = MainActivity.getUser();

        if (userName == null || u == null) {

            return;
        }

        points.setText("Points: " + u.getPoints());
        level.setText("" + LevelPicker.levelPicker((int) u.getPoints()));
        userName.setText(MainActivity.getMeRequest().getMe().getFirstName());

        byte[] profilePictureArray = u.getInAppProfilePicture();

        if (profilePictureArray != null) {

            profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(profilePictureArray, 0,
                profilePictureArray.length));

        }else{
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_24dp));
        }

        if(MainActivity.getUserContentPreview() != null){

            contentPicture.setImageBitmap(MainActivity.getUserContentPreview());

        }else{

            contentPicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_assignment_24dp));
        }
    }

    public void selectProfilePicture(Intent data) {

        Uri selectedImage = data.getData();

        try {

            Bitmap finalImage = ImageHandler.getPortraitImage(selectedImage, getActivity(), 300, 200);
            profilePicture.setImageBitmap(finalImage);

            Bitmap thumbNail = ImageHandler.getPortraitImage(selectedImage, getActivity(), 35, 23);

            //http://stackoverflow.com/questions/26292969/can-i-store-image-files-in-firebase-using-java-api
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            stream.reset();
            thumbNail.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String thumbNString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            MainActivity.getUser().setProfilePicture(imageString, stream.toByteArray());
            stream.close();

            new FbDataChange("/users/" + MainActivity.getUser().getUid() +
                    "/profilePicture", imageString).execute();

            new FbDataChange("/users/" + MainActivity.getUser().getUid() +
                    "/profileThumb", thumbNString).execute();
        } catch (IOException e) {}
    }
}
