package me.salturbone.rwg;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import me.salturbone.rwg.biomes.BiOcean;

public class CCGen extends ChunkGenerator {


    public static int minHeight = 61;
    public CCGen() {
        
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        //Biomes
        BiOcean ocean_biome = new BiOcean(world.getSeed(), 0.0005D, 0.004D, chunk, biome);


        int currentHeight = minHeight;
        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                //FLAT GENERATOR
                biome.setBiome(X, Z, Biome.PLAINS);
                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                for (int i = currentHeight - 1; i > currentHeight - 5; i--) {
                    chunk.setBlock(X, i, Z, Material.DIRT);
                }
                for (int i = currentHeight - 5; i > 0; i--) {
                    chunk.setBlock(X, i, Z, Material.STONE);
                }
                chunk.setBlock(X, 0, Z, Material.BEDROCK);


                currentHeight = calculateMaxHeight(X, Z, chunk);
                ocean_biome.draw(X, currentHeight, Z, chunkX, chunkZ);
            }
        }

        return chunk;
    }

    public static int calculateMaxHeight(int x, int z, ChunkData chunk_data) {
        for (int i = 255; i > 0; i--) {
            if (chunk_data.getType(x, i, z) != Material.AIR && chunk_data.getType(x, i, z) != Material.WATER) {
                return i;
            }
        }
        return 0;
    }

}
