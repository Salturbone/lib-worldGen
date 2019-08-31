package me.salturbone.rwg.noise_c;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.spongepowered.noise.module.source.Perlin;

/**
 * Worm
 */
public class Worm {

    public Location startPos;
    public Location headPos;
    public Location noisePos;
    public Perlin noise;
    public int segmentCount = 30;
    public double segmentLength = 2;
    public double radius;
    public double twistiness = 1;
    public List<WormSegment> segments = new ArrayList<WormSegment>();
    public double yMultiplier = 0.1;

    public Worm(Location start) {
        headPos = start.clone();
        noisePos = start.clone();
        segmentLength = 5D;
        radius = 3;
        startPos = start.clone();
        noise = new Perlin();
    }

    public double getTaperAmount(int segment) {
        double curSegment = (double) segment;
        double segmentCount = (double) this.segmentCount;
        double halfSegmentCount = segmentCount / 2.0;
        double baseTaperAmount = 1.0 - Math.abs((curSegment / halfSegmentCount) - 1.0);
        return Math.sqrt(baseTaperAmount); // sqrt better defines the tapering.
    }

    public void generate(int segmentCount) {
        for (int i = 0; i < segmentCount; i++) {
            addSegment();
        }

    }

    public void addSegment() {
        segmentCount++;
        WormSegment nSegment = getNextSegment();

        segments.add(nSegment);
        headPos = headPos.add(nSegment.offset);
        noisePos = noisePos.add(nSegment.offset.clone().multiply(twistiness));
        if (segments.size() > 2) {
            Location lastEnd = segments.get(segments.size() - 2).end();
            Location newEnd = nSegment.end();
            Bukkit.broadcastMessage(newEnd.subtract(lastEnd).toVector().length() + "");
        }

    }

    public List<WormSegment> getSegments() {
        return segments;
    }

    public double getXZNoise() {
        return noise.getValue(noisePos.getX(), noisePos.getY() + 500, noisePos.getZ());
    }

    public double getYNoise() {
        return noise.getValue(noisePos.getX(), noisePos.getY(), noisePos.getZ());
    }

    public WormSegment getNextSegment() {
        double xzNoiseValue = getXZNoise();
        double yNoiseValue = getYNoise();
        Location start = headPos.clone();

        double yaw = (xzNoiseValue - 0.9) * 5 * 2D * Math.PI;
        double pitch = (yNoiseValue - 0.9) * 5 * 2D * Math.PI;
        // Bukkit.broadcastMessage("xzNoise: " + (xzNoiseValue - 0.9) * 5 + " yNoise: "
        // + (yNoiseValue - 0.9) * 5);
        Vector xzOffset = new Vector(Math.cos(yaw), 0, Math.sin(yaw)).normalize();
        xzOffset = xzOffset.multiply(Math.sin(pitch));
        // Bukkit.broadcastMessage("XZLength: " + xzOffset.length() + "");
        Vector totalOffset = new Vector(0, Math.sin(pitch) * yMultiplier, 0).add(xzOffset).normalize();
        // Bukkit.broadcastMessage("Total Length:" + totalOffset.length());
        Random rnd = new Random();

        totalOffset.multiply((Math.abs(rnd.nextGaussian()) + 1) * segmentLength);
        return new WormSegment(start, totalOffset);
    }

    public class WormSegment {
        public Location start;
        public Vector offset;

        public WormSegment(Location start, Vector offset) {
            this.start = start;
            this.offset = offset;
        }

        public Location end() {
            return start.clone().add(offset);
        }
    }

}