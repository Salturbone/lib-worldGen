package me.salturbone.rwg;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class CCGen extends ChunkGenerator {

    private final int ocean_limit = 60;
    private final int ocean_type_limit = 61;

    private double frequency;
    private int octaves;

    public CCGen(double frequency, int octaves) {
        this.frequency = frequency;
        this.octaves = octaves;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        PerlinOctaveGenerator per_ter_gen = new PerlinOctaveGenerator(new Random(world.getSeed()), octaves);
        PerlinOctaveGenerator per_ter_gen0 = new PerlinOctaveGenerator(new Random(world.getSeed()), octaves);
        SimplexOctaveGenerator simplex_gen = new SimplexOctaveGenerator(new Random(world.getSeed()), 5);
        simplex_gen.setScale(0.005D);
        per_ter_gen.setScale(0.01D);
        per_ter_gen0.setScale(0.1D);

        double biomeHandler = 0;
        int currentHeight = 0;
        int curPosState;
        int kindofrandom;

        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                biomeHandler = simplex_gen.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency, 0.5D, true);
                if (Math.abs(biomeHandler) >= 0.3) {
                    biome.setBiome(X, Z, Biome.PLAINS);
                    currentHeight = (int) ((per_ter_gen.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency, 0.5D, true)
                            + 1) * 40D + 30D);
                } else {
                    biome.setBiome(X, Z, Biome.DEEP_OCEAN);
                    currentHeight = (int) ((per_ter_gen0.noise(chunkX * 16 + X, chunkZ * 16 + Z, frequency, 0.5D, true)
                            + 1) * 30D + 40D);
                }

                curPosState = (int) Math.sqrt(Math.pow(chunkX * 16 + X, 2D) + Math.pow(chunkZ * 16 + Z, 2D));

                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                chunk.setBlock(X, currentHeight - 1, Z, Material.DIRT);

                if (currentHeight <= ocean_type_limit) {
                    chunk.setBlock(X, currentHeight, Z, Material.SAND);

                    if (currentHeight <= ocean_limit) {
                        for (int i = 1; i <= ocean_limit - currentHeight; i++) {
                            chunk.setBlock(X, currentHeight + i, Z, Material.WATER);
                        }
                    }
                }

                for (int i = currentHeight - 2; i > 0; i--) {
                    chunk.setBlock(X, i, Z, Material.STONE);
                }
                if (curPosState >= 8000 && curPosState < 8500) {
                    kindofrandom = random.nextInt(64);
                    if (kindofrandom <= 4) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 8500 && curPosState < 9000) {
                    kindofrandom = random.nextInt(64);
                    if (kindofrandom <= 8) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 9000 && curPosState < 9500) {
                    kindofrandom = random.nextInt(64);
                    if (kindofrandom <= 16) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 9500 && curPosState < 10000) {
                    kindofrandom = random.nextInt(64);
                    if (kindofrandom <= 32) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 10000) {
                    for (int i = 1; i < currentHeight + 1; i++) {
                        chunk.setBlock(X, i, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState == 10000) {
                    for (int i = 1; i < 10; i++) {
                        chunk.setBlock(X, currentHeight + i, Z, Material.NETHER_BRICK);
                    }
                }
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        }

        return chunk;
    }

}