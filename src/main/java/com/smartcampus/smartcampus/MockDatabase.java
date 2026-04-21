/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus;

import com.smartcampus.smartcampus.model.Room;
import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author L E N O V O
 */
public class MockDatabase {

    // Core data collections
    public static final Map<String, Room> ROOMS = new HashMap<>();
    public static final Map<String, Sensor> SENSORS = new HashMap<>();
    public static final Map<String, List<SensorReading>> SENSOR_READINGS = new HashMap<>();

    static {
        // --------------------
        // Initialise Rooms
        // --------------------
        Room room1 = new Room("LIB-301", "Library Quiet Study", 120);
        Room room2 = new Room("ENG-105", "Engineering Lab", 60);

        ROOMS.put(room1.getId(), room1);
        ROOMS.put(room2.getId(), room2);

        // --------------------
        // Initialise Sensors
        // --------------------
        Sensor sensor1 = new Sensor(
                "TEMP-001",
                "Temperature",
                "ACTIVE",
                22.5,
                "LIB-301"
        );

        Sensor sensor2 = new Sensor(
                "CO2-002",
                "CO2",
                "MAINTENANCE",
                415.0,
                "ENG-105"
        );

        SENSORS.put(sensor1.getId(), sensor1);
        SENSORS.put(sensor2.getId(), sensor2);

        // Link sensors to rooms
        room1.getSensorIds().add(sensor1.getId());
        room2.getSensorIds().add(sensor2.getId());

        // --------------------
        // Initialise Sensor Readings
        // --------------------
        List<SensorReading> tempReadings = new ArrayList<>();
        tempReadings.add(new SensorReading(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                22.5
        ));

        SENSOR_READINGS.put(sensor1.getId(), tempReadings);

        // No readings yet for sensor2
        SENSOR_READINGS.put(sensor2.getId(), new ArrayList<>());
    }
}
