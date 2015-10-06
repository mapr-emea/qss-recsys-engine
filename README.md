Recommendation Engine
=====================

This is a sample recsys developed based on the co-occurence of items that are consumed by the same user. The (log) of co-occurence sets a similarity metric between items. Recommendations for a particular user are made by returning a list of items that are similar to the ones that user has consumed recently.

This template project can be extended and used for other recommendation approaches.

The project is composed of 3 main modules:
* scripts – scripts for preparing data to load into ElasticSearch (scripts made for MovieLens dataset but can be easily adapted)
* recsys-api – implementation of the serving layer with a Spring-based REST service
* recsys-ui – a Bootstrap+AngularJS web UI that interacts with the REST API

The whole system relies on external stores to persist item similarity and item definition. The backends used are based on search-engines and it currently supports both Apache Solr and ElasticSearch.

Dependencies
=========

The recommendation engine relies on ElasticSearch. Please make sure you install it on the current node or any other.

The ElasticSearch will contain both item definitions and well as the indicators of similar items, according to the machine learning algorithm.

The application server will use the settings defined on `recsys-api/src/main/resources/application.properties` to communicate with the correct elasticsearch index.

It also relies on Apache Mahout to run the machine learning operations on the data.


Deployment
=========

To deploy the whole solution in a CentOS based:

1. Install ElasticSearch – [documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-repositories.html)

1a. Start ElasticSearch with `service elasticsearch start`.

1b. Check if ES is running, by running `lsof -i :9300` as root.

2. Install Apache Mahout – `yum install -y mapr-mahout`

3. Clone or extract this repository into one of the nodes and set it as your current working directory `cd rec-engine`.

4. Edit search engine settings for recsys application server in `recsys-api/src/main/resources/application.properties`. By default, it assumes ElasticSearch is running on localhost. Please adapt if required.

5. Build UI

To build the UI, bower will be used. Bower is a package manager for managing web frameworks, libraries, utilities, assets etc. 

To install bower on CentOS, npm and nodejs will have to be installed in the first place. The following commands will install npm and bower:

`yum install npm nodejs`

Use npm package manager to download bower:
`npm install bower -g`

Check bower version:
`bower -v`


Now that bower is installed, let's build the UI using bower now:

`cd recsys-ui && bower install; cd ..`


6. Build the JAR

To build the application server JAR, first you need to create a link of the static files (UI) to a special directory so they can be served by the application server as static resources (HTML, CSS and JS).

First, make sure you have Java build tools installed: Java JDK and Maven build tool.

Download the YUM repository file:

`wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo`

Install it using yum command:

`yum install apache-maven`


Run the following command while in project root:

`ln -s ../../../../recsys-ui/app recsys-api/src/main/resources/static`

This will make sure the UI contents are available as static files in the application server (recsys-api). The recsys-api will serve both static files and API calls, through HTTP. Static files are mainly Javascript, CSS and HTML content that compose the client-side application and they will executed in the browser. The client application will perform REST requests to the API, that will be served by the webserver application.

To build the application server:

`cd recsys-api && mvn clean install`


7. Run the application server 

The JAR is under `recsys-api/target/recsys-api-1.0-SNAPSHOT.jar` and has a main class that already boots up a Tomcat based server. Run it as follows:

`java -jar recsys-api/target/recsys-api-1.0-SNAPSHOT.jar`

The web UI will be accessible in port 8080.


Bootstrapping
==========

The next steps show how to download a sample dataset, calculate recommendations and populate the backend system to generate recommendations right away.

The steps 8 and 10 are optional if you have the DFS mounted through NFS – that way you can work directly on the files.


1. Create ElasticSearch index 

`curl -XPUT 'localhost:9200/movie'`

The command assumes ES is running locally.


2. Download and extract the MovieLens dataset

`wget http://files.grouplens.org/datasets/movielens/ml-latest.zip && unzip ml-latest.zip`


3. Generate the ES JSON payload for item definitions

`./scripts/movielens/load_items.py ml-latest/movies.csv > index.json`

4. Upload the item definitons into ES index

`curl -s -XPOST localhost:9200/_bulk --data-binary @index.json;`

5. Import ratings data to the DFS. This will be the input for Machine Learning algorithms run by Mahout

`hadoop dfs -copyFromLocal ml-latest/ratings.csv /user/mapr/`

6. Edit the script `scripts/calculate_recommendations.sh` to make sure `MAHOUT_HOME` is well defined and run the script.

`./scripts/calc_recommendations.sh`

7. Copy the results to local filesystem

hadoop dfs -copyToLocal /user/mapr/recsys_output/ output

8. Transform recommendations into ES JSON format

`./scripts/movielens/load_results.py output/part-r-00000 > update.json`


9. Upload the recommendations into ES index

`curl -s -XPOST localhost:9200/_bulk --data-binary @update.json;`

The data will be then fetched from ES by the application server.

The recommendations can be recalculated and updated on ES multiple times a day.






