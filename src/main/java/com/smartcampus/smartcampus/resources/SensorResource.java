/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resources;

import com.smartcampus.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.smartcampus.model.Sensor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author L E N O V O
 */
@Path("/sensors")
public class SensorResource {

    private static Map<String, Sensor> sensors = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {

        if (type == null) {
            return sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();
        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(sensor).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor) {

        if (sensor == null || sensor.getRoomId() == null || sensor.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid sensor data\"}")
                    .build();
        }

        // Validate room existence
        if (!RoomResource.roomExists(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        sensors.put(sensor.getId(), sensor);

        // Link sensor to room
        RoomResource.getRoom(sensor.getRoomId())
                .getSensorIds()
                .add(sensor.getId());

        return Response.status(Response.Status.CREATED).build();
    }

    // Sub-resource locator
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadings(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException();
        }
        return new SensorReadingResource(sensor);
    }

    public static Sensor getSensor(String sensorId) {
        return sensors.get(sensorId);
    }
}
