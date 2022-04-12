Solar Optimization Simulator
Created by Mark Capka, Sean Zahller, Anthony Hirt, and Josh Canton
Seperate Project Repository Individually created/changed by Sean Zahller
EWU Senior Project 2022 Winter Quarter

Program is meant to show the user details of how sunlight will affect a house at a certain location and time.
Returns details on sunlight intensity, sunrise/sunset times, and solar panel information.
Able to manipulate position of objects and manipulate time of day between sunrise and sunset.

Original Setup:
JavaFX, Maven, IntelliJ IDE.

Before Installation:
1)Make sure all of the following are updated to the newest versions:
	-JDK and SDK for JavaFX
	-IntelliJ
2)Know where your workspace folder for intelliJ is located

To install and run:
1)Download zip and extract into IntelliJ workspace folder
2)Open IntelliJ, choose "Open project", Locate and select extracted folder as project root, Press OK
3)Load maven project, should be a pop asking to load maven project select yes
3)Go to "File -> Project Structure -> Modules -> Depencies"
4)Click the "+" and locate sun1.jar in the Resources folder of the project, Press OK
5)Repeat step 4 for jim3dsModelImporterJFX.jar
6)Press Apply, And then press OK
7)Copy all resource files besides the jar files(All 3ds files, and PNG/JPEG), Paste them to your C: folder
OR
7)CTRL+F, search for C:, change code of file location to its path on your system within the resources folder
8)Edit run configurations -> New Application
9)Change the name to SkyBoxApplication, Change JAVA SDK to atleast 17, Choose main in project: SkyBoxApplication.java
10)Click Apply, Click OK.

Troubleshooting Steps:
With step 9)Choose working directory: Select project folder "Solar Optimization"
With step 3)"File -> Project Structure -> Modules -> Sources". If source, resources, and target files not specified:
src/main/java = source folder
src/main/resources = resources folder
target = target folder

Program should now run, If not restart and look for load maven project tip, and redo any steps necessary.

UPDATE/To Work On:
*SkyBox needs better folding
*Error checking on sunrise/sunset times
*Refactor into neater code/seperate classes
*Change military time to digital time
*UI Tweaks: Button spacing, fonts, size, etc.
*Moving roof solar panels
*Individually add/remove ground or roof solar panel
