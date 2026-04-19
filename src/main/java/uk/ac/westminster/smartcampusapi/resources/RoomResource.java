/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.resources;

/**
 *
 * @author L E N O V O
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import uk.ac.westminster.smartcampusapi.Room;
import uk.ac.westminster.smartcampusapi.exceptions.RoomNotEmptyException;

@Path("/rooms")   // 🔴 MUST be exactly this
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class RoomResource {

    @GET
    public String test() {
        return "WORKING";
    }
    // In-memory data store
    private static final Map<String, Room> rooms = new HashMap<>();

    // POST /api/v1/rooms
    @POST
    public Response createRoom(Room room) {

        // Validation
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Room ID is required")
                    .build();
        }

        // Conflict check
        if (rooms.containsKey(room.getId())) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("Room already exists")
                    .build();
        }

        rooms.put(room.getId(), room);

        return Response
                .status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    // GET /api/v1/rooms
    @GET
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public static boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    // GET /api/v1/rooms/{roomId}
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {

        Room room = rooms.get(roomId);

        if (room == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        return Response.ok(room).build();
    }

    // ---------------- DELETE ROOM ----------------
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {

        Room room = rooms.get(roomId);

        // If room does not exist → DELETE is still safe (idempotent)
        if (room == null) {
            return Response.noContent().build(); // 204
        }

        // Business rule: cannot delete room with sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId + " cannot be deleted because it has active sensors");
        }

        rooms.remove(roomId);

        return Response.noContent().build(); // 204 No Content
    }
}
