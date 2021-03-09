package com.getsimplex.steptimer.service;


import com.getsimplex.steptimer.model.Customer;
import com.getsimplex.steptimer.model.Reservation;
import com.getsimplex.steptimer.utils.JedisClient;
import com.getsimplex.steptimer.utils.JedisData;
import com.google.gson.Gson;
import spark.Request;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Mandy on 10/4/2016.
 */
public class FindReservation {

    private static Gson gson = new Gson();
    private static JedisClient jedisClient = new JedisClient();
    private static Object dueDateGreaterThan;

    public static String handleRequest(Request request) throws Exception {
        String reservationId = request.params(":reservationId");
        String dueDateGreaterThanString = request.queryParams("dueDateGreaterThan");
        String dueDateLessThanString = request.queryParams("dueDateLessThan");

        if (reservationId != null && !reservationId.isEmpty()) {
            Optional<Reservation> matchingReservation = findReservationByReservationId(reservationId);

            if (matchingReservation.isPresent()) {
                return gson.toJson(matchingReservation.get());
            } else {
                return null;
            }

        // this used to require both parameters startingDate and EndingDate
        // BUT we realized it is easier to pass only one date from the UI
        // so if they pass a greaterthan date, we assume we want everything greater than that date
        // if they pass a lessthan date, we assume we want everything less than that date
        } else if (dueDateGreaterThanString != null || dueDateLessThanString != null ) {
            Date oneYearInFuture = new Date();
            oneYearInFuture = new Date(oneYearInFuture.getTime()+365*24*60*60*1000);
            Long dueDateLessThan = oneYearInFuture.getTime();

            Date oneYearInPast = new Date();
            oneYearInPast = new Date(Long.valueOf(oneYearInPast.getTime()-365*24*60*60*1000));
            Long dueDateGreaterThan = oneYearInPast.getTime();

            if (dueDateGreaterThanString != null && !dueDateGreaterThanString.isEmpty()){

                dueDateGreaterThan = Long.valueOf((dueDateGreaterThanString));
            }

            if (dueDateLessThanString!=null && !dueDateLessThanString.isEmpty()){
                dueDateLessThan = Long.valueOf((dueDateLessThanString));
            }

            List<Reservation> matchingReservations = findReservationByDueDate(dueDateGreaterThan, dueDateLessThan);
            return gson.toJson(matchingReservations);

        } else return gson.toJson(getAllReservations());
    }

    private static List<Reservation> findReservationByDueDate(Long dueDateGreaterThan, Long dueDateLessThan) throws Exception {

        List<Reservation> reservations = JedisData.getEntities(Reservation.class, dueDateGreaterThan, dueDateLessThan);
        return reservations;
    }

    public static Optional<Reservation> findReservationByReservationId(String reservationId) throws Exception {
        return JedisData.getEntity(Reservation.class, reservationId);
    }

    public static List<Reservation> getAllReservations() throws Exception{
        List<Reservation> reservations = JedisData.getEntities(Reservation.class);
        List<Customer> customers = GetAllCustomers.getCustomers();
        for (Reservation reservation: reservations) {
            Predicate<Customer> findExistingCustomerPredicate = customer -> customer.getEmail().equals(reservation.getCustomerId());
            Optional<Customer> matchingCustomer = customers.stream().filter(findExistingCustomerPredicate).findAny();
            reservation.setCustomerName(matchingCustomer.get().getCustomerName());
        }
        return reservations;
    }
}
