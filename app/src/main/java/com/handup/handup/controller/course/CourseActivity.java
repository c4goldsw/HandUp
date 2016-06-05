package com.handup.handup.controller.course;

import android.app.Activity;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.HandUp;
import com.handup.handup.R;
import com.handup.handup.controller.course.content.ApproveDialog;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.content.MyContentRecyclerViewAdapter;
import com.handup.handup.controller.course.user.ConnectDialog;
import com.handup.handup.controller.course.user.SubscribeDialog;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.controller.course.user.UserRecyclerViewAdapter;
import com.handup.handup.controller.main.MainActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ImageHandler;
import com.handup.handup.model.Content;
import com.handup.handup.model.fbquery.ContentPullTask;
import com.handup.handup.model.CourseUsersQueryTask;
import com.handup.handup.model.fbquery.ContentPushTask;
import com.handup.handup.model.fbquery.GetLectureTimes;
import com.handup.handup.model.User;
import com.handup.handup.model.fbquery.GetSubscriptionTask;
import com.handup.handup.model.fbquery.SubscribedContentPullTask;
import com.handup.handup.view.CourseSectionPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class CourseActivity extends AppCompatActivity implements UserFragment.UserListFragmentInteractionListener,
        ContentFragment.OnContentInteractionListener, CourseUsersQueryTask.CourseUserQueryImplementer,
        GetLectureTimes.OnLectureTimeGetFinish, ContentPullTask.ContentQueryImplementer,
        GetSubscriptionTask.GetSubscriptionTaskUser, SubscribeDialog.SubscribeDialogListener,
        ApproveDialog.ApproveDialogListener, ConnectDialog.ConnectDialogListener{

    /*===========================================================================================
    Variables
    ===========================================================================================*/

    private CourseSectionPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    private static int courseID;
    private static String userName;
    private static String displayName;
    private static String uid;
    private static String courseName;

    private TabLayout tabLayout;

    /**
     * Used to keep track of the number of tabs
     */
    public static final int tabCount = 2;

    private int currentTab = 0;

    /**
     * An array of Drawables containing each icon used in the tabs
     */
    private Drawable[] icons;

    //The following arrays are used for business logic
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Date> lectureTimes;
    private ArrayList<Integer> subscriptionIDs;

    private UserFragment userFragment;
    private ContentFragment contentFragment;

    private Date selectedLecture;

    public static boolean canStartChangingSubscription = false;
    boolean canStartLookingForOtherUsers = false;

    /*===========================================================================================
    Model/ Controller methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseID = getIntent().getIntExtra(Constants.PUT_EXTRA_COURSE_ID, 0);
        userName = getIntent().getStringExtra(Constants.PUT_EXTRA_USERNAME);
        uid      = getIntent().getStringExtra(Constants.PUT_EXTRA_UID);
        getSupportActionBar().setTitle(courseName = getIntent().getStringExtra(Constants.PUT_EXTRA_COURSE_NAME));
        displayName = getIntent().getStringExtra(Constants.PUT_EXTRA_DISPLAY_NAME);
        setupTabs();

        //have to reset, since the variable is static
        canStartChangingSubscription = false;

        //mFab = (FloatingActionButton) findViewById(R.id.fab);
        /*mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check to see which tab we're pressing the button in
                if (currentTab == 0) {

                    //no longer implementing this feature, but leaving code in
                    if (0 == 1/*HandUp.doesSupportBluetooth()) {

                        if(!canStartLookingForOtherUsers){
                            Snackbar.make(view, "Please wait for all user IDs to be retrieved",
                                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            return;
                        }

                        //check to see if the phone is discoverable or not - make discoverable otherwise
                        if (HandUp.getBluetoothAdapter().getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                            Intent bluetoothDiscoverySettings = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            bluetoothDiscoverySettings.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                            startActivityForResult(bluetoothDiscoverySettings, Constants.BLUETOOTH_MAKE_DISCOVERABLE);
                        } else {
                            Bundle dialogInfo = new Bundle();

                            //get all the UIDs of the other course users
                            ArrayList<String> uidStrings = new ArrayList<String>();
                            for(User u : users){
                                uidStrings.add(u.getUid());
                            }

                            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_OID, uidStrings);
                            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_NAME, MainActivity.getUser().getDisplayName());
                            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_COURSE_ID, Integer.toString(courseID));
                            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_UID, uid);

                            //Taken from http://tinyurl.com/3xatjj5
                            DialogFragment newFragment = new ConnectDialog();
                            newFragment.setArguments(dialogInfo);
                            newFragment.show(getFragmentManager(), "ConnectDialog");
                        }
                    }else{
                        Snackbar.make(view, "Uh oh! Bluetooth isn't supported on this device!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                } else if (currentTab == 1) {
                    submitContent(view);
                }
            }
        });*/

        new GetLectureTimes(Integer.toString(courseID), this).execute();
        new CourseUsersQueryTask(courseID, this, userName, uid).execute();
        new GetSubscriptionTask(uid, Integer.toString(courseID), this).execute();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        userFragment.mRecyclerViewAdapater.emptyFbLists();
        contentFragment.mRecyclerViewAdapter.emptyFbLists();
    }

    //TODO: change currentTimeMillis to something system-time independent
    @Override
    public void submitContent(final View view){

        boolean inSubmittingPeriod = false;

        if(lectureTimes == null) {
            Snackbar.make(view, "Still loading", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        //if we're in range of one of the submission dates, submit
        for(Date time: lectureTimes){
            //Day to millisecond conversion: http://stackoverflow.com/questions/6980376/convert-from-days-to-milliseconds
            if(time.getTime() < System.currentTimeMillis() && System.currentTimeMillis() < time.getTime()
                     + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)){

                selectedLecture = time;
                inSubmittingPeriod = true;
            }
        }

        if(!inSubmittingPeriod) {
            Snackbar.make(view, "Content submission closed, try again later.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        //check to see if we haven't submitted anything for the day
        Firebase checkSubmission = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                "/" + courseID + "/lastContent/" + Constants.CONTENT_DAY);
        checkSubmission.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Empty entry ==> it's the first time this is happening
                if (dataSnapshot.getValue() == null) {

                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            , Constants.TAKE_PHOTO);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(System.currentTimeMillis()));

                    //If the user is attemping to submit content on a new day
                    if (!Integer.toString(c.get(Calendar.DAY_OF_YEAR)).equals(dataSnapshot.getValue()
                            .toString())) {

                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                , Constants.TAKE_PHOTO);
                    } else {
                        Snackbar.make(view, "You've already submitted content for today! Try again" +
                                " later", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //taken from http://bit.ly/1UmSnOi , on SO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {

            Calendar c = Calendar.getInstance();
            c.setTime(selectedLecture);

            try {

                Bitmap image = ImageHandler.getPortraitImage(
                        data.getData(), this, 250, 259);

                //users can only submit one piece of content per day - the previous content
                //from a previous day will be overwritten and removed from the content feed
                contentFragment.mRecyclerViewAdapter.removeItem(uid);

                Content newContent = new Content(image);
                newContent.setApproved(new ArrayList<Integer>());
                newContent.addApproval(Integer.parseInt(uid));
                newContent.setOwner(Integer.parseInt(uid));
                newContent.setDescription(displayName);

                contentFragment.updateUI(newContent);

                String imageString = ImageHandler.getImageString(image);

                new ContentPushTask(displayName, courseName, imageString, uid, Integer.toString(courseID), this,
                        Integer.toString(c.get(Calendar.DAY_OF_YEAR))).execute();

                //this is done to indicate to the main activity that new content was added
                //the content preview can then be updated accordingly
                setResult(Constants.COURSE_ACT_CONTENT_ADDED);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == Constants.BLUETOOTH_MAKE_DISCOVERABLE && resultCode == 1){

            HandUp.getBluetoothAdapter().setName(displayName);

            Bundle dialogInfo = new Bundle();
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_COURSE_ID, Integer.toString(courseID));

            //Taken from http://tinyurl.com/3xatjj5
            DialogFragment newFragment = new ConnectDialog();
            newFragment.setArguments(dialogInfo);
            newFragment.show(getFragmentManager(), "ConnectDialog");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedStateInstance){

        savedStateInstance.putSerializable(Constants.DATE, selectedLecture);
        super.onSaveInstanceState(savedStateInstance);
    }

    @Override
    public void onRestoreInstanceState(Bundle b){

        selectedLecture = (Date) b.getSerializable(Constants.DATE);
    }

    /*===========================================================================================
    View (UI) methods
    ===========================================================================================*/

    /**
     * Sets up the tabs and their needed adapters for switching between activities
     */
    private void setupTabs(){

        /* Create the adapter that will return a fragment for each of the three
         primary sections of the activity. We're sending it the screen dimensions for the
         content section, which uses a grid list which needs to be size appropriately*/
        mSectionsPagerAdapter = new CourseSectionPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Context c = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private Drawable[] buttonIcons;
            //Initializer
            {
                buttonIcons = new Drawable[2];
                buttonIcons[0] = getResources().getDrawable(R.drawable.ic_alarm_24dp);
                buttonIcons[1] = getResources().getDrawable(R.drawable.ic_camera_alt_24dp);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //change color of tab icons
                icons[currentTab].setAlpha(77);
                icons[currentTab].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                icons[position].setAlpha(255);
                icons[position].setColorFilter(ContextCompat.getColor(c, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                currentTab = position;

                //animate button: http://stackoverflow.com/questions/4689918/move-image-from-left-to-right-in-android
                /*TranslateAnimation anim = new TranslateAnimation(0f, 0, 0, 300);
                AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
                anim.setInterpolator(adi);
                anim.setDuration(250);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(1);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                        if(currentTab == 0)
                            mFab.setImageDrawable(buttonIcons[0]);
                        else if(currentTab == 1)
                            mFab.setImageDrawable(buttonIcons[1]);
                    }
                });

                mFab.startAnimation(anim);*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* Here, we start adding the nessescary components for the actual tabs*/

        tabLayout = (TabLayout) findViewById(R.id.tab_course);
        tabLayout.setupWithViewPager(mViewPager);

        icons = new Drawable[tabCount];

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        //Used to be in position two
        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_assignment_24dp);
        icons[1].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        icons[1].setAlpha(77);

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);

    }

    /*===========================================================================================
    Fragment related methods
    ===========================================================================================*/

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void setUserFragment(UserFragment userFragment) {
        this.userFragment = userFragment;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ArrayList<Integer> getSubscriptionIDs() {
        return subscriptionIDs;
    }

    @Override
    public void setContentFragment(ContentFragment contentFragment){ this.contentFragment = contentFragment; }

    @Override
    public void removeUserFromContentFeed(String uid){

        contentFragment.mRecyclerViewAdapter.removeItem(uid);
        subscriptionIDs.remove(new Integer(Integer.parseInt(uid)));
    }

    @Override
    public void addUserToContentFeed(final String uid){

        Firebase contentRef = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid + "/" +
                getCourseID() + "/lastContent");
        contentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null)
                    return;

                String imageString = (String) dataSnapshot.child("image").getValue();

                //get each approval
                ArrayList<Integer> approvals = new ArrayList<Integer>();
                for (Iterator<DataSnapshot> approvalIt = dataSnapshot.child(Constants.CONTENT_APPROVALS).getChildren().iterator();
                     approvalIt.hasNext(); ) {

                    approvals.add(Integer.parseInt(approvalIt.next().getKey()));
                }

                Content c = new Content();
                c.setApproved(approvals);
                c.setOwner(Integer.parseInt(uid));
                c.setImage(imageString);
                c.setContentDescription((String) dataSnapshot.child(Constants.CONTENT_DESCRIPTION).getValue()
                        , c.getApprovalCount() + ((c.getApprovalCount() == 1) ? " approve" : " approves"));

                contentFragment.mRecyclerViewAdapter.addItem(c);
                contentFragment.mRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(Constants.DEBUG_FIREBASE, "CourseActivty, Add single content to feed after subscribing: " +
                        firebaseError.toString());
            }
        });

        subscriptionIDs.add(Integer.parseInt(uid));
    }

    /*===========================================================================================
    Query interaction methods
    ===========================================================================================*/

    @Override
    public void onCourseUserQueryFinish(User u, boolean canStart) {

        canStartLookingForOtherUsers = canStart;
        users.add(u);
        userFragment.updateUI(u);
    }

    @Override
    public void onLectureTimeGetFinish(ArrayList<Date> times) {
        lectureTimes = times;
    }

    @Override
    public void onContentQueryFinish(Content c, boolean canStartChangingSubscription) {

        this.canStartChangingSubscription = canStartChangingSubscription;

        if(c != null) //TODO: Consider removing
            contentFragment.updateUI(c);
    }

    @Override
    public void onGetSubscriptionFinish(ArrayList<String> subscriptionIDs) {

        this.subscriptionIDs = new ArrayList<>();
        for(String s : subscriptionIDs)
            this.subscriptionIDs.add(Integer.parseInt(s));

        /*Pull the subscribed content into the content feed.  Note, the user themselves is
        "subscribed" to their own content for simplicity*/
        this.subscriptionIDs.add(Integer.parseInt(uid));
        new SubscribedContentPullTask(this.subscriptionIDs, Integer.toString(courseID), this).execute();
    }


    /*===========================================================================================
    Getters and Setters
    ===========================================================================================*/

    public static int getCourseID() {
        return courseID;
    }

    public static String getUid() {
        return uid;
    }

    @Override
    public void removeApproval(String oid) {
        contentFragment.mRecyclerViewAdapter.removeApproval(Integer.parseInt(oid), Integer.parseInt(uid));
    }

    @Override
    public void addApproval(String oid) {
        contentFragment.mRecyclerViewAdapter.addApproval(Integer.parseInt(oid), Integer.parseInt(uid));
    }

    @Override
    public void onConnectionCompleted() {

    }
}
