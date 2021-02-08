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
        } else if (dueDateGreaterThanString != null && dueDateLessThanString != null ) {
            Long dueDateGreaterThan = Long.valueOf((dueDateGreaterThanString));
            Long dueDateLessThan = Long.valueOf((dueDateLessThanString));
            Optional<Reservation> matchingReservation = findReservationByDueDate(dueDateGreaterThan, dueDateLessThan);

            if (matchingReservation.isPresent()) {
                return gson.toJson(matchingReservation.get());
            } else {
                return null;
            }
        } else return gson.toJson(getAllReservations());
    }

    private static Optional<Reservation> findReservationByDueDate(Long dueDateGreaterThan, Long dueDateLessThan) throws Exception {
        List<Reservation> reservations = JedisData.getEntityList(Reservation.class);
        Predicate<Reservation> findExistingReservationPredicate = reservation -> reservation.getDueDate() > dueDateGreaterThan && reservation.getDueDate() < dueDateLessThan;
        Optional<Reservation> matchingReservation   = reservations.stream().filter(findExistingReservationPredicate).findAny();
        return matchingReservation;
    }

    public static Optional<Reservation> findReservationByReservationId(String reservationId) throws Exception {
        List<Reservation> reservations = getAllReservations();
        Predicate<Reservation> findExistingReservationPredicate = reservation -> reservation.getReservationId().equals(reservationId);
        Optional<Reservation> matchingReservation = reservations.stream().filter(findExistingReservationPredicate).findAny();
        return matchingReservation;
    }

    public static List<Reservation> getAllReservations() throws Exception{
        List<Reservation> reservations = JedisData.getEntityList(Reservation.class);
        List<Customer> customers = GetAllCustomers.getCustomers();
        for (Reservation reservation: reservations) {
            Predicate<Customer> findExistingCustomerPredicate = customer -> customer.getEmail().equals(reservation.getCustomerId());
            Optional<Customer> matchingCustomer = customers.stream().filter(findExistingCustomerPredicate).findAny();
            reservation.setCustomerName(matchingCustomer.get().getCustomerName()) ;
        }
        return reservations;
    }
}
