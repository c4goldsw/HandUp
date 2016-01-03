package com.handup.handup.model.lsquery;

/**
 * Created by Christopher on 1/3/2016.
 */
/**
 * GSON POJO holder for /me requests
 */
public class Me {

    private MeContent me;

    public MeContent getMe() {
        return me;
    }

    public void setMe(MeContent me) {
        this.me = me;
    }

    public Me(){

        me = new MeContent();
    }

    public static class MeContent {

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