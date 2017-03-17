# Virtual-Conference
This projects aims at building a video conferencing application which allows multiple participants to join the video conference
application. The application is built on a WebRTC framework. WebRTC allows peer to peer connection among the clients through the browser such that there is no need for downloading any additional software to enable the communication. The clients involved in the connection exchange session information in the form of offers and answers and then establish a WebRTC communication channel between them.
We have built a multi-party communication application using WebRTC where each client is connected to every other client
in the session and sends media to all the clients in the session.

## Implementation

The JAVA classes and their resposibilities are as follows.
Classes									 Responsibilities
MediaServer              JAVA class resposible for establishing peer to peer connections with participants 
                         through web sockets. It manages answers, offers and ICS candidate selection while 
                         establishing RTP connection between the peers.
Participant              JAVA class to maintain a HashMap of participant names and their session ids.
Room                     JAVA class to manage communications between all participants connected to a room
                         and who want to communicate with each other.
index.html               The UI for logging in and setting up a call with others connected to the application.
                         
## Installation

It is a MAVEN project and runs on TOMCAT 7 or above. Needs jdk 7 and jre to run the application.

## API Reference

https://webrtc.org/native-code/native-apis/


