package me.salturbone.rwg;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.spongepowered.noise.Utils;

import me.salturbone.rwg.noise_c.Worm;
import me.salturbone.rwg.noise_c.Worm.WormSegment;

public class Main extends JavaPlugin implements Listener {

    public static Random random = new Random();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("qadrgen")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("del")) {
                deleteWorld("QadrYerleri");
                sender.sendMessage("Silmesi lazım fln");
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("tp")) {
                if (args.length >= 2 && args[1].equalsIgnoreCase("w")) {
                    World world = Bukkit.getWorld("world");
                    Player p = (Player) sender;
                    p.teleport(world.getSpawnLocation());
                    return true;
                }
                World world = Bukkit.getWorld("QadrYerleri");
                Player p = (Player) sender;
                p.teleport(world.getSpawnLocation());
                return true;
            }
            long seed = random.nextLong();
            int octaves = 5;
            double frequency = 0.05D;

            if (getFromArgs(args, "f") != null) {
                frequency = Double.valueOf(getFromArgs(args, "f"));
            }

            if (getFromArgs(args, "s") != null) {
                seed = Long.valueOf(getFromArgs(args, "s"));
            }
            if (getFromArgs(args, "o") != null) {
                octaves = Integer.valueOf(getFromArgs(args, "o"));
            }
            if (Bukkit.getWorld("QadrYerleri") != null) {
                deleteWorld("QadrYerleri");
            }
            WorldCreator wc = new WorldCreator("QadrYerleri");
            wc.generator(new CCGen(frequency, octaves));
            wc.seed(seed);
            World world = getServer().createWorld(wc);
            world.setSpawnLocation(world.getSpawnLocation().add(0, 30, 0));
            sender.sendMessage("Frequency: " + ChatColor.DARK_AQUA + frequency);
            sender.sendMessage("Octaves: " + ChatColor.DARK_AQUA + octaves);
            sender.sendMessage("Seed: " + ChatColor.DARK_AQUA + seed);
            if (sender instanceof Player) {
                ((Player) sender).teleport(world.getSpawnLocation());
            }
            return true;
        } else if (label.equalsIgnoreCase("sutgen")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("worm")) {
                Player p = (Player) sender;
                int octaves = 5;
                double frequency = 0.05D;
                double lacunarity = 1D;
                double twistiness = 10;
                if (getFromArgs(args, "t") != null) {
                    twistiness = Double.valueOf(getFromArgs(args, "t"));
                }
                if (getFromArgs(args, "l") != null) {
                    lacunarity = Double.valueOf(getFromArgs(args, "l"));
                }
                double presistence = 0.5D;
                if (getFromArgs(args, "p") != null) {
                    presistence = Double.valueOf(getFromArgs(args, "p"));
                }
                if (getFromArgs(args, "f") != null) {
                    frequency = Double.valueOf(getFromArgs(args, "f"));
                }
                if (getFromArgs(args, "o") != null) {
                    octaves = Integer.valueOf(getFromArgs(args, "o"));
                }
                Worm worm = new Worm(p.getLocation());
                worm.noise.setFrequency(frequency);
                worm.noise.setLacunarity(lacunarity);
                worm.noise.setPersistence(presistence);
                worm.noise.setOctaveCount(octaves);
                worm.twistiness = twistiness;
                worm.segmentLength = 2;
                worm.generate(30);
                for (WormSegment segment : worm.getSegments()) {
                    segment.start.getBlock().setType(Material.QUARTZ_BLOCK);
                }
                p.sendMessage("Worm olması lzm");
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("del")) {
                deleteWorld("SutYerleri");
                sender.sendMessage("Silmesi lazım fln");
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("tp")) {
                if (args.length >= 2 && args[1].equalsIgnoreCase("w")) {
                    World world = Bukkit.getWorld("world");
                    Player p = (Player) sender;
                    p.teleport(world.getSpawnLocation());
                    return true;
                }
                World world = Bukkit.getWorld("SutYerleri");
                Player p = (Player) sender;
                p.teleport(world.getSpawnLocation());
                return true;
            }
            long seed = random.nextLong();
            int octaves = 5;
            double frequency = 0.05D;
            double lacunarity = 1D;
            if (getFromArgs(args, "l") != null) {
                lacunarity = Double.valueOf(getFromArgs(args, "l"));
            }
            double presistence = 0.5D;
            if (getFromArgs(args, "p") != null) {
                presistence = Double.valueOf(getFromArgs(args, "p"));
            }
            if (getFromArgs(args, "f") != null) {
                frequency = Double.valueOf(getFromArgs(args, "f"));
            }
            if (getFromArgs(args, "s") != null) {
                seed = Long.valueOf(getFromArgs(args, "s"));
            }
            if (getFromArgs(args, "o") != null) {
                octaves = Integer.valueOf(getFromArgs(args, "o"));
            }
            if (Bukkit.getWorld("SutYerleri") != null) {
                deleteWorld("SutYerleri");
            }
            WorldCreator wc = new WorldCreator("SutYerleri");
            // wc.generator(new CCGen(frequency, octaves));
            wc.generator(new SutGen(frequency, presistence, lacunarity, octaves));
            wc.seed(seed);
            World world = getServer().createWorld(wc);
            world.setSpawnLocation(world.getSpawnLocation().add(0, 30, 0));
            sender.sendMessage("Frequency: " + ChatColor.DARK_AQUA + frequency);
            sender.sendMessage("Octaves: " + ChatColor.DARK_AQUA + octaves);
            sender.sendMessage("Presistence: " + ChatColor.DARK_AQUA + presistence);
            sender.sendMessage("Lacunarity: " + ChatColor.DARK_AQUA + lacunarity);
            sender.sendMessage("Seed: " + ChatColor.DARK_AQUA + seed);
            sender.sendMessage(Utils.RANDOM_VECTORS.length + "");
            if (sender instanceof Player) {
                ((Player) sender).teleport(world.getSpawnLocation());
            }
            return true;
        }
        return true;
    }

    public void deleteWorld(String worlds) {
        World world = Bukkit.getWorld(worlds);
        if (world == null)
            return;
        for (Player p : world.getPlayers()) {
            p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
        Bukkit.unloadWorld(worlds, false);
        File path = world.getWorldFolder();
        deleteWorldFile(path);
    }

    public void deleteWorldFile(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorldFile(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    public static String getFromArgs(String[] args, String prefix) {
        for (String str : args) {
            if (str.startsWith(prefix + ":"))
                return str.replaceFirst(prefix + ":", "");
        }
        return null;
    }

    /*
     * Function to linearly interpolate between a0 and a1 Weight w should be in the
     * range [0.0, 1.0]
     *
     * as an alternative, this slightly faster equivalent function (macro) can be
     * used: #define lerp(a0, a1, w) ((a0) + (w)*((a1) - (a0)))
     */
    float lerp(float a0, float a1, float w) {
        return (1.0f - w) * a0 + w * a1;
    }

    // Computes the dot product of the distance and gradient vectors.
    static public double noise(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
                Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
                Z = (int) Math.floor(z) & 255;
        Bukkit.broadcastMessage("x to X:" + X);
        Bukkit.broadcastMessage("z to Z:" + X);
        x -= Math.floor(x); // FIND RELATIVE X,Y,Z
        y -= Math.floor(y); // OF POINT IN CUBE.
        z -= Math.floor(z);
        double u = fade(x), // COMPUTE FADE CURVES
                v = fade(y), // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES OF
                B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
                grad(p[BA], x - 1, y, z)), // BLENDED
                lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
                        grad(p[BB], x - 1, y - 1, z))), // FROM 8
                lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
                        grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
                        lerp(u, grad(p[AB + 1], x, y - 1, z - 1), grad(p[BB + 1], x - 1, y - 1, z - 1))));
    }

    static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    static double grad(int hash, double x, double y, double z) {
        int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
        double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    static final int p[] = new int[512], permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194,
            233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26,
            197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168,
            68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220,
            105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208,
            89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
            124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39,
            253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238,
            210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157,
            184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
            141, 128, 195, 78, 66, 215, 61, 156, 180 };
    static {
        for (int i = 0; i < 256; i++)
            p[256 + i] = p[i] = permutation[i];
    }
}