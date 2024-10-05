package uk.ac.ed.inf.PizzaDronz.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.inf.PizzaDronz.models.LngLat;
import uk.ac.ed.inf.PizzaDronz.models.LngLatPairRequest;
import uk.ac.ed.inf.PizzaDronz.models.NextPositionRequest;

@RestController
public class Controller {

    @GetMapping("/isAlive")
    public boolean isAlive(){
        return true;
    }

    @GetMapping("/uuid")
    public String uuid(){
        return "s2222816";
    }

    @PostMapping("/distanceTo")
    public ResponseEntity<Double> distanceTo(@RequestBody LngLatPairRequest lngLatPairRequest){
        if (lngLatPairRequest == null || !lngLatPairRequest.isValid()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double distance = calculateDistance(lngLatPairRequest);
        return new ResponseEntity<>(distance, HttpStatus.OK);
    }

    private double calculateDistance(LngLatPairRequest lngLatPairRequest) {
        double lng1 = lngLatPairRequest.getPosition1().getLng();
        double lat1 = lngLatPairRequest.getPosition1().getLat();
        double lng2 = lngLatPairRequest.getPosition2().getLng();
        double lat2 = lngLatPairRequest.getPosition2().getLat();

        // Euclid distance formula
        return Math.sqrt(Math.pow(lng2 - lng1, 2) + Math.pow(lat2 - lat1, 2));
    }

    @PostMapping("/isCloseTo")
    public ResponseEntity<Boolean> isCloseTo(@RequestBody LngLatPairRequest lngLatPairRequest){
        if (lngLatPairRequest == null || !lngLatPairRequest.isValid()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double distance = calculateDistance(lngLatPairRequest);

        if (distance <= 0.00015){
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else if (distance > 0.00015) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/nextPosition")
    public ResponseEntity<LngLat> nextPosition(@RequestBody NextPositionRequest nextPositionRequest) {
        if (nextPositionRequest == null || !nextPositionRequest.isValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LngLat nextPosition = calculateNextPosition(nextPositionRequest);
        return new ResponseEntity<>(nextPosition, HttpStatus.OK);
    }

    private LngLat calculateNextPosition(NextPositionRequest nextPositionRequest) {
        double angle = Math.toRadians(nextPositionRequest.getAngle()); // Convert angle to radians
        double deltaLng = 0.00015 * Math.cos(angle); // Calculate change in longitude
        double deltaLat = 0.00015 * Math.sin(angle); // Calculate change in latitude

        double newLng = nextPositionRequest.getStart().getLng() + deltaLng;
        double newLat = nextPositionRequest.getStart().getLat() + deltaLat;

        return new LngLat(newLng, newLat);
    }


}
