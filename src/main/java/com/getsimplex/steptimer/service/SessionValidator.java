package com.getsimplex.steptimer.service;
import com.getsimplex.steptimer.utils.JedisData;
import com.getsimplex.steptimer.model.Token;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

/**
 * Created by sean on 8/12/2016.
 */
public class SessionValidator {

    public static HashMap<org.eclipse.jetty.websocket.api.Session, String> sessionTokens = new HashMap<org.eclipse.jetty.websocket.api.Session, String>();


    public static Token getMachineToken(String tokenValue) throws Exception{
        Token token = JedisData.get(tokenValue, Token.class);
        return token;
    }

    public static Boolean validateToken(String tokenString) throws Exception{
        return TokenService.validateToken(tokenString);
    }



}
