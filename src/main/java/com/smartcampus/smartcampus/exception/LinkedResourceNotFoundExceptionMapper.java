/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exception;

import com.smartcampus.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author L E N O V O
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {

        ErrorMessage error = new ErrorMessage(
                ex.getMessage(),
                Status.BAD_REQUEST.getStatusCode(),
                "http://localhost:8080/api/docs/errors"
        );

        return Response.status(Status.BAD_REQUEST)
                .entity(error)
                .build();
    }

}
