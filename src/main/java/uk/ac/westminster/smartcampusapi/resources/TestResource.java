/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author L E N O V O
 */
@Path("/test")

public class TestResource {

    @GET
    public String test() {
        return "OK";
    }
}
