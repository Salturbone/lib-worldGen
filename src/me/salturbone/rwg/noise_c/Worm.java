package me.salturbone.rwg.noise_c;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < segmentCount; i++)
            addSegment();
    }

    public void addSegment() {
        segmentCount++;
        WormSegment nSegment = getNextSegment();
        segments.add(nSegment);
        headPos.add(nSegment.offset);
        noisePos.add(nSegment.offset.clone().multiply(twistiness));
    }

    public List<WormSegment> getSegments() {
        return segments;
    }

    public double getXZNoise() {
        return noise.getValue(noisePos.getX(), 500, noisePos.getZ());
    }

    public double getYNoise() {
        return noise.getValue(noisePos.getX(), 0, noisePos.getZ());
    }

    public WormSegment getNextSegment() {
        double xzNoiseValue = getXZNoise();
        double yNoiseValue = getYNoise();
        Location start = headPos.clone();

        double yaw = xzNoiseValue * 2D * Math.PI;
        double pitch = yNoiseValue * 2D * Math.PI;
        Bukkit.broadcastMessage("xzNoise: " + xzNoiseValue + "           yNoise: " + yNoiseValue);
        Vector xzOffset = new Vector(Math.cos(yaw), 0, Math.sin(yaw)).normalize();
        xzOffset = xzOffset.multiply(Math.sin(pitch));
        Vector totalOffset = new Vector(0, Math.sin(pitch), 0).add(xzOffset).normalize();

        totalOffset.multiply(segmentLength);
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