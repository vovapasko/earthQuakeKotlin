# nearby-earthquakes
[USGS Earthquake Hazards Program](https://earthquake.usgs.gov/) is an organization that analyzes earthquake threats around the world. They expose REST API outlining details of recent earthquakes happening around the world - location, magnitude, etc.

We would like to write a program that for a given city (we will provide lat/lon) will find out 10 most nearby earthquakes (earthquakes that happened in the closest proximity of that city).

## Getting list of earthquakes
Web services for fetching Earthquakes are located here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php  
We are interested in all earthquakes that happened during last 30 days: https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson

For each earthquake there is a lat/lon location of that earthquake. We would like the program to connect to this web service and pull the earthquake data.

## Calculating distance
We expect the program to correctly calculate curve distance between two lat/lon points.

## Program input
Program should accept two numbers on standard input: the latitude and longitude of a city. So for New York the program should be started with numbers:
```
40.730610  
-73.935242  
```
Source: https://www.latlong.net/place/new-york-city-ny-usa-1848.html

## Program output
As an output, the program lists **10** earthquakes that happened in the closest proximity to input point, in the order 
from the closest to the furthest. For each earthquake it prints the content of a `title` field followed by ` || ` and `distance` (rounded to full kilometers).
```
title || distance  
title || distance  
title || distance  
```


## Summary
The solution does the following:
* Read two float numbers from standard input that represent the lat/lon of a city
* Read a list of earthquakes that happened during last 30 days
* Calculate distance between given city and each of the earthquakes
* Print the 10 earthquakes with the shortest distance to the given city
* The output list should contain earthquake title and distance in kilometers
* If two earthquakes happened in exactly the same location (they have the same lat/lon) we only want one of them to be printed

## Example usage:
Example Input:
```
40.730610  
-73.935242  
```

Example Output:
```
M 1.3 - 2km SSE of Contoocook, New Hampshire || 331  
M 1.3 - 2km ENE of Belmont, Virginia || 354  
M 2.4 - 83km ESE of Nantucket, Massachusetts || 406  
M 1.3 - 13km ENE of Barre, Vermont || 410  
M 0.7 - 18km NW of Norfolk, New York || 476  
M 2.0 - 17km NW of Norfolk, New York || 476  
M 1.7 - 19km NNW of Beaupre, Canada || 758  
M 1.9 - 13km SW of La Malbaie, Canada || 814  
M 2.4 - 16km N of Lenoir, North Carolina || 840  
M 2.4 - 12km ESE of Carlisle, Kentucky || 896  
```

