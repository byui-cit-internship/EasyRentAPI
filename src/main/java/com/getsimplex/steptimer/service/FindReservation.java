package com.getsimplex.steptimer.service;


import com.getsimplex.steptimer.model.Reservation;
import com.getsimplex.steptimer.utils.JedisClient;
import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import spark.Request;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Mandy on 10/4/2016.
 */
public class FindReservation {

    private static Gson gson = new Gson();
    private static JedisClient jedisClient = new JedisClient();

    public static String handleRequest(Request request) throws Exception{
        String reservationId = request.params(":reservationId");
        if (reservationId != null && !reservationId.isEmpty()){
            Optional<Reservation> matchingReservation = findReservation(reservationId);

            if (matchingReservation.isPresent()) {
                return gson.toJson(matchingReservation.get());
            } else {
                return null;
            }
        }else{
            return gson.toJson(getAllReservations());
        }
    }

    public static Optional<Reservation> findReservation(String reservationId) throws Exception{
        List<Reservation> reservations = JedisData.getEntityList(Reservation.class);
        Predicate<Reservation> findExistingReservationPredicate = reservation -> reservation.getReservationId().equals(reservationId);
        Optional<Reservation> matchingReservation = reservations.stream().filter(findExistingReservationPredicate).findAny();
        return matchingReservation;
    }
    public static List<Reservation> getAllReservations() throws Exception{
        List<Reservation> reservations = JedisData.getEntityList(Reservation.class);
        return reservations;
    }
}
