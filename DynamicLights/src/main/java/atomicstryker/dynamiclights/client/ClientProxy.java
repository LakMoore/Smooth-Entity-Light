package atomicstryker.dynamiclights.client;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    static Minecraft mcinstance;

    /**
     * The Keybinding instance to monitor
     */
    static KeyBinding toggleButton;
    static long nextKeyTriggerTime;

    public void preInit(FMLPreInitializationEvent evt) {
        Config.doConfig(evt.getSuggestedConfigurationFile());

        DynamicLights.globalLightsOff = false;
        ClientProxy.mcinstance = FMLClientHandler.instance().getClient();
        DynamicLights.lightValueMap = new HashMap<Class<? extends Entity>, Boolean>();
        DynamicLights.glowValueMap = new HashMap<Class<? extends Entity>, Integer>();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());

        ClientProxy.nextKeyTriggerTime = System.currentTimeMillis();
        DynamicLights.nextLightUpdateTime = System.currentTimeMillis();

    }

    public void init() {
        ClientProxy.toggleButton = new KeyBinding("Dynamic Lights toggle", Keyboard.KEY_L, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(ClientProxy.toggleButton);
        DynamicLights.coloredLights = Loader.isModLoaded("easycoloredlights");
    }

}
