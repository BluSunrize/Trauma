![Logo](https://raw.githubusercontent.com/BluSunrize/Trauma/master/src/main/resources/assets/trauma/logo.png)
================

Trauma is a Minecraft Mod about critical injuries to limbs.
It makes various types of damages inflict separately tracked injuries, like a broken leg or a concussed head. These then have additional impact on the game, applying various debuffs to the player.
Healing these injuries takes quite a bit of time, so it's probably best just not to get hurt.



### Adding Compatability
Your easiest way to add compat with Trauma to your mods is to grab it from maven.
To do so, add this to your buildscript:
```
repositories {
    maven {
        name "Trauma Maven"
        url "http://maven.blamejared.com"
    }
}
```
As well as:
```
dependencies {
    deobfCompile "blusunrize:Trauma:[VERSION]"
}
```
Make sure to replace [VERSION] with the version of the mod you want to work with.

### Credits
Trauma's logo uses a modified image from game-icons.net, licensed under CC BY 3.0
