package me.salturbone.rwg;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
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
        WorldCreator wc = new WorldCreator("DeliYerler");
        wc.generator(new CCGen());
        getServer().createWorld(wc);
        sender.sendMessage("Yaratması lazım fln");
        return true;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CCGen();
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
}