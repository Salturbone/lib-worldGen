package me.salturbone.rwg.noise_c;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.spongepowered.noise.NoiseQuality;
import org.spongepowered.noise.module.source.Perlin;

public class Worms {

  // constants
  public static final double WORM_LATERAL_SPEED = (2.0 / 8192.0);
  public static final double WORM_SEGMENT_LENGTH = (1.0 / 64.0);
  public static final int WORM_SEGMENT_COUNT = 112;
  public static final double WORM_SPEED = (3.0 / 2048.0);
  public static final double WORM_THICKNESS = (4.0 / 256.0);
  public static final double WORM_TWISTINESS = (4.0 / 256.0);
  public static double g_wormLateralSpeed = WORM_LATERAL_SPEED;
  public static double g_wormSegmentLength = WORM_SEGMENT_LENGTH;
  public static int g_wormSegmentCount = WORM_SEGMENT_COUNT;
  public static double g_wormSpeed = WORM_SPEED;
  public static double g_wormThickness = WORM_THICKNESS;
  public static double g_wormTwistiness = WORM_TWISTINESS;
  public static int g_curWormCount = 32;

  public static final int MAX_WORM_COUNT = 1024;
  public static Worms[] g_wormArray = new Worms[MAX_WORM_COUNT];

  // locals
  Location m_headNoisePos;
  Perlin m_noise;
  Vector2D m_headScreenPos;
  double m_lateralSpeed;
  int m_segmentCount;
  double m_segmentLength;
  double m_speed;
  double m_thickness;
  double m_twistiness;

  public Worms() {
    m_headNoisePos.setX(7.0 / 2048.0);
    m_headNoisePos.setY(1163.0 / 2048.0);
    m_headNoisePos.setZ(409.0 / 2048.0);

    m_noise.setSeed(0);
    m_noise.setFrequency(1.0D);
    m_noise.setLacunarity(2.375D);
    m_noise.setOctaveCount(3);
    m_noise.setPersistence(0.5D);
    m_noise.setNoiseQuality(NoiseQuality.STANDARD);

    m_lateralSpeed = WORM_LATERAL_SPEED;
    m_segmentCount = WORM_SEGMENT_COUNT;
    m_segmentLength = WORM_SEGMENT_LENGTH;
    m_speed = WORM_SPEED;
    m_thickness = WORM_THICKNESS;
    m_twistiness = WORM_TWISTINESS;
  }

  public double getTaperAmount(int segment) {
    double curSegment = (double) segment;
    double segmentCount = (double) m_segmentCount;
    double halfSegmentCount = segmentCount / 2.0;
    double baseTaperAmount = 1.0 - Math.abs((curSegment / halfSegmentCount) - 1.0);
    return Math.sqrt(baseTaperAmount); // sqrt better defines the tapering.
  }

  public void setHeadScreenPos(Vector2D pos) {
    m_headScreenPos = pos;
  }

  public void setLateralSpeed(double lateralSpeed) {
    m_lateralSpeed = lateralSpeed;
  }

  public void setSeed(int seed) {
    m_noise.setSeed(seed);
  }

  public void setSegmentCount(double segmentCount) {
    m_segmentCount = (int) segmentCount;
  }

  public void setSegmentLength(double segmentLength) {
    m_segmentLength = segmentLength;
  }

  public void setSpeed(double speed) {
    m_speed = speed;
  }

  public void setThickness(double thickness) {
    m_thickness = thickness;
  }

  public void setTwistiness(double twistiness) {
    m_twistiness = twistiness;
  }

  public void draw() {
    Vector2D curSegmentScreenPos = m_headScreenPos;
    Vector2D offsetPos = new Vector2D();
    Vector curNoisePos = new Vector();
    Vector2D curNormalPos = new Vector2D();

    for (int curSegment = 0; curSegment < m_segmentCount; curSegment++) {
      curNoisePos.setX(m_headNoisePos.getX() + (curSegment * m_twistiness));
      curNoisePos.setY(m_headNoisePos.getY());
      curNoisePos.setZ(m_headNoisePos.getZ());
      double noiseValue = m_noise.getValue(curNoisePos.getX(), curNoisePos.getY(), curNoisePos.getZ());
      double taperAmount = getTaperAmount(curSegment) * m_thickness;

      offsetPos.x = Math.cos(noiseValue * 2.0 * Math.PI);
      offsetPos.y = Math.sin(noiseValue * 2.0 * Math.PI);

      curNormalPos.x = (-offsetPos.y) * taperAmount;
      curNormalPos.y = (offsetPos.x) * taperAmount;

      offsetPos.x *= m_segmentLength;
      offsetPos.y *= m_segmentLength;

      // double x0 = (double) (curSegmentScreenPos.x + curNormalPos.x);
      // double y0 = (double) (curSegmentScreenPos.y + curNormalPos.y);
      // double x1 = (double) (curSegmentScreenPos.x - curNormalPos.x);
      // double y1 = (double) (curSegmentScreenPos.y - curNormalPos.y);

      // Draw the segment using OpenGL.
      // glTexCoord2f((float) curSegment, 0.0f);
      // glVertex2d(x0, y0);
      // glTexCoord2f((float) curSegment, 1.0f);
      // glVertex2d(x1, y1);
      curSegmentScreenPos.x += offsetPos.x;
      curSegmentScreenPos.y += offsetPos.y;
    }
  }

  public void update() {
    double noiseValue = m_noise.getValue(m_headNoisePos.getX(), m_headNoisePos.getY(), m_headNoisePos.getZ());
    m_headScreenPos.x -= (Math.cos(noiseValue * 2.0 * Math.PI) * m_speed);
    m_headScreenPos.y -= (Math.sin(noiseValue * 2.0 * Math.PI) * m_speed);
    m_headNoisePos.setX(m_headNoisePos.getX() - (m_speed * 2));
    m_headNoisePos.setY(m_headNoisePos.getY() + m_lateralSpeed);
    m_headNoisePos.setZ(m_headNoisePos.getZ() + m_lateralSpeed);
  }
  /*
   * Worm oluşturan zımbırtı for (int i = 0; i < MAX_WORM_COUNT; i++) { Vector2D
   * pos; pos.x = noise::ValueNoise3D (i + 1000, i + 2000, i + 3000); pos.y =
   * noise::ValueNoise3D (i + 1001, i + 2001, i + 3001); g_wormArray[i].SetSeed
   * (i); g_wormArray[i].SetHeadScreenPos (pos); }
   * 
   * çizmek için de g_wormArray[i].draw(); g_wormArray[i].update();
   */
}