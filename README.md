# 1 About Paint

This is a touch-based paint application for personal computers developed with Java. 
It is possible to alter and create image and pdf files. This program is used for writing down lecturenotes at university.

The user is able to select different pens for being able to differ between diverse types of text. It is possible to focus the painted elements, to change their size and to remove them. The image can be exported into the most popular formats like PNG, JPEG, PDF, GIF and lots more. It is possible to change the type of the painted text afterwards. Copy and Paste functionality works cutting across programs.
Just added the possibility to load and alter PDF documents and save certain pages as image files. The implementation of the feature has not finished yet, thus usability-related issues and certain tasks have not been implemented.

It is possible to execute the program right away by either double-clicking at the file paint.jar or by executing
```
java -jar paint.jar
```
having a java runtime environment installed. For being able to adapt the code and to contribute to the project, the working environment has to be set up:



# 2 Working environment


The maven project is developed using Eclipse. It is possible to use a different editor which supports maven projects.
Both the used Checkstyle configuration and the dictionary are  pushed into repository's root folder.

Used 
- Eclipse
- Maven
- Checkstyle
- JUnit
- Apache Open Box 


## 2.1 Install Git, Maven, Svn, Eclipse, Eclipse Plugins (Maven, Checkstyle, JUnit), setup of maven dependencies
Git Page
- https://git-scm.com/
- Installation of git, maven, svn
```
sudo apt-get install git
sudo apt-get install maven
sudo apt-get install svn
```
Eclipse Download Page:
- http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr

Add the following update pages to Eclipse:
- Maven:      http://download.eclipse.org/technology/m2e/releases/
- Checkstyle: http://eclipse-cs.sourceforge.net/update

Install Maven, Checkstyle plugin.

Maven dependencies:
Clone apache box project using svn
```
svn checkout http://svn.apache.org/repos/asf/pdfbox/trunk/
```
Import the projects into Eclipse and execute them.


### 2.1.1 Use recommended Checkstyle configuration file
Due to an error it is necessary to install libwebkitgtk-1.0-0 for being able to load the config file.

```
sudo apt-get install libwebkitgtk-1.0-0
```


# 3 Operating Systems

Tested for
* Elementary OS 
* Ubuntu (12.04 LTS)
* Windows 8, 8.1, 10
* Mac OS X Yosemite (10.10.4)

Minor display errors occured using Windows.
