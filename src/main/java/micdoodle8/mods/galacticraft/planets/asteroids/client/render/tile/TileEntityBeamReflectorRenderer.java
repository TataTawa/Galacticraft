package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityBeamReflectorRenderer extends TileEntitySpecialRenderer<TileEntityBeamReflector>
{
    private static OBJModel.OBJBakedModel reflectorModelBase;
    private static OBJModel.OBJBakedModel reflectorModelAxle;
    private static OBJModel.OBJBakedModel reflectorModelEnergyBlaster;
    private static OBJModel.OBJBakedModel reflectorModelRing;

    private void updateModels()
    {
        if (reflectorModelBase == null)
        {
            try
            {
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/reflector.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = new Function<ResourceLocation, TextureAtlasSprite>()
                {
                    @Override
                    public TextureAtlasSprite apply(ResourceLocation location)
                    {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };
                reflectorModelBase = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Base"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelAxle = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Axle"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelEnergyBlaster = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("EnergyBlaster"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelRing = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Ring"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(TileEntityBeamReflector tile, double d, double d1, double d2, float partialTickTime, int par9, float alpha)
    {
        GL11.glPushMatrix();

        GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        RenderHelper.disableStandardItemLighting();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        updateModels();

        ClientUtil.drawBakedModel(reflectorModelBase);
        GL11.glRotatef(tile.yaw, 0, 1, 0);
        ClientUtil.drawBakedModel(reflectorModelAxle);
        float dX = 0.0F;
        float dY = 1.13228F;
        float dZ = 0.0F;
        GL11.glTranslatef(dX, dY, dZ);
        GL11.glRotatef(tile.pitch, 1, 0, 0);
        GL11.glTranslatef(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelEnergyBlaster);
        GL11.glTranslatef(dX, dY, dZ);
        GL11.glRotatef(tile.ticks * 5, 0, 0, 1);
        GL11.glTranslatef(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelRing);

//        TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Base");
//        GL11.glRotatef(tileEntity.yaw, 0, 1, 0);
//        TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Axle");
//        float dX = 0.0F;
//        float dY = 1.13228F;
//        float dZ = 0.0F;
//        GL11.glTranslatef(dX, dY, dZ);
//        GL11.glRotatef(tileEntity.pitch, 1, 0, 0);
//        GL11.glTranslatef(-dX, -dY, -dZ);
//        TileEntityBeamReflectorRenderer.reflectorModel.renderPart("EnergyBlaster");
//        GL11.glTranslatef(dX, dY, dZ);
//        GL11.glRotatef(tileEntity.ticks * 500, 0, 0, 1);
//        GL11.glTranslatef(-dX, -dY, -dZ);
//        TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Ring");

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }
}
