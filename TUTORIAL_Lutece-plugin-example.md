
# 1. Introduction

This tutorial will show you how to develop a Lutèce plug-in step by step :

* Install requirements
* Use of Plugin-WIZARD site to initialize the project
* Build and execute this plugin "out of the box" as a webapp
* Update the display of the pages
* Update the data model
* Lauch this plugin in a Lutèce Site among others plugins
* Add a REST webservice
* Add a cache feature
* Add a collaborative feature (vote/comment)
* Finalyze this plugin (format, tests, package...)

To help you to code step by step this plugin, the Git repository contains branchs that correspond to each step of this tutorial.

Any questions ? You can consult the Lutèce Forum to help you, or post a question : https://fr.lutece.paris.fr/fr/suggest/forum-lutece.html

# 2. Requirements
You will need the following tools :

* JDK1.8
* Maven 3.x
* Ant
* MySQL 5.x or MariaDB 10.x
* Tomcat 8.x
* (Git)
* (NetBeans or Eclipse or any JAVA IDE)

We recommend you to use GIT version control system to save and compare your code :

* Create a working directory, e.g. : `(...)/lutece-dev`.
* Clone this project plugin-example in this directory (command : `git clone https://github.com/lutece-platform/lutece-dev-plugin-example.git`)
* Create a local branch based on Master for your work :

```
cd lutece-dev-plugin-example
git branch myWork master
git checkout myWork`
```


At each step of this tutorial, you will be able to compare your code and the proposed code corresponding at the new feature. For example, to see the additionnal code between two steps, run : `git diff step0 step1`

Use an "ignore" file in your git home repository (`$HOME/.config/git/`) to exclude some files from the versionning, as :

```
target/
```



#  3. Initialize the plugin code with Plugin-Wizard


In this step we will use the "Lutèce plugin Wizard" application to generate the basic files of the Lutèce plugin `example`.

Go to  `https://dev.lutece.paris.fr/tools/jsp/site/Portal.jsp?page=pluginwizard` , and start creating your plugin.
The "Lutèce plugin Wizard" looks like this :

![screenshot](/picture/plugin_wizard.gif)

Follow the proposed steps and fill the forms as :


* Description  :

| Field| Data|
|-----------------|-----------------|
| Name of the plugin :| example |
| Author :| Mairie de Paris|


* Add a business Class in "Business" (this is your data model) :

| Field| Data|
|-----------------|-----------------|
| Business Class :| Project|
| Table name :| example_project|


* Add some attributes at the business class :

| Field| Data|
|-----------------|-----------------|
| name | short text ( 50 chars), mandatory|
| description | medium text (255 chars), mandatory|
| image_url| URL, mandatory|


* Create an "admin feature", to manage your data in the backoffice in `Administration` menu :

| Field| Data|
|-----------------|-----------------|
| Title :| Manage project|
| Description :| Project Management|
| Technical name :| ManageProject|
| Right name :| PROJECT_MANAGEMENT|
| Right level :| level 0|
| Business class :| Project|


* Add an XPage to create a public page to present the datas in frontoffice in `XPage` menu :

| Field| Data|
|-----------------|-----------------|
| XPage name :| project|
| XPage Class :| ProjectApp|
| Business class :| Project|


Click NEXT until the "Plugin Generation" Page, check your datas and click the "generate the sources" button to download a ZIP archive containing your plugin `plugin-example`.

Once the ZIP archive downloaded, unzip the files in your working directory `lutece-dev-plugin-example`  (git branch : myWork).

> Warning : Do not create a "plugin-example"  directory !



# 4. Build the plugin, create the database  and run your application

  ## 4.1. Build the project  'plugin-example'

Verify in the  `pom.xml` file that the  `lutece-global-pom` version in the  'parent' tag is equals or superior than 4.0.2. (fix it if necessary).

In the  `.../lutece-dev/lutece-dev-plugin-example` directory, run the Maven cmd :

   `$ mvn clean lutece:exploded `

The plugin with all his Lutèce environnment is created as a standalone application.

> To find more information about the Lutèce Maven plugin, see  : http://lutece.paris.fr/support/jsp/site/Portal.jsp?page=wiki&view=page&page_name=home




## 4.2. Create the database

To create the database, we need to configure the path to the database server. This will be used to create the database, and then by the application to access the database.

To avoid loosing this configuration file at each plugin build, we will create a default configuration file outside of the 'target' directory.

Copy the generated file  :
  ``lutece-dev/lutece-dev-plugin-example/target/lutece/WEB-INF/conf/db.properties``.



In the local profile user directory ('/home/<username>/...' for linux or '%SystemDrive%\Users\<username>\(...)' for windows)
create a directory ``/home/<user>/lutece/conf/plugin-example/WEB-INF/conf/``. Paste the ``db.properties`` file into.

(You may also use the directory :  ``<lutece-dev>/lutece-dev-plugin-example/src/conf/default/WEB-INF/conf/``, but it is not recommended to commit your personnal configuration in the public project sources.)

Update this file  `db.properties`  :

  - Modify the portal.url porperty to update the database path with the name of your database (for example :  “lutece_test”)  :

  ```
  portal.url=jdbc:mysql://localhost/lutece_test?autoReconnect=true&useUnicode=yes&characterEncoding=utf8
  ```


  - Modify the credentials for  your personnal configuration :

  ```
  portal.user=root
  portal.password=pwd
  ```

Buil the plugin again (to generate the webapp with the updated configuration):

  `$ mvn clean lutece:exploded `

The generated sources contain now the default database access configuration file :

A Ant build configuration file `build.xml` has been created in the folder :
``...target/lutece/WEB-INF/sql/build.xml``.

Simply run the generated ant script to build the database :

`$ ant -f target/lutece/WEB-INF/sql/build.xml`


A new database “lutece_test” is created ! We have now a webapp and a database ready, we will now prepare the tomcat environnment to  launch the Lutèce site.


##  4.3. Tomcat configuration & launch the site

We will now run our webapplication in the tomcat application server.

In the `tomcat/conf/server.xml` configuration file of your Tomcat server, add those lines between the  `<HOST>` tags :

   `<Context docBase="(...)/lutece-dev/lutece-dev-plugin-example/target/lutece" path="/example" workDir="(...)/lutece-dev/lutece-dev-plugin-example/target/work" />`

Start Tomcat.


You can now access to your first Lutèce site with your brower by calling the URL :  http://localhost:8080/example/ (replace the tomcat port number if necessary)

You should see a page like this...

  ![image première page](/picture/first_page.gif)



> Tip : NetBeans users, see the wiki page  : http://lutece.paris.fr/support/wiki/netbeans.html


##  4.4. Activate your plugin

To execute your plugin, it is necessary to activate it, and to authorize the Lutèce users to access the BackOffice or FrontOffice pages.

Go to the Lutèce backoffice page :

 http://localhost:8080/example/jsp/admin/AdminLogin.jsp

... Use the default "admin" credentials
* login : admin
* mot de passe : adminadmin

Proceed to the change of the password and reconnect.

Select in the menu the option  : `System > Plugin management`.

The corresponding page displays :

![screenshot](/picture/plugin_management.gif)

The plugin `example`   should appear in the plugin list. Activate the plugin using the green "play" button" on the right.

## 4.5. Allow Lutèce users to use the plugin


In the  menu `Managers > Users Management`, click on the `update` button of the `admin` user.

Select the `Rights` tab, and check the checkboxes corresponding of the plugin example features. Click on `Apply rights list`.

![screenshot](/picture/user_rights.gif)

The plugin is now ready to use !

## 4.6. Tests

We built a webapplication that allow public and private management of "project" items.

In front office (public mode), the project list is displayed at this page :

http://localhost:8080/example/jsp/site/Portal.jsp?page=project

In back office (private mode / administration), here is the page to manage the project items :

http://localhost:8080/example/jsp/admin/plugins/example/ManageProjects.jsp?view=manageProjects


Congratulations, you just created your first Lutèce Plugin !

>  You can commit your work :
>
> `$ git add --all`
>
> `$ git commit -a -m"Initial commit"`
>
> and compare your work to the step0 git branch
>
> `$ git diff myWork step0`


To get more information about the plugin code structure, see the wiki page :
http://lutece.paris.fr/support/wiki/howto-create-plugin.html

... or the Lutèce architecture presentation page  :
http://lutece.paris.fr/support/wiki/architecture-overview.html

We will now modify and adapt the code of this plugin to add some features to our application.
After each modification you will have to rebuild your project.




# 5. Add features to the  plugin

## 5.1.  Step 1 : Modify the public project list display

In this part, we will add 8 projects and improve the project list public display.

**Note :**
* The HTML front office templates are stored in the directory : /webapp/WEB-INF/templates/skin
* The HTML back office templates are stored in the directory :  /webapp/WEB-INF/templates/admin

Here is the file tree of our  plugin :

![screenshot](/picture/plugin_folder_tree.gif)

The public URL to display the project list is  : http://localhost:8080/example/jsp/site/Portal.jsp?page=project

To add projects, you should have a page like this one :

![screenshot](/picture/project_management.gif)

Create 8 projects (with the images of your choice).

To improve the presentation, we will modify the page "manage_projets.html" and display the project list by blocs, to get a responsive behaviour, as the picture below :

![screenshot](/picture/project_display.gif)

You may use the Lutèce Freemarker macros. To get some help about the use of bootstrap and freemarker, see : http://lutece.paris.fr/support/wiki/bo-bootstrap.html

The freemarker macros are declared in the file  : `.../lutece-dev/lutece-dev-plugin-example/target/lutece/WEB-INF/templates/commons.html` (also present on github : https://github.com/lutece-platform/lutece-core/blob/develop/webapp/WEB-INF/templates/commons.html).

> To check your code, you can compare it to the git branch  `step1`.
> To compare the code between two steps, run :
>  `$ git diff step0 step1`.

## 5.2. Step 2 : Create a detail page for a project

We will now add a page details_project.html that will present the datas of a project (id, name, description, imageUrl) in front-office.

This page will be displayed when selecting a specific project at the URL :  http://localhost:8080/example/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1. (example with the project Id =1)

You should have a page like this one :

![screenshot](/picture/detail_project.gif)

**Note :**
* Manage the front office views and actions in the XPage java class   :  src/java/fr/paris/lutece/plugins/example/web/ProjectXPage
* Manage the back office views and actions in the JSPBean java class   :  src/java/fr/paris/lutece/plugins/example/web/ProjectJSPBean

You will need to add or modify labels or messages. To get some help about internationalization (i18N), see : http://lutece.paris.fr/support/wiki/i18n.html

Type the following command to control your code for this step :

 `$ git diff step1 step2`.

## 5.3. Step 3 : Add a "cost" attribute to a project item

In this part, we will add a cost attribute to projects.

This attribute has three constraints:

* The cost value must be a number between 5 and 25.
* The value must be a multiple of 5.

Modify the table example_project of the database, as well as all necessary files, including SQL files, DAOs, templates, unit tests ...

Lutèce wiki dedicated to validation rules: http://lutece.paris.fr/support/wiki/bean-validation.html

Type the following command to get the changes from the previous step:

 `$git diff origin/step2 origin/step3`

# 6. Adding existing features from other plugins

To allow the interaction of several plugins within a Lutece site, it is necessary to go through another mode of site construction.
Two modes are proposed: the "site" mode, or the "multi-project" mode.

## 6.1. Configuring the environment in "site" mode

### 6.1.1. Create a pom.xml for the lutèce site

We will use the online service "createSitePom" to automatically build the pom.xml file that will allow us to generate the site including a selection of plugins, accessible at this address:

  https://dev.lutece.paris.fr/tools/jsp/site/Portal.jsp?page=createsitepom

Enter the list of plugins below (which will be used for the rest of the project) :

```
plugin-example
library-lucene
plugin-mylutece
module-mylutece-database
plugin-mylutecetest
plugin-extend
module-extend-rating
module-extend-comment
plugin-rest
plugin-avatar
plugin-mydashboard

```

Generate the pom.xml. You need to get a file like this:
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    <parent>
        <artifactId>lutece-site-pom</artifactId>
        <groupId>fr.paris.lutece.tools</groupId>
        <version>2.0.4</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <groupId>fr.paris.lutece</groupId>
    <artifactId>site-mysite</artifactId>
    <packaging>lutece-site</packaging>
    <version>1.0.0-SNAPSHOT</version>
    <name>My site</name>

    <repositories>
        <repository>
            <id>luteceSnapshot</id>
            <name>luteceSnapshot</name>
            <url>http://dev.lutece.paris.fr/snapshot_repository</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>lutece</id>
            <name>luteceRepository</name>
            <url>http://dev.lutece.paris.fr/maven_repository</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>fr.paris.lutece</groupId>
            <artifactId>lutece-core</artifactId>
            <version>6.0.0-RC-04</version>
            <type>lutece-core</type>
        </dependency>
        <dependency>
            <groupId>fr.paris.lutece.plugins</groupId>
            <artifactId>plugin-example</artifactId>
            <version>Release not found</version>
            <type>lutece-plugin</type>
        </dependency>

        (...)


```
Copy this pom.xml file into the previously created `` lutece-dev`` directory (the parent directory of the ``lutece-dev-plugin-example`` directory).

Edit in this file the name of the artifact (** artifactId ** tag) and the name of the site (** name ** tag) as below:

```
<modelVersion>4.0.0</modelVersion>
<groupId>fr.paris.lutece</groupId>
<artifactId>lutece-test</artifactId>
<packaging>lutece-site</packaging>
<version>1.0.0-SNAPSHOT</version>
<name>lutece-test</name>
```

Also modify the ** version ** tag of the plugin-example with the current version number: 1.0.0-SNAPSHOT

```
...
<dependencies>
    <dependency>
        <groupId>fr.paris.lutece</groupId>
        <artifactId>lutece-core</artifactId>
        <version>5.0.1</version>
        <type>lutece-core</type>
    </dependency>
    <dependency>
      <groupId>fr.paris.lutece.plugins</groupId>
      <artifactId>plugin-example</artifactId>
      <version>1.0.0-SNAPSHOT</version>
      <type>lutece-plugin</type>
    </dependency>
</dependencies>
...

```

> Note: The lutece-core dependency is mandatory, it is the core of the Lutece CMS. This element manages the coordination of all the other plugins.

### 6.1.2. Register the project 'plugin-example' in your local maven repository

Build again your project "plugin-example", with the 'install' option.

 `$mvn clean lutece:exploded install`

> The INSTALL option is necessary for Maven to register your project in its local repository (and use this local version instead of other versions in public repositories).
> The CLEAN option assures you to build from scratch you project. It can be omited to speed up the build but this could be a source of trouble.
> You may add the `-DskipTests -Dmaven.test.skip=true` to omit the test phase, to speed up the build, but do not use those options permanently, this can affect the project global quality.

### 6.1.3. Site build :

Go to the `` lutece-dev`` parent folder and run the Maven command :

 `$mvn clean lutece:site-assembly`

This will generate a "target" folder containing a site deliverable: "lutece-test-1.0.0-SNAPSHOT" folder.

> for each modification of the plugin, you will need to build the the plugin (`mvn [clean] lutece:exploded` in the`lutece-dev-plugin-example` folder) and build the site (`mvn [clean] lutece:site-assembly` in the` lutece-dev` folder).

## 6.2. Using multi-project mode

Another alternative to build a lutèce site is to use the multi-project mode.

In this part we will realize a multi project integrating several plugins. This mode requires the prior download of Lutèce sources and its main plugins.

### 6.2.1. Complete download of Lutèce sources

The Lutece project was originally versioned with SVN before switching to Git and some plugins have not yet been migrated to Git.

To start, place yourself above the `lutece-dev` directory and download Lutèce sources from SVN with the following command:

 `$ svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev`

Once this command executed, it is necessary to download  the git scripts to clone automatically all the sources of Github.

In another directory run :

 `$ git clone https://github.com/lutece-platform/tools-git-scripts`

Then copy the contents of this  folder into the lutece-dev folder:

 `$ cp -r tools-git-scripts/* lutece-dev/` ( **note :**  unix command)

Then merge the two sources by typing in the lutece-dev folder:

 `$ ./lutece.sh config -k -d`

Give your name, your github login and password  (create an account if necessary on https://github.com)

Then type:

 `$ ./lutece.sh sync -k -t https`

 **Note :** If you type these commands into Git Bash on windows, a problem can occur with the curl that Git Bash uses by default (prompt for password is not displayed). To work around the problem, you can use the command: `winpty bash`

### 6.2.2. Configuring the "multi-project" mode

The `lutece-dev-plugin-example` folder is normally in our` lutece-dev` folder.
renam (or duplicate) this directory to `plugin-example`, to get the same for the directory and the artifact.

At the root of the `lutece-dev` folder is a pom.xml file. This is the main pom of the site previously used for the "site" mode.

For example, copy it to pom.xml.dependencies (to keep it).

Modify the pom.xml file, and paste in the content below, which allows you to declare the different modules that will be used.

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <parent>
        <artifactId>lutece-global-pom</artifactId>
        <groupId>fr.paris.lutece.tools</groupId>
        <version>4.0.3</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <groupId>fr.paris.lutece</groupId>
    <artifactId>lutece-multi-project-pom</artifactId>
    <name>Lutece Multi-project Project</name>
    <url>http://dev.lutece.paris.fr/</url>
    <packaging>pom</packaging>
    <version>1.0.0-SNAPSHOT</version>

    <repositories>
        <repository>
            <id>luteceSnapshot</id>
            <name>luteceSnapshot</name>
            <url>http://dev.lutece.paris.fr/snapshot_repository</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>false</enabled>
            </releases>
        </repository>
        <repository>
            <id>lutece</id>
            <name>luteceRepository</name>
            <url>http://dev.lutece.paris.fr/maven_repository</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>

    <profiles>    
      <profile>
          <id>multi-project</id>
          <modules>
              <module>lutece-core</module>
              <module>plugin-example</module>
              <module>plugins/search/library-lucene</module>
              <module>plugins/auth/plugin-mylutece</module>
              <module>plugins/extends/plugin-extend</module>
              <module>plugins/extends/module-extend-rating</module>
              <module>plugins/extends/module-extend-comment</module>
              <module>plugins/auth/module-mylutece-database</module>
              <module>plugins/myapps/plugin-avatar</module>
              <module>plugins/myapps/plugin-mydashboard</module>
              <module>plugins/auth/plugin-mylutecetest</module>
              <module>plugins/tech/plugin-rest</module>
          </modules>
      </profile>
    </profiles>

</project>
```

Compile the project with the following command:

 `$mvn lutece:clean lutece:exploded -P multi-project`

> This command builds both the plugins and the lutèce site.

## 6.3. Finalizing the site environment

The following steps are identical regardless of the mode used, site or multi-project.

### 6.3.1. Initializing a new database:

It is necessary to reconfigure a new database that will contain all the plugin tables used by the site.

As before, we will first create a file to store the default configuration of access to the database, which will be reused each time the site is built.

Copy the file:
``<lutece-dev>/target/lutece[-test-1.0.0]/WEB-INF/conf/db.properties``

Then create one of two default configuration deployment directories:
* ``.../<lutece-dev>/src/conf/default/WEB-INF/conf/`` (site-assembly mode)
* ``/home/<user>/lutece/conf/<artifact-id-du-projet>/WEB-INF/conf/``  (site-assembly and  multiproject mode)

Paste the db.properties file into it.

Then edit this db.properties file:

- Rename the database used by the site to "lutece_test2" (for example) by modifying the portal.url property as follows:
```
portal.url=jdbc:mysql://localhost/lutece_test2?autoReconnect=true&useUnicode=yes&characterEncoding=utf8
```

- Change the authentication settings also to your mysql server:
```
portal.user=root
portal.password=motdepasse
```

Restart a site build from the `` <lutece-dev> `` directory (as previously).

An Ant `build.xml` file has been generated in the directory:
``...target/lutece-test-1.0.0/WEB-INF/sql/build.xml``.

In the '' ... target / lutece-test-1.0.0 / WEB-INF / sql / 'directory, run Ant: `` ant -f build.xml`` to build the new database with the corresponding tables to all selected plugins.

A new database named "lutece_test2" is created on the local MySql server.

### 6.3.2. Configuring the deployment of the site on Tomcat:

To do this, in the conf folder of your Tomcat application, add the following line in the `<HOST>` tag of the server.xml file:

 `<Context docBase="[path]/lutece-dev/target/lutece-test-1.0.0-SNAPSHOT" path="/lutece-test" workDir="[path]/lutece-dev/target/work" />`

Start the Tomcat, then you can check that the site is accessible on the URL http://localhost:8080/lutece-test/ (if the tomcat uses port 8080).

The page corresponding to the following image is displayed:

![image première page](/picture/first_page.gif)

### 6.3.3. Activation of plugins

Log in to the backoffice:

http://localhost:8080/lutece-test/jsp/admin/AdminLogin.jsp

Activate the plugins on the back office in `System / Plugin Management '(as before).

Give the rights to the plugins to the user "admin".

## 6.4. Step 4 - Setting up a REST Web service

In this part we will use the lutece REST plugin.

We will search a dataset from our database and display them in JSON format.

Our technology choice is to use Jersey JAX-RS and the Jackson API.

You can follow this tutorial on setting up a Lutèce REST service:

  https://lutece.paris.fr/support/wiki/howto-rest.html

 **Exercise :**

Code three features in REST Web Service:

* List of projects in JSON format
* Details of the data of a project in JSON format
* Detail of the data of a project as well as the number of views of this project in JSON format

for example: the URL `http://localhost:8080/lutece-test/rest/example/projects` should return a file of type:

```
{"projects": [
    {
    "id": 1,
    "name": "projet12",
    "description": "détail projet",
    "cost": "10"
  },
    {
    "id": 2,
    "name": "projet2",
    "description": "détail projet",
    "cost": "20"
  },
    {
    "id": 3,
    "name": "projet3",
    "description": "détail projet",
    "cost": "30"
  }
]}
```

Type the following command to get the changes from the previous step:

 `$ git diff step3 step4`

## 6.5. Step 5: Cache Management

In this part we will save the response of the Web Service in the cache.

You can follow this tutorial : http://lutece.paris.fr/support/wiki/cache-management.html

 **Exercise :**

Add a caching of each project to each detail view of a project.

  ** Note: ** after following the instructions of the wiki, activate your back office cache in System / Cache Management.

Type the following command to get the changes from the previous step:

 `$ git diff origin/step4 origin/step5`
 
 ## 6.6. Step 6: Extend Plugin Integration (Collaboration)

Implement the extend plugin in the example plugin, following this tutorial:

 http://lutece.paris.fr/support/wiki/howto-extend.html

 **Note :** When modifying the template modify_project.html, do not insert the marker into the `<form>` tag because it will generate `<form>` tags

 **Exercise :**

* Add the number of views (Hit) in each project and display it in the project list without incrementing it.
* Add comments in the details page of each project.
* Add the vote for each project in the project details page and display it in the project list.

You must have a project page display similar to this:

![screenshot](/picture/project_display_2.gif)

For the display of each project, you must have a page similar to this:

![screenshot](/picture/detail_project_2.gif)

Type the following command to get the changes from the previous step :

 `$ git diff step5 step6`

 

# 7. Finalizing the plugin

## 7.1. Formatting sources

To add the headers and correctly format the plugin's source files, use the maven commands:

 `mvn license:format`

 `mvn formatter:format`

Build site maven (tests, checkstyle, PMD...) :

 `mvn site`

Generation of README.md file from the  site.xml file :

 `mvn xdoc2md`

(or)

 `mvn fr.paris.lutece.tools:xdoc2md-maven-plugin:readme`

## 7.2. Tests

To automatically test the plugin (unit test), you can use the following command:

 `mvn lutece:exploded antrun:run test`

## 7.3. Packaging

Generation of a plugin distribution:

 `mvn lutece:assembly`
