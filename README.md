# FancyFirework

![bStats Servers](https://img.shields.io/bstats/servers/13728)
![bStats Servers](https://img.shields.io/bstats/players/13728)
![Spiget Downloads](https://img.shields.io/spiget/downloads/98809)
![Spiget Stars](https://img.shields.io/spiget/stars/98809)

A simple plugin that adds custom fireworks to your server.  
Implements support for vote rewards using NuVotifier and loot table customization to acquire firework items.  

# ! WARNING! This plugin requires Paper!  


# Default Fireworks  
There are a couple default fireworks available that can be used out of the box.  
Batteries,  
Fountains,  
and a Bang Snap  
The batteries and fountains come in different colors (blue, green, orange, pink, purple, red, yellow) and other varieties like pride and rainbow.  
  
![Example](https://fantacs.de/Urus198.png)  
(An example of three types of fountains)  
  
  
# Acquire Fireworks  
There are multiple ways players can acquire fireworks.  
  
## Spawn Random Fireworks  
One way would be to use the firework spawn feature of this plugin.  
This will spawn fireworks on players. Once a firework explodes, it drops a random item from all available firework items.  
The fireworks will only spawn if the player has no blocks above, blocking the sky.  
This spawn feature can be enabled and configured in the config.  
```yml
enabled: false  # Toggle the spawn feature  
spawnrate: 100  # The spawn rate of the fireworks. The higher the number, the less likely it will spawn.  
```
  
## Vote Rewards  
Another way would be to use NuVotifier, which will draw a random custom firework as a vote reward.  
Can be toggled in the config.  
```yml 
votereward: false # Toggle vote reward feature  
```
  
## Loottables  
You can also enable them inside all chest or chest minecart loot tables.  
This feature can be configured in the config.  
```yml
loottable:  
  enabled: false   # Toggle the loot table feature  
  chance: 0.5      # The chance of the item spawning. (e.g. 0.5 = 50%)
``` 
  
# Commands  
Main command: /fancyfirework or alias /ff  
  
## /ff enable or /ff disable  
**Permission: fancyfirework.admin**  
Enables or Disables the spawning of fireworks on players.  
See Spawn Random Fireworks above.  
  
## /ff setspawnrate  
**Permission: fancyfirework.admin**    
Sets the spawn rate of the random firework spawning.  
The higher the number, the less likely they will spawn.  
See Spawn Random Fireworks above.  
  
## /ff give [key] <amount>  
**Permission: fancyfirework.mod**    
Gives you the specified firework item of the specified quantity.  
  
(This plugin was created in a matter of two days! Documentation and possible future features will be added later on!)  
![BSTATS](https://bstats.org/signatures/bukkit/FancyFirework.svg)
