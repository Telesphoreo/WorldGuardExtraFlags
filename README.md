# WorldGuard ExtraFlags (WGEF)

WGEF is a Paper plugin extension that provides extra flags for [WorldGuard](https://github.com/EngineHub/WorldGuard), including support for FastAsyncWorldEdit (FAWE).

## About
WorldGuard allows protecting areas of land by the creation of regions which then can be customized further by applying special flags. WorldGuard provides an API that 3th party plugins can use to provide their own flags.

This plugin adds extra flags to allow customizing regions even further.

WorldGuard ExtraFlags is extension to WorldGuard that adds 26 new flags listed below!

- ``teleport-on-entry`` & ``teleport-on-exit`` | Teleports the player to given location when player enters/exists the region 
- ``command-on-entry`` & ``command-on-exit`` | Executes a command when player enters/exists the region (Use %username% for player username placeholder)!
- ``console-command-on-entry`` & ``console-command-on-exit`` | Executes a command as console when player enters/exists the region (Use %username% for player username placeholder)!
- ``walk-speed`` & ``fly-speed`` | Sets the walking speed inside the region 
- ``keep-inventory`` | Does the player keep their inventory on death inside the region 
- ``keep-exp`` | Does the player keep their exp on death inside the region 
- ``chat-prefix`` | Chat prefix shown when inside the region 
- ``chat-suffix`` | Chat suffix shown when inside the region 
- ``godmode`` | Does the player the damage inside the region 
- ``blocked-effects`` | Block effects inside the region 
- ``respawn-location`` | Sets the players respawn location when inside the region
- **``worldedit``** | Is WorldEdit permitted inside the region 
- ``give-effects`` | Gives effects while inside the region and restores old effects on region leave with correct time left 
- ``fly`` | Whatever flying is enabled or disabled when entering the region 
- ``play-sounds`` | Allows you to play sounds once or on repeat. When running 1.9 server or above the sound will stop playing once the player leaves the region 
- ``frostwalker`` | Is frostwalker permitted inside the region 
- ``nether-portals`` | Is creation of nether portals permitted inside the region 
- ``glide`` | Is flying with Elytra allowed inside the region. Can also be used to give the player glide effect without wearing one 
- ``chunk-unload`` | Is chunk unloading permitted inside the region 
- ``item-durability`` | Is item durability allowed inside the region 
- ``join-location`` | Teleports the player to given location when logging in to the region

## How to use?
Simply use the WorldGuard region flag command. All of the flags can be interacted that way, just like any other flag.

### WorldEdit / FAWE 

``worldedit`` flag is a special flag that allows you to use WorldEdit inside the region. This is useful if you want to allow players to use WorldEdit inside the region without giving them full permissions to use it everywhere.

There are also some permissions which can help to identify issues with that:
``wgef.worldedit.debug`` - Send Debug Messages to console on allow/deny
``wgef.worldedit.disable_notify`` - Disable the player notification on deny
``wgef.worldedit.bypass`` - bypass the flag completly

## FAQ

###  Flag bypass behavior:

The following applies to versions of 4.2.0 and above. By default, administrators bypass most of the flags to allow region management. This can be disabled by using WorldGuard's command, /rg bypass.

You can also change that administrators do not have bypass enabled by default with the WorldGuard's configuration option disable-bypass-by-default.

### Flag is not working:

Make sure you understand the flag bypass behavior and that you have use-player-move-event set to true in WorldGuard's config.

### A word about giving out permissions with command-on flags.
Unless you want to permanently give out permissions to players upon entering/exiting a region, you should NEVER use the command-on-flag's to give out temporary permissions. This comes down to security, reliability and performance impact.

Instead, you can use permission context (https://luckperms.net/wiki/Context) that determines which permissions are allowed inside the region. This also requires an extension for LuckPerms which can be acquired from here: https://github.com/LuckPerms/ExtraContexts

### Can I get the player's username when executing a command?
Yes, you can use the %username% placeholder.

### Can I use multiple commands in the command-on flags?
Yes, use comma to separate them.

### What if my command contains commas?
Wrap the command in quotation marks and it will be seen as one whole command. As for example this can be encountered when using the title command, so the correct way to define it would be:

/rg flag region-name console-command-on-entry "title %username% title {"text":"Hey!","color":"green"}"

### Server crashes when using teleport commands in command-on-entry or in command-on-exit:
Using teleport commands is not supported scenario and there is a special flags for them; teleport-on-entry and teleport-on-exit. These flags are more careful when teleporting the player around and tries to prevent any circular teleport loops.

### A word about custom commands
Custom commands do not work as they are not "real" commands. Using plugins like Skript where you can define your own commands are seen as "fake" commands and their existence is unknown to other plugins.

The developers of said plugins should provide alternative way to execute these commands to allow compatibility.

### When using keep-inventory flag my inventory gets duplicated:
This is not the plugins fault and some other plugin is conflicting with the flag.

### What are the values for play-sounds?
These values depend on your Minecraft server version. Any value that works with /playsound works as value here.

### What are the values for walk-speed?
The flag uses same values as Minecraft does. The default value being 0.2.

### Respawn-location flag is not working:
Make sure you have read the section above about troubleshooting.

Some other plugins are known to cause problems with respawn-location flag by choosing some other location to respawn the player instead of the location provided by the flag.

If you are using Essentials you can fix this by changing the respawn-listener-priority to lowest or low in the Essentials config.

### Not working on Thermos:
https://github.com/CyberdyneCC/Thermos/issues/498#issuecomment-247083549

## More
Downloads are provided on this repository's [Releases](../../releases) page.
