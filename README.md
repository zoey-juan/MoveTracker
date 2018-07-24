# Introduction

Notice!!
1. Add your `google-services.json` under `/app`
2. You might have issue about can't get GPS location via mock location on Emulator.
   Change Location Mode to High Accuracy in Settings should work.

Keywords in this implementation:
1. ViewModel/LiveData/Room
2. Kotlin/rxjava2
3. Kotlin
4. unit test - junit/mockito/robolectric
5. Others - retrofit2/picasso


## A. Architecture

Refer to the link if you can see the pic: https://drive.google.com/open?id=1M-OCdhPYnqwUkWvNlwUHRMYzI8snJn76
![GitHub][github]

[github]: https://drive.google.com/open?id=1M-OCdhPYnqwUkWvNlwUHRMYzI8snJn76 "Graph"

## B. Testing - junit/mockito/robolectric

1. The data, domain, presentation layer are defined clearly. so we can get rid of complex
   dependencies between layers.
   You can also see this implementation are easy to test each layer separately.

2. I wrote the TrackAdapterTest as example to show how we can test the class with Mock and Robolectric.

3. For MainActivity and , we can mock it easily after replace the variable which in the MainActivity.inject()
   to Dagger Injection.

## C. Others - retrofit2/picasso, simple memory cache for reducing request

1. use 3rd-party library here to handle http request and image cache loading easily.

## D. Known Issue For More Improvement

1. Replaced variables in MainActivity and LocationTrackingService by Dagger Injection for testing easier. 

2. Structure for keeping Locations is not efficient. Should be handled better

3. LocationTrackingService should change the variables to inject way  for easy testing.

4. The other known issues were written in the code as comment.