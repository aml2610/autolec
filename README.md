# Autolec

This is an **Android** app developed during WarwickHack2016 by:
* Alexandru Blinda
* Alexandru Rosu
* Tudor Suruceanu
* Andrei-Marius Longhin, all of them members of **Project ATAA** team

 This app brought ** Project ATAA ** two prizes at ** WarwickHack2016**: 
* **1st place** for the "Build a hack that helps students, using NFC tags or QR Codes" challenge by sponsor** Black Pepper Software**
* **3rd place** overall for the whole hackathon, awarded by **Major League Hacking **

## What it does

**Automates** operations **BEFORE** (when you place your phone against TAG when entering):

* enables wifi
* disables sounds
* sets low brightness
* the 3 above are configurable by students via a Control panel

* signs you up as present and sends the time you arrived to class to the database
* takes you to a link for your lecture notes for the day, **IF** they are available
* the 2 above are **NOT** configurable and will happen everytime

... and **AFTER** lectures (when you place your phone against the TAG one more time, when exiting):

* sends the time you got out to the database
* disables wifi
* enables sounds
* sets med-high brightness
* takes you to an online survey where you submit feedback, **IF** one is available
* again, they are configurable or not in a similar fashion to the **BEFORE** actions

## How it works
One NFC tag is placed at the entrance of each lecture room, seminar room etc. Students hold their phone near the NFC tag for validation when they enter the room and once again when they exit. 
The app then sends data (time student entered and exited the room) and retrieves data (link to lecture notes, link to survey) from a remotely-stored database.
Furthermore, the phone's "environment" is adjusted as configured by the student previously, via the Control panel provided by the app.

## How to run it 
WILL BE UPDATED SOON



