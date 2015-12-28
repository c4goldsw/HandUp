package com.handup.handup.helper;

/**
 * Created by Christopher on 12/26/2015.  Helper class used to convert API responses from Json into
 * POJOs.
 */
public class JsonConverter {

    /**
     * GSON POJO holder for /me requests
     */
    public static class Me {

        private MeContent me;

        public MeContent getMe() {
            return me;
        }

        public void setMe(MeContent me) {
            this.me = me;
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

    /**
     *
     */

}
