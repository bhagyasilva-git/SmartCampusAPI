/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import uk.ac.westminster.smartcampusapi.resources.DiscoveryResource;

import uk.ac.westminster.smartcampusapi.resources.RoomResource;
import uk.ac.westminster.smartcampusapi.resources.SensorReadingResource;
import uk.ac.westminster.smartcampusapi.resources.SensorResource;

/**
 *
 * @author L E N O V O
 */
@ApplicationPath("/api/v1")

public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // REGISTER ALL RESOURCES HERE
        classes.add(RoomResource.class);
        classes.add(DiscoveryResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorReadingResource.class);

        return classes;
    }
}
