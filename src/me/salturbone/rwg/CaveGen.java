package me.salturbone.rwg;

import java.util.Random;

import com.google.common.base.MoreObjects;

import net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.ChunkSnapshot;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.Material;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.World;

/**
 * CaveGen
 */
public class CaveGen extends EWorldGenBase {
    protected static IBlockData a, b, c, d;

    protected void azOlaylı(long seed, int chunkX, int chunkZ, ChunkSnapshot chunkSnapshot, double küçükRandom,
            double açılıRandom, double çifteRandom) {
        this.çokOlaylı(seed, chunkX, chunkZ, chunkSnapshot, küçükRandom, açılıRandom, çifteRandom,
                1.0F + random.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void çokOlaylı(long seed, int var3, int chunkZ, ChunkSnapshot chunkSnapshot, double küçükRandom,
            double açılıRandom, double çifteRandom, float var12, float var13, float var14, int var15, int var16,
            double var17) {
        double ortaX = (double) (var3 * 16 + 8);
        double ortaZ = (double) (chunkZ * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(seed);
        if (var16 <= 0) {
            int var26 = this.e * 16 - 16;
            var16 = var26 - var25.nextInt(var26 / 4);
        }

        boolean var55 = false;
        if (var15 == -1) {
            var15 = var16 / 2;
            var55 = true;
        }

        int var27 = var25.nextInt(var16 / 2) + var16 / 4;

        for (boolean var28 = var25.nextInt(6) == 0; var15 < var16; ++var15) {
            double var29 = 1.5D + (double) (MathHelper.sin((float) var15 * 3.1415927F / (float) var16) * var12);
            double var31 = var29 * var17;
            float var33 = MathHelper.cos(var14);
            float var34 = MathHelper.sin(var14);
            küçükRandom += (double) (MathHelper.cos(var13) * var33);
            açılıRandom += (double) var34;
            çifteRandom += (double) (MathHelper.sin(var13) * var33);
            if (var28) {
                var14 *= 0.92F;
            } else {
                var14 *= 0.7F;
            }

            var14 += var24 * 0.1F;
            var13 += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
            if (!var55 && var15 == var27 && var12 > 1.0F && var16 > 0) {
                this.çokOlaylı(var25.nextLong(), var3, chunkZ, chunkSnapshot, küçükRandom, açılıRandom, çifteRandom,
                        var25.nextFloat() * 0.5F + 0.5F, var13 - 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
                this.çokOlaylı(var25.nextLong(), var3, chunkZ, chunkSnapshot, küçükRandom, açılıRandom, çifteRandom,
                        var25.nextFloat() * 0.5F + 0.5F, var13 + 1.5707964F, var14 / 3.0F, var15, var16, 1.0D);
                return;
            }

            if (var55 || var25.nextInt(4) != 0) {
                double var35 = küçükRandom - ortaX;
                double var37 = çifteRandom - ortaZ;
                double var39 = (double) (var16 - var15);
                double var41 = (double) (var12 + 2.0F + 16.0F);
                if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
                    return;
                }

                if (küçükRandom >= ortaX - 16.0D - var29 * 2.0D && çifteRandom >= ortaZ - 16.0D - var29 * 2.0D
                        && küçükRandom <= ortaX + 16.0D + var29 * 2.0D && çifteRandom <= ortaZ + 16.0D + var29 * 2.0D) {
                    int var56 = MathHelper.floor(küçükRandom - var29) - var3 * 16 - 1;
                    int var36 = MathHelper.floor(küçükRandom + var29) - var3 * 16 + 1;
                    int var57 = MathHelper.floor(açılıRandom - var31) - 1;
                    int var38 = MathHelper.floor(açılıRandom + var31) + 1;
                    int var58 = MathHelper.floor(çifteRandom - var29) - chunkZ * 16 - 1;
                    int var40 = MathHelper.floor(çifteRandom + var29) - chunkZ * 16 + 1;
                    if (var56 < 0) {
                        var56 = 0;
                    }

                    if (var36 > 16) {
                        var36 = 16;
                    }

                    if (var57 < 1) {
                        var57 = 1;
                    }

                    if (var38 > 248) {
                        var38 = 248;
                    }

                    if (var58 < 0) {
                        var58 = 0;
                    }

                    if (var40 > 16) {
                        var40 = 16;
                    }

                    boolean var59 = false;

                    int var43;
                    for (int var42 = var56; !var59 && var42 < var36; ++var42) {
                        for (var43 = var58; !var59 && var43 < var40; ++var43) {
                            for (int var44 = var38 + 1; !var59 && var44 >= var57 - 1; --var44) {
                                if (var44 >= 0 && var44 < 256) {
                                    IBlockData var45 = chunkSnapshot.a(var42, var44, var43);
                                    if (var45.getBlock() == Blocks.FLOWING_WATER || var45.getBlock() == Blocks.WATER) {
                                        var59 = true;
                                    }

                                    if (var44 != var57 - 1 && var42 != var56 && var42 != var36 - 1 && var43 != var58
                                            && var43 != var40 - 1) {
                                        var44 = var57;
                                    }
                                }
                            }
                        }
                    }

                    if (!var59) {
                        MutableBlockPosition var60 = new MutableBlockPosition();

                        for (var43 = var56; var43 < var36; ++var43) {
                            double var61 = ((double) (var43 + var3 * 16) + 0.5D - küçükRandom) / var29;

                            for (int var46 = var58; var46 < var40; ++var46) {
                                double var47 = ((double) (var46 + chunkZ * 16) + 0.5D - çifteRandom) / var29;
                                boolean var49 = false;
                                if (var61 * var61 + var47 * var47 < 1.0D) {
                                    for (int var50 = var38; var50 > var57; --var50) {
                                        double var51 = ((double) (var50 - 1) + 0.5D - açılıRandom) / var31;
                                        if (var51 > -0.7D && var61 * var61 + var51 * var51 + var47 * var47 < 1.0D) {
                                            IBlockData var53 = chunkSnapshot.a(var43, var50, var46);
                                            IBlockData var54 = (IBlockData) MoreObjects
                                                    .firstNonNull(chunkSnapshot.a(var43, var50 + 1, var46), b);// Hava
                                            if (var53.getBlock() == Blocks.GRASS
                                                    || var53.getBlock() == Blocks.MYCELIUM) {
                                                var49 = true;
                                            }

                                            if (this.a(var53, var54)) {
                                                if (var50 - 1 < 10) {
                                                    chunkSnapshot.a(var43, var50, var46, a);// Lava koy
                                                } else {
                                                    chunkSnapshot.a(var43, var50, var46, b);// Hava koy
                                                    if (var49 && chunkSnapshot.a(var43, var50 - 1, var46)
                                                            .getBlock() == Blocks.DIRT) {
                                                        var60.c(var43 + var3 * 16, 0, var46 + chunkZ * 16);
                                                        chunkSnapshot.a(var43, var50 - 1, var46,
                                                                this.world.getBiome(var60).q.getBlock().getBlockData());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (var55) {
                            break;
                        }
                    }
                }
            }
        }

    }

    protected boolean a(IBlockData var1, IBlockData var2) {
        if (var1.getBlock() == Blocks.STONE) {
            return true;
        } else if (var1.getBlock() == Blocks.DIRT) {
            return true;
        } else if (var1.getBlock() == Blocks.GRASS) {
            return true;
        } else if (var1.getBlock() == Blocks.HARDENED_CLAY) {
            return true;
        } else if (var1.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
            return true;
        } else if (var1.getBlock() == Blocks.SANDSTONE) {
            return true;
        } else if (var1.getBlock() == Blocks.RED_SANDSTONE) {
            return true;
        } else if (var1.getBlock() == Blocks.MYCELIUM) {
            return true;
        } else if (var1.getBlock() == Blocks.SNOW_LAYER) {
            return true;
        } else {
            return (var1.getBlock() == Blocks.SAND || var1.getBlock() == Blocks.GRAVEL)
                    && var2.getMaterial() != Material.WATER;
        }
    }

    @Override
    protected void worldGendenGelen(World world, int XRandomu, int ZRandomu, int chunkX, int chunkZ,
            ChunkSnapshot chunkSnapShot) {
        int var7 = random.nextInt(random.nextInt(random.nextInt(15) + 1) + 1);
        if (random.nextInt(7) != 0) {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8) {
            double RandomXInChunk = (double) (XRandomu * 16 + random.nextInt(16));
            double RandomYInChunk = (double) random.nextInt(random.nextInt(120) + 8);
            double RandomZInChunk = (double) (ZRandomu * 16 + random.nextInt(16));
            int temp = 1;
            // 0,25 ihtimalle hiç anlamadığımız az olaylı şeyler yap
            if (random.nextInt(4) == 0) {
                this.azOlaylı(random.nextLong(), chunkX, chunkZ, chunkSnapShot, RandomXInChunk, RandomYInChunk,
                        RandomZInChunk);
                temp += random.nextInt(4);
            }

            for (int var16 = 0; var16 < temp; ++var16) {
                float açılırandom = random.nextFloat() * 2 * (float) Math.PI;
                float küçükrandom = (random.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float çifterandom = random.nextFloat() * 2.0F + random.nextFloat();
                // 1/10 ihtimalle neler neler yap kim bilir
                if (random.nextInt(10) == 0) {
                    küçükrandom *= random.nextFloat() * random.nextFloat() * 3.0F + 1.0F;
                }

                this.çokOlaylı(random.nextLong(), chunkX, chunkZ, chunkSnapShot, RandomXInChunk, RandomYInChunk,
                        RandomZInChunk, küçükrandom, açılırandom, çifterandom, 0, 0, 1.0D);
            }
        }

    }

    static {
        a = Blocks.LAVA.getBlockData();
        b = Blocks.AIR.getBlockData();
        c = Blocks.SANDSTONE.getBlockData();
        d = Blocks.RED_SANDSTONE.getBlockData();
    }

}