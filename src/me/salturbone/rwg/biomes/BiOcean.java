package me.salturbone.rwg.biomes;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import me.salturbone.rwg.CCGen;
import me.salturbone.rwg.lib_noise.SimplexNoiseGenerate;

public class BiOcean {
    long m_seed;
    SimplexNoiseGenerate biome_noise, draw_noise;
    ChunkData chunk;
    BiomeGrid biome;
    int ocean_limit = 61;
    public BiOcean(long seed, double biome_scale, double draw_scale, ChunkData chunk_data, BiomeGrid biome_data) {

        m_seed = seed;

        biome_noise = new SimplexNoiseGenerate(seed);
        biome_noise.setScale(biome_scale);

        draw_noise = new SimplexNoiseGenerate(seed);
        draw_noise.setScale(draw_scale);

        chunk = chunk_data;
        biome = biome_data;

    }

    public void draw(int x, int y, int z, int chunk_x, int chunk_z) {
        int h = 0;
        double c_noise = biome_noise.noise(chunk_x * 16 + x, chunk_z * 16 + z);
        double d_noise = draw_noise.noise(chunk_x * 16 + x, chunk_z * 16 + z);
        if (c_noise < -0.1 && c_noise >= -0.15) {
            biome.setBiome(x,z,Biome.OCEAN);
            h = (int) (y + (-1 * (Math.abs(d_noise) * 13)) + 3);
            chunk.setBlock(x, h - 1, z, Material.SAND);
            chunk.setBlock(x, h - 2, z, Material.SAND);
            for (int i = h; i < CCGen.minHeight + 1; i++) {
                chunk.setBlock(x, i, z, Material.AIR);
            }
        }
        if (c_noise < -0.15) {
            biome.setBiome(x,z, Biome.DEEP_OCEAN);
            h = (int) (y + (-1 * ((Math.abs(d_noise) * 30))));
            chunk.setBlock(x, h - 1, z, Material.SAND);
            for (int i = h; i < CCGen.minHeight + 1; i++) {
                chunk.setBlock(x, i, z, Material.AIR);
            }
        }
        h = CCGen.calculateMaxHeight(x, z, chunk);
        if (h < ocean_limit) {
            for (int i = h + 1; i < CCGen.minHeight; i++) {
                chunk.setBlock(x, i, z, Material.WATER);
            }
        }

    }


}