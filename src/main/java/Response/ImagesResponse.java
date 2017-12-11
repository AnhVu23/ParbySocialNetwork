/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Response;

import Model.Images;
import Model.Users;
import java.io.Serializable;

/**
 *
 * @author GamingPc
 */
public class ImagesResponse implements Serializable{
    private Images image;
    private Users uploader;

    public ImagesResponse() {
    }

    public ImagesResponse(Images image, Users uploader) {
        this.image = image;
        this.uploader = uploader;
    }

    public Images getImage() {
        return image;
    }

    public void setImage(Images image) {
        this.image = image;
    }

    public Users getUploader() {
        return uploader;
    }

    public void setUploader(Users uploader) {
        this.uploader = uploader;
    }
    
    
}
