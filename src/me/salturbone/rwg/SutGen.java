package me.salturbone.rwg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.spongepowered.noise.model.Plane;
import org.spongepowered.noise.module.source.Perlin;

/**
 * SutGen
 */
public class SutGen extends ChunkGenerator {

    List<SutHeight> heights = new ArrayList<>();

    public static int MountainStart = 85;
    public static int OceanEnd = 60;
    public static int SandEnd = 63;

    private double frequency, presistence, lacunarity;
    private int octaves;

    public SutGen(double frequency, double presistence, double lacunarity, int octaves, SutHeight... heights) {
        this.frequency = frequency;
        this.presistence = presistence;
        this.lacunarity = lacunarity;
        this.octaves = octaves;
        this.heights.addAll(Arrays.asList(heights));
        Collections.sort(this.heights);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        int seed = (int) world.getSeed();
        Perlin perlin = new Perlin();
        perlin.setSeed(seed);
        perlin.setLacunarity(lacunarity);
        perlin.setPersistence(presistence);
        perlin.setFrequency(frequency);
        perlin.setOctaveCount(octaves);
        Plane pl = new Plane(perlin);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int curHeight = (int) ((pl.getValue(chunkX * 16 + x, chunkZ * 16 + z) + 1) * 40D + 29D);
                generateXZ(world, seed, x, curHeight, z);
                for (int y = 0; y <= curHeight; y++)
                    chunk.setBlock(x, y, z, Material.STONE);
            }
        }
        return chunk;
    }

    public void updateHeights() {
        Collections.sort(heights);
    }

    public void generateXZ(World world, long seed, int x, int height, int z) {
        for (SutHeight sHeight : heights) {
            if (sHeight.isIn(height)) {
                sHeight.generateXZ(world, seed, x, height, z);
            }
        }
    }
}