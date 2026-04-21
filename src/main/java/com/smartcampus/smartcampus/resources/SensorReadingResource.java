/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resources;

import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author L E N O V O
 */
public class SensorReadingResource {

    private Sensor sensor;
    private static Map<String, List<SensorReading>> readings = new HashMap<>();

    public SensorReadingResource(Sensor sensor) {
        this.sensor = sensor;
        readings.putIfAbsent(sensor.getId(), new ArrayList<>());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getAllReadings() {
        return readings.get(sensor.getId());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid reading\"}")
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Sensor under maintenance\"}")
                    .build();
        }

        reading.setTimestamp(System.currentTimeMillis());

        List<SensorReading> sensorReadings = readings.get(sensor.getId());
        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            readings.put(sensor.getId(), sensorReadings);
        }

        sensorReadings.add(reading);

        // Side effect: update current value
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).build();
    }
}
