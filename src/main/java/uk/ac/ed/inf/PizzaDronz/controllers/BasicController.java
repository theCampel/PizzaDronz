package uk.ac.ed.inf.PizzaDronz.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("/isAlive")
    public boolean isAlive(){
        return true;
    }

    @GetMapping("/studentID/{name}")
    public String studentId(@PathVariable String name){
        // TODO: check for bad input and detail this better
        if(name.equalsIgnoreCase("cameron")){
            return "s2222817";
        }
        else {
            return "Sorry - not in database";
        }
    }
}
