package dev.aaronhowser.mods.ariadnesthread.datagen

import dev.aaronhowser.mods.ariadnesthread.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    pOutput: PackOutput,
    pRegistries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(pOutput, pRegistries) {

    override fun buildRecipes(pRecipeOutput: RecipeOutput) {
        ariadnesThread.save(pRecipeOutput)
    }

    companion object {
        private val ariadnesThread =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ARIADNES_THREAD)
                .pattern("GSG")
                .pattern("SCS")
                .pattern("GSG")
                .define('G', Tags.Items.NUGGETS_GOLD)
                .define('S', Items.STRING)
                .define('C', Items.COMPASS)
                .unlockedBy("has_item", has(ModItems.ARIADNES_THREAD))
    }

}