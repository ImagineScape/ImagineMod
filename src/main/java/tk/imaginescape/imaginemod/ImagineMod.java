package tk.imaginescape.imaginemod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ImagineMod.MODID, version = ImagineMod.VERSION)
public class ImagineMod
{
    public static final String MODID = "imaginemod";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        PlayerEventHandler playerEventHandler = new PlayerEventHandler();
        FMLCommonHandler.instance().bus().register(playerEventHandler);
    }
}
