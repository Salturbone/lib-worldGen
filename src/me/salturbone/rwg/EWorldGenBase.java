package me.salturbone.rwg;

import java.util.Random;

import net.minecraft.server.v1_12_R1.ChunkSnapshot;
import net.minecraft.server.v1_12_R1.World;

public class EWorldGenBase {
    protected int e = 8; // Ne kadar doğru sallıyorum emin değilim ama bir cave sanırım en fazla 16x16
                         // chunkluk olabiliyor
    protected Random random = new Random();
    protected World world;

    public void a(World world, int chunkX, int chunkZ, ChunkSnapshot chunkSnapshot) {
        int bu8 = this.e;
        this.world = world;
        random.setSeed(world.getSeed());
        long XRandomuSabit = random.nextLong();
        long ZRandomuSabit = random.nextLong();

        for (int tempXCh = chunkX - bu8; tempXCh <= chunkX + bu8; ++tempXCh) {
            for (int tempZCh = chunkZ - bu8; tempZCh <= chunkZ + bu8; ++tempZCh) {
                long XRandomu = (long) tempXCh * XRandomuSabit;
                long ZRandomu = (long) tempZCh * ZRandomuSabit;
                random.setSeed(XRandomu ^ ZRandomu ^ world.getSeed());
                this.worldGendenGelen(world, tempXCh, tempZCh, chunkX, chunkZ, chunkSnapshot);
            }
        }

    }

    // Bu tepedekinin tekil halini fln yapıyor sanırım ama kullanıldığı yer görmedim
    public static void a(long seed, Random random, int chunkX, int chunkZ) {
        random.setSeed(seed);
        long XRandomuSabit = random.nextLong();
        long ZRandomuSabit = random.nextLong();
        long XRandomu = (long) chunkX * XRandomuSabit;
        long ZRandomu = (long) chunkZ * ZRandomuSabit;
        random.setSeed(XRandomu ^ ZRandomu ^ seed);
    }

    protected void worldGendenGelen(World var1, int var2, int var3, int var4, int var5, ChunkSnapshot var6) {
    }
}
