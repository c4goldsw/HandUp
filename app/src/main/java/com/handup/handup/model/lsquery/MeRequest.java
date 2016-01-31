package com.handup.handup.model.lsquery;

/**
 * Created by Christopher on 1/3/2016.
 */
/**
 * GSON POJO holder for /me requests
 */
public class MeRequest {

    private Me me;

    public Me getMe() {
        return me;
    }

    public void setMe(Me me) {
        this.me = me;
    }

    public MeRequest(){

        me = new Me();
    }

    public static class Me {

        private String id;
        private String firstName;
        private String lastName;

        /*private String emailAddress;
        private String clientString;*/

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /*public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getClientString() {
            return clientString;
        }

        public void setClientString(String clientString) {
            this.clientString = clientString;
        }*/
    }
}