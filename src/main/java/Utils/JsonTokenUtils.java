/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author GamingPc
 */
public class JsonTokenUtils {
    public static String tokenTransform(String token) {
        String jsonToken = "{\"" + "authorization"+ "\":" + "\"" + token + "\"}";
        return jsonToken;
    }
}
