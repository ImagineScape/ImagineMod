package tk.imaginescape.imaginemod.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by sbarbour on 3/1/15.
 */
public class CommandSmack extends CommandBase {
    @Override
    public String getCommandName() {
        return "smack";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Deliver comeuppance to <player>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] params) {
        if (params.length > 0) {
            EntityPlayerMP target = getPlayer(sender, params[0]);
            target.getEntityWorld().createExplosion(target, target.posX, target.posY, target.posZ, 0, true);
            if (target.getHealth() > 4) {
                target.setHealth(4);
                target.performHurtAnimation();
            }
            if (target.getFoodStats().getFoodLevel() > 4) {
                target.getFoodStats().setFoodLevel(4);
                target.getFoodStats().setFoodSaturationLevel(4);
            }
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(target.getDisplayName() + " has been struck by the heavy hand of justice!"));
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }


}
