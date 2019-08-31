package me.salturbone.rwg;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import org.bukkit.craftbukkit.v1_12_R1.generator.NormalChunkGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.ChunkProviderGenerate;
import net.minecraft.server.v1_12_R1.NoiseGenerator3;
import net.minecraft.server.v1_12_R1.WorldGenCaves;
import net.minecraft.server.v1_12_R1.WorldServer;

public class Main extends JavaPlugin implements Listener {

    public static Random random = new Random();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
            World world = Bukkit.getWorld("DeliYerler");
            Bukkit.unloadWorld(world, false);
            File worldFolder = world.getWorldFolder();
            deleteWorld(worldFolder);

            sender.sendMessage("Silmesi lazım fln");
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("teleport")) {
            if (args.length >= 2 && args[1].equalsIgnoreCase("world")) {
                World world = Bukkit.getWorld("world");
                Player p = (Player) sender;
                p.teleport(world.getSpawnLocation());
                return true;
            }
            World world = Bukkit.getWorld("DeliYerler");
            Player p = (Player) sender;
            p.teleport(world.getSpawnLocation());
            return true;
        }
        long seed = random.nextLong();
        int octaves = 5;
        double frequency = 2.5D;
        if (getFromArgs(args, "f") != null) {
            frequency = Double.valueOf(getFromArgs(args, "f"));
        }
        if (getFromArgs(args, "s") != null) {
            seed = Long.valueOf(getFromArgs(args, "s"));
        }
        if (getFromArgs(args, "o") != null) {
            octaves = Integer.valueOf(getFromArgs(args, "o"));
        }
        WorldCreator wc = new WorldCreator("DeliYerler");
        wc.generator(new CCGen(frequency, octaves));
        wc.seed(seed);
        getServer().createWorld(wc);
        sender.sendMessage("Frequency: " + ChatColor.DARK_AQUA + frequency);
        sender.sendMessage("Octaves: " + ChatColor.DARK_AQUA + octaves);
        sender.sendMessage("Seed: " + ChatColor.DARK_AQUA + seed);
        World world = Bukkit.getWorld("world");
        WorldServer w = ((CraftWorld) world).getHandle();
        Bukkit.broadcastMessage(w.getChunkProviderServer().chunkGenerator + "");
        Bukkit.broadcastMessage(w.worldProvider.getChunkGenerator() + "");
        NormalChunkGenerator ncg;
        ChunkProviderGenerate cpg;
        InternalChunkGenerator icg;
        WorldGenCaves wgc;
        NoiseGenerator3 ng3;
        return true;
    }

    public boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static String getFromArgs(String[] args, String prefix) {
        for (String str : args) {
            if (str.startsWith(prefix + ":"))
                return str.replaceFirst(prefix + ":", "");
        }
        return null;
    }

}