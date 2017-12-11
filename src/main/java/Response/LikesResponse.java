/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Response;

import Model.Likes;
import Model.Users;
import java.io.Serializable;

/**
 *
 * @author GamingPc
 */
public class LikesResponse implements Serializable{
    private Likes like;
    private Users user;

    public LikesResponse() {
    }

    public LikesResponse(Likes like, Users user) {
        this.like = like;
        this.user = user;
    }

    public Likes getLike() {
        return like;
    }

    public void setLike(Likes like) {
        this.like = like;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    
   

    
    
}
