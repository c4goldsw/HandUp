package com.handup.handup.controller;

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
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.General;
import com.handup.handup.model.fbquery.FbDataChange;
import com.handup.handup.model.fbquery.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView userName;
    private TextView points;
    private ImageView profilePicture;

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

        Log.d("ProfileFragment", "onCreateView: starting!");

        //get UI views
        points = (TextView) ui.findViewById(R.id.rank_card_points);
        userName = (TextView) ui.findViewById(R.id.profile_card_name);
        profilePicture = (ImageView) ui.findViewById(R.id.profile_card_picture);

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

    //taken from http://bit.ly/1UmSnOi , on SO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_IMAGE)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                selectProfilePicture(data);
            }
    }

     /*===========================================================================================
    UI Methods
    ===========================================================================================*/

    public void updateUI() {

        Log.d("updateUI", "Being called!");

        User u = MainActivity.getUser();

        if (userName == null || u == null) {

            Log.d("updateUI", "userName " + userName + " user " + u);
            return;
        }

        points.setText("Points: " + u.getPoints());
        userName.setText(MainActivity.getMe().getMe().getFirstName());

        byte[] profilePictureArray = u.getInAppProfilePicture();

        if (profilePictureArray != null) {

            Bitmap picture = BitmapFactory.decodeByteArray(profilePictureArray, 0,
                    profilePictureArray.length);
            profilePicture.setImageBitmap(picture);
        }
    }

    public void selectProfilePicture(Intent data) {

        Uri selectedImage = data.getData();

        try {

            Bitmap finalImage = General.getPortraitImage(selectedImage, getActivity(), 300, 200);
            profilePicture.setImageBitmap(finalImage);

            //http://stackoverflow.com/questions/26292969/can-i-store-image-files-in-firebase-using-java-api
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String imageString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            MainActivity.getUser().setProfilePicture(imageString, stream.toByteArray());

            new FbDataChange("/users/" + MainActivity.getUser().getUid() +
                    "/profilePicture", imageString).execute();
        } catch (IOException e) {}
    }
}
