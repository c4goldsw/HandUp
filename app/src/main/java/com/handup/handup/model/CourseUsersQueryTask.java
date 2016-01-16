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
import com.handup.handup.model.lsquery.LsQueryObject;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Christopher on 1/4/2016. Contains code taken from the Perason API Java
 * documentation, source code for the Pearson API
 */
public class CourseUsersQueryTask extends AsyncTask<Void, Void, Void> {

    private CourseUserQueryImplementer cu;
    private int courseID;
    private String username;
    private String userID;

    private static final String lsQueryFinal = "/courses/?/roster";
    private String lsQuery;

    public CourseUsersQueryTask(int courseID, CourseUserQueryImplementer cu, String username,
                                String userID){
        this.courseID = courseID;
        this.cu = cu;
        this.username = username;
        this.userID = userID;
    }

    @Override
    protected Void doInBackground(Void... params) {

        lsQuery = lsQueryFinal.replace("?","" + Integer.toString(courseID));

        //====================== Get data from learning studio ======================
        String lsJsonResponse = makeLsQueries(lsQuery);

        try {
            //====================== Get data from learning studio responses ======================
            Gson g  = new Gson();

            final ArrayList<User> users = getUsersFromJson(lsJsonResponse, g);
            final HashMap<String, User> userTemp = new HashMap<>();
            for(User u : users) {
                userTemp.put(u.getUid(), u);
            }

            //====================== Get data from FireBase ======================
            Firebase ref = new Firebase("https://intense-inferno-38.firebaseio.com/users");

            for(User u : users) {

                Firebase userRef = ref.child(u.getUid());

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //============ Get send data to main activity, start updating UIs ============
                        User user = dataSnapshot.getValue(User.class);

                        if(user != null){
                            user.setDisplayName(userTemp.get(user.getUid()).getDisplayName());
                            cu.onCourseUserQueryFinish(user);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("FireBase", "Error:" + firebaseError);
                    }
                });
            }

        } catch (JsonSyntaxException e) {
            Log.d("Async", "Error json: " + e);
        } catch (NullPointerException b){
            Log.d("Async", "Error null: " + b);
        }

        return null;
    }

    /* Referenced / taken from http://bit.ly/1PaMdgv and http://bit.ly/1ZKaktX */
    private ArrayList<User> getUsersFromJson(String response, Gson g){

        ArrayList<User> users = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(response).getAsJsonObject();
        JsonArray jCourses = json.get("roster").getAsJsonArray();

        for(Iterator<JsonElement> userIter = jCourses.iterator(); userIter.hasNext();){
            try
            {
                JsonObject user = userIter.next().getAsJsonObject();

                User u = new User();
                u.setUid(Integer.toString(user.get("id").getAsInt()));

                if(!u.getUid().equals(userID)) {
                    u.setDisplayName(user.get("firstName").getAsString() + " " + user.get("lastName")
                            .getAsString());

                    users.add(u);
                }
            }
            catch(NullPointerException e){Log.d("GetCourse","Err: " + e);}
        }

        return users;
    }

    private String makeLsQueries(String query){

        BasicService b = LsQueryObject.getSingleton().getBasicService();
        b.useOAuth2(username);

        Response r = null;

        try{
            r = b.doMethod(AbstractService.HttpMethod.GET, query, null);

        } catch (IOException e){
            return null;
        }

        if(r.isError()) {
            int httpStatusCode = r.getStatusCode();// 404
            String httpStatusMessage = r.getStatusMessage();// Not Found

            System.out.println(httpStatusCode + " " + httpStatusMessage);

            return null;
        }

        return r.getContent();
    }

    public interface CourseUserQueryImplementer {
        public void onCourseUserQueryFinish(User u);
    }
}
