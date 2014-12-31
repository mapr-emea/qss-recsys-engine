Recommendation Engine
=====================

This is a sample recsys developed based on the co-occurence of items that are consumed by the same user. The (log) of co-occurence sets a similarity metric between items. Recommendations for a particular user are made by returning a list of items that are similar to the ones that user has consumed recently.

This template project can be extended and used for other recommendation approaches.

The project is composed of 3 main modules:
* recsys-dp – contains data preparation and data processing classes
* recsys-api – implementation of the serving layer with a Spring-based REST service
* recsys-ui – a Bootstrap+AngularJS web UI that interacts with the REST API

The whole system relies on external stores to persist item similarity and item definition. The backends used are based on search-engines and it currently supports both Apache Solr and ElasticSearch.

Modules
=======

% TODO



Run it locally
==============

First, let's start putting the data into MapR distributed filesystem. This way it will be accessible from all the nodes in the cluster and computation can leverage the data-locality.

cp -R data/ /mapr/<my.cluster.name>/user/mapr # feel free to choose wherever you want the data

