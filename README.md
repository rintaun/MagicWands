# Magic Wands
*Command Block in Your Hand*

## Description
This mod adds a new item: the Magic Wand. The Magic Wand is, for all intents
and purposes, a Command Block that you can carry around and activate by
right-clicking. Which is super awesome, amirite!?!?!???!1

Currently, the only way to get a working Magic Wand is via the `/give`
command. Example:

```
/give @p magicwands:item_wand 1 0 {Command:"say hi"}
```

Use the above command and you will have an amazing Magic Wand which says "hi"
when you right click with it -- usefulness truly unparalleled by any other
mod. Of course, it can also do anything that any Command Block can do, as far
as I'm aware.

By default, the effect will be centered on the wielding player. However, by
modifying the `UseTargetPos` tag, the position of the target will be used
instead. Example:

```
/give @p magicwands:item_wand 1 0 {Command:"summon LightningBolt",UseTargetPos:1b}
```

Perhaps just a tiny little bit more useful than the previous example, we now
have a LIGHTNING WAND to DESTROY YOUR ENEMIES. I haven't tested this (I will
soon though!) but you could probably use `"kill @e[c=1]"` as the command too.
^_^

Unfortunately the chat input length is pretty limiting, so as a workaround,
for the moment you could use a Command Block to give yourself a wand.

You can also create a custom recipes (e.g. with
[MineTweaker3](http://minecraft.curseforge.com/projects/minetweaker3)) to give
you a particular wand.

One goal of this mod is to make wands safe to give to normal players, so the
command will only ever be editable by an OP by default. As such, normal
players remain limited to the pre-programmed command.

## Future Plans
* Hijack the Command Block GUI so that you can actually edit the commands
  easily... =/
* Make wands able to be limited to a certain number of uses.
* Add a command (`/projectile`?) which summons a projectile and flings it in
  the direction the specified entity is facing (or a direction relative to
  that direction). This will make fireballs super easy!
* Create a projectile that executes a command upon impact. Because that is
  awesome.

## Contributing
I look forward to your thoughts and comments. Any bug reports, feature ideas,
etc. are greatly appreciated. And please do not hesitate to send pull
requests!
