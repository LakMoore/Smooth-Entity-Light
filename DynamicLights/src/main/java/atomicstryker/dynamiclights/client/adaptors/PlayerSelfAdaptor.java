package atomicstryker.dynamiclights.client.adaptors;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import atomicstryker.dynamiclights.client.Config;
import atomicstryker.dynamiclights.client.DynamicLights;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.registry.GameData;

/**
 * 
 * @author AtomicStryker
 *
 * Offers Dynamic Light functionality to the Player Entity itself.
 * Handheld Items and Armor can give off Light through this Module.
 * 
 * With version 1.1.3 and later you can also use FMLIntercomms to use this 
 * and have the player shine light. Like so:
 * 
 * FMLInterModComms.sendRuntimeMessage(sourceMod, "DynamicLights_thePlayer", "forceplayerlighton", "");
 * FMLInterModComms.sendRuntimeMessage(sourceMod, "DynamicLights_thePlayer", "forceplayerlightoff", "");
 * 
 * Note you have to track this yourself. Dynamic Lights will accept and obey, but not recover should you
 * get stuck in the on or off state inside your own code. It will not revert to off on its own.
 *
 */
public class PlayerSelfAdaptor extends BaseAdaptor
{
	EntityPlayer thePlayer;
	
    public PlayerSelfAdaptor(EntityPlayer entity) {
		super(entity);
		thePlayer = entity;
	}

    @Override
    public int getLightLevel()
    {
        if (thePlayer != null && thePlayer.isEntityAlive() && !DynamicLights.globalLightsOff && thePlayer.addedToChunk)
        {
            List<IMCMessage> messages = FMLInterModComms.fetchRuntimeMessages(this);
            if (messages.size() > 0)
            {
                // just get the last one
                IMCMessage imcMessage = messages.get(messages.size()-1);
                if (imcMessage.key.equalsIgnoreCase("forceplayerlighton"))
                {
                    if (!DynamicLights.fmlOverrideEnable)
                    {
                    		DynamicLights.fmlOverrideEnable = true;
		                	return 15;
                    }
                }
                else if (imcMessage.key.equalsIgnoreCase("forceplayerlightoff"))
                {
                    if (DynamicLights.fmlOverrideEnable)
                    {
                    		DynamicLights.fmlOverrideEnable = false;
                    		return 0;
                    }
                }
            }
            
            if (!DynamicLights.fmlOverrideEnable)
            {
                if (thePlayer.isBurning())
                {
                    return 15;
                }
                else
                {
                    //Get the light from the held item
                    ItemStack item = thePlayer.getCurrentEquippedItem();
                    int lightLevel = Config.itemsMap.getLightFromItemStack(item);
                                        
                    //if we are underwater and the source is extinguishable
                    boolean inWater = checkPlayerWater(thePlayer);
                    if (inWater
                    && item != null
                    && Config.notWaterProofItems.retrieveValue(GameData.getItemRegistry().getNameForObject(item.getItem()), item.getMetadata()) == 1)
                    {
                        lightLevel = 0;
                    }
                    
                    //go through the armor slots looking for brighter items
                    for (ItemStack armor : thePlayer.inventory.armorInventory)
                    {
                        if (armor != null && (!inWater || Config.notWaterProofItems.retrieveValue(GameData.getItemRegistry().getNameForObject(armor.getItem()), armor.getMetadata()) == 0))
                        {
                            lightLevel = DynamicLights.maxLight(lightLevel, Config.itemsMap.getLightFromItemStack(armor));
                        }
                    }
                    return lightLevel;
                }
            }
        }        
        return 0;
    }
    
    private boolean checkPlayerWater(EntityPlayer thePlayer)
    {
        if (thePlayer.isInWater())
        {
            int x = MathHelper.floor_double(thePlayer.posX + 0.5D);
            int y = MathHelper.floor_double(thePlayer.posY + thePlayer.getEyeHeight());
            int z = MathHelper.floor_double(thePlayer.posZ + 0.5D);
            return thePlayer.worldObj.getBlock(x, y, z).getMaterial() == Material.water;
        }
        return false;
    }
    
	@Override
	public void kill() {
		super.kill();
		thePlayer = null;
	}


}
