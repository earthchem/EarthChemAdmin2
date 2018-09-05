# EarthChemAdmin2
Admin tools for Earthchem database person, organization, affiliation, citation, author_list, citation_external_identifier, person_external_identifier and ec_status_info.

# Requirements
* Java SE 8 (jdk1.8.0_66), language, http://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html
* Wildfly-10.1.0.Final, server, http://wildfly.org/downloads/
* PostgreSQL including postgresql-9.2-1004.jdbc4.jar, database with ODM2 schema, https://www.postgresql.org/ 
* Eclipse 4.5.0, an integrated development environment (IDE), https://eclipse.org/ide/
* PostgreSQL including postgresql-9.2-1004.jdbc4.jar, database with ODM2 schema, https://www.postgresql.org/ 
* Apache-maven-3.5.0, a software project management and comprehension tool based on the concept of a project object model (POM). https://maven.apache.org/download.cgi
* JSF (javax.faces-api-2.2.jar), a Java specification for building component-based user interfaces for web applications, https://mvnrepository.com/artifact/javax.faces/javax.faces-api/2.2
* PrimeFaces (primefaces-6.0.jar), a comprehensive set of 100+ JSF UI components compatible with HTML5, https://www.primefaces.org/downloads/

# Installation
* Install Java at C:\Program Files\Java\jdk1.8.0_66
* Install Eclipse at C:\eclipse, right click New at Eclipse, click Maven Project to create the project.
* Add javax.faces-api-2.2.jar and primefaces-6.0.jar to WEB-INF/lib
* Install apache-maven-3.5.0 at C:\apache-maven-3.5.0, add dependencies to pow.xml
* Install Wildfly-10.1.0.Final at C:\wildfly-10.1.0.Final
  1. Add postgresql-9.2-1004.jdbc4.jar to C:\wildfly-10.1.0.Final\modules\system\layers\base\org\postgresql\main
  2. Edit C:\wildfly-10.1.0.Final\standalone\configuration\standalone.xml for database sources
  
 # Deployment
 * Setup Wildfly server in Eclipse and start the server
 * Right-click project name in Eclipse --> run as --> Maven build

# What's here:

## .settings
This contains – or at least should contain – vital information needed to successfully build your project inside Eclipse, such as the character encoding used for source code, Java compiler setthttps://github.com/iedadata/EarthChemAdmin2ings, and much more.

## WebContent
This folder is the mandatory location of all Web resources.

## WebContent/META-INF/jboss-deployment-structure.xml
This is a JBoss specific deployment descriptor that can be used to control class loading in a fine grained manner.
This folder is not been used.

## WebContent/WEB-INF
This contains lib folder and web.xml.

## WebContent/WEB-INF/lib
This contains jar files, which contain libraries required for this project.

## EarthChemAdmin2/WebContent/faces
This contains all JSF files.

## EarthChemAdmin2/WebContent/faces/EarthChemAdmin.xhtml
Main JSF page.

## EarthChemAdmin2/WebContent/faces/authorList.xhtml
JSF page for author_list.

## EarthChemAdmin2/WebContent/faces/citationEdit.xhtml
JSF page for editing citation.

## EarthChemAdmin2/WebContent/faces/citationView.xhtml
JSF Page for citation view.

## EarthChemAdmin2/WebContent/faces/citationsByStatus.xhtml
JSF Page for citation list.

## EarthChemAdmin2/WebContent/faces/empty.xhtml
JSF Page for empty page used for no table selected.

## EarthChemAdmin2/WebContent/faces/organizationDesc.xhtml
JSF Page (not used).

## EarthChemAdmin2/WebContent/faces/organizationEdit.xhtml
JSF Page for editing organization.

## EarthChemAdmin2/WebContent/faces/organizationNew.xhtml
JSF Page for adding new organization.

## EarthChemAdmin2/WebContent/faces/organizationView.xhtml
JSF Page for organization view.

## EarthChemAdmin2/WebContent/faces/personDesc.xhtml
JSF Page (not used).

## EarthChemAdmin2/WebContent/faces/personEdit.xhtml
JSF Page for editing person.

## EarthChemAdmin2/WebContent/faces/personNew.xhtml
JSF Page for updating person.

## EarthChemAdmin2/WebContent/faces/personView.xhtml
JSF Page for person view.

## EarthChemAdmin2/WebContent/error.xhtml
Error page.

## EarthChemAdmin2/WebContent/expired.xhtml
Session expired page

## EarthChemAdmin2/WebContent/login.xhtml
Login page.

## EarthChemAdmin2/WebContent/notFound.xhtml
URL not found page.

## EarthChemAdmin2/WebContent/serverError.xhtml
Server error page.

## src/org/earthChem
The folder contains all Java files.

## EarthChemAdmin2/src/org/earthChem/db
The folder (Data Access Layer) contains Java files accessing to database.

## EarthChemAdmin2/src/org/earthChem/db/CitationDB.java
This file is used for selecting or updating citation and its associated tables.

## EarthChemAdmin2/src/org/earthChem/db/CitationList.java
This file is used for getting citation list data for citationsByStatus.xhtml.

## EarthChemAdmin2/src/org/earthChem/db/DBUtil.java
This is database utility file used for making database connection, for retrieving data from database, and for saving data to database by JDBC.  

## EarthChemAdmin2/src/org/earthChem/db/OrganizationDB.java
This file is used for selecting or updating Organization table.

## EarthChemAdmin2/src/org/earthChem/db/PersonDB.java
This file is used for selecting or updating Person and its associated tables.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm
This folder contains Java bean files.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/Affiliation.java
Java bean for alliliation table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/AuthorList.java
Java bean for Author_List table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/Citation.java
Java bean for Citation table

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/CitationExternalIdentifier.java
Java bean for Citation_External_Identifier table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/EcStatusInfo.java
Java an for Ec_Status_Info table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/Organization.java
Java bean for Organization table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/Person.java
Java bean for Person table.

## EarthChemAdmin2/src/org/earthChem/db/postgresql/hbm/PersonExternalIdentifier.java
Java bean for Person_External_Identifier.java table.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf
The folder contains all presentation layer files. 

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/CitationBean.java
JSF managed bean for citation JSF files.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/HtmlOptions.java
This file provides re-used drop down lists for JSF files.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/LogoutBean.java
JSF managed bean for logout.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/OrganizationBean.java
JSF managed bean for Organization JSF files.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/PersonBean.java
JSF managed bean for viewing and updating person JSF files.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/PersonNewBean.java
JSF managed bean for adding new person.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/SearchBean.java
JSF managed bean for main search JSF file.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme
This folder contains files used for Primefaces AutoComplete.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/CommentConverter.java
The AutoComplete conver file for citation internal comment.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/CommentService.java
The file provides citation internal comment data for AutoComplete.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/OrganizationConverter.java
The AutoComplete conver file for organization names.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/OrganizationService.java
The file provides organization data for AutoComplete.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/Theme.java
Java bean for element of AutoComplete list.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/ThemeConverter.java
The AutoComplete conver file for person names.

## EarthChemAdmin2/src/org/earthChem/presentation/jsf/theme/ThemeService.java
The file provides person data for AutoComplete.

## EarthChemAdmin2/src/org/earthChem/rest
The folder contains files used for restful web services.

## EarthChemAdmin2/src/org/earthChem/rest/CitationRest.java
The web service Java file for doi loop-up.

## EarthChemAdmin2/src/org/earthChem/rest/InvalidDoiException.java
Java exception file used in CitationRest.java.

## .gitignore
A file which Git has been explicitly told to ignore. This is where secret information (e.g. DB usernames and logins) used by a local installation should be kept. Double check that your 'secrets' files are not being synced to the online Github repo.

## .classpath
The classpath files contains src and target entries that correspond with folders in the project.

## .project
This is the Eclipse project description file.

## pom.xml
A Project Object Model is the fundamental unit of work in Maven.

