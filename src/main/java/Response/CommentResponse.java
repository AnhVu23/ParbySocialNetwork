/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Response;

import Model.Comments;
import Model.Users;
import java.io.Serializable;

/**
 *
 * @author GamingPc
 */
public class CommentResponse implements Serializable{
   
    private Comments comment;
    private Users user;

    public CommentResponse(Comments comment, Users user) {
        this.comment = comment;
        this.user = user;
    }

    public CommentResponse() {
    }

    public Comments getComment() {
        return comment;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
}
