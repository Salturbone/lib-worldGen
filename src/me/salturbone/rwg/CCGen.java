package me.salturbone.rwg;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class CCGen extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        SimplexOctaveGenerator simp_gen = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        PerlinOctaveGenerator per_ter_gen = new PerlinOctaveGenerator(new Random(world.getSeed()/10), 5);
        per_ter_gen.setScale(0.008D);
        PerlinOctaveGenerator per_gen0 = new PerlinOctaveGenerator(new Random(world.getSeed()), 1);
        PerlinOctaveGenerator per_gen1 = new PerlinOctaveGenerator(new Random(world.getSeed() - world.getSeed()/10), 1);
        per_gen0.setScale(0.1D);
        per_gen1.setScale(0.01D);
        

        simp_gen.setScale(0.008D);
        
        int perCurH0 = 0;
        int perCurH1 = 0;
        int currentHeight = 0;
        int simplexH = 0;
        int perlinH = 0;
        int curPosState;
        int kindofrandom;
        Random rnd = new Random();
        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                simplexH = (int) ((simp_gen.noise(chunkX * 16 + X, chunkZ *16 + Z, 0.5D, 0.5D, true)+1) * 40D + 50D);
                perlinH = (int) ((per_ter_gen.noise(chunkX * 16 + X, chunkZ *16 + Z, 0.5D, 0.5D, true)+1) * 40D + 50D);
                if (simplexH > perlinH) {
                    currentHeight = simplexH;
                } else {
                    currentHeight = perlinH;
                }
                //perCurH = (int) ((per_gen.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.5D, true) + 1)* 60D + 10D);
                perCurH0 = (int) ((per_gen0.noise(chunkX * 16.0D + X, chunkZ * 16.0D + Z, 0.5D, 0.5D, true) + 1) * 40D + 10D);
                perCurH1 = (int) ((per_gen1.noise(chunkX * 16.0D + X, chunkZ * 16.0D + Z, 0.5D, 1.0D, true) + 1) * 20D + 30D);

                curPosState = (int) Math.sqrt(Math.pow(chunkX*16+X, 2D) + Math.pow(chunkZ*16+Z, 2D));
                
                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
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

                if (chunk.getType(X, perCurH1, Z) == Material.STONE || chunk.getType(X, perCurH1, Z) == Material.NETHERRACK || chunk.getType(X, perCurH1, Z) == Material.AIR) {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            chunk.setBlock(X + j, perCurH1 + 1, Z + i, Material.AIR);
                            chunk.setBlock(X + j, perCurH1, Z, Material.AIR);
                            chunk.setBlock(X + j, perCurH1 - 1, Z + i, Material.AIR);
                        }
                    }
                }

                if (chunk.getType(X, perCurH0, Z) == Material.STONE || chunk.getType(X, perCurH0, Z) == Material.NETHERRACK || chunk.getType(X, perCurH0, Z) == Material.AIR) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            chunk.setBlock(X + j, perCurH0 + 1, Z + i, Material.STONE);
                            chunk.setBlock(X + j, perCurH0, Z, Material.STONE);
                            chunk.setBlock(X + j, perCurH0 - 1, Z + i, Material.STONE);
                        }
                    }
                }
                
                for(int i = perCurH1; i > perCurH0; i--) {
                    if (perCurH1 > perCurH0) {
                        chunk.setBlock(X, i, Z, Material.AIR);
                    }
                }
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        }

        return chunk;
    }

}