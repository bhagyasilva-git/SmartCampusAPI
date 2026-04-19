/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.resources;

/**
 *
 * @author L E N O V O
 */
import uk.ac.westminster.smartcampusapi.exceptions.RoomNotEmptyException;
import uk.ac.westminster.smartcampusapi.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.ac.westminster.smartcampusapi.exceptions.LinkedResourceNotFoundException;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorResource {

    // temporary in-memory storage
    private static final Map<String, Sensor> sensors = new HashMap<>();
    private static int idCounter = 1;

    @Path("{sensorId}/readings")
    public SensorReadingResource getSensorReadingsResource(@PathParam("sensorId") int sensorId) {
        return new SensorReadingResource(sensorId);
    }

    // pretend this is your room database
    private static List<Integer> roomIds = List.of(1, 2, 3, 4);

    @POST
    public Response createSensor(Sensor sensor) {

        // Room validation (Part 5 – 422)
        if (!RoomResource.roomExists(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        sensors.put(sensor.getId(), sensor);

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    public Response getAllSensors() {
        return Response.ok(sensors).build();
    }

    @GET
    public Response getSensors(@QueryParam("type") String type) {

        List<Sensor> result = new ArrayList<>(sensors.values());

        if (type != null && !type.isEmpty()) {
            result = result.stream()
                    .filter(s -> s.getType() != null
                    && s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(result).build();
    }

    // Used by sub-resource
    public static Sensor getSensor(int id) {
        return sensors.get(id);
    }

}
