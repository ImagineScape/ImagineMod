package tk.imaginescape.imaginemod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.config.Configuration;
import tk.imaginescape.imaginemod.commands.CommandSmack;
import tk.imaginescape.imaginemod.events.PlayerEventHandler;

@Mod(modid = ImagineMod.MODID, version = ImagineMod.VERSION)
public class ImagineMod
{
    public static final String MODID = "imaginemod";
    public static final String VERSION = "1.0";
    public static int spawnX;
    public static int spawnY;
    public static int spawnZ;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        spawnX = config.getInt("X","OffworldRespawn",0,-10000000,10000000,"");
        spawnY = config.getInt("Y","OffworldRespawn",64,1,253,"");
        spawnZ = config.getInt("Z","OffworldRespawn",0,-10000000,10000000,"");
        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        PlayerEventHandler playerEventHandler = new PlayerEventHandler();
        FMLCommonHandler.instance().bus().register(playerEventHandler);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandSmack());
    }
}
