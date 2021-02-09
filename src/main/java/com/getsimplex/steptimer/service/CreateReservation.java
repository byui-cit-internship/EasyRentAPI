package com.getsimplex.steptimer.service;

import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.Reservation;
import com.getsimplex.steptimer.utils.JedisClient;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import spark.Request;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class CreateReservation {
    private static JedisClient jedisClient = new JedisClient();
    private static Gson gson = new Gson();

    public static String handleRequest(Request request) throws Exception{
        String newReservationRequest = request.body();
        Reservation newReservation = gson.fromJson(newReservationRequest, Reservation.class);
        String uuid = UUID.randomUUID().toString();
        newReservation.setReservationId(uuid);
        return createReservation(newReservation);

    }

    public static String createReservation(Reservation newReservation) throws Exception{

        if (newReservation != null && newReservation.getReservationId() != null && newReservation.getReservationItems() != null && !newReservation.getReservationItems().isEmpty() && newReservation.getCustomerId() != null) {
           Optional customerOptional = FindCustomer.findCustomer(newReservation.getCustomerId());
           if (!customerOptional.isPresent()){
               throw new Exception("Invalid Customer " + gson.toJson(newReservation));
           }
           //SAVE USER TO REDIS
            JedisData.loadToJedis(newReservation, Reservation.class);
        }else{
            throw new Exception("Invalid Request " + gson.toJson(newReservation));
        }
        return "Created: " + newReservation.getReservationId();
    }

}