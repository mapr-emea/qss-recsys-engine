#!/usr/bin/env python
import re
import json
import sys

if len(sys.argv) != 2:
    sys.stderr.write("Usage: ./load_items.py <movie.data>")
    sys.exit(1)

with open(sys.argv[1],'rb') as csv_file:
    next(csv_file) # skip first line
    for row in csv_file:
      tokens = row.split(",")
      categories = tokens[2].split("|")
      categories[-1] = re.sub(r"\s+","", categories[-1])
      print '{ "create" : { "_index" : "movie", "_type" : "film", "_id" : "%s" } }' % tokens[0]
      print '{ "title" : "%s", "url":"http://us.imdb.com/M/title-exact?%s", "genre": %s }' % (tokens[1], tokens[1].replace(" ", "%20"), json.dumps(categories))


