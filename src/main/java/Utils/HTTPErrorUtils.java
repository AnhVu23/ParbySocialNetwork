/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import HTTPError.HTTPError;

/**
 *
 * @author GamingPc
 */
public class HTTPErrorUtils {

    public static HTTPError badRequest(String errorMessage) {
        return new HTTPError("400", errorMessage);
    }

    public static HTTPError unauthorized(String errorMessage) {
        return new HTTPError("401", errorMessage);
    }

    public static HTTPError internalServerError(String errorMessage) {
        return new HTTPError("500", errorMessage);
    }

    public static HTTPError noContent(String errorMessage) {
        return new HTTPError("204", errorMessage);
    }
    
    public static HTTPError Ok(String errorMessage) {
        return new HTTPError("200", errorMessage);
    }
}
