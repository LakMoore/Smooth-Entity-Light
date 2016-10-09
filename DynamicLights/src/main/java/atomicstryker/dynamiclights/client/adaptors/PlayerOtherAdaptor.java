package atomicstryker.dynamiclights.client.adaptors;

import atomicstryker.dynamiclights.client.DynamicLights;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerOtherAdaptor extends BaseAdaptor
    {        
        private EntityPlayer player;
        
        public PlayerOtherAdaptor(EntityPlayer p)
        {
        	super(p);
            player = p;
        }
        
        public void onTick()
        {
            if (player.isBurning())
            {
                lightLevel = 15;
            }
            else
            {                
                lightLevel = DynamicLights.itemsMap.getLightFromItemStack(player.getCurrentEquippedItem());
                for (ItemStack armor : player.inventory.armorInventory)
                {
                    lightLevel = DynamicLights.maxLight(lightLevel, DynamicLights.itemsMap.getLightFromItemStack(armor));
                }            	
            }
            
            checkForchange();
        }
             
    }