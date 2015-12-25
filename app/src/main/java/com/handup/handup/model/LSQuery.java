package com.handup.handup.model;

import com.handup.handup.helper.Constants;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;

/**
 * Created by Christopher on 12/25/2015.  Used to make queries to learning studio.  The class
 * is a singleton so that no additional resources are used making additional elements.
 */
public class LSQuery {

    /**
     * The singleton
     */
    private static LSQuery singleton;

    private OAuthConfig config;

    /**
     * Getter method for the singleton.  If the singleton is hasn't been instantiated, we do so.
     * @return The singleton
     */
    public static LSQuery getSingleton(){
        return singleton != null ? singleton : new LSQuery() ;
    }

    private LSQuery(){

        //set the singleton to be this
        singleton = this;

        //create the config object
        config = new OAuthConfig();
        config.setApplicationId(Constants.APPLICATION_ID);
        config.setApplicationName(Constants.APP_NAME);
        config.setConsumerKey(Constants.TOKEN_KEY);
        config.setConsumerSecret(Constants.APPLICATION_SECRET);
        config.setClientString(Constants.CLIENT_STRING);
    }

    public OAuthConfig getConfig(){

        return config;
    }
}
