package me.salturbone.rwg.biomes;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import me.salturbone.rwg.CCGen;
import me.salturbone.rwg.lib_noise.SimplexNoiseGenerate;

public class BiOcean {
    long m_seed;
    SimplexNoiseGenerate biome_noise, draw_noise, d_draw_noise;
    ChunkData chunk;
    BiomeGrid biome;
    int ocean_limit = 61;
    int deep_ocean_limit;
    public BiOcean(SimplexNoiseGenerate sng,double draw_scale, ChunkData chunk_data, BiomeGrid biome_data) {

        m_seed = sng.getSeed();

        biome_noise = sng;
        biome_noise.setScale(draw_scale / 2);

        draw_noise = new SimplexNoiseGenerate(m_seed);
        draw_noise.setScale(draw_scale / 2);

        d_draw_noise = new SimplexNoiseGenerate(m_seed);
        d_draw_noise.setScale(draw_scale*10);

        chunk = chunk_data;
        biome = biome_data;

    }

    public void draw(int x, int y, int z, int chunk_x, int chunk_z) {
        int h = y;
        double d_noise = draw_noise.noise(chunk_x * 16 + x, chunk_z * 16 + z);
        if (h < ocean_limit) {
            biome.setBiome(x,z, Biome.OCEAN);
            h = (int) (1 + y + -1 * (Math.sqrt(Math.abs(biome_noise.noise(chunk_x * 16 + x, chunk_z * 16 + z)) * Math.abs(d_noise))));
            chunk.setBlock(x, h - 1, z, Material.SAND);
            chunk.setBlock(x, h - 2, z, Material.SAND);
            for (int i = h; i < ocean_limit + 1; i++) {
                chunk.setBlock(x, i, z, Material.AIR);
            }
        }
        h = CCGen.calculateMaxHeight(x, z, chunk);

        if (h <= deep_ocean_limit) {
            biome.setBiome(x,z, Biome.DEEP_OCEAN);
            h = (int) ((d_draw_noise.noise(chunk_x * 16 + x, chunk_z * 16 + z)) * 10 + h);
            chunk.setBlock(x, h - 1, z, Material.SAND);
            chunk.setBlock(x, h - 2, z, Material.SAND);
            for (int i = h; i < ocean_limit + 1; i++) {
                chunk.setBlock(x, i, z, Material.AIR);
            }
        }

        h = CCGen.calculateMaxHeight(x, z, chunk);
        if (h < ocean_limit) {
            for (int i = h + 1; i < ocean_limit; i++) {
                chunk.setBlock(x, i, z, Material.WATER);
            }
        }

    }


}