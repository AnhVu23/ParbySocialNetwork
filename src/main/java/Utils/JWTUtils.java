/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.Key;
import java.io.UnsupportedEncodingException;
import java.util.Date; 


/**
 *
 * @author GamingPc
 */
public class JWTUtils {
    // Token expiration will be 3 days
    private static final int expiration = 3600*24*3;
    
    public static String createJWT(String userId) {
    try {
            Algorithm algorithm = Algorithm.HMAC256("parby");
                String token = JWT.create()
                    .withExpiresAt(new Date(expiration + System.currentTimeMillis()))
                    .withIssuer(userId)
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            return null;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return null;
        }
    }
    //Verify the token
    public static int parseJWT(String token) {
    //This line will throw an exception if it is not a signed JWS (as expected)
    try {
            System.out.println("token: " + token);
            DecodedJWT jwt = JWT.decode(token);
            String stringId = jwt.getIssuer();
            int userId = Integer.parseInt(stringId);
            return userId;
        } catch (JWTDecodeException exception){
            //Invalid token
            return -1;
        }
}
}
