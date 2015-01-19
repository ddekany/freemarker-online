freemarker-online
====================

freemarker-online is a tool for any freemarker users to evalaute their freemarker expressions with an input.
You can use it for enabling non developers to develop freemarker templates and evaluate it's values without the need for a developement enviornment.
For a deployed version of this tool you can visit http://freemarker-online.kenshoo.com/

Development Instuctions
------------------------
* Clone the repository to a local directory
* Run "./gradlew build" from the cloned directory
* If you want to run it using IDEA run "./gradlew cleanidea idea" - this will generate the IDEA project for you.
* For running the software from a command line just hit " java -jar build/libs/freemarker-online-0.1.undef.jar server  src/main/resources/freemarker-online.yml"


