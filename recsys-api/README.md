RecSys API
==========

This is the main component of the serving layer. The API consists in a Spring-backed RESTful web API that serves recommendations and items. 
To do so, it communicates with 3 different backends:
- ItemHistoryRepository that fetches the items recently consumed by the user
- ItemRepository that fetches the items (by id)
- RecommendationService that fetches recommended items given a list of other items' ids.

The application is done with Dependency Injection and all the providers can be replaced or extended. There are dummy classes for all the interfaces and there is an ElasticSearch implementation.

The API is also responsible to manage the list of recently consumed items. For that effect, it interacts with the ItemHistoryRepository to fetch and update the existing list.

Solr and HBase support is underway.

List of API Calls
=================


Build & Deploy
==============

Gradle and Maven will supported. The right dependencies are in Maven at moment.

