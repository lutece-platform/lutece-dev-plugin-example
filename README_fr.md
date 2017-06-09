
# 1. Introduction

Le but de ce tutoriel est de réaliser une application basique fonctionnant sous Lutece (un plugin), en différentes étapes :

* Installation des prérequis
* utilisation de l'application plugin-WIZARD permettant de créer facilement le squelette de notre projet
* lancement du plugin au sein d'un site lutece
* modification des pages de l'application
* modification du modèle de données
* lancement du plug-in au sein d'un site lutece utilisant d'autres plugins
* ajout d'une fonctionnalité de vote / commentaires aux pages
* ajout d'un webservice REST
* ajout de l'implémentation d'un système de cache

# 2. Prérequis
Pour ce TP il est nécessaire de disposer de l’environnement suivant :

* JDK1.8
* Maven 3.x
* Ant
* MySQL 5.x
* Tomcat 8.x
* Git & svn
* NetBeans ou Eclipse

Pour commencer, créez un répertoire de travail pour le projet, par exemple : `.../lutece-dev`.



#  3. Génération du plugin à l'aide de plugin-wizard


Nous allons dans cette étape utiliser le plugin Wizard pour générer les sources du plugin `example`.

Allez à l’adresse http://fr.lutece.paris.fr/fr/jsp/site/Portal.jsp?page=pluginwizard . La fenêtre correspondant à l'image  picture/plugin_wizard.gif s’affiche.

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
| image_URL| URL, obligatoire.|


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


Une fois le fichier ZIP téléchargé, de-zippez le dans le répertoire `lutece-dev` créé précedemment.

**Suivi des modifications :**

 Dans le répertoire  `plugin-example`, initialisez le suivi GIT pour démarrer le suivi des modifications de votre projet :

 `$ git init`

Récupérez ensuite  les sources du projet, ce qui permettra de faire des comparaisons entre les étapes successives d'ajout ou modification des fichiers.

`$ git clone https://github.com(...)`

Créez votre branche de travail à partir du tag "init" pour démarrer le projet à l'état initial (vide !) :

`$ git checkout -b mybranch init`





# 4. Initialisation de l'environnement de compilation et d'exécution du plugin

  ## 4.1. Build du projet 'plugin-example'

  Vérifiez dans le fichier `pom.xml`  que la version du `lutece-global-pom` dans la balise 'parent' est bien supérieure ou églale à  4.0.2. (et corriger si nécessaire).

  Positionnez-vous dans le dossier `.../lutece-dev/plugin-example`, et exécutez la commande Maven (les tests sont ignorés car la base de données n'est  pas encore configurée):

   `$ mvn clean lutece:exploded `

> Documentation sur l'utilisation de Maven dans  lutèce : https://fr.flossmanuals.net/lutece-guide-developpeur/les-builds/




## 4.2. Mise en place de la configuration de la base de données

  Avant de continuer, nous allons créer un fichier permettant de stocker la configuration par défaut d'accès à la base de données, qui sera réutilisé à chaque construction du site.

Copiez le fichier :
  ``<lutece-dev>/plugin-example/target/lutece/WEB-INF/conf/db.properties``

Créez le répertoire   ``<lutece-dev>/plugin-example/src/conf/default/WEB-INF/conf/`` et collez le fichier `db.properties` dedans.


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

Relancez un build de l'application :

  `$ mvn clean lutece:exploded `

Un fichier Ant `build.xml` a été généré dans le répertoire :
``...target/lutece/WEB-INF/sql/build.xml``.

Dans le répertoire ''...target/lutece/WEB-INF/sql/', lancez Ant   pour construire la base de données :

`$ ant -f build.xml`


  Une nouvelle base de données nommée “lutece_test” est alors créée sur le serveur MySql local.


##  4.3. Configuration et lancement du Tomcat

  Nous pouvons à présent déployer notre site sur Tomcat.

  Pour cela, modifiez le fichier `server.xml` dans le dossier `tomcat/conf` de votre application Tomcat, ajoutez la ligne suivante entre les balises `<HOST>` :

   `<Context docBase="(...)/lutece-dev/plugin-example/target/lutece" path="/example" workDir="(...)/lutece-dev/plugin-example/target/work" />`

Démarrez Tomcat.

> Si vous utilisez NetBeans, vous pouvez consulter le wiki correspondand au paramétrage de Tomcat depuis NetBeans : http://fr.lutece.paris.fr/fr/wiki/netbeans.html

  Vous pouvez ensuite vérifier que le site est bien accessible sur l’Url http://localhost:8084/example/
  (remplacez le port utilisé par tomcat si nécessaire, par ex. 8080).

  La page correspondand à l'image picture/first_page.gif s’affiche.

  ![image première page](/picture/first_page.gif)



##  4.4. Activation du plug-in

Pour pouvoir utiliser le plugin, il est d’abord nécessaire de l’activer et d’affecter les droits à l’utilisateur.

Pour cela il faut aller dans la partie "administration" du site à l’adresse :
 http://localhost:8084/example/jsp/admin/AdminLogin.jsp

... et s'authentifier avec le compte admin par défaut :
* login : admin
* mot de passe : adminadmin

Procédez au changement du mot de passe par défaut du compte admin, et reconnectez vous.

Sur la page qui s’affiche, sélectionnez dans le menu : `Système > Gestion des Plugin`.

La page correspondant picture/plugin_management.gif s’affiche.

![screenshot](/picture/plugin_management.gif)

Votre  plugin `example` doit apparaître dans la liste. Activez le en cliquant sur le bouton vert à droite de l'écran.

## 4.5. Déclaration droits utilisateur admin


Sélectionnez ensuite le menu `Gestionnaires > Gestion des utilisateurs`. Sur l’écran qui s’affiche sélectionnez le bouton Modifier du user `admin`.

Sur la page qui s’affiche, allez sur l’onglet “Droits” comme dans l'image d'exemple  picture/user_rights.gif ci-dessous,  puis cochez les cases correspondant aux fonctionnalités du plugin `example`.

![screenshot](/picture/user_rights.gif)

Le plugin est maintenant prêt à être utilisé.

## 4.6. Tests bon fonctionnement

Le Wizard a permis d'obtenir une application permettant la consultation de projets en mode public et privé.

En mode public (front office), la liste des projets est affichée avec la page :

http://localhost:8084/example/jsp/site/Portal.jsp?page=project

En mode privé (administration, ou back office), vous avec également  une page permettant de gérer une liste de projets :

http://localhost:8084/example/jsp/admin/plugins/example/ManageProjects.jsp?view=manageProjects


Félicitations, vous avez créé  votre premier plugin lutèce !

Vous pouvez consulter le wiki de présentation des plugin Lutece pour comprendre l'organisation du code généré :
http://fr.lutece.paris.fr/fr/wiki/howto-create-plugin.html


Nous allons maintenant modifier le code de ce plugin pour ajouter des fonctionnalités à cette application.



> La version initiale des sources du plugin se situe dans la branche 'step0' du projet sur Github.


# 5. Ajout de fonctionnalités au plugin example

## 5.1.  Etape 1 : Modification de l'affichage public de la liste des projets

Dans cette partie nous allons ajouter 8 projets, améliorer l’affichage de l’interface front et créer une page HTML pour afficher les détails d’un projet (id, name, description).

 **Note :**
* Le dossier plugin-example/webapp/WEB-INF/templates/admin contient les modèles HTML du Back.
* Le dossier plugin-example/webapp/WEB-INF/templates/skin contient les modèles HTML du Front.


Vous trouverez l'image de l'arborescence du plugin dans picture/plugin_folder_tree.gif.

![screenshot](/picture/plugin_folder_tree.gif)


Pour l'ajout des projets, vous devez avoir une page similaire à l'image picture/project_management.gif accessible à http://localhost:8084/example/jsp/site/Portal.jsp?page=project

Créez les 8 projets via l'interface d'administration des projets.

![screenshot](/picture/project_management.gif)

Pour améliorer cette affichage nous allons modifier la page manage_projets.html afin d’avoir un changement dynamique au moment de la réduction de la fenêtre (comportement 'responsive').

Faire le nécessaire pour obtenir  une page similaire à l'image picture/project_display.gif accessible à http://localhost:8084/example/jsp/site/Portal.jsp?page=project.

![screenshot](/picture/project_display.gif)

Wiki lutèce concernant l'utilisation de bootstrap et les macros Freemarker :
http://fr.lutece.paris.fr/fr/wiki/bo-bootstrap.html

> Pour contrôler votre code, vous pouvez le comparer à la nouvelle version de l'application qui se trouve dans la branche git `step1` du projet.

## 5.2. Etape 2 : Création de la page de détail d'un projet

Nous allons ajouter une page details_project.html qui contient les informations d’un projet (id, name, description, imageUrl) coté front.

Cette page sera affichée lorsqu’un utilisateur cliquera sur un projet.

Vous devez avoir une page qui ressemble à l'image picture/detail_project.gif,  accessible à http://localhost:8084/example/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1.

![screenshot](/picture/detail_project.gif)



En cas de problème liés aux labels et messages, vous pouvez consulter le wiki lutèce  http://fr.lutece.paris.fr/fr/wiki/i18n.html

> Pour contrôler votre code, vous pouvez le comparer à la nouvelle version de l'application qui se trouve dans la branche `step2` du projet. Tapez la commande suivante pour obtenir les différences entre les étapes :  `$ git diff step1 step2`.

## 5.3. Etape 3 : Ajouter un attribut "coût" au Projet

Dans cette partie, nous allons ajouter un attribut coût aux projets.

Cet attribut a trois contraintes :

* La valeur du coût doit être un nombre compris entre 5 et 25.
* La valeur doit être un multiple de 5.
* La valeur ne doit pas être nulle.

Modifiez la table example_projet de la base de données, ainsi que tous les fichiers nécessaires, notamment les fichiers SQL, les DAOs, les templates, les tests unitaires...

Wiki lutèce dédié aux règles de validation : http://fr.lutece.paris.fr/fr/wiki/bean-validation.html


> Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :  `$git diff step2 step3`



# 6. Ajout de fonctionnalités provenant d'autres plugins

Pour permettre l'interaction de plusieurs plugins au sein d'un site Lutèce, il est nécessaire de passer par un autre mode de construction de site.
Deux modes sont proposés : le mode "site", ou le mode "multi-projet".

## 6.1. Configuration de l'environnement en mode "site"

### 6.1.1. Construction du pom.xml pour le site lutèce

Nous allons utiliser le service en ligne "createSitePom" permettant de construire automatiquement le fichier pom.xml qui nous permettra de générer le site Lutèce incluant un choix de plugins, accessible à cette adresse :

 https://dev.lutece.paris.fr/incubator/jsp/site/Portal.jsp?page=createsitepom

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

Générez le pom.xml, vous devez obtenir un fichier tel que celui-ci :
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
Recopiez ce fichier pom.xml dans le répertoire ``lutece-dev`` créé précedemment (répertoire parent du répertoire ``plugin-example``).

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

 `$mvn clean lutece:exploded install -DskipTests -Dmaven.test.skip=true`

> L'option INSTALL est nécessaire pour que Maven prenne en compte votre projet dans son repository local et non le projet publié sur github. Les tests sont ignorés car la base de données n'est pas encore initialisée.


### 6.1.3. Construction du site :

Positionnez-vous dans le dossier parent ``lutece-dev`` et exécutez la commande Maven :

 `$mvn clean lutece:site-assembly`

Ceci va générer un dossier “target” contenant un livrable du site : dossier "lutece-test-1.0.0-SNAPSHOT".

> Un build `lutece:exploded` du plugin (dans le dossier `plugin-example`) et un build `lutece:site-assembly` du site lutèce (dans le dossier `lutece-dev`) seront nécessaires à chaque modification du projet.



## 6.2. Utilisation du mode multi-projet

Une autre alternative de construction du site lutèce consiste à utiliser le mode multi-projet.

Dans cette partie nous allons réaliser un multi projet intègrant plusieurs plugins.  Ce mode nécessite le téléchargement préalable des sources de Lutèce.


### 6.2.1. Récupération complète des sources de Lutèce

Le projet Lutèce a été initialement versionné avec SVN avant de passer sur Git et certains plugins n'ont pas encore été migrés vers Git.

Pour commencer, il faut récupérer les sources depuis SVN avec la commande suivante :

 `$ svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev`

Une fois cette commande exécutée, il faut récupérer les scripts git permettant de cloner automatiquement toutes les sources de Github.

Dans un autre répertoire faites :

 `$ git clone https://github.com/lutece-platform/tools-git-scripts`

Ensuite copiez le contenu de ce dossier scripts dans le dossier lutece-dev :

 `$ cp -r tools-git-scripts/* lutece-dev/` ( **note :** commande unix)

Puis fusionnez les deux sources en tapant dans le dossier lutece-dev :

 `$ ./lutece.sh config –d`

Donner votre nom, votre login et mot de passe github (https://github.com)

 **Note :** Si vous tapez ces commandes dans Git Bash sur windows, un problème peut survenir avec le curl que Git Bash utilise par défaut (Le prompt demandant le mot de passe ne s'affiche pas). Pour contourner le problème, vous pouvez tenter la commande :  `winpty bash`

Ensuite tapez :

 `$ ./lutece.sh sync -t https`

### 6.2.2. Configuration du mode "multi-projet"


Le dossier `plugin-example` se trouve normalement dans  notre dossier `lutece-dev`. A la racine du dossier `lutece-dev`, se trouve un fichier pom.xml. Il s'agit du pom principal du site dans lequel nous allons ajouter le mode multi projet.

Ouvrez ce fichier et ajoutez le bloc 'profile' suivant :


```
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
```


Compilez le projet grâce à la commande suivante :

 `$mvn lutece:clean lutece:exploded -P multi-project`

> Cette commande construit à la fois les plugin et le site lutèce

## 6.3. Finalisation de l'environnement du site

(Les  étapes suivantes sont identiques quelque soit le mode utilisé, site ou multi-projet.)

### 6.3.1. Initialisation d'une nouvelle base de données :

Il est nécessaire de reconfigurer une nouvelle base de données qui contiendra toutes les tables des plugin utilisés.

Comme précedemment, nous allons tout d'abord créer un fichier permettant de stocker la configuration par défaut d'accès à la base de données, qui sera réutilisé à chaque construction du site.

Copiez le fichier :
``<lutece-dev>/target/lutece-test-1.0.0/WEB-INF/conf/db.properties``

Créez le répertoire ``<lutece-dev>/src/conf/default/WEB-INF/conf/`` et collez le fichier db.properties dedans.

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

Dans le répertoire ''...target/lutece-test-1.0.0/WEB-INF/sql/', lancez Ant  : `` ant -f build.xml`` pour construire la nouvelle  base de données.

Une nouvelle base de données nommée “lutece_test2” est alors créée sur le serveur MySql local.


### 6.3.2. Configuration du déploiement du site sur Tomcat :


Pour cela, dans le dossier conf de votre application Tomcat, ajoutez la ligne suivante dans la balise `<HOST>` du fichier server.xml :

 `<Context docBase="[path]/lutece-dev/target/lutece-test-1.0.0-SNAPSHOT" path="/lutece-test" workDir="[path]/lutece-dev/target/work" />`

Vous pouvez ensuite vérifier que le site est bien accessible sur l’Url http://localhost:8080/lutece-test/ (si le tomcat utilise le port 8080).

La page correspondand à l'image picture/first_page.gif s’affiche.

![image première page](/picture/first_page.gif)


### 6.3.3. Activation des plugins

Connectez vous au backoffice :

http://localhost:8080/lutece-test/jsp/admin/AdminLogin.jsp

Activez les plugins sur le back office dans `Système/Gestion des plugins` (comme précédemment).

Donnez les droits sur les plugins à l'utilisateur "admin".

## 6.4. Etape 4 : Intégration du plugin Extend (collaboration)


Implémentez le plugin extend dans le plugin example, en suivant ce tutoriel :
 http://fr.lutece.paris.fr/fr/wiki/howto-extend.html

 **Note :** Au moment de modifier le template modify_project.html, ne pas insérer le markeur dans la balise `<form>` car celui-ci va lui-même générer des balises `<form>`

 **Exercice :**

* Ajouter l'extension nombre de vues (Hit) dans chaque projet et l’afficher dans la liste des projets sans l’incrémenter.
* Ajouter des commentaires dans la page détails de chaque projet.
* Ajouter le vote pour chaque projet dans la page détails du projet et l'afficher dans la liste de projets.


Pour l'affichage des projets, vous devez avoir une page similaire à picture/project_display_2.gif.

![screenshot](/picture/project_display_2.gif)

Pour l'affichage de chaque projet, vous devez avoir une page similaire à picture/detail_project_2.gif.

![screenshot](/picture/detail_project_2.gif)

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step3 step4`

## 6.5. Etape 5 - Mise en place d’un Web service REST

Dans cette partie nous allons utiliser le plugin REST de lutece.

Nous allons chercher un ensemble de données de notre base de données et les afficher sous le format JSON.

Notre choix de technologies est d’utiliser Jersey JAX-RS et l’API Jackson.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/howto-rest.html

 **Exercice :**

Réalisez trois  fonctions en Web Service REST :

* Liste des projets au format JSON
* Détail des données d'un projet au format JSON
* Détail des données d'un projet ainsi que le nb de vues de ce projet au format JSON

exemple : l'URL `http://localhost:8084/lutece-test/rest/example/projects` doit renvoyer un fichier du type :

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

 `$git diff step4 step5`

## 6.6. Etape 6 : Gestion du cache

Dans cette partie nous allons sauvegarder la réponse du Web Service dans le cache.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/cache-management.html

 **Exercice :**

*  Ajoutez une mise en cache de chaque projet à chaque consultation de détail d'un projet.


 **Note :** après avoir suivi les instructions du wiki, activez votre cache en back office dans Système/Gestion des caches.

Tapez la commande suivante pour obtenir les modifications à apporter par rapport à l'étape précédente :

 `$git diff step5 step6`

# 7. Finalisation du plugin


## 7.1. Formatage des sources

Pour formater correctement les fichiers sources du plugin, utilisez la commande maven :

`mvn formatter:format`

Vérification des règles de nommage :

`mvn checkstyle:check`

## 7.2. Tests

Pour tester automatiquement le plugin (test unitaires), on peut utiliser la commande suivante:

`mvn lutece:exploded antrun:run test`

## 7.3. Packaging


Génération d'une distribution du plugin :

`mvn lutece:assembly`
