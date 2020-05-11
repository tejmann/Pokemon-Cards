# Pokemon-Cards
Pokemon Cards is an easy to play trading-card game for pokemon aficionados who can compete against other players online to collect all the pokemon cards.

In this app you’ll find : 
- The [Model-View-ViewModel (MVVM) Pattern](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b).
- Kotlin [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for background operations.
- [Koin](https://insert-koin.io/) used for Dependency Injection.
- A single activity architecture.
- Reactive UIs using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).
- A custom **RecyclerView Adapter** to work with any viewholder. 
- **Firebase** to store users and their data.
- **Cloud Functions** for Firebase is used for backend trigger operations. 
- Animation using the new [Motion Layout](https://developer.android.com/training/constraint-layout/motionlayout)
- Uses [detekt](https://github.com/detekt/detekt) to avoid code smells.

The pokemon related data is derived from [Pokeapi](https://pokeapi.co/).

![login](https://github.com/tejmann/Pokemon-Cards/blob/master/gif/login-new.gif)    ![collection](https://github.com/tejmann/Pokemon-Cards/blob/master/gif/collection-new.gif)    ![gameplay](https://github.com/tejmann/Pokemon-Cards/blob/master/gif/gameplay-new.gif)

## How to get it running and gameplay:
Clone the GitHub repository and build the project using Android Studio. This project is for Android API 21+. 
Once the app is installed, select play online and signup using your email.

Next, you reach the waiting lobby where you can either create your game-room or join someone else’s. If you create your own room, you have to wait for some other player to join it.

Once another player has joined the room, the game starts. Both players get 20 random cards and the winner is decided after all 20 cards are played. The winners get a new card added to their collection.

## Features to come:
The following are in the works or currently planned:
- Playing against the computer.
- Setting up FCM push notifications to improve the room joining flow. 
- Chat messaging inside the game.
- Adding unit tests.

