/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HTTPError;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author GamingPc
 */
public class HTTPError implements Serializable{
    private String httpError;
    private String errorMessage;

    public HTTPError() {
    }

    public HTTPError(String httpError, String errorMessage) {
        this.httpError = httpError;
        this.errorMessage = errorMessage;
    }

    public String getHttpError() {
        return httpError;
    }

    public void setHttpError(String httpError) {
        this.httpError = httpError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
    
}
