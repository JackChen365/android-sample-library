### 1.0.0
*Date:* 2020/2/1<br>
*Author:* Jack chen

This is the first version of this project.

1. Component
2. Function
3. ActionProcessor

all done


### 1.0.1
*Date:* 2020/2/2<br>
*Author:* Jack chen
Fixed a few bugs

1. MessageComponent remove System.err pip line stream
2. ViewPager default page limit in CompanionComponent

### 1.0.2
*Date:* 2020/2/6<br>
*Author:* Jack chen
Fixed a few bugs

1. MessageComponent add two action buttons.
    The first one is auto scroll to bottom.
    The second one is clean all the message


### 1.0.3
*Date:* 2020/2/16<br>
*Author:* Jack chen

* Fixed SampleSystemConsole's PipedInputStream write end dead
* You could check this [how-can-i-fix-my-write-end-dead-and-read-end-dead-errors-for-objectinputstre](https://stackoverflow.com/questions/43640846/how-can-i-fix-my-write-end-dead-and-read-end-dead-errors-for-objectinputstre)

### 1.0.4
*Date:* 2020/2/20<br>
*Author:* Jack chen

* Fixed SampleSystemConsole's how to read input stream data

### 1.0.5
*Date:* 2020/3/28<br>
*Author:* Jack chen

* Fixed DataProvider
* Fixed MarkdownView and SourceCodeView load failed
* SourceCode file list support regex to filter the files.

### 1.0.6
*Date:* 2020/3/29<br>
*Author:* Jack chen

* SourceCodeView Add retry mechanism.
* SourceCodeView wrap by NestedScrollView.



## All the changes from now was on Jitpack.

### 1.0.8
*Date:* 2020/5/27<br>
*Author:* Jack chen

* Fixed the SystemConsoleService.
* Turn the AnnotationProcessor to a Gradle Plugin
* Packing all the source codes to the assets.
* Packing all the documents to the assets.
* Add a sample hierarchy page.

### 1.0.9
*Date:* 2020/5/28<br>
*Author:* Jack chen

* Fixed the problem when the annotation is default value causes the crash in the Plugin.

### 1.1.0
*Date:* 2020/6/27<br>
*Author:* Jack chen

* Fixed the problem in different build tool version we can not find the merged manifest file.

### 1.1.0~1.1.5
*Date:* 2020/6/27<br>
*Author:* Jack chen

* Add category generator in order to generate the category by the class package.
* Update the annotation: Register and Category that allow you only configure the title without the description.

### 1.1.6~1.2.0
*Date:* 2020/7/29<br>
*Author:* Jack chen

* Add category generator in order to generate the category by the class package.
* Update the annotation: Register and Category that allow you only configure the title without the description.

### 1.2.1

*Date:* 2020/7/30<br>
*Author:* Jack chen

* Fixed the crash when open the hierarchy Activity.

### 1.2.8

*Date:* 2020/9/23<br>
*Author:* Jack chen

* Fixed the crash when activity change the orientation configuration.
* Support landscape message output component.

### 1.2.9

*Date:* 2020/9/24<br>
*Author:* Jack chen

* Synchronize the code with the support library.

### 1.3.0

*Date:* 2020/9/24<br>
*Author:* Jack chen

* Fixed the crash when activity change the orientation configuration in some of the specific models that did not call onCreate.