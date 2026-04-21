/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.smartcampus.smartcampus.model.ErrorMessage;

/**
 *
 * @author L E N O V O
 */
@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage error = new ErrorMessage(
                exception.getMessage(),
                Status.NOT_FOUND.getStatusCode(),
                "http://localhost:8080/api/docs/errors"
        );

        return Response.status(Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}
