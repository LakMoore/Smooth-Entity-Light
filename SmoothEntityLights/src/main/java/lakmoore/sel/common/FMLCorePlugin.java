package lakmoore.sel.common;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({
    "lakmoore"
})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("SmoothEntityLight")
@IFMLLoadingPlugin.SortingIndex(1001)   //1001 = After Notch to Srg de-obfuscation
public class FMLCorePlugin implements IFMLLoadingPlugin
{
	
	public static File location;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"lakmoore.sel.common.Transformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    	//This will return the jar file of this mod's .jar"
    	location = (File) data.get("coremodLocation");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

}
