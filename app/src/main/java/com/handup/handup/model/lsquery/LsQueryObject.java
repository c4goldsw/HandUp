package com.handup.handup.model.lsquery;

import com.handup.handup.helper.Constants;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.oauth.OAuthServiceFactory;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;

/**
 * Created by Christopher on 12/25/2015.  Used to make queries to learning studio.  The class
 * is a singleton so that no additional resources are used making additional elements.  Includes
 * code taken from the Pearson API Java documenation
 */
public class LsQueryObject {

    /**
     * The singleton
     */
    private static LsQueryObject singleton;

    private OAuthConfig config;
    private OAuthServiceFactory factory;

    private BasicService bService;

    /**
     * Getter method for the singleton.  If the singleton is hasn't been instantiated, we do so.
     * @return The singleton
     */
    public static LsQueryObject getSingleton(){
        return singleton != null ? singleton : new LsQueryObject() ;
    }

    /**
     * Constructor for the class.  Instantiates all other objects too
     */
    private LsQueryObject(){

        //set the singleton to be this
        singleton = this;

        //create the config object
        config = new OAuthConfig();
        config.setApplicationId(Constants.APPLICATION_ID);
        config.setApplicationName(Constants.APP_NAME);
        config.setConsumerKey(Constants.TOKEN_KEY);
        config.setConsumerSecret(Constants.APPLICATION_SECRET);
        config.setClientString(Constants.CLIENT_STRING);

        factory = new OAuthServiceFactory(config);

        bService = new BasicService(factory);
    }

    public OAuthServiceFactory getFactory(){

        return factory;
    }

    public BasicService getBasicService(){

        return  bService;
    }
}
