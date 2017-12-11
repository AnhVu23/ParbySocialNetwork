/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.service;

import Model.Comments;
import Model.Images;
import Model.Likes;
import Model.Users;
import Response.CommentResponse;
import Response.LikesResponse;
import Utils.HTTPErrorUtils;
import Utils.JWTUtils;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author GamingPc
 */
@Stateless
@Path("likes")
public class LikesFacadeREST extends AbstractFacade<Likes> {

    @PersistenceContext(unitName = "com.mycompany_Parby_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public LikesFacadeREST() {
        super(Likes.class);
    }

     @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Likes entity) {
        super.create(entity);
    }

    //Like if not exist, unlike otherwise
    @Path("images/{imageid}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLike(@HeaderParam("authorization") String token, @PathParam("imageid") Integer imageid) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = em.find(Users.class, userId);
            if (user != null) {
                Images image = em.find(Images.class, imageid);
                System.out.println(image.getCaption());
                if (image != null) {
                    List<Likes> listLike = em.createNamedQuery("Likes.findByImageId", Likes.class).setParameter("imageId", imageid).getResultList();
                    for (Likes like : listLike) {
                        if (like.getUploader() == userId) {
                            super.remove(like);
                            return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Unlike")).build();
                        }
                    }
                    Likes newLike = new Likes();
                    newLike.setImageId(imageid);
                    newLike.setUploader(userId);
                    super.create(newLike);
                    return Response.status(Response.Status.CREATED).entity(new LikesResponse(newLike, user)).build();
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
    public void edit(@PathParam("id") Integer id, Likes entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("images/{imageid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countLikes(@PathParam("imageid") Integer imageid) {
        Images image = em.find(Images.class, imageid);
        if (image != null) {
            List<Likes> listLike = em.createNamedQuery("Likes.findByImageId", Likes.class).setParameter("imageId", imageid).getResultList();
            if(listLike.size() > 0) {
                 List<LikesResponse> LikesResponsesList = new ArrayList<>();
            for (Likes likeFound : listLike) {
                Users user = em.find(Users.class, likeFound.getUploader());
                LikesResponsesList.add(new LikesResponse(likeFound, user));
            }
            GenericEntity<List<LikesResponse>> entities = new GenericEntity<List<LikesResponse>>(LikesResponsesList) {
            };
            return Response.status(Response.Status.OK).entity(entities).build();
            } else {
                return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok(listLike.size() + "")).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the image")).build();
        }

    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Likes> findAll() {
        return super.findAll(); 
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Likes> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
