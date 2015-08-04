paint
=====

This is a touch-based paint application for personal computers designed in Java. 
Its purpose is to write down notes (e. g. at university). 

The user is able to select different pens for being able to differ between diverse types of text. It is possible to focus the painted elements, to change their size and to remove them. The image can be exported into the most popular formats like PNG, JPEG, PDF, GIF and lots more. It is possible to change the type of the painted text afterwards. Copy and Paste functionality works cutting across programs.


Working environment
=====

The project is a maven project developed in eclipse. Both the used Checkstyle configuration and the dictionary are  pushed into repository's root folder.

Used 
- Eclipse
- Maven
- Checkstyle
- JUnit

Install Maven, Checkstyle, JUnit for Eclipse
==

Add the following update pages to Eclipse:
- Maven:      http://download.eclipse.org/technology/m2e/releases/
- Checkstyle: http://eclipse-cs.sourceforge.net/update

Install Maven, Checkstyle plugin.


Use recommended checkstyle configuration file
====

- due to an error it is necessary to install libwebkitgtk-1.0-0 for being able to load the config file

```
sudo apt-get install libwebkitgtk-1.0-0
```


Operating Systems
====
Tested for
* Elementary OS 
* Ubuntu (12.04 LTS)
* Windows 8, 8.1, 10
* Mac OS X Yosemite (10.10.4)

Minor display errors for windows usage.
