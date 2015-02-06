#!/bin/bash

# Drop tables
mysql CS144 < drop.sql

# Create tables
mysql CS144 < create.sql

# Parse the data
ant
ant run-all

# Populate database
mysql CS144 < load.sql

# Remove temp files
#rm *.dat