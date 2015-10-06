#!/bin/sh
export MAHOUT_HOME=/opt/mapr/mahout/mahout-0.11.0
RECSYS_MPPU=1000
RECSYS_MAXSIMPERITEM=100
$MAHOUT_HOME/bin/mahout itemsimilarity  -i /user/mapr/ratings.csv -o /user/mapr/recsys_output -s SIMILARITY_LOGLIKELIHOOD --tempDir /user/mapr/temp_`date '+%s'` -mppu $RECSYS_MPPU -m $RECSYS_MAXSIMPERITEM -b true