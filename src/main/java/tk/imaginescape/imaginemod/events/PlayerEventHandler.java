package tk.imaginescape.imaginemod.events;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import tk.imaginescape.imaginemod.ImagineMod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sbarbour on 3/1/15.
 */
public class PlayerEventHandler {

    List<Relocation> relocateQueue = new ArrayList<Relocation>();

    @SubscribeEvent
    public void doPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        FMLLog.info("ImagineMod: " + e.player.getDisplayName() + " logged in.");
        if (e.player.dimension != 0) {
            ChunkCoordinates spawnPoint = new ChunkCoordinates(ImagineMod.spawnX, ImagineMod.spawnY, ImagineMod.spawnZ);
            relocateQueue.add(new Relocation(e.player, 0, spawnPoint));
        }
    }

    @SubscribeEvent
    public void doMovement(TickEvent.PlayerTickEvent e) {
        Iterator<Relocation> it = relocateQueue.iterator();
        while (it.hasNext()) {
            Relocation entry = it.next();
            if (entry.getPlayer().equals(e.player) && e.player instanceof EntityPlayerMP){
                FMLLog.info("Moving player: " + e.player.getDisplayName() + " to dim: " + entry.getDimension() + " @ " + entry.getCoords().toString());
                WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(entry.getDimension());
                IChunkProvider ichunkprovider = worldserver.getChunkProvider();
                ichunkprovider.loadChunk(entry.getCoords().posX - 3 >> 4, entry.getCoords().posZ - 3 >> 4);
                ichunkprovider.loadChunk(entry.getCoords().posX + 3 >> 4, entry.getCoords().posZ - 3 >> 4);
                ichunkprovider.loadChunk(entry.getCoords().posX - 3 >> 4, entry.getCoords().posZ + 3 >> 4);
                ichunkprovider.loadChunk(entry.getCoords().posX + 3 >> 4, entry.getCoords().posZ + 3 >> 4);

                for (int y = entry.getCoords().posY; y <= worldserver.getHeight(); y++) {
                    Material material = worldserver.getBlock(entry.getCoords().posX, y, entry.getCoords().posZ).getMaterial();
                    Material material1 = worldserver.getBlock(entry.getCoords().posX, y + 1, entry.getCoords().posZ).getMaterial();
                    boolean flag1 = !material.isSolid() && !material.isLiquid();
                    boolean flag2 = !material1.isSolid() && !material1.isLiquid();
                    if (flag1 && flag2) {
                        entry.getCoords().posY = y;
                        break;
                    }
                }
                ((EntityPlayerMP) e.player).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) e.player, entry.getDimension(), new Teleporter(worldserver));
                e.player.setPositionAndUpdate(entry.getCoords().posX, entry.getCoords().posY, entry.getCoords().posZ);
                it.remove();
             }
         }
    }

}

class Relocation {
    private EntityPlayer player;
    private int dimension;
    private ChunkCoordinates coords;

    public Relocation(EntityPlayer player, int dimension, ChunkCoordinates coords) {
        this.player = player;
        this.dimension = dimension;
        this.coords = coords;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public int getDimension() {
        return dimension;
    }

    public ChunkCoordinates getCoords() {
        return coords;
    }

}
