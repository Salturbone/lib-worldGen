package me.salturbone.rwg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.source.Perlin;

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

            if (Bukkit.getWorld("QadrYerleri") != null) {
                deleteWorld("QadrYerleri");
            }
            WorldCreator wc = new WorldCreator("QadrYerleri");
            wc.generator(new CCGen());
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
            if (args.length >= 1 && args[0].equalsIgnoreCase("random")) {

                long seed = 3;
                int octaves = 1;
                double frequency = 0.05D;
                double lacunarity = 1D;
                double presistence = 0.5D;
                if (getFromArgs(args, "l") != null) {
                    lacunarity = Double.valueOf(getFromArgs(args, "l"));
                }

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

                Perlin perlin = new Perlin();
                perlin.setSeed((int) seed);
                perlin.setFrequency(frequency);
                perlin.setOctaveCount(octaves);
                perlin.setLacunarity(lacunarity);
                perlin.setPersistence(presistence);
                Player p = (Player) sender;
                // Location l = p.getLocation();
                Double x, y, z;
                x = Double.valueOf(args[1]);
                y = Double.valueOf(args[2]);
                z = Double.valueOf(args[3]);
                p.sendMessage("Random: " + perlin.getValue(x, y, z));
                return true;
            }
            if (args.length >= 1 && args[0].equalsIgnoreCase("worm")) {
                Player p = (Player) sender;
                int octaves = 1;
                double frequency = 3D;
                double lacunarity = 2D;
                double twistiness = 0.3d;
                double length = 4;
                int count = 30;
                double yMutliplier = 0.25;
                if (getFromArgs(args, "y") != null) {
                    yMutliplier = Double.valueOf(getFromArgs(args, "y"));
                }
                if (getFromArgs(args, "c") != null) {
                    count = Integer.valueOf(getFromArgs(args, "c"));
                }
                if (getFromArgs(args, "t") != null) {
                    twistiness = Double.valueOf(getFromArgs(args, "t"));
                }
                if (getFromArgs(args, "l") != null) {
                    lacunarity = Double.valueOf(getFromArgs(args, "l"));
                }
                if (getFromArgs(args, "len") != null) {
                    length = Double.valueOf(getFromArgs(args, "len"));
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
                worm.segmentLength = length;
                worm.yMultiplier = yMutliplier;
                worm.generate(count);
                // int c = 0;
                for (WormSegment segment : worm.getSegments()) {
                    Location loc = segment.start.clone();
                    // createSphere(loc, 7, Material.QUARTZ_BLOCK);
                    for (double i = 0.5; i <= segment.offset.length(); i += 1) {
                        Location center = loc.clone().add(segment.offset.clone().multiply(i / segment.offset.length()));
                        createSphere(center, 5, Material.STONE);
                    }
                    // c++;
                }
                for (WormSegment segment : worm.getSegments()) {
                    Location loc = segment.start.clone();
                    // createSphere(loc, 7, Material.QUARTZ_BLOCK);
                    for (double i = 0.5; i <= segment.offset.length(); i += 1) {
                        Location center = loc.clone().add(segment.offset.clone().multiply(i / segment.offset.length()));
                        createSphere(center, 3, Material.AIR);
                    }
                    // c++;
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

    public void createSphere(Location center, int radius, Material type) {
        int radiusSquared = radius * radius;
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    int distSquared = x * x + z * z + y * y;
                    if (distSquared >= radiusSquared) {
                        continue;
                    }
                    center.clone().add(x, y, z).getBlock().setType(type);
                }
            }
        }
    }

    public List<Location> drawHollowCircle(Location center, int r) {
        List<Location> locs = new ArrayList<>();
        int x = 0;
        int z = r;
        double p = (5d / 4) - r;
        while (x <= z) {
            locs.add(center.clone().add(x, 0, z));
            locs.add(center.clone().add(x, 0, -z));
            locs.add(center.clone().add(-x, 0, z));
            locs.add(center.clone().add(-x, 0, -z));
            locs.add(center.clone().add(z, 0, x));
            locs.add(center.clone().add(z, 0, -x));
            locs.add(center.clone().add(-z, 0, x));
            locs.add(center.clone().add(-z, 0, -x));
            if (p < 0)
                p += (4 * x) + 6;
            else {
                p += (2 * (x - z)) + 5;
                z--;
            }
            x++;
        }
        return locs;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CCGen();
    }
}