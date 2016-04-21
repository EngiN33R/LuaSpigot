# LuaSpigot
A Lua scripting engine for Spigot making it possible to customise Minecraft gameplay without diving into Java code.

## Installation and usage
LuaSpigot is fully Maven-integrated. If Maven installation is impossible, correct functioning of the plugin depends on having LuaJ in the classpath.

LuaSpigot is used like any other Spigot plugin - simply drop it into the `plugins` directory and let it work its magic from there.

To use LuaSpigot in your own plugin, you will first need to initialise it (in `onEnable` or similar):

    LuaSpigot luaSpigot = new LuaSpigot(this);

After initialisation, enable any libraries you wish to expose to scripts:

    luaSpigot.getLibraryRegistry().register(new ChatLibrary());
    luaSpigot.getLibraryRegistry().register(new PlayerLibrary());

Once LuaSpigot is set up, you can create a state for scripts to use:

    State state = luaSpigot.newState();

The state object can then be used to execute Lua code from a string or a file:

    state.execute("print('Hello world!')");
    state.load("scripts/test.lua"); // Relative to server JAR