#!/usr/bin/env python2.6
import csv
import json

with open('movies.dat','rb') as csv_file:
    for row in csv_file:
      tokens = row.split("::")
      categories = tokens[2].split("|")
      categories[-1] = categories[-1].replace("\n","")
      print '{ "create" : { "_index" : "movie", "_type" : "film", "_id" : "%s" } }' % tokens[0]
      print '{ "title" : "%s", "url":"http://us.imdb.com/M/title-exact?%s", "genre": %s }' % (tokens[1], tokens[1].replace(" ", "%20"), json.dumps(categories))


