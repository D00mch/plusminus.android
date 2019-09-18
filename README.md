# plusminus.android
Android client for plusminus.me

Check web implementation on [plusminus.me][1]

## Warning (don't look the code until you read this)

I am experimenting with this one, trying to solve basic android problems with different kinda functional approach.

Saving state (to survive screen rotations or destruction due to low memory) is handled by having AppDb object, which is persisted on stop event (single activity onStop).

Views and Screens (Presenters) are represented as functions.

Ui is updated when AppDb changes.

I am also trying to avoid using xml for ui by relying on [Anvil][2] library. 

[2]: https://github.com/anvil-ui/anvil
[1]: https://github.com/Liverm0r/plusminus.me
