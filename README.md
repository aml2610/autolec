# Autolec

This is an **Android** app developed during [Warwick Hack 2016](http://www.hack.warwick.tech/) by:
* Alexandru Blinda
* Alexandru Rosu
* Tudor Suruceanu
* Andrei-Marius Longhin

We are **Project ATAA**, a team of enthusiastic students at [University of Birmingham](http://www.birmingham.ac.uk) who love coding. This app brought us two prizes at **WarwickHack2016**: 
* **1st place** for the "*Build a hack that helps students, using NFC tags or QR Codes*" challenge by sponsor **Black Pepper Software**
* **3rd place** overall for the whole hackathon, awarded by **Major League Hacking**

## What it does

This application is targeted to students and **automates** tedious operations. **Before a lecture**, it:

* signs you up as attenting and sends the time you arrived to class to [the database](#database)
* gives a link for your lecture notes for the day, **IF** they are available
* enables wifi
* disables sounds
* sets the brightness on low

**After** lectures, it:

* sends the time you got out to [the database](#database)
* takes you to an online survey where you submit feedback, **IF** one is available
* disables wifi
* enables sounds
* sets med-high brightness

(The last 3 features of each list above can be configured by the student.)

## How it works
An NFC tag is placed at the entrance of each lecture room, seminar room etc. Students hold their phone near the NFC tag for validation when they enter the room and once again when they exit. 
The app then sends data (the time students enter and exit the room) and retrieves data (link to lecture notes, link to survey) from a remotely-stored database.
Furthermore, the phone's "environment" is adjusted as configured by the student previously via the control panel provided by the app.

## How to run it 
WILL BE UPDATED SOON

## <a name="database">The database</a>
It contains a list of the students enrolled to the courses, their student id and the time they last got in/out of a lecture. It also contains the list of the courses, among with their lecturers and the last lecture notes and surveys.

 For now, the database is hardcoded as a test one hosted by Amazon Web Services, but in a future release it may be configurable.
 
## The future ##
* select database
* show in/out times for particular courses
* enroll students for particular courses
* make changing the student id only possible when installing the app 
* validating actions via a face recognition system
