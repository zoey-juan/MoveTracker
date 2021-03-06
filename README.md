# Introduction

Notice!!
1. Add your `google-services.json` under `/app`
2. You might have issue about can't get GPS location via mock location on Emulator.
   Change Location Mode to High Accuracy in Settings should work.

Keywords in this implementation:
1. ViewModel/LiveData/Room
2. Kotlin/rxjava2
3. Unit test - junit/mockito/robolectric
4. Others 3rd-party API - retrofit2/picasso/renaudcerrato:static-maps-api


## A. Architecture

Refer to the link if you can see the pic: https://drive.google.com/open?id=1M-OCdhPYnqwUkWvNlwUHRMYzI8snJn76
![GitHub][github]

[github]: https://github.com/zoey-juan/MoveTracker/blob/master/design_graph.png "Graph"

## B. Testing - junit/mockito/robolectric

1. The data, domain, presentation layer are defined clearly. so we can get rid of complex
   dependencies between layers.
   You can also see this implementation are easy to test each layer separately.

2. I wrote the TrackAdapterTest as example to show how we can test the class with Mock and Robolectric.

3. For MainActivity and , we can mock it easily after replace the variable which in the MainActivity.inject()
   to Dagger Injection.

## C. Others - retrofit2/picasso/renaudcerrato:static-maps-api

1. Use 3rd-party library to handle http request, image cache loading and build static-maps URL easily.

## D. Known Issue For More Improvement

1. Replaced variables in MainActivity and LocationTrackingService by Dagger Injection for testing easier. 

2. Structure for keeping Locations is not efficient. Should be handled better

3. LocationTrackingService should change the variables to inject way  for easy testing.

4. The other known issues were written in the code as comment.
