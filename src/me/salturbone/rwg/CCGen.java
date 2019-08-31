package me.salturbone.rwg;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

public class CCGen extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        PerlinOctaveGenerator per_ter_gen = new PerlinOctaveGenerator(new Random(world.getSeed()), 80);
        per_ter_gen.setScale(0.005D);
        int currentHeight = 0;
        int curPosState;
        int kindofrandom;
        Random rnd = new Random();
        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                currentHeight = (int) ((per_ter_gen.noise(chunkX * 16 + X, chunkZ *16 + Z, 2.5D, 0.1D, true)+1) * 60D + 29D);

                curPosState = (int) Math.sqrt(Math.pow(chunkX*16+X, 2D) + Math.pow(chunkZ*16+Z, 2D));
                
                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
                if (currentHeight <= 60) {
                    for (int i = 1; i <= 60 - currentHeight; i++) {
                        chunk.setBlock(X,currentHeight + i, Z, Material.WATER);
                    }
                }

                for(int i = currentHeight-2; i > 0; i--) {
                    chunk.setBlock(X, i, Z, Material.STONE);
                }
                if (curPosState >= 8000 && curPosState < 8500) {
                    kindofrandom = rnd.nextInt(64);
                    if (kindofrandom <= 4) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 8500 && curPosState < 9000) {
                    kindofrandom = rnd.nextInt(64);
                    if (kindofrandom <= 8) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 9000 && curPosState < 9500) {
                    kindofrandom = rnd.nextInt(64);
                    if (kindofrandom <= 16) {
                        chunk.setBlock(X, currentHeight, Z, Material.NETHERRACK);
                    }
                }
                if (curPosState >= 9500 && curPosState < 10000) {
                    kindofrandom = rnd.nextInt(64);
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