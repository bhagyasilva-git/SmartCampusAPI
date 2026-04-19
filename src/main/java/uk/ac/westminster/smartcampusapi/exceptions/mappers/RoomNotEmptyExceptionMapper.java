/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.exceptions.mappers;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import uk.ac.westminster.smartcampusapi.exceptions.RoomNotEmptyException;

/**
 *
 * @author L E N O V O
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Room not empty");
        error.put("message", ex.getMessage());

        return Response.status(Response.Status.CONFLICT) // 409
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
