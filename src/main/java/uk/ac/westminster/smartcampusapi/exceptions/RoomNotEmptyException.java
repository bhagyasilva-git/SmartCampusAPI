/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.smartcampusapi.exceptions;

/**
 *
 * @author L E N O V O
 */
public class RoomNotEmptyException extends RuntimeException    {

    public RoomNotEmptyException(String message) {
        super(message);
    }
}
