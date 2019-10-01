# CityVenue
JSON request for Foursquare Places API to obtain venues for five selected cities. Venues are populated from the selected location 
(city, state) with the name, thumbnail, address, and the category of the venue. User can bookmark and remove bookmark for these venues. 
Bookmarks are persistently stored in real-time, and is retrievable in view if data exists.<br>
**NOTE**: Sample data is used due to Foursquare API call limit for free account and in case call quota is exceeded.

### Points that must be considered:

1. ***Personal account for Foursquare limits the number of API calls per day*** <br>
Due to this restriction, own sample data were implemented that will load (3) venues for the city selected and (50) sample photos for the 
grid photos. This is commented as *"Dummy testing of data and actions if API isn't fetching"*, and can be removed so only Foursquare API 
can be used when possible.<br><br> 
The sample data won't work perfectly since Foursquare Places API has unique venue Id for each venue, 
and the sample data uses same venues with duplicate venue Id's for each city. This will store the bookmark as one merged bookmark for 
different sample venues since the the venue id is used as the key for the hashmap.<br><br>
Nevertheless, using just the Foursquare API should work fine to store the venues in the bookmark.

2. ***Foursquare API for personal accounts limit 1 photo per venue*** <br>
This makes it not possible to load 50 photos (the number of the photos call in this code) for each venue to the grid for screen 3, but 
just one. Therefore, (50) sample photos along with 1 Foursquare photo (if API quota didn't exceed) for the grid is used for screen 3. 
Again, the Foursquare API to fetch 50 photos should work fine if allowed.

3. ***Photos may not exist for venues*** <br>
A "No Photo" image is used by default in thumbnail in case of non existant photo for venue

4. ***Categories may not exist for venues*** <br>
"Empty category" is placed in the description for the venue if category does not exist in JSON

5. ***Placed 3 "ID & Secret for Foursquare API" and the first one is uncommented and being used for the project. User's bookmark can be 
retrieved from the top right corner screen in screen 1***
