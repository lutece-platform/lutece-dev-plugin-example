
#Introduction

This tutorial aims at creating a plugin working on Lutece by means of plugin WIZARD which ensure easy project skeleton making.

You have to install the following environnement on your comuter :


 
* Maven 3.0.5
* Git
* SVN
* Ant
* MySQL
* Tomcat 7
* Tomcat admin (not necessary but usefull for projects deployement)


#Step 0 – Cloning from SVN and configuring with Git-Script

Lutece project was previously versionned with SVN but was migrated to Git. Whereas some plugins are still on SVN.

First, some sources must be downloaded from SVN repository with this command :

 `$svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev` 

After this command execution, git scripts must be recovered in order to clone automatically sources from Github.

In another folder type :

 `$git clone https://github.com/lutece-platform/tools-git-scripts` 

Then copy this folder in the lutece-dev folder :

 `$cp -r tools-git-scripts/* lutece-dev/` ( **note :** Unix command)

Next type this command in the lutece-dev folder so that the two ranges of sources is merged :

 `$./lutece.sh config –d` 

Inform your name, login and password for https://github.com

 **Note :** If this command is typed in Git Bash a problem can occured by the Git Bash default curl. In this case password prompt line doesn't appear.

After type :

 `$./lutece.sh sync -t https` 

#Step 1 - Plugin WIZARD 4.1.1 deployement

In this step we download from GitHub a "site" project containing plugin Wizard 4.1.1 release then we execute it in order to start our own plugin named “Participatory Budget”.

##1) Site POM retrieval on GitHub :

To retrive the project type this command :

 `$git clone https://github.com/lutece-platform/lutece-dev-TP.git MyNamePlugin-TP` 

This command creates a MyNamePlugin-TP folder wihch contains a pom.xml file. One can see in this file the following dependencies :


```
<dependencies>
    <dependency>
        <groupId>fr.paris.lutece</groupId>
        <artifactId>lutece-core</artifactId>
        <version>5.0.1</version>
        <type>lutece-core</type>
    </dependency>
    <dependency>
        <groupId>fr.paris.lutece.plugins</groupId>
        <artifactId>plugin-pluginwizard</artifactId>
        <version>4.1.1</version>
        <type>lutece-plugin</type>
    </dependency>
</dependencies>
```


We have in particular :


 
* lutece-core : It corresponds to the core of CMS Lutece. This element manage, among other things, plugins interactions.
* plugin-pluginwizard : This plugin eases creation of a new lutece plugin by creating the skeleton of a JEE application working on lutece.


##2) Project construction :

Execute this Maven command in MyNamePlugin-TP folder :

 `$mvn lutece:site-assembly` 

It generate a “target” folder which contains a runnable site release ("lutece-TP-1.0.0-SNAPSHOT" folder).

##3) Databse initialisation :

Before deploying our site on tomcat, we need to configure and initialize its database (MySql).


 
* 
In the “lutece-TP-1.0.0-SNAPSHOT” folder, go to /WEB-INF/conf/ and edit the db.properties file. Rename database used for site in “lutece_pwizard” by modifying portal.url property :
 `portal.url=jdbc:mysql://localhost/lutece_pwizard?autoReconnect=true&useUnicode=yes&characterEncoding=utf8` 
* 
Also modify portal.password property with password of your mysql server :
 `portal.user=root`  `portal.password=motdepasse` 
* 
Execute Ant script for database initialization :
 `ant [path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT/WEB-INF/sql/build.xml` 


A new database named “lutece_pwizard” is now created on the local MySql server.

##4) Site deployment on Tomcat :

Now we can deploy the site on Tomcat.

On this purpose, in the conf folder of your Tomcat application, add the next line in tag `<HOST>` of server.xml file :

 `<Context docBase="[path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT" path="/wizard" workDir="[path]/MyNamePlugin-TP/target/work" />` 

Then you can check if application is well launched at http://localhost:8080/wizard/ (if wizard is the context chosen for the application).

The page corresponding to picture/step1/first_page.gif is displayed.

##5) Plugin activation :

In order to use the plugin Wizard, you need to activate it and assign corresponding rights to current user.

Go to site back office at http://localhost:8080/wizard/jsp/admin/AdminLogin.jsp and authenticate yourself :


 
* login : admin
* password : adminadmin


On page displayed, click on System > Plugins management.

Page corresponding to picture/step1/plugin_management.gif is displayed.

Activate pluginwizard with click on the green button.

Then go to Managers > Users management. On form displayed select Modify button in admin user.

On page displayed, go to “Rights” tab as seen in picture/step1/user_rights.gif then check boxes corresponding to plugin wizard's features.

Plugin Wizard is now ready to be used by the admin user.

#Step 2 - Plugin participatory budget creation

In this step we use plugin Wizard in order to generate plugin participatory budget sources. On that purpose, we use the deployed site front office.

Go to http://localhost:8080/wizard/jsp/site/Portal.jsp?page=pluginwizard. Screen corresponding to picture/step2/plugin_wizard.gif is displayed.

Follow form steps with this informations :


 
* Describe :

| Heading| Value|
|-----------------|-----------------|
| Plugin name :| bp|
| Author :| Mairie de Paris|


* Add a business class in Business :

| Heading| Value|
|-----------------|-----------------|
| Business Classe :| Project|
| Table name :| bp_project|


* Add these attributes to Project classe :

| Name| Type|
|-----------------|-----------------|
| Name| texte court( 50 caractères), mandatory.|
| Description| texte moyen 255 caractères, mandatory.|
| image_url| URL, mandatory.|


* Create an adminfeature in Features:

| Heading| Value|
|-----------------|-----------------|
| Feature title :| Manage project|
| Feature description :| Project Management|
| Technical name :| ManageBp|
| Feature Management :| BP_MANAGEMENT|
| Feature level :| Level 0|
| Business Classes :| Project|


* Add an XPage :

| Heading| Value|
|-----------------|-----------------|
| Name of the application :| project|
| The name of the application :| BpApp|
| Business Classes :| Project|




When you are in “Génération” step, check plugin summary then click on “Generate the zip”.

A zip file containing the participatory budget plugin is downloaded.

#Step 3 - Participatory budget plugin deployment

In this step we deploy participatory budget plugin previously generated.

First extract zip file from the preceeding step, and go to the extract folder (where a pom.xml file is located).

 **Note :** 


 
* You have to change in pom.xml file the release number of parent project (tag `<version>` of `<parent>` : change 3.0 in 3.0.3)
* You have to move the extract folder in the lutece-dev folder (where sources from SVN and Git are located). We use pom.xml file for multi project implementation.


To build the project, execute the following Maven command :

 `$mvn lutece:exploded` 

This commande generate a “target” folder which contain the participatory budget plugin application release.

A database must be initialized as for the Wizard plugin site. Follow instructions described in “Database initialization” subsection of step 1.

 **Note :** the database name must be specific for each application in order not to overwrite former database. We name “lutece_bp” the databse for participatory budget plugin.

You can now deploy the application on Tomcat (see “Site deployment on Tomcat” subsection of step 1).

Activate the participatory budget plugin and assign rights corresponding to this plugin to admin user (see “Plugin activation” subsection of step 1).

A Lutece plugin is divided in two parts: front office and back office.

Front office is available at http://localhost:8080/bp/jsp/site/Portal.jsp. You can find a picture of this interface in picture/step3/front_office.gif.

And back office at http://localhost:8080/bp/jsp/admin/AdminMenu.jsp. You can find a picture of this interface in picture/step3/back_office.gif.

#Étape 4 - Front office cleaning + Bootstrap

In this step we add 8 projects in participatory budget plugin, improve front office interface and create an HTML page to display project details (id, name and description).

 **Note :** 


 
* The plugin-bp/webapp/WEB-INF/templates/admin folder contains HTML templates for back office.
* The plugin-bp/webapp/WEB-INF/templates/skin folder contains HTML templates for front office.


You can find a picture of plugin folder tree in picture/step4/plugin_folder_tree.gif.

In plugin-bp/webapp/WEB-INF/templates/skin/plugins/bp, delete create_project.html and modify_project.html pages.

For project management, you must have a page like picture/step4/project_management.gif. This page is located at http://localhost:8080/bp/jsp/admin/plugins/bp/ManageProjects.jsp?view=manageProjects.

To improve display we add Bootstrap code in manage_projets.html in order to change dynamically the page design when window is reduced.

For project display, you must have a page like picture/step4/project_display.gif. This page is located at http://localhost:8080/bp/jsp/site/Portal.jsp?page=project.

##details_project.html page creation :

We add a details_project.html page which contains project informations (id, name, description and imageUrl) in front office.

This page is displayed when a user click on project.

You must have a page like picture/step4/detail_project.gif. This page is located at http://localhost:8080/bp/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1.

Type the following command to obtain differences between final result and preceeding step :

 `$git diff step2 step3` 

#Step 5 - Add a cost attributes to projects

In this step we add a cost attribute in table bp_projet of the database.

This attribute has three constraints :


 
* Cost value must be between 5 and 25.
* Cost value must be a multiple of 5.
* Cost value can't be null.


Don't forget to modify all necessary files.

Type the following command to obtain differences between final result and preceeding step :

 `$git diff step3 step4` 

#Step 6 - Extend plugin integration (HIT, RATING, COMMENT)

In this step we create a multi project which integrate several plugins.

If you correctly follow the guide, you will have a project folder as in picture/step6/project-folder.gif.

The plugin-bp-TP folder (or the other name indicate when you created the plugin in Wizard plugin) is the initial skeleton of our plugin. You can put this folder in /plugins or in a subfolder of lutece-dev folder. A pom.xml file is located in root of lutece-dev folder. It's the main pom of our multi projet. Open this file and add the following dependencies :


```
<profile>
    <id>multi-project</id>
    <modules>
        <module>lutece-core</module>
        <module>plugin-bp-TP</module>
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
```


Compile the project with the following command and deploy it on Tomcat :

 `$mvn lutece:exploded -P multi-project` 

Activate plugins on back office in System/Plugins management as in picture/step6/plugin_management_2.gif.

Implement the extend plugin in the participatory budget plugin (You can follow this tutorial : http://fr.lutece.paris.fr/fr/wiki/howto-extend.html).

 **Note :** When you modify the modify_project.html template, don't insert marker in tag `<form>` because it also generate tag `<form>` 

 **Exercice :** 


 
* Add the Hit extension in all projects and display it in the projects list without increment it.
* Add the comment extension in project details page of all projects.
* Add the rating extension in project details page of all projects and display it in the projects list.


For projects display, you must have a page like picture/step6/project_display_2.gif.

For any project display, you must have a page like picture/step6/detail_project_2.gif.

Type the following command to obtain differences between final result and preceeding step :

 `$git diff step4 step5` 

#Step 7 - Implementation of a RESTful web services

In this step we use the REST Lutece plugin.

The goal is to provide a set of data from our database in a JSON format.

For that purpose, we add the REST plugin in the multiproject.

To support JSON, we chose Jersey JAX-RS and Jackson API.

You can follow this tutorial : http://fr.lutece.paris.fr/fr/wiki/howto-rest.html

 **Exercise :** 

Create 2 features for the RESTful web services :


 
* First feature displays some project informations : total number of view, total number of votes, total number of published and unpublished comments.
* Second feature displays the total number of view, the total number of published and unpublished comments, the total number of votes for all projects.


You must have a page like picture/step7/rest_request.gif.

Type the following command to obtain differences between final result and preceeding step :

 `$git diff step5 step6` 

#Étape 8 - Cache management

In this step we save our Web Service responses in a Web cache.

You can follow this tutorial : http://fr.lutece.paris.fr/fr/wiki/cache-management.html

 **Note :** after finishing the tutorial, you must activate the Web cache, so in back office go to System/Caches management.

Type the following command to obtain differences between final result and preceeding step :

 `$git diff step6 step7` 

#Final project retrieve

You can return the final project with these instructions :

Follow Step 0, then type :

 `$git checkout step7` 

Finally deploy project like in step 3.


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/lutece-TP/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*