package com.getsimplex.steptimer.service;


import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.Reservation;
import com.getsimplex.steptimer.utils.JedisClient;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Mandy on 10/4/2016.
 */
public class DeleteReservation {

    private static Gson gson = new Gson();
    private static JedisClient jedisClient = new JedisClient();
    private static Object dueDateGreaterThan;

    public static void handleRequest(Request request, Response response) throws Exception {
        String reservationId = request.params(":reservationId");
        Boolean deleted = deleteReservationByReservationId(reservationId);
        response.body(gson.toJson(deleted));
        return;
    }

    private static List<Reservation> findReservationByDueDate(Long dueDateGreaterThan, Long dueDateLessThan) throws Exception {

        List<Reservation> reservations = JedisData.getEntities(Reservation.class, dueDateGreaterThan, dueDateLessThan);
        for (Reservation reservation: reservations) {
            Optional<Customer> matchingCustomer = JedisData.getEntity(Customer.class, reservation.getCustomerId().toLowerCase());
            reservation.setCustomerName(matchingCustomer.get().getCustomerName());
        }
        return reservations;
    }

    public static Boolean deleteReservationByReservationId(String reservationId) throws Exception {
        Long recordsDeleted= JedisData.deleteEntity("Reservation",reservationId);
        Boolean deletedRecord=recordsDeleted ==1;//should delete one and only one record
        return deletedRecord;
    }

    public static List<Reservation> getAllReservations() throws Exception{
        List<Reservation> reservations = JedisData.getEntities(Reservation.class);
        for (Reservation reservation: reservations) {
            Optional<Customer> matchingCustomer = JedisData.getEntity(Customer.class, reservation.getCustomerId().toLowerCase());
            reservation.setCustomerName(matchingCustomer.get().getCustomerName());
        }
        return reservations;
    }
}
