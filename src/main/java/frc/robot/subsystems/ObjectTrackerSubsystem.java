package frc.robot.subsystems;

import com.google.gson.Gson;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class ObjectTrackerSubsystem extends SubsystemBase {

  private final NetworkTable monsterVision;
  private String jsonString;
  private final String source;
  private final Gson gson = new Gson();

  public double visionZ;
  public double visionX;
  public double visionY;
  public double visionYa;

  public DetectionList yoloObjects;
  public DetectionList aprilTags;

  public ObjectTrackerSubsystem(String source) {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();

    this.source = source;
    monsterVision = inst.getTable("MonsterVision");

    jsonString = "";

    yoloObjects = new DetectionList();
    aprilTags = new DetectionList();
  }

  public void data() {
    NetworkTableEntry entry =
        monsterVision.getEntry("ObjectTracker-" + source);

    jsonString = entry.getString("[]");

    try {
      updateDetections(jsonString, gson);

      Detection nearestTag = getNearestAprilTagDetection();

      if (nearestTag != null) {
        SmartDashboard.putNumber("VisionX", nearestTag.x);
        SmartDashboard.putNumber("VisionY", nearestTag.y);
        SmartDashboard.putNumber("VisionZ", nearestTag.z);
        SmartDashboard.putNumber("VisionYa", nearestTag.ya);
      }

    } catch (Exception e) {
      // Ignore bad/missing vision data for this loop
    }
  }

  // ------------------------------------------------------------
  // Vision values
  // ------------------------------------------------------------

  private double getVisionValue(
      Detection detection,
      ToDoubleFunction<Detection> valueGetter) {

    return detection == null
        ? 0
        : valueGetter.applyAsDouble(detection);
  }

  private double getVisionValue(
      int tagId,
      ToDoubleFunction<Detection> valueGetter) {

    return getVisionValue(
        getSpecificAprilTag(tagId),
        valueGetter);
  }

  private double getVisionValue(
      int[] tagIds,
      ToDoubleFunction<Detection> valueGetter) {

    for (int tagId : tagIds) {

      Detection detection =
          getSpecificAprilTag(tagId);

      if (detection != null) {
        return valueGetter.applyAsDouble(detection);
      }
    }

    return 0;
  }

  public double getVisionX() {
    visionX =
        getVisionValue(
            getNearestAprilTagDetection(),
            detection -> detection.x);

    return visionX;
  }

  public double getVisionY() {
    visionY =
        getVisionValue(
            getNearestAprilTagDetection(),
            detection -> detection.y);

    return visionY;
  }

  public double getVisionZ() {
    visionZ =
        getVisionValue(
            getNearestAprilTagDetection(),
            detection -> detection.z);

    return visionZ;
  }

  public double getVisionYa() {
    visionYa =
        getVisionValue(
            getNearestAprilTagDetection(),
            detection -> detection.ya);

    return visionYa;
  }

  public double getVisionX(int tagId) {
    return getVisionValue(
        tagId,
        detection -> detection.x);
  }

  public double getVisionY(int tagId) {
    return getVisionValue(
        tagId,
        detection -> detection.y);
  }

  public double getVisionZ(int tagId) {
    return getVisionValue(
        tagId,
        detection -> detection.z);
  }

  public double getVisionYa(int tagId) {
    return getVisionValue(
        tagId,
        detection -> detection.ya);
  }

  public double getVisionX(int[] tagIds) {
    return getVisionValue(
        tagIds,
        detection -> detection.x);
  }

  public double getVisionY(int[] tagIds) {
    return getVisionValue(
        tagIds,
        detection -> detection.y);
  }

  public double getVisionZ(int[] tagIds) {
    return getVisionValue(
        tagIds,
        detection -> detection.z);
  }

  public double getVisionYa(int[] tagIds) {
    return getVisionValue(
        tagIds,
        detection -> detection.ya);
  }

  // ------------------------------------------------------------
  // AprilTag positioning
  // ------------------------------------------------------------

  public Pose2d getDistVector(
      double xPrime,
      double zPrime,
      double finalYa,
      int tagId) {

    Detection detection =
        getSpecificAprilTag(tagId);

    if (detection == null) {

      SmartDashboard.putBoolean(
          "ableToSeeAT",
          false);

      return new Pose2d(
          0,
          0,
          new Rotation2d(0));
    }

    SmartDashboard.putBoolean(
        "ableToSeeAT",
        true);

    double visionYa =
        Math.atan(
            detection.z
                / (detection.x + 0.00001));

    double xVt =
        xPrime
                * Math.cos(
                    Math.toRadians(visionYa))
            - zPrime
                * Math.sin(
                    Math.toRadians(visionYa));

    double zVt =
        xPrime
                * Math.sin(
                    Math.toRadians(visionYa))
            + zPrime
                * Math.cos(
                    Math.toRadians(visionYa));

    double deltaCamX =
        -(detection.x + xVt);

    double deltaCamY =
        -(detection.z + zVt);

    double finalAngle =
        visionYa + finalYa;

    return new Pose2d(
        Units.inchesToMeters(deltaCamX),
        Units.inchesToMeters(deltaCamY),
        new Rotation2d(
            Units.degreesToRadians(
                finalAngle)));
  }

  public Pose2d visionAutoData(
      double xPrime,
      double zPrime,
      double finalYa,
      int tagId) {

    Detection detection =
        getSpecificAprilTag(tagId);

    if (detection == null) {

      SmartDashboard.putBoolean(
          "ableToSeeAT",
          false);

      return null;
    }

    SmartDashboard.putBoolean(
        "ableToSeeAT",
        true);

    double visionYa =
        -detection.ya;

    double xVt =
        xPrime
                * Math.cos(
                    Math.toRadians(visionYa))
            - zPrime
                * Math.sin(
                    Math.toRadians(visionYa));

    double zVt =
        xPrime
                * Math.sin(
                    Math.toRadians(visionYa))
            + zPrime
                * Math.cos(
                    Math.toRadians(visionYa));

    double deltaRobotX =
        -(detection.x
            + xVt
            - Constants.CAM_X_OFFSET);

    double deltaRobotY =
        -(detection.z
            + zVt
            - Constants.CAM_Y_OFFSET);

    double botRadians = 0;

    double deltaFieldX =
        deltaRobotX
                * Math.cos(botRadians)
            - deltaRobotY
                * Math.sin(botRadians);

    double deltaFieldY =
        deltaRobotX
                * Math.sin(botRadians)
            + deltaRobotY
                * Math.cos(botRadians);

    double finalAngle =
        visionYa
            + finalYa
            + Units.radiansToDegrees(
                botRadians);

    return new Pose2d(
        Units.inchesToMeters(
            deltaFieldX),

        Units.inchesToMeters(
            deltaFieldY),

        new Rotation2d(
            Units.degreesToRadians(
                finalAngle)));
  }

  // ------------------------------------------------------------
  // Object / YOLO methods
  // ------------------------------------------------------------

  public String getObjectsJson() {
    return jsonString;
  }

  public int numberOfObjects() {

    if (yoloObjects == null) {
      return 0;
    }

    return yoloObjects.size();
  }

  public Detection[] getObjects(
      double minimumConfidence) {

    if (yoloObjects == null
        || yoloObjects.isEmpty()) {

      return null;
    }

    List<Detection> filtered =
        new ArrayList<>();

    for (Detection detection : yoloObjects) {

      if (detection != null
          && detection.confidence
              >= minimumConfidence) {

        filtered.add(detection);
      }
    }

    return filtered.toArray(
        new Detection[0]);
  }

  public Detection[] getObjectsOfType(
      String objectLabel) {

    if (yoloObjects == null
        || yoloObjects.isEmpty()) {

      return null;
    }

    List<Detection> filtered =
        new ArrayList<>();

    for (Detection detection : yoloObjects) {

      if (detection != null
          && detection.objectLabel != null
          && detection.objectLabel.contains(
              objectLabel)
          && detection.confidence > 0.40) {

        filtered.add(detection);
      }
    }

    return filtered.toArray(
        new Detection[0]);
  }

  public Detection getClosestObject(
      String objectLabel) {

    Detection[] objects =
        getObjectsOfType(objectLabel);

    if (objects == null
        || objects.length == 0) {

      return null;
    }

    return objects[0];
  }

  public Detection getClosestObject() {

    Detection[] objects =
        getObjects(0.5);

    if (objects == null
        || objects.length == 0) {

      return null;
    }

    return objects[0];
  }

  public Detection getSecondClosestObject(
      String objectLabel) {

    Detection[] objects =
        getObjectsOfType(objectLabel);

    if (objects == null
        || objects.length < 2) {

      return null;
    }

    return objects[1];
  }

  public Detection getNearestYoloDetection() {

    if (yoloObjects == null
        || yoloObjects.isEmpty()) {

      return null;
    }

    return yoloObjects.get(0);
  }

  public Detection[] getNearestYoloDetections(
      int count) {

    return getNearestDetections(
        yoloObjects,
        count);
  }

  // ------------------------------------------------------------
  // AprilTag methods
  // ------------------------------------------------------------

  public Detection getClosestAprilTag() {
    return getNearestAprilTagDetection();
  }

  public Detection getNearestAprilTagDetection() {

    if (aprilTags == null
        || aprilTags.isEmpty()) {

      return null;
    }

    return aprilTags.get(0);
  }

  public int getNearestAprilTag() {

    Detection detection =
        getNearestAprilTagDetection();

    if (detection == null
        || detection.objectLabel == null
        || detection.objectLabel.length()
            <= 10) {

      return -1;
    }

    try {

      return Integer.parseInt(
          detection.objectLabel.substring(10));

    } catch (NumberFormatException e) {

      return -1;
    }
  }

  public Detection getSpecificAprilTag(
      int id) {

    data();

    for (int i = 0;
        i < aprilTags.size();
        i++) {

      Detection currentAprilTag =
          aprilTags.get(i);

      if (currentAprilTag == null
          || currentAprilTag.objectLabel
              == null
          || currentAprilTag.objectLabel
                  .length()
              <= 10) {

        continue;
      }

      if (currentAprilTag.objectLabel
          .substring(10)
          .equals(
              String.valueOf(id))) {

        return currentAprilTag;
      }
    }

    return null;
  }

  public Detection getNearestAprilTagFromList(
      int[] ids) {

    data();

    for (int i = 0;
        i < aprilTags.size();
        i++) {

      Detection currentAprilTag =
          aprilTags.get(i);

      if (currentAprilTag == null
          || currentAprilTag.objectLabel
              == null
          || currentAprilTag.objectLabel
                  .length()
              <= 10) {

        continue;
      }

      for (int id : ids) {

        if (currentAprilTag.objectLabel
            .substring(10)
            .equals(
                String.valueOf(id))) {

          return currentAprilTag;
        }
      }
    }

    return null;
  }

  public Detection getAprilTagDetections(
      int[] tagIds) {

    for (int tagId : tagIds) {

      Detection detection =
          getSpecificAprilTag(tagId);

      if (detection != null) {
        return detection;
      }
    }

    return null;
  }

  public Detection[] getAllAprilTagDetections(
      int[] tagIds) {

    Detection[] detections =
        new Detection[tagIds.length];

    for (int i = 0;
        i < tagIds.length;
        i++) {

      detections[i] =
          getSpecificAprilTag(
              tagIds[i]);
    }

    return detections;
  }

  private Detection[] getNearestDetections(
      DetectionList detections,
      int count) {

    if (detections == null
        || count < 0
        || count > detections.size()) {

      return null;
    }

    Detection[] result =
        new Detection[count];

    for (int i = 0;
        i < count;
        i++) {

      result[i] =
          detections.get(i);
    }

    return result;
  }

  public Detection[] getNearestAprilTagsDetection(
      int count) {

    return getNearestDetections(
        aprilTags,
        count);
  }

  // ------------------------------------------------------------
  // Distance presets
  // ------------------------------------------------------------

  public Pose2d getNearestAprilTagDistShooter() {
    return getNearestAprilTagDist(0.6);
  }

  public Pose2d getNearestAprilTagDistTurret() {
    return getNearestAprilTagDist(0.8);
  }

  private Pose2d getNearestAprilTagDist(
      double forwardMeters) {

    int nearestTag =
        getNearestAprilTag();

    if (nearestTag == -1) {
      return new Pose2d();
    }

    double lateralMeters;

    if (nearestTag == 21
        || nearestTag == 26
        || nearestTag == 18
        || nearestTag == 10
        || nearestTag == 5
        || nearestTag == 2) {

      lateralMeters = 0;

    } else if (
        nearestTag == 25
            || nearestTag == 27
            || nearestTag == 9
            || nearestTag == 11) {

      lateralMeters = 0.35;

    } else if (
        nearestTag == 24
            || nearestTag == 8) {

      lateralMeters = -0.35;

    } else {

      return new Pose2d();
    }

    return getDistVector(
        Units.metersToInches(
            lateralMeters),

        Units.metersToInches(
            forwardMeters),

        0,
        nearestTag);
  }

  // ------------------------------------------------------------
  // Geometry
  // ------------------------------------------------------------

  public double getThetaYZField(
      Detection detection) {

    double camX = detection.x;
    double camZ = detection.z;
    double yCamAngle = detection.ya;

    double thetaYZ =
        Math.tanh(
            camZ / camX);

    return 90.0
        - yCamAngle
        - thetaYZ;
  }

  public double getYFieldAprilFromDetection(
      Detection detection) {

    return Math.cos(
            getThetaYZField(
                detection))
        * getRadius(detection);
  }

  public double getXFieldAprilFromDetection(
      Detection detection) {

    return Math.sin(
            getThetaYZField(
                detection))
        * getRadius(detection);
  }

  public double getRadius(
      Detection detection) {

    return Math.hypot(
        detection.x,
        detection.z);
  }

  // ------------------------------------------------------------
  // Snapshot
  // ------------------------------------------------------------

  private Detection[] getAllCurrentDetections() {

    int aprilCount =
        aprilTags == null
            ? 0
            : aprilTags.size();

    int yoloCount =
        yoloObjects == null
            ? 0
            : yoloObjects.size();

    Detection[] all =
        new Detection[
            aprilCount + yoloCount];

    int index = 0;

    if (aprilTags != null) {

      for (Detection detection : aprilTags) {
        all[index++] = detection;
      }
    }

    if (yoloObjects != null) {

      for (Detection detection : yoloObjects) {
        all[index++] = detection;
      }
    }

    return all;
  }

  public void saveVisionSnapshot(
      String fileName)
      throws IOException {

    data();

    try (BufferedWriter writer =
        new BufferedWriter(
            new FileWriter(fileName))) {

      writer.write(
          gson.toJson(
              getAllCurrentDetections()));
    }
  }

  public Detection[] loadVisionSnapshot(
      String fileName)
      throws IOException {

    String json =
        Files.readString(
            Path.of(fileName));

    return gson.fromJson(
        json,
        Detection[].class);
  }

  // ------------------------------------------------------------
  // Update detections from MonsterVision
  // ------------------------------------------------------------

  public void updateDetections(
      String detectionsString,
      Gson gson) {

    DetectionList gsonOut =
        gson.fromJson(
            detectionsString,
            DetectionList.class);

    if (gsonOut == null) {
      return;
    }

    String fpsString =
        monsterVision
            .getEntry(
                "ObjectTracker-fps")
            .getString("");

    if (fpsString.length() > 5) {

      try {

        SmartDashboard.putNumber(
            "CameraFPS",
            Double.parseDouble(
                fpsString.substring(5)));

      } catch (
          NumberFormatException ignored) {

        // Ignore bad FPS string
      }
    }

    aprilTags.clear();
    yoloObjects.clear();

    for (int i = 0;
        i < gsonOut.size();
        i++) {

      Detection detection =
          gsonOut.get(i);

      if (detection == null) {
        continue;
      }

      if (detection.objectLabel != null
          && detection.objectLabel
              .startsWith("tag")) {

        aprilTags.add(detection);

      } else {

        yoloObjects.add(detection);
      }
    }

    SmartDashboard.putBoolean(
        "AlgaeVisible",
        !yoloObjects.isEmpty());
  }

  @Override
  public void periodic() {
    data();
  }
}