# plusminus.android
Android client for plusminus.me

Check web implementation on [plusminus.me][1]

## Warning (don't look the code until you read this)

I am experimenting with this one, trying to solve basic android problems with functional approach.

Saving state (to survive screen rotations) is handled by having 
AppDb object, which is persisted on activity onStop event.

Ui is updated when AppDb changes.

Business logic is implemented with event and effect Handlers.

Dispatchers listen for UI messages and update the AppDb store, delegating decisions to handlers. 

I am also trying to avoid using xml for ui by relying on [Anvil][2] library. 

[2]: https://github.com/anvil-ui/anvil
[1]: https://github.com/Liverm0r/plusminus.me
