/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.exceptions.mappers;

/**
 *
 * @author L E N O V O
 */
import uk.ac.westminster.smartcampusapi.exceptions.LinkedResourceNotFoundException;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;


@Provider

public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "INVALID_ROOM_REFERENCE");
        error.put("message", ex.getMessage());

        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
