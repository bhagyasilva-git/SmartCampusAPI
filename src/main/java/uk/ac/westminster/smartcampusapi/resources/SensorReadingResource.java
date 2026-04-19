/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.resources;

/**
 *
 * @author L E N O V O
 */
import uk.ac.westminster.smartcampusapi.Sensor;
import uk.ac.westminster.smartcampusapi.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import uk.ac.westminster.smartcampusapi.Sensor;
import uk.ac.westminster.smartcampusapi.SensorReading;
import uk.ac.westminster.smartcampusapi.exceptions.SensorUnavailableException;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorReadingResource {

    private int sensorId;

    // shared sensor store (same as SensorResource)
    private static List<Sensor> sensors = SensorResourceHelper.sensors;

    public SensorReadingResource(int sensorId) {
        this.sensorId = sensorId;
    }

    // GET /readings
    @GET
    public Response getReadings() {
        Sensor sensor = findSensor(sensorId);

        return Response.ok(sensor.getReadings()).build();
    }

    // POST /readings
    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = findSensor(sensorId);

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor in maintenance");
        }

        
        // add reading to sensor history
        sensor.getReadings().add(reading);

        // 🔥 SIDE EFFECT: update current value
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }

    // helper method
    private Sensor findSensor(int id) {
        return sensors.stream()
                .filter(s -> Integer.parseInt(s.getId()) == sensorId)
                .findFirst()
                .orElseThrow(()
                        -> new WebApplicationException("Sensor not found", 404));
    }
}
