
# 1. Introduction

Le but de ce tutoriel est de réaliser une application basique fonctionnant sous Lutece (un plugin), en différentes étapes :

* Installation des prérequis
* utilisation de l'application plugin-WIZARD permettant de créer facilement le squelette de notre projet
* lancement du plugin au sein d'un site lutece
* modification des pages de l'application
* modification du modèle de données
* lancement du plug-in au sein d'un site lutece utilisant d'autres plugins
* ajout d'un webservice REST
* ajout de l'implémentation d'un système de cache
* ajout d'une fonctionnalité de vote / commentaires aux pages du plugin
* finalisation du plugin (formatage, tests, packaging...)

Les branches GIT de ce projet sont utilisées de la façon suivante :
* branches step0 à step6 : branches correspondant aux états successifs des sources du plugin à chaque ajout de fonctionnalité
* branche develop : version complète du plugin
* branche Master : branche initiale du projet contenant ce fichier


Pour toute question ou suggestion, vous pouvez utiliser le forum Lutèce à l'adresse suivante :  https://fr.lutece.paris.fr/fr/suggest/forum-lutece.html

# 2. Prérequis
Pour ce TP il est nécessaire de disposer de l’environnement suivant :

* JDK1.8
* Maven 3.x
* Ant
* MySQL 5.x
* Tomcat 8.x
* Git & svn
* NetBeans ou Eclipse

L'usage suivant de GIT pour le suivi de version des sources de ce projet est proposé :

* Pour commencer, créez un répertoire de travail pour le projet, par exemple : `.../lutece-dev`.
* Clonez le projet plugin-example dans ce répertoire (commande : `git clone https://github.com/lutece-platform/lutece-dev-plugin-example.git`)
* Créez une branche locale de travail à partir de la branche Master :
```
cd lutece-dev-plugin-example
git branch myWork master
git checkout myWork`
```

A chaque étape du tutoriel, vous pourrez comparer votre code avec le code proposé dans les branches origin/stepX correspondantes, par exemple  avec  : `git diff myWork step0`, ou dans l'IDE de votre choix.

> vous pouvez aussi créer un fichier `ignore` dans votre répertoire `$HOME/.config/git/` contenant les exclusions ci-dessous afin de ne suivre avec Git que les sources de vos projets :
```
.classpath
.project
.settings/
target/
```



#  3. Génération du plugin à l'aide de plugin-wizard


Nous allons dans cette étape utiliser le plugin Wizard pour générer les sources du plugin `example`.

Allez à l’adresse https://dev.lutece.paris.fr/tools/jsp/site/Portal.jsp?page=pluginwizard. La fenêtre correspondant à l'image ci-dessous s’affiche :

![screenshot](/picture/plugin_wizard.gif)

Suivez les étapes du formulaire en renseignant les informations suivantes :


* Description du plugin :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom du plugin :| example |
| Auteur :| Mairie de Paris|


* Ajoutez une classe métier dans Métier :

| Rubrique| Valeur|
|-----------------|-----------------|
| Classe métier :| Project|
| Nom de la table :| example_project|


* Ajoutez les attributs suivants à la classe Project :

| Nom| Type|
|-----------------|-----------------|
| name | texte court( 50 caractères), obligatoire.|
| description | texte moyen 255 caractères, obligatoire.|
| image_url| URL, obligatoire.|


* Créez une fonctionnalité pour gérer les projets en backoffice (adminfeature) dans `Administration` :

| Rubrique| Valeur|
|-----------------|-----------------|
| Titre :| Manage project|
| Description :| Project Management|
| Nom technique :| ManageProject|
| Droit :| PROJECT_MANAGEMENT|
| Niveau droit :| Niveau 0|
| Classe métier :| Project|


* Ajoutez une XPage pour gérer les projets en front office dans `XPage` :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom de la XPage :| project|
| Classe de XPage :| ProjectApp|
| Classe métier :| Project|


Une fois à l’étape “Génération”, vérifiez le récapitulatif des sources à générer puis cliquez sur “Générer l’archive des sources”.

Vous téléchargez ainsi un ZIP contenant le plugin `plugin-example`.


Une fois le fichier ZIP téléchargé, décompressez les fichiers et copiez les fichiers directement  dans votre répertoire `lutece-dev-plugin-example` créé précedemment (toujours dans la branche de travail GIT 'myWork').

> Ne pas créer de répertoire "plugin-example" !



# 4. Initialisation de l'environnement de compilation et d'exécution du plugin

  ## 4.1. Build du projet 'plugin-example'

  Vérifiez dans le fichier `pom.xml`  que la version du `lutece-global-pom` dans la balise 'parent' est bien supérieure ou églale à  4.0.2. (et corriger si nécessaire).

  Positionnez-vous dans le dossier `.../lutece-dev/lutece-dev-plugin-example`, et exécutez la commande Maven (les tests sont ignorés car la base de données n'est  pas encore configurée):

   `$ mvn clean lutece:exploded `

> Documentation sur l'utilisation de Maven dans  lutèce : https://fr.flossmanuals.net/lutece-guide-developpeur/les-builds/




## 4.2. Mise en place de la configuration de la base de données

  Avant de continuer, nous allons créer un fichier permettant de stocker la configuration par défaut d'accès à la base de données, qui sera réutilisé à chaque construction du site.

Copiez le fichier :
  ``<lutece-dev>/lutece-dev-plugin-example/target/lutece/WEB-INF/conf/db.properties``

Dans le répertoire local de l'utilisateur ('/home/<username>/...' pour linux ou '%SystemDrive%\Users\<username>\(...)' pour windows), créez le répertoire ``<username>/lutece/conf/plugin-example/WEB-INF/conf/``  et collez le fichier `db.properties` dedans. 
(Vous pouvez également utiliser le répertoire ``<lutece-dev>/lutece-dev-plugin-example/src/conf/default/WEB-INF/conf/``, mais il est préférable de ne pas  publier sur un repository public partagé votre configuration locale de développement pour ce projet.)

Modifiez ensuite ce fichier `db.properties`  :

  - Renommez la base de données utilisée par le site en “lutece_test” (par exemple) en modifiant la propriété portal.url comme suit :

  ```
  portal.url=jdbc:mysql://localhost/lutece_test?autoReconnect=true&useUnicode=yes&characterEncoding=utf8
  ```


  - Modifiez également les paramètres d'authentification à votre serveur mysql :

  ```
  portal.user=root
  portal.password=motdepasse
  ```

Relancez un build de l'application pour prendre en compte cette nouvelle configuration :

  `$ mvn clean lutece:exploded `

Un fichier Ant `build.xml` a été généré dans le répertoire :
``...target/lutece/WEB-INF/sql/build.xml``.

Dans le répertoire ''...target/lutece/WEB-INF/sql/', lancez Ant   pour construire la base de données :

`$ ant -f build.xml`


  Une nouvelle base de données nommée “lutece_test” est alors créée sur le serveur MySql local.


##  4.3. Configuration et lancement du Tomcat

  Nous pouvons à présent déployer notre site sur Tomcat.

  Pour cela, modifiez le fichier `server.xml` dans le dossier `tomcat/conf` de votre application Tomcat, ajoutez la ligne suivante entre les balises `<HOST>` :

   `<Context docBase="(...)/lutece-dev/lutece-dev-plugin-example/target/lutece" path="/example" workDir="(...)/lutece-dev/lutece-dev-plugin-example/target/work" />`

Démarrez Tomcat.

> Si vous utilisez NetBeans, vous pouvez consulter le wiki correspondand au paramétrage de Tomcat depuis NetBeans : http://lutece.paris.fr/support/wiki/netbeans.html

Vous pouvez ensuite vérifier que le site est bien accessible sur l’Url http://localhost:8080/example/ (remplacez le port utilisé par tomcat si nécessaire, par ex. 8084).

La page correspondand à l'image suivante s’affiche :

  ![image première page](/picture/first_page.gif)

##  4.4. Activation du plug-in

Pour pouvoir utiliser le plugin, il est d’abord nécessaire de l’activer et d’affecter les droits à l’utilisateur.

Pour cela il faut aller dans la partie "administration" du site à l’adresse :
 http://localhost:8080/example/jsp/admin/AdminLogin.jsp

... et s'authentifier avec le compte admin par défaut :
* login : admin
* mot de passe : adminadmin

Procédez au changement du mot de passe par défaut du compte admin, et reconnectez vous.

Sur la page qui s’affiche, sélectionnez dans le menu : `Système > Gestion des Plugin`.

La page correspondant à l'image suivante s’affiche :

![screenshot](/picture/plugin_management.gif)

Votre  plugin `example` doit apparaître dans la liste. Activez le en cliquant sur le bouton vert à droite de l'écran.

## 4.5. Déclaration droits utilisateur admin


Sélectionnez ensuite le menu `Gestionnaires > Gestion des utilisateurs`. Sur l’écran qui s’affiche sélectionnez le bouton Modifier du user `admin`.

Sur la page qui s’affiche, allez sur l’onglet “Droits” comme dans l'image ci-dessous, puis cochez les cases correspondant aux fonctionnalités du plugin `example`, et appliquez les modifications en cliquant sur le bouton en bas de page.

![screenshot](/picture/user_rights.gif)

Le plugin est maintenant prêt à être utilisé.

## 4.6. Tests bon fonctionnement

Le Wizard a permis d'obtenir une application permettant la consultation de projets en mode public et privé.

En mode public (front office), la liste des projets est affichée avec la page :

http://localhost:8080/example/jsp/site/Portal.jsp?page=project

En mode privé (administration, ou back office), vous avec également  une page permettant de gérer une liste de projets :

http://localhost:8080/example/jsp/admin/plugins/example/ManageProjects.jsp?view=manageProjects


Félicitations, vous avez créé  votre premier plugin lutèce !

>  Vous pouvez lancer un premier commit afin de comparer les fichiers générés à l'aide des commandes suivantes :
>
> `$ git add --all`
>
> `$ git commit -a -m"premier commit"`
>
> `$ git diff myWork step0`


Vous pouvez également consulter le wiki de présentation des plugin Lutece pour comprendre l'organisation du code généré :
http://lutece.paris.fr/support/wiki/howto-create-plugin.html

... ou encore consulter le wiki sur l'architecture logicielle de Lutèce :
http://lutece.paris.fr/support/wiki/architecture-overview.html


Nous allons maintenant modifier le code de ce plugin pour ajouter des fonctionnalités à cette application.




# 5. Ajout de fonctionnalités au plugin example

## 5.1.  Etape 1 : Modification de l'affichage public de la liste des projets

Dans cette partie nous allons ajouter 8 projets et améliorer l’affichage de la liste des projets dans l’interface front.

**Note :**
* Le dossier /webapp/WEB-INF/templates/skin contient les modèles HTML du Front.
* Le dossier /webapp/WEB-INF/templates/admin contient les modèles HTML du Back.

Voici l'arborescence du plugin :

![screenshot](/picture/plugin_folder_tree.gif)

L'adresse de la page d'affichage public de la liste des projets est la suivante : http://localhost:8080/example/jsp/site/Portal.jsp?page=project

Pour l'ajout des projets, vous devez avoir une page similaire à ceci :

![screenshot](/picture/project_management.gif)

Ajoutez 8 projets (choisissez n'importe quelles URL d'images).

Pour améliorer cet affichage nous allons modifier la page "manage_projets.html" afin d’avoir un affichage par blocs et un changement dynamique au moment de la réduction de la fenêtre (comportement 'responsive'), et ainsi obtenir une page similaire à l'image ci-dessous :

![screenshot](/picture/project_display.gif)

Utilisez les macros Freemarker proposées par Lutèce, en vous aidant du Wiki lutèce concernant l'utilisation de bootstrap et les macros Freemarker : http://lutece.paris.fr/support/wiki/bo-bootstrap.html

Le fichier de déclaration des macros freemarker peut être consulté dans le répertoire  : `.../lutece-dev/lutece-dev-plugin-example/target/lutece/WEB-INF/templates/commons.html` ou sur github (https://github.com/lutece-platform/lutece-core/blob/develop/webapp/WEB-INF/templates/commons.html).

> Pour contrôler votre code, vous pouvez le comparer à la nouvelle version de l'application qui se trouve dans la branche git `step1` du projet.

Tapez la commande suivante pour obtenir les différences entre les étapes :

 `$ git diff step0 step1`.

## 5.2. Etape 2 : Création de la page de détail d'un projet

Nous allons ajouter une page details_project.html contenant les informations d’un projet (id, name, description, imageUrl) coté front-office.

Cette page sera affichée lorsqu’un utilisateur cliquera sur un projet et accessible à http://localhost:8080/example/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1.

Vous devez avoir une page qui ressemble à l'image suivante :

![screenshot](/picture/detail_project.gif)

**Note :**
* La classe java :  src/java/fr/paris/lutece/plugins/example/web/ProjectXPage gère les vues et actions du front-office.
* La classe java :  src/java/fr/paris/lutece/plugins/example/web/ProjectJSPBean gère les vues et actions du back-office.

En cas de problème liés aux labels et messages, vous pouvez consulter le wiki lutèce  http://lutece.paris.fr/support/wiki/i18n.html

Tapez la commande suivante pour obtenir les différences entre les étapes :

 `$ git diff step1 step2`.

## 5.3. Etape 3 : Ajouter un attribut "coût" au Projet

Dans cette partie, nous allons ajouter un attribut coût aux projets.

Cet attribut a trois contraintes :

* La valeur du coût doit être un nombre compris entre 5 et 25.
* La valeur doit être un multiple de 5.

Modifiez la table example_projet de la base de données, ainsi que tous les fichiers nécessaires, notamment les fichiers SQL, les DAOs, les templates, les tests unitaires...

Wiki lutèce dédié aux règles de validation : http://lutece.paris.fr/support/wiki/bean-validation.html

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step2 step3`

# 6. Ajout de fonctionnalités provenant d'autres plugins

Pour permettre l'interaction de plusieurs plugins au sein d'un site Lutèce, il est nécessaire de passer par un autre mode de construction de site.
Deux modes sont proposés : le mode "site", ou le mode "multi-projet".

## 6.1. Configuration de l'environnement en mode "site"

### 6.1.1. Construction du pom.xml pour le site lutèce

Nous allons utiliser le service en ligne "createSitePom" permettant de construire automatiquement le fichier pom.xml qui nous permettra de générer le site Lutèce incluant un choix de plugins, accessible à cette adresse :

  https://dev.lutece.paris.fr/tools/jsp/site/Portal.jsp?page=createsitepom

Saisir la liste de plugins ci-dessous (qui seront utilisés pour la suite du projet) :

```
plugin-example
library-lucene
plugin-mylutece
plugin-extend
module-extend-rating
module-extend-comment
module-mylutece-database
plugin-avatar
plugin-mydashboard
plugin-mylutecetest
plugin-rest
```

Générez le pom.xml. Vous devez obtenir un fichier tel que celui-ci :
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
Recopiez ce fichier pom.xml dans le répertoire ``lutece-dev`` créé précedemment (répertoire parent du répertoire ``lutece-dev-plugin-example``).

Modifier dans ce fichier le nom de l'artifact (balise **artifactId**) et le nom du site (balise **name**) comme ci-dessous :

```
<modelVersion>4.0.0</modelVersion>
<groupId>fr.paris.lutece</groupId>
<artifactId>lutece-test</artifactId>
<packaging>lutece-site</packaging>
<version>1.0.0-SNAPSHOT</version>
<name>lutece-test</name>
```

Modifiez également la balise **version** du plugin-example avec le numéro de version courante : 1.0.0-SNAPSHOT

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

> Remarque : La dépendance lutece-core est obligatoire, c'est le coeur du CMS Lutece. Cet élément gère entre autre la coordination de tous les autres plugins

### 6.1.2. Compilation du projet 'plugin-example'

Compilez à nouveau votre projet "plugin-example".

 `$mvn clean install  -Dmaven.test.skip=true`

> L'option INSTALL est nécessaire pour que Maven prenne en compte votre projet dans son repository local et non le projet publié sur github. Les tests sont ignorés car la base de données n'est pas encore initialisée.

### 6.1.3. Construction du site :

Positionnez-vous dans le dossier parent ``lutece-dev`` et exécutez la commande Maven :

 `$mvn clean lutece:site-assembly`

Ceci va générer un dossier “target” contenant un livrable du site : dossier "lutece-test-1.0.0-SNAPSHOT".

> A chaque modification du projet, il faudra lancer un build du plugin (`mvn [clean] lutece:exploded` dans le dossier `lutece-dev-plugin-example`) et un build du site (`mvn [clean] lutece:site-assembly` dans le dossier `lutece-dev`).

## 6.2. Utilisation du mode multi-projet

Une autre alternative de construction du site lutèce consiste à utiliser le mode multi-projet.

Dans cette partie nous allons réaliser un multi projet intégrant plusieurs plugins.  Ce mode nécessite le téléchargement préalable des sources de Lutèce ainsi que de ses principaux plugins.

### 6.2.1. Récupération complète des sources de Lutèce

Le projet Lutèce a été initialement versionné avec SVN avant de passer sur Git et certains plugins n'ont pas encore été migrés vers Git.

Pour commencer, positionnez vous au dessus du répertoire `lutece-dev` et téléchargez les sources Lutèce depuis SVN avec la commande suivante :

 `$ svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev`

Une fois cette commande exécutée, il faut récupérer les scripts git permettant de cloner automatiquement toutes les sources de Github.

Dans un autre répertoire faites :

 `$ git clone https://github.com/lutece-platform/tools-git-scripts`

Ensuite copiez le contenu de ce dossier scripts dans le dossier lutece-dev :

 `$ cp -r tools-git-scripts/* lutece-dev/` ( **note :** commande unix)

Puis fusionnez les deux sources en tapant dans le dossier lutece-dev :

 `$ ./lutece.sh config -k -d`

Donnez votre nom, votre login et mot de passe github (créez un compte si nécessaire sur https://github.com)

Ensuite tapez :

 `$ ./lutece.sh sync -k -t https`

 **Note :** Si vous tapez ces commandes dans Git Bash sur windows, un problème peut survenir avec le curl que Git Bash utilise par défaut (Le prompt demandant le mot de passe ne s'affiche pas). Pour contourner le problème, vous pouvez utiliser la commande :  `winpty bash`

### 6.2.2. Configuration du mode "multi-projet"

Le dossier `lutece-dev-plugin-example` se trouve normalement dans  notre dossier `lutece-dev`. Renommez (ou dupliquez) ce répertoire en `plugin-example`, pour que le nom du dossier corresponde au nom de l'artifact du projet.

A la racine du dossier `lutece-dev`, se trouve un fichier pom.xml. Il s'agit du pom principal du site utilisé précedemment pour le mode "site".

Renommez le par exemple en pom.xml.dependencies (pour le conserver).

Créez un nouveau fichier pom.xml, et copiez dedans le contenu ci-dessous, qui permet de déclarer les différents modules qui seront utilisés.

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

Compilez le projet grâce à la commande suivante :

 `$mvn lutece:clean lutece:exploded -P multi-project`

> Cette commande construit à la fois les plugins et le site lutèce.

## 6.3. Finalisation de l'environnement du site

Les étapes suivantes sont identiques quelque soit le mode utilisé, site ou multi-projet.

### 6.3.1. Initialisation d'une nouvelle base de données :

Il est nécessaire de reconfigurer une nouvelle base de données qui contiendra toutes les tables des plugin utilisés.

Comme précedemment, nous allons tout d'abord créer un fichier permettant de stocker la configuration par défaut d'accès à la base de données, qui sera réutilisé à chaque construction du site.

Copiez le fichier :
``<lutece-dev>/target/lutece[-test-1.0.0]/WEB-INF/conf/db.properties``

Créez ensuite l'un des deux répertoires de déploiement de configuration par défaut :
* ``.../<lutece-dev>/src/conf/default/WEB-INF/conf/`` (mode site-assembly uniquement)
* ``/home/<user>/lutece/conf/<artifact-id-du-projet>/WEB-INF/conf/``  (mode site-assembly et multiproject)

Collez le fichier db.properties dedans.

Modifiez ensuite ce fichier db.properties  :

- Renommez la base de données utilisée par le site en “lutece_test2” (par exemple) en modifiant la propriété portal.url comme suit :
```
portal.url=jdbc:mysql://localhost/lutece_test2?autoReconnect=true&useUnicode=yes&characterEncoding=utf8
```

- Modifiez également les paramètres d'authentification à votre serveur mysql :
```
portal.user=root
portal.password=motdepasse
```

Relancez un build  du site depuis le répertoire ``<lutece-dev>`` (comme précedemment).

Un fichier Ant `build.xml` a été généré dans le répertoire :
``...target/lutece-test-1.0.0/WEB-INF/sql/build.xml``.

Dans le répertoire ''...target/lutece-test-1.0.0/WEB-INF/sql/', lancez Ant  : `` ant -f build.xml`` pour construire la nouvelle  base de données avec les tables correspondant à tous les plugins sélectionés.

Une nouvelle base de données nommée “lutece_test2” est alors créée sur le serveur MySql local.

### 6.3.2. Configuration du déploiement du site sur Tomcat :

Pour cela, dans le dossier conf de votre application Tomcat, ajoutez la ligne suivante dans la balise `<HOST>` du fichier server.xml :

 `<Context docBase="[path]/lutece-dev/target/lutece-test-1.0.0-SNAPSHOT" path="/lutece-test" workDir="[path]/lutece-dev/target/work" />`

Démarrez le Tomcat, vous pouvez ensuite vérifier que le site est bien accessible sur l’Url http://localhost:8080/lutece-test/ (si le tomcat utilise le port 8080).

La page correspondand à l'image suivante s’affiche :

![image première page](/picture/first_page.gif)

### 6.3.3. Activation des plugins

Connectez vous au backoffice :

http://localhost:8080/lutece-test/jsp/admin/AdminLogin.jsp

Activez les plugins sur le back office dans `Système/Gestion des plugins` (comme précédemment).

Donnez les droits sur les plugins à l'utilisateur "admin".


## 6.4. Etape 4 - Mise en place d’un Web service REST

Dans cette partie nous allons utiliser le plugin REST de lutece.

Nous allons chercher un ensemble de données de notre base de données et les afficher sous le format JSON.

Notre choix de technologies est d’utiliser Jersey JAX-RS et l’API Jackson.

Vous pouvez suivre ce tutoriel sur la mise en place d'un service REST Lutèce : http://lutece.paris.fr/support/wiki/howto-rest.html

 **Exercice :**

Réalisez trois  fonctions en Web Service REST :

* Liste des projets au format JSON
* Détail des données d'un projet au format JSON
* Détail des données d'un projet ainsi que le nb de vues de ce projet au format JSON

exemple : l'URL `http://localhost:8080/lutece-test/rest/example/projects` doit renvoyer un fichier du type :

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

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$ git diff step3 step4`

## 6.5. Etape 5 : Gestion du cache

Dans cette partie nous allons sauvegarder la réponse du Web Service dans le cache.

Vous pouvez suivre ce tutoriel : http://lutece.paris.fr/support/wiki/cache-management.html

 **Exercice :**

Ajoutez une mise en cache de chaque projet à chaque consultation de détail d'un projet.

 **Note :** après avoir suivi les instructions du wiki, activez votre cache en back office dans Système/Gestion des caches.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$ git diff step4 step5`
 
 ## 6.6. Etape 6 : Intégration du plugin Extend (collaboration)

Implémentez le plugin extend dans le plugin example, en suivant ce tutoriel :
 http://lutece.paris.fr/support/wiki/howto-extend.html

 **Note :** Au moment de modifier le template modify_project.html, ne pas insérer le markeur dans la balise `<form>` car celui-ci va lui-même générer des balises `<form>`

 **Exercice :**

* Ajouter l'extension nombre de vues (Hit) dans chaque projet et l’afficher dans la liste des projets sans l’incrémenter.
* Ajouter des commentaires dans la page détails de chaque projet.
* Ajouter le vote pour chaque projet dans la page détails du projet et l'afficher dans la liste de projets.

Pour l'affichage des projets, vous devez avoir une page similaire à ceci :

![screenshot](/picture/project_display_2.gif)

Pour l'affichage de chaque projet, vous devez avoir une page similaire à ceci :

![screenshot](/picture/detail_project_2.gif)

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$ git diff step5 step6`

# 7. Finalisation du plugin

## 7.1. Formatage des sources

Pour ajouter les entêtes et formater correctement les fichiers sources du plugin, utilisez les commandes maven :

 `mvn license:format`

 `mvn formatter:format`

Construction du site maven (tests, checkstyle, PMD...) :

 `mvn site`

Génération du fichier  README.md à partir du fichier site.xml :

 `mvn xdoc2md`

ou

 `mvn fr.paris.lutece.tools:xdoc2md-maven-plugin:readme`

## 7.2. Tests

Pour tester automatiquement le plugin (test unitaires), on peut utiliser la commande suivante:

 `mvn lutece:exploded antrun:run test`

## 7.3. Packaging

Génération d'une distribution du plugin :

 `mvn lutece:assembly`
