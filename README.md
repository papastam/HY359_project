# Project Summary

For the project of the HY359 course we created a doctor-user website. In this website a user can create an account, find a doctor and make an appointment. There is also
messaging functionality. We used java servlets to implement the websites functionality.

Webapp guide can be found in the [Project Webapp indes](#Project-Webapp)

The full assignment can be found in the [assignment PDF](https://github.com/papastam/HY359_project/blob/master/%CE%97%CE%A5-359_Project_2021_Personalized%20Health_V1.0.pdf) 

The project's report can be found in the `./report` directory

# Requirements
* It is recommended that you run the project as a IntelliJ Project!
* The web app requires a database, xampp is recommended.
* For running the project, [JDK](https://www.oracle.com/ie/java/technologies/javase/javase8-archive-downloads.html) is required *(recommended version: jdk1.8.0_241)*
* [Tomcat](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.58/bin/apache-tomcat-9.0.58.zip) is also required to run the project *(included in xampp)*

# Set up
<h3>Initial Setup</h3>
* The database can be initialized by running: <br>`src/main/java/Database_HY359/src/database/init/InitDatabase.java`
* You can optionally add dummy users to the database by running: <br> `src/main/java/Database_HY359/src/database/init/AddDummyUsers.java`
<br>
<h3>InteliJ Setup</h3>

+ Add a compile configuration from the rop right menu by clicking the `Add Configuration...`
 button.
+ Select `Tomcat Server -> Remote` from the configurations dropdown menu.
+ Click on `Configure` next to `Application server` selection, and select the tomcat installation directory in the `Tomcat Home` selection.
+ Apply the configuration and run the project!

# Project Webapp
The webapp is divided in three web pages (and an admin one):

+ Main webpage

![Main Webpage Screenshot](https://github.com/papastam/HY359_project/blob/master/report/documents/Screenshots/front%20page.png?raw=true)
+ User's webpage *(Login as a user)*

![Users Webpage Screenshot](https://github.com/papastam/HY359_project/blob/master/report/documents/Screenshots/user%20page.png?raw=true)
+ Doctor's webpage *(Login as a user)*

![Main Webpage Screenshot](https://github.com/papastam/HY359_project/blob/master/report/documents/Screenshots/doctor%20page.png?raw=true)
+ Admin's webpage *(Login as admin)*
  
![Main Webpage Screenshot](https://github.com/papastam/HY359_project/blob/master/report/documents/Screenshots/admin%20page.png?raw=true)


# Co owners
[Dimitris Bisias](https://github.com/dbisias)
