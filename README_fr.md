
#Introduction

Le but de ce TP est de réaliser un plugin fonctionnant sous Lutece, en utilisant le plugin WIZARD permettant de créer facilement le squelette de notre projet.

Pour ce TP il est nécessaire de disposer de l’environnement suivant :


 
* Maven 3.0.5
* Git
* SVN
* Ant
* MySQL
* Tomcat 7
* Tomcat admin (optionnel mais très utile pour déployer les projets)


#Étape 0 – Clone depuis SVN et Configuration Git-Script

Le projet Lutece a été initialement versionné avec SVN avant de passer sur Git et certains plugins n'ont pas encore été migré vers Git.

Pour commencer, il faut récupérer les sources depuis SVN avec la commande suivante :

 `$svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev` 

Une fois cette commande exécutée, il faut récupérer les scripts git permettant de cloner automatiquement toutes les sources de Github.

Dans un autre répertoire faites :

 `$git clone https://github.com/lutece-platform/tools-git-scripts` 

Ensuite copiez le contenu de ce dossier scripts dans le dossier lutece-dev :

 `$cp -r tools-git-scripts/* lutece-dev/` ( **note :** commande unix)

Puis fusionnez les deux sources en tapant dans le dossier lutece-dev :

 `$./lutece.sh config –d` 

Donner votre nom, votre login et mot de passe (https://github.com)

 **Note :** si vous tapez ces commandes dans Git Bash un problème peut survenir avec le curl que Git Bash utilise par défaut. Le prompt demandant le mot de passe ne s'affiche pas.

Ensuite tapez :

 `$./lutece.sh sync -t https` 

#Étape 1 - Déploiement du plugin WIZARD 4.1.1

Dans cette étape nous allons récupérer sur GitHub un projet “site” contenant le plugin Wizard version 4.1.1 puis nous allons l'exécuter pour initier notre plugin “Budget Participatif”.

##1) Récupération du POM de site sur GitHub :

Pour récupérer le projet entrez la commande suivante :

 `$git clone https://github.com/lutece-platform/lutece-dev-TP.git MyNamePlugin-TP` 

Cette commande créé le dossier MyNamePlugin-TP contenant un fichier pom.xml. Celui-ci fait apparaitre les dépendances suivantes :


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


On a notamment :


 
* lutece-core : C'est le coeur du CMS Lutece. Cet élément gère entre autre la coordination de tous les autres plugins
* plugin-pluginwizard : C'est un plugin qui a pour rôle de faciliter la création d'un nouveau plugin lutece en générant le squelette d’une application JEE compatible lutece.


##2) Construction du projet :

Positionnez-vous dans le dossier MyNamePlugin-TP et exécutez la commande Maven :

 `$mvn lutece:site-assembly` 

Ceci va générer un dossier “target” contenant un livrable du site (dossier "lutece-TP-1.0.0-SNAPSHOT").

##3) Initialisation de la base de données :

Avant de déployer notre version du site sur tomcat, il faut configurer et initialiser la base de données (MySql) qui sera utilisée par celui-ci.


 
* Dans le dossier du site “lutece-TP-1.0.0-SNAPSHOT”, allez dans /WEB-INF/conf/ et éditez le fichier db.properties. Renommez la base de données utilisée par le site en “lutece_pwizard” en modifiant la propriété portal.url comme suit : `portal.url=jdbc:mysql://localhost/ **lutece_pwizard** ?autoReconnect=true&useUnicode=yes&characterEncoding=utf8` 
* Modifiez également la propriété portal.password par le mot de passe root de votre serveur mysql : `portal.user=root`  `portal.password=motdepasse` 
* Exécutez le script Ant d’initialisation de la base de données : `ant -f [path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT/WEB-INF/sql/build.xml` 


Une nouvelle base de données nommé “lutece_pwizard” est alors créée sur le serveur MySql local.

##4) Déploiement du site sur Tomcat :

Nous pouvons à présent déployer notre site sur Tomcat.

Pour cela, dans le dossier conf de votre application Tomcat, ajoutez la ligne suivante dans la balise `<HOST>` du fichier server.xml :

 `<Context docBase="[path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT" path="/wizard" workDir="[path]/MyNamePlugin-TP/target/work" />` 

Vous pouvez ensuite vérifier que l’application est bien lancée sur l’Url http://localhost:8080/wizard/ (si wizard est le chemin de contexte choisi pour l’application).

La page correspond à l'image picture/step1/first_page.gif s’affiche.

##5) Activation du plugin :

Pour pouvoir utiliser le plugin Wizard, il est d’abord nécessaire de l’activer et d’affecter les droits à l’utilisateur.

Pour cela il faut aller dans la partie back office du site à l’adresse http://localhost:8080/wizard/jsp/admin/AdminLogin.jsp et s'authentifier en :


 
* login : admin
* mot de passe : adminadmin


Sur la page qui s’affiche, cliquez sur Système > Gestion des Plugin.

La page correspondant picture/step1/plugin_management.gif s’affiche.

Activez le pluginwizard en cliquant sur le bouton vert.

Allez ensuite sur Gestionnaires > Gestion des utilisateurs. Sur l’écran qui s’affiche sélectionnez le bouton Modifier du user admin.

Sur la page qui s’affiche, allez sur l’onglet “Droits” comme dans l'image picture/step1/user_rights.gif puis cochez les cases correspondant aux fonctionnalités du plugin wizard.

Le plugin Wizard est maintenant prêt à être utilisé par le compte admin.

#Étape 2 - Création du plugin budget participatif

Nous allons dans cette étape utiliser le plugin Wizard pour générer les sources du plugin Budget Participatif. Pour cela, nous utiliserons la partie front office du site précédemment déployé.

Allez à l’adresse http://localhost:8080/wizard/jsp/site/Portal.jsp?page=pluginwizard. La fenêtre correspondant à picture/step2/plugin_wizard.gif s’affiche.

Suivez les étapes du formulaire en renseignant les informations suivantes :


 
* Description du plugin :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom du plugin :| bp|
| Auteur :| Mairie de Paris|


* Ajoutez une classe métier dans Métier :

| Rubrique| Valeur|
|-----------------|-----------------|
| Classe métier :| Project|
| Nom de la table :| bp_project|


* Ajoutez les attributs suivants à la classe Project :

| Nom| Type|
|-----------------|-----------------|
| Name| texte court( 50 caractères), obligatoire.|
| Description| texte moyen 255 caractères, obligatoire.|
| image_url| URL, obligatoire.|


* Créez une fonctionnalité d’administration (adminfeature) dans Administration :

| Rubrique| Valeur|
|-----------------|-----------------|
| Titre :| Manage project|
| Description :| Project Management|
| Nom technique :| ManageBp|
| Droit :| BP_MANAGEMENT|
| Niveau droit :| Niveau 0|
| Classe métier :| Project|


* Ajoutez une XPage dans XPage :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom de la XPage :| project|
| Classe de XPage :| BpApp|
| Classe métier :| Project|




Une fois à l’étape “Génération”, vérifiez le récapitulatif des sources à générer puis cliquez sur “Générer l’archive des sources”.

Vous téléchargez ainsi un ZIP contenant le plugin bp.

#Étape 3 - Déploiement du plugin Budget Participatif

Dans cette étape nous déployons le plugin bp qui a été généré.

Commencez par extraire le fichier zip récupéré dans l’étape précédente, et positionnez-vous dans le dossier extrait (au niveau du fichier pom.xml).

 **Note :** 


 
* Il est nécessaire de changer dans le fichier pom.xml le numéro de version du projet parent (balise `<version>` de `<parent>` par défaut à 3.0 ; mettre 3.0.3)
* Vous devez mettre le dossier extrait dans le dossier lutece-dev (où sont les sources de SVN et ceux de Git, nous allons utiliser le fichier pom.xml pour le mutil project)


Exécutez la commande Maven permettant de construire le projet :

 `$mvn lutece:exploded` 

Cette commande va générer un dossier “target” contenant le livrable de notre application plugin bp.

De la même façon que le site du plugin Wizard, nous devons initialiser une base de données pour cette l’application.

Suivez donc les mêmes étapes décrites dans la partie “Initialisation de la base de données” de l’Étape 1, pour le plugin bp.

 **Note :** Le nom de la base de données à créer doit être spécifique à chaque application pour éviter d'écraser des bases existantes. Pour l’application plugin bp nous la nommerons “lutece_bp”.

Déployez maintenant l’application sur Tomcat en utilisant le gestionnaire d’applications Web Tomcat (voir partie “Déploiement du site sur Tomcat” de l’Étape 1).

Activez le Plugin bp et affectez les droits d’utilisation à admin (voir partie “Activation du plugin” de l’Étape 1).

Un plugin Lutece se compose de deux parties: front office et back office.

Le front office est accessible à l’url : http://localhost:8080/bp/jsp/site/Portal.jsp. Vous trouverez une image correspondante dans picture/step3/front_office.gif.

Et le back office sur : http://localhost:8080/bp/jsp/admin/AdminMenu.jsp. Vous trouverez une image correspondante dans picture/step3/back_office.gif.

#Étape 4 - Nettoyage Front + Bootstrap

Dans cette partie nous allons ajouter 8 projets dans le plugin budget participatif, améliorer l’affichage de l’interface front et créer une page HTML pour afficher les détails d’un projet (id, name, description).

 **Note :** 


 
* Le dossier plugin-bp/webapp/WEB-INF/templates/admin contient les modèles HTML du Back.
* Le dossier plugin-bp/webapp/WEB-INF/templates/skin contient les modèles HTML du Front.


Vous trouverez une image de l'arborescence du plugin dans picture/step4/plugin_folder_tree.gif.

Dans plugin-bp/webapp/WEB-INF/templates/skin/plugins/bp, supprimez les pages create_project.html et modify_project.html.

Pour l'ajout des projets, vous devez avoir une page similaire à l'image picture/step4/project_management.gif accessible à http://localhost:8080/bp/jsp/admin/plugins/bp/ManageProjects.jsp?view=manageProjects.

Pour améliorer cette affichage nous allons ajouter du code Bootsrap dans la page manage_projets.html afin d’avoir un changement dynamique au moment de la réduction de la fenêtre.

Pour l'affichage des projets, vous devez avoir une page similaire à l'image picture/step4/project_display.gif accessible à http://localhost:8080/bp/jsp/site/Portal.jsp?page=project.

##Création de la page details_project.html :

Nous allons ajouter une page details_project.html qui contient les informations d’un projet (id, name, description, imageUrl) coté front.

Cette page sera affichée lorsqu’un utilisateur cliquera sur un projet.

Vous devez avoir une page qui ressemble à picture/step4/detail_project.gif accessible à http://localhost:8080/bp/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step2 step3` 

#Étape 5 - Ajouter un attribut coût au Projet

Dans cette partie, nous allons ajouter un attribut coût dans la table bp_projet de la base de données.

Cet attribut a trois contraintes :


 
* La valeur du coût doit être un nombre compris entre 5 et 25.
* La valeur doit être un multiple de 5.
* La valeur ne doit pas être nulle.


N’oubliez pas de modifier tous les fichiers nécessaires.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step3 step4` 

#Étape 6 - Intégration du plugin extend (HIT, RATING, COMMENT)

Dans cette partie nous allons réaliser un multi projet intègrant plusieurs plugins.

Si vous suivez bien ce guide, vous devriez avoir l'arborescence décrite dans picture/step6/project-folder.gif.

Le dossier plugin-bp-TP (ou tout autre nom renseigné au moment de la création dans le plugin Wizard) est notre squelette de base pour notre plugin. On peut mettre ce dossier dans le dossier /plugins ou dans un répertoire fils de notre dossier lutece-dev. Dans la racine du dossier lutece-dev, se trouve un fichier pom.xml. Il s'agit du pom principal du multi projet. Ouvrez ce fichier et mettez les dépendances suivantes :


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


Compilez le projet grâce à la commande suivante puis déployez-le sur Tomcat :

 `$mvn lutece:exploded -P multi-project` 

Activez les plugins sur le back office dans Système/Gestion des plugins comme dans picture/step6/plugin_management_2.gif.

Implémentez le plugin extend dans le plugin bp. (Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/howto-extend.html).

 **Note :** Au moment de modifier le template modify_project.html, ne pas insérer le markeur dans la balise `<form>` car celui-ci va lui-même générer des balises `<form>` 

 **Exercice :** 


 
* Ajouter l'extension nombre de vues (Hit) dans chaque projet et l’afficher dans la liste des projets sans l’incrémenter.
* Ajouter des commentaires dans la page détails de chaque projet.
* Ajouter le vote pour chaque projet dans la page détails du projet et l'afficher dans la liste de projets.


Pour l'affichage des projets, vous devez avoir une page similaire à picture/step6/project_display_2.gif.

Pour l'affichage de chaque projet, vous devez avoir une page similaire à picture/step6/detail_project_2.gif.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step4 step5` 

#Étape 7 - Mise en place d’un Web service REST

Dans cette partie nous allons utiliser le plugin REST de lutece.

Nous allons chercher un ensemble de données de notre base de données et les afficher sous le format JSON.

Pour cela nous allons intégrer le plugin rest dans le multi projet.

Notre choix de technologies est d’utiliser Jersey JAX-RS et l’API Jackson.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/howto-rest.html

 **Exercice :** 

Réalisez deux fonctions en Web Service REST :


 
* La première fonction affiche par projet : le nombre total de vues, le nombre total de votes, le nombre total de commentaires publiés et non publiés.
* La deuxième affiche le nombre total de vue, le nombre total de commentaires publiés et non publiés, le nombre total de votes de tous les projets.


Vous devez avoir un affichage comme dans picture/step7/rest_request.gif.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step5 step6` 

#Étape 8 - Gestion du cache

Dans cette partie nous allons sauvegarder la réponse du Web Service dans le cache.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/cache-management.html

 **Note :** après avoir suivi les instructions du wiki, activez votre cache en back office dans Système/Gestion des caches.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step6 step7` 

#Récupération du projet final

Vous pouvez récupérer le projet final de la manière suivanate :

Après avoir appliqué l'étape 0, tapez :

 `$git checkout step7` 

Déployez ensuite le projet comme à l'étape 3.


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/lutece-TP/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*
