package com.getsimplex.steptimer.service;


/**
 * Created by sean on 8/10/2016 based on https://github.com/tipsy/spark-websocket/tree/master/src/main/java
 */


import akka.actor.dsl.Inbox;
import com.getsimplex.steptimer.model.*;
import com.getsimplex.steptimer.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Optional;

import static spark.Spark.*;

public class WebAppRunner {

    public static void main(String[] args){
        Spark.port(getHerokuAssignedPort());
        staticFileLocation("/public");
        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, PATCH, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        Long now = (System.currentTimeMillis());
        System.out.println("The current date is: "+ (now));
        System.out.println("The config is located here: "+System.getProperty("config"));



        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
        });

    
		//secure("/Applications/steptimerwebsocket/keystore.jks","password","/Applications/steptimerwebsocket/keystore.jks","password");

        //post("/sensorUpdates", (req, res)-> WebServiceHandler.routeDeviceRequest(req));
        //post("/generateHistoricalGraph", (req, res)->routePdfRequest(req, res));
        //get("/readPdf", (req, res)->routePdfRequest(req, res));
        post("/user", (req, res)-> {
            res.type("application/json");
            return callUserDatabase(req);
        });
        post("/reservations",((request, response) -> {
            response.type("application/json");
            return CreateReservation.handleRequest(request);
        }));

        put("/reservations",((request, response) ->{
            try {
                response.type("application/json");
                UpdateReservation.handleRequest(request);
            } catch (Exception e){
                response.status(500);
                response.body(e.getMessage());
            }
            return "Ok";
        }));


        get ("/stephistory/:customer", (req, res)-> {
//            try{
//                userFilter(req, res);
//            } catch (Exception e){
//                res.redirect("/");
//            }
            res.type("application/json");
            return null;
        });
        post("/customer", (req, res)-> {

            try {
//                userFilter(req, res);
                createNewCustomer(req, res);

            } catch (Exception e){
                System.out.println("*** Error Creating Customer: "+e.getMessage());
                res.status(500);
                res.body(e.getMessage());
            }
            res.type("application/json");
            return null;
        });
        get("/customer/:customer    ", (req, res)-> {
            try {
//                userFilter(req, res);
            } catch (Exception e){
                res.status(401);
                System.out.println("*** Error Finding Customer: "+e.getMessage());
                return null;
            }
            res.type("application/json");
            return FindCustomer.handleRequest(req);

        });

        get("/customer", (req, res)-> {
            try {
//                userFilter(req, res);
            } catch (Exception e){
                res.status(401);
                System.out.println("*** Error Finding Customer: "+e.getMessage());
                return null;
            }
            res.type("application/json");
            return GetAllCustomers.handleRequest(req);

        });

        get("/reservations/:reservationId", (req, res)-> {
            try {
//                userFilter(req, res);
            } catch (Exception e){
                res.status(401);
                System.out.println("*** Error Finding Reservation : "+e.getMessage());
                return null;
            }
            res.type("application/json");
            return FindReservation.handleRequest(req);

        });
        get("/reservations", (req, res)-> {
            try {
//                userFilter(req, res);
            } catch (Exception e){
                res.status(401);
                System.out.println("*** Error Finding Reservation : "+e.getMessage());
                return null;
            }
            res.type("application/json");
            return FindReservation.handleRequest(req);

        });
        post("/login", (req, res)->{
            res.type("application/json");
            return loginUser(req);
        });
        post("/rapidsteptest", (req, res)->{
            try{
//                userFilter(req, res);
            } catch (Exception e){
                res.status(401);
            }

            res.type("application/json");
            return "Saved";
        });
        get("/riskscore/:customer",((req,res) -> {
            try{
//                          userFilter(req, res);
            } catch (Exception e){
                res.status(401);
                System.out.println("*** Error Finding Risk Score: "+e.getMessage());
                throw e;
            }
            res.type("application/json");
            return null;
        }));
        //post ("/sensorTail",(req,res) -> saveTail(req,res) );

        init();
    }

    private static void userFilter(Request request, Response response) throws Exception{
        String tokenString = request.headers("suresteps.session.token");

            Optional<User> user = TokenService.getUserFromToken(tokenString);//

            Boolean tokenExpired = SessionValidator.validateToken(tokenString);

            if (user.isPresent() && tokenExpired && !user.get().isLocked()){//if a user is locked, we won't renew tokens until they are unlocked
                TokenService.renewToken(tokenString);
                return;
            }

            if (!user.isPresent()) { //Check to see if session expired
                throw new Exception("Invalid user token: user not found using token: "+tokenString);
            }

            if (tokenExpired.equals(true)){
                throw new Exception("Invalid user token: "+tokenString+" expired");
            }

    }




    public static void createNewCustomer(Request request, Response response) throws Exception{
            CreateNewCustomer.handleRequest(request);
    }

    private static String callUserDatabase(Request request)throws Exception{
        return CreateNewUser.handleRequest(request);
    }

    private static String loginUser(Request request) throws Exception{
        return LoginController.handleRequest(request);

    }


    public static String authenticateSession(Request request) throws Exception{

        String tokenString = request.headers("suresteps.session.token");

        Optional<User> user = TokenService.getUserFromToken(tokenString);//

        if (!user.isPresent()){
            throw new Exception("Could not find user with token");
        }
        else {
            return user.get().getUserName();
        }

    }
	
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return Configuration.getConfiguration().getInt("suresteps.port"); //return default port if heroku-port isn't set (i.e. on localhost)
    }

    private static void createTestUser(){//for Udacity course local use only

        try {
            User user = new User();
            user.setUserName("clinicmanager");
            user.setPassword("Cl1n1cM@n@ger");
            user.setVerifyPassword("Cl1n1cM@n@ger");
            user.setAccountType("personal");
            CreateNewUser.createUser(user);
        } catch (Exception e){
            System.out.println("Unable to create test user due to exception: "+e.getMessage());
        }
    }

    private static void createTestCustomer() {
        try{
            Customer customer = new Customer();
            customer.setCustomerName("Steady Senior");
            customer.setEmail("steady@stedi.fit");
            customer.setPhone("8015551212");
            customer.setBirthDay("1901-01-01");
            CreateNewCustomer.createCustomer(customer);
        }
        catch (Exception e){
            System.out.println("Unable to create customer due to exception: "+e.getMessage());
        }
    }

}
