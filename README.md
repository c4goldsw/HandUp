# HandUp
Education app for the 2015/16 Perason Coding Contest.  [See the most recent update here.](https://www.youtube.com/watch?v=ThRieahvHM4)

## Inspiration
I'm a huge fan of Memrise, a vocabulary memorization app that awards user's points for doing memorization exercises.  It's a hugely successful model and based off of that,  I thought that a points-based model could have other applications.  Therefore, for the Pearson Student Coding Contest, I decided that I would use such a system to make a study motivation app.

## What it does:
The idea is that it motivates students to take notes and go the class - in other words, it's targeted towards students with poor study habits.  HandUp will award points to users for doing one of the following two tasks:

 - Taking pictures of lecture notes or content for a particular course and submitting it to the app - other users in that course can then "approve" it, giving the user points.
 - Going to class.  During the lecture period, students can connect on the app via BlueTooth, in turn verifying that the students are in their lecture.  By requiring the users to connect during lecture time, the odds of them both going to lecture are increased (**not implemented yet**).

This app is integrated with Pearson Learning Studio, allowing me to find out what courses users are enrolled in, as well as who their class mates are (given their campus uses learning studio).  Using a FireBase database, I can then keep track individual use information, such as the profile picture of a user or the number of points they have.

## How I built it
Memrise is an Android application built with Android studio.  The backend of the app consists of a test instance of Pearson Learning Studio (accessed through a REST API), a learning management system and FireBase, a NoSQL database.

## Challenges I ran into
This is the first Android app I've made outside of school and the first time I'm using a RESTful API, so I spent a great deal of time making sense of the Android environment as well as using and parsing queries to Pearson.  Making user data accessible via an online database also presented issues, such as downloading latency issues.

## Accomplishments that I'm proud of
This is the first Android app I've made outside of school - I'm trying to make it reach the most professional standards I can reasonably achieve and I feel that I'm headed in a good direction with the app.  Also, one major feature has finally been implemented.

## What I learned
A ton!

## What's next for HandUp
The only outstanding feature that needs to be implemented is meeting verification.  For meeting verification, a Bluetooth connection system for the app has to be put in place.  This  will allow users to get points by connecting via BlueTooth on the app during lecture times, as described above.  Aside from that, I also have to manage memory better (i.e. downloading smaller images for thumbnails, ensuring I don't have to many continuous connections to my FireBase DB, etc).

## Attribution:

Title Image

Taken from [Wikimedia Commons](https://commons.wikimedia.org/wiki/File:5th_Floor_Lecture_Hall.jpg), By Xbxg32000 [CC BY-SA 3.0 (http://creativecommons.org/licenses/by-sa/3.0) or GFDL (http://www.gnu.org/copyleft/fdl.html)], via Wikimedia Commons
