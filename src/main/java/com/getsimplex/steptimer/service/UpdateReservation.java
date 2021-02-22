package com.getsimplex.steptimer.service;


import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.Reservation;
import com.getsimplex.steptimer.utils.JedisClient;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import spark.Request;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Mandy on 10/4/2016.
 */
public class UpdateReservation {

    private static Gson gson = new Gson();
    private static JedisClient jedisClient = new JedisClient();
    private static Object dueDateGreaterThan;

    public static void handleRequest(Request request) throws Exception {
        Reservation updated = gson.fromJson(request.body(), Reservation.class);
        if (updated.getReservationId()==null || updated.getReservationId().isEmpty()){
            throw new Exception("Reservation does not contain Reservation Id: "+ request.body());
        }
        updateReservation(updated);
    }

    private static void updateReservation(Reservation updated) throws Exception{
        if (updated != null && updated.getReservationId() != null && updated.getReservationItems() != null && !updated.getReservationItems().isEmpty() && updated.getCustomerId() != null)
        {
            JedisData.update(updated, updated.getReservationId());
        }
            throw new Exception ("Invalid JSON (customerId, reservationId, and reservationItems are required fields: "+gson.toJson(updated));
        }
    }

}
