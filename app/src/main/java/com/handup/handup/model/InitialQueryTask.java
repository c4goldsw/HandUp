package com.handup.handup.model;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.handup.handup.controller.main.MainActivity;
import com.handup.handup.model.fbquery.User;
import com.handup.handup.model.lsquery.Course;
import com.handup.handup.model.lsquery.LsQueryObject;
import com.handup.handup.model.lsquery.MeRequest;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Christopher on 12/31/2015. Contains code taken from the Perason API Java
 * documentation, source code for the Pearson API
 */
public class InitialQueryTask extends AsyncTask<Void, Void, Void> {

    /**
     * The username is used to make requests to LS
     */
    String username;
    InitialQueryUser usingClass;

    private static final int ME = 0;
    private static final int ME_COURSES = 1;
    private String[] lsQueryURIS = {"/me","/me/courses?expand=course"};

    /**
     * Constructor for the async task
     * @param username username that is used to make the REST call to learning studio
     * @param usingClass the object that is using this task
     */
    public InitialQueryTask(String username, InitialQueryUser usingClass){

        this.username = username;
        this.usingClass = usingClass;
    }

    @Override
    protected Void doInBackground(Void... params) {


        //====================== Get data from learning studio ======================
        HashMap<String, String> jsonResponses = new HashMap<>();

        jsonResponses = makeLsQueries(jsonResponses);

        try {
            //====================== Get data from learning studio responses ======================
            Gson g  = new Gson();

            MeRequest meRequest = g.fromJson(jsonResponses.get(lsQueryURIS[ME]),
                    MeRequest.class);
            MainActivity.setMeRequest(meRequest);

            //Taken from http://bit.ly/1TzoZV0;
            MainActivity.setCourses(getCoursesFromJson(jsonResponses.get(lsQueryURIS[ME_COURSES]), g));

            //====================== Get data from FireBase ======================
            final Firebase ref = new Firebase("https://intense-inferno-38.firebaseio.com/users/" +
                    MainActivity.getMeRequest().getMe().getId());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //============ Get send data to main activity, start updating UIs ============
                    User user = dataSnapshot.getValue(User.class);

                    if(user == null) {//we must check to see if the user exists in FB. else, make.
                        user = new User();
                        user.setUid(MainActivity.getMeRequest().getMe().getId());
                        ref.setValue(user);
                    }

                    MainActivity.setUser(user);

                    usingClass.onInitialQueryFinish();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("FireBase", "Error:" + firebaseError);
                }
            });

        } catch (JsonSyntaxException e) {
            Log.d("Async", "Error json: " + e);
        } catch (NullPointerException b){
            Log.d("Async", "Error null: " + b);
        }

        return null;
    }

    /* Referenced / taken from http://bit.ly/1PaMdgv and http://bit.ly/1ZKaktX */
    private ArrayList<Course> getCoursesFromJson(String courseResponse, Gson g){

        ArrayList<Course> courses = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(courseResponse).getAsJsonObject();
        JsonArray jCourses = json.get("courses").getAsJsonArray();

        for(Iterator<JsonElement> courseIter = jCourses.iterator(); courseIter.hasNext();){
            try
            {
                JsonObject element = courseIter.next().getAsJsonObject();
                JsonArray links = element.get("links").getAsJsonArray();


                Course c = new Course();

                JsonObject courseTemp = links.iterator().next().getAsJsonObject();
                JsonObject course = courseTemp.get("course").getAsJsonObject();
                c.setId(course.get("id").getAsInt());
                c.setName(course.get("displayCourseCode").toString().replace("\"",""));

                courses.add(c);
            }
            catch(NullPointerException e){Log.d("GetCourse","Err: " + e);}
        }

        return courses;
    }

    private HashMap<String, String> makeLsQueries(HashMap<String, String> jsonResponses){

        BasicService b = LsQueryObject.getSingleton().getBasicService();
        b.useOAuth2(username);

        Response r = null;

        for(int i = 0; i < lsQueryURIS.length; i++){
            try{
                r = b.doMethod(AbstractService.HttpMethod.GET, lsQueryURIS[i], null);

            } catch (IOException e){
                return null;
            }

            if(r.isError()) {
                int httpStatusCode = r.getStatusCode();// 404
                String httpStatusMessage = r.getStatusMessage();// Not Found

                System.out.println(httpStatusCode + " " + httpStatusMessage);
            }
            else {
                jsonResponses.put(lsQueryURIS[i], r.getContent());
            }
        }

        return jsonResponses;
    }

    public interface InitialQueryUser {

        public void onInitialQueryFinish();
    }

}