/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.service;

import Model.Comments;
import Model.Images;
import Model.Users;
import Response.CommentResponse;
import Response.ImagesResponse;
import Utils.HTTPErrorUtils;
import Utils.JWTUtils;
import Utils.TextUtils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author GamingPc
 */
@Stateless
@Path("comments")
public class CommentsFacadeREST extends AbstractFacade<Comments> {

    @PersistenceContext(unitName = "com.mycompany_Parby_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public CommentsFacadeREST() {
        super(Comments.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Comments entity) {
        super.create(entity);
    }

    @Path("images/{imageid}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@HeaderParam("authorization") String token, @QueryParam("content") String content, @PathParam("imageid") Integer imageid) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = em.find(Users.class, userId);
            if (user != null) {
                Images image = em.find(Images.class, imageid);
                if (image != null) {
                    if (!TextUtils.isEmpty(content)) {
                        Comments newComment = new Comments();
                        newComment.setComment(content);
                        newComment.setImageId(imageid);
                        newComment.setUploader(userId);
                        super.create(newComment);
                        return Response.status(Response.Status.CREATED).entity(new CommentResponse(newComment, user)).build();
                    } else {
                        return Response.status(Response.Status.NO_CONTENT).entity(HTTPErrorUtils.noContent("The content field should not be empty")).build();
                    }
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the image")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Comments entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{commentid}")
    public Response remove(@HeaderParam("authorization") String token, @PathParam("commentid") Integer commentid) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = em.find(Users.class, userId);
            if (user != null) {
                Comments deleteComment = em.find(Comments.class, commentid);
                if (deleteComment != null) {
                    if (deleteComment.getUploader().equals(user)) {
                        super.remove(deleteComment);
                        return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Delete comment successfully")).build();
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
                    }
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the comment")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
        }
    }

    //Get all comments in 1 image
    @GET
    @Path("images/{imageid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCommentIn1Image(@PathParam("imageid") Integer imageid) {
        Images image = em.find(Images.class, imageid);
        List<Comments> listComment = em.createNamedQuery("Comments.findByImageId", Comments.class).setParameter("imageId", imageid).getResultList();
        if (!listComment.isEmpty()) {
            List<CommentResponse> commentsResponsesList = new ArrayList<>();
            for (Comments commentFound : listComment) {
                Users user = em.find(Users.class, commentFound.getUploader());
                commentsResponsesList.add(new CommentResponse(commentFound, user));
            }
            GenericEntity<List<CommentResponse>> entities = new GenericEntity<List<CommentResponse>>(commentsResponsesList) {
            };
            return Response.status(Response.Status.OK).entity(entities).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).entity(HTTPErrorUtils.noContent("Can not find comment")).build();
        }

    }


    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comments> findAll() {
        return super.findAll();
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
