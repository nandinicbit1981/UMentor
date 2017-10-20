# UMentor

This repository contains the source code for UMentor Android App.

What is UMentor?
This app helps Mentors to connect with Mentees. This app uses firebase as the realtime database.
Features include:<br/> <br/>

    - Login using Google/Email.<br/>
    - Search for existing mentors.<br/>
    - Realtime notifications on adding/accepting mentor requests.<br/>
    - Realtime Chat applications for the mentors and mentees to interact with each other.<br/>
    - Rating a mentor based on the mentorship received.

    <br/>
    <br/>

# To run the app
<br />

Step 1 : git clone https://github.com/nandinicbit1981/UMentor.git<br />
Step 2: Update FIREBASEDATABASE in Constants.java with firebase database name.(Ref : https://firebase.google.com/docs/android/setup)<br />
Step 3 : Update the API_KEY constant in Constants.java with the Web API Key(This can be found in the overview tab in the firebase console.)<br /><br />

# Workflow :

1. Login screen : <br/>
This app uses firebase as the database, for authentication, for analytics as well as notifications. <br/>
We can sign up using google, email or facebook. <br/> <br/>
         <img src="https://github.com/nandinicbit1981/UMentor/blob/master/login.png" width="200" height="350" />

 <br/>
 <br/>
 
2. Edit Profile screen : <br/>
This screen can be used to edit the profile information of the user <br /> <br/>
         <img src="https://github.com/nandinicbit1981/UMentor/blob/master/edit_profile.png" width="200" height="350" />

 <br/>
 <br/>
 
 3. Search Screen : <br />
 On clicking on the search icon at the bottom navigation bar, mentors can be searched <br/> <br/>
         <img src="https://github.com/nandinicbit1981/UMentor/blob/master/search.png" width="200" height="350" />

 <br/>
 <br/>
 
 4. On search menu click : <br/>
 On selecting category and clicking on search icon, mentors belonging to the particular category will be displayed <br/> <br/>
        <img src="https://github.com/nandinicbit1981/UMentor/blob/master/search_mentors.png" width="200" height="350" />

 <br/>
 <br/>
 
 5. On search button click : <br/>
 On selecting category and clicking on search icon, mentors belonging to the particular category will be displayed <br/> <br/>
        <img src="https://github.com/nandinicbit1981/UMentor/blob/master/search_mentors.png" width="200" height="350" />

 <br/>
 <br/>
 
 6. Request a mentor : <br/>
 On clicking any of the items in the list, the profile of the user opens up, and we can send a request to add the user as our mentor <br/> <br/>
       <img src="https://github.com/nandinicbit1981/UMentor/blob/master/mentor_profile.png" width="200" height="350" />

 <br/>
 <br/>
 
 7 . Mentor will receive a notification , where he will be able to accept/ reject the request <br/> <br/>
       <img src="https://github.com/nandinicbit1981/UMentor/blob/master/notification_add_request.png" width="200" height="350" />

 <br/>
 <br/>
 
 8. List of Mentors can be selected using the mentor icon at the bottom navigation <br/>
       <img src="https://github.com/nandinicbit1981/UMentor/blob/master/mentors.png" width="200" height="350" />

 <br/>
 <br/>
 
 9. List of Messages can be seen using the messages icon at the bottom navigation <br />
    
       <img src="https://github.com/nandinicbit1981/UMentor/blob/master/messages.png" width="200" height="350" />

 <br/>
 <br/>
 
 10. On selecting the message icon at the above list, we can send messages to the mentor/mentee <br />
       <img src="https://github.com/nandinicbit1981/UMentor/blob/master/message.png" width="200" height="350" />

 <br/>
 <br/>
 
 11. Notifications are sent when request is sent, messages are received, or mentee rates a mentor.By clicking the notifications option at the end of the screen, we can see the list of notifications received.
          
        <img src="https://github.com/nandinicbit1981/UMentor/blob/master/notifications.png" width="200" height="350" />

 <br/>
 <br/>
 
 In addition to above, when we view a mentor's profile, we can rate the mentor and that will show up on his profile.
   <img src="https://github.com/nandinicbit1981/UMentor/blob/master/rating.png" width="200" height="350" />

 <br/>
 <br/>

 Version 2 : will have the user being able to post the rating to linkedin and boost up the profile
 Please contact me if you have any questions please send an email to "nandinicbit@gmail.com"
 
 
 
 
 
 
