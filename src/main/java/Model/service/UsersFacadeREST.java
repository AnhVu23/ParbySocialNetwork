/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.service;

import Model.Users;
import Utils.HTTPErrorUtils;
import Utils.JWTUtils;
import Utils.JsonTokenUtils;
import Utils.TextUtils;
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
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author GamingPc
 */
@Stateless
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "com.mycompany_Parby_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

     @POST
    @Path("test")
    @Produces(MediaType.APPLICATION_XML)
    public Response test() {
        Users user = new Users();
        user.setEmail("vu.haianh@gmail");
        user.setPassword("asjfsda");
        user.setFullname("Anh Vu");
        user.setUsername("anhv");
        super.create(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@QueryParam("email") String email, 
            @QueryParam("password") String password, 
            @QueryParam("fullname") String fullName, 
            @QueryParam("username") String userName ) {
        List<Users> list = super.findAll();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(userName))
            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("The fields should not be empty")).build();       
        for(Users user : list) {
            if(user.getEmail().equals(email)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("The email already exists")).build();
            } 
            else if(user.getUsername().equals(userName)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("The username already exists")).build();
            }
        }
        Users registerUser = new Users();
        registerUser.setEmail(email);
    
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        registerUser.setPassword(encodedPassword);
        registerUser.setUsergroups(0);
        registerUser.setUsername(userName);
        registerUser.setFullname(fullName);
        super.create(registerUser);
        registerUser = super.findNewest();
        String userid = registerUser.getUserid() + "";
        System.out.println(userid);
        String token = JWTUtils.createJWT(userid);
        System.out.println(token);
        token = JsonTokenUtils.tokenTransform(token);
        return Response.status(Response.Status.CREATED).entity(token).build();   
    }
    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@QueryParam("email") String email, @QueryParam("password") String password) {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("The fields should not be empty")).build();
        }
        List<Users> list = super.findAll();
        for(Users user : list ){
            if(user.getEmail().equals(email)) {
                if(BCrypt.checkpw(password, user.getPassword())) {
                    String userId = user.getUserid() + "";
                    String token = JWTUtils.createJWT(userId);
                    token = JsonTokenUtils.tokenTransform(token);
                    return Response.status(Response.Status.OK).entity(token).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Wrong password")).build();
                }
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Wrong email")).build();
    }
    /*@PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, User entity) {
        super.edit(entity);
    } */
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("home")
    public Response editMe(@HeaderParam("authorization") String token,
            @QueryParam("email") String email,
            @QueryParam("username") String userName,
            @QueryParam("fullname") String fullName,
            @QueryParam("oldpassword") String oldPassword,
            @QueryParam("newpassword") String newPassword) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users myUser = super.find(userId);
            if (myUser != null) {
                if (!TextUtils.isEmpty(newPassword)) {
                    if (!TextUtils.isEmpty(oldPassword)) {
                        if (BCrypt.checkpw(oldPassword, myUser.getPassword())) {
                            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
                            myUser.setPassword(hashedPassword);
                            super.edit(myUser);
                            return Response.status(Response.Status.OK).entity(myUser).build();
                        } else {
                            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Wrong password")).build();
                        }
                    }
                }
                if (!TextUtils.isEmpty(userName)) {
                    List<Users> list = findAll();
                    for (Users user : list) {
                        if (user.getUsername().equals(userName)) {
                            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Username already exists")).build();
                        }
                    }
                    myUser.setUsername(userName);
                    super.edit(myUser);
                }
                if (!TextUtils.isEmpty(email)) {
                    List<Users> list = findAll();
                    for (Users user : list) {
                        if (user.getEmail().equals(email)) {
                            return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Email already exists")).build();
                        }
                    }
                    myUser.setEmail(email);
                    super.edit(myUser);
                }
                if (!TextUtils.isEmpty(fullName)) {
                    myUser.setFullname(fullName);
                    super.edit(myUser);
                }
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(newPassword) && TextUtils.isEmpty(fullName) && TextUtils.isEmpty(userName)) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("You must fill at least one of the field")).build();
                }
                return Response.status(Response.Status.OK).entity(myUser).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action.")).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action.")).build();
    }
    
    @DELETE
    @Path("home")
    public Response removeMe(@HeaderParam("authorization") String token) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = super.find(userId);
            if (user!= null) {
                super.remove(user);
                return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Delete successfully user " + user.getEmail())).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(HTTPErrorUtils.unauthorized("The user is not found. Can not delete the user"))
                        .build();
            }
                               
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action."))
                    .build();
        }
    }
    
    /*@DELETE
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@HeaderParam("authorization") String token, @PathParam("id") Integer id) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = super.find(id);
            if (user!= null) {
                super.remove(user);
                return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Delete successfully user " + user.getEmail())).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(HTTPErrorUtils.unauthorized("The user is not found. Can not delete the user"))
                        .build();
            }
                               
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action."))
                    .build();
        }
    } */
    
    //Get specific user
    @GET
    @Path("getUser/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findId(@HeaderParam("authorization") String token, @PathParam("id") Integer id) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = super.find(id);
            if (user!= null) {
                return Response.status(Response.Status.OK)
                    .entity(user)
                    .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(HTTPErrorUtils.unauthorized("The user is not found"))
                        .build();
            }
                               
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action."))
                    .build();
        }
    }
    
    //Get myuser
    @GET
    @Path("home")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findMe(@HeaderParam("authorization") String token) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users user = super.find(userId);
            if (user!= null) {
                return Response.status(Response.Status.OK)
                    .entity(user)
                    .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(HTTPErrorUtils.unauthorized("Can not found the user"))
                        .build();
            }
                               
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action."))
                    .build();
        }
    }
    
    //get all users
    @GET
    @Path("all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(@HeaderParam("authorization") String token) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            List<Users> listUser = super.findAll();
            GenericEntity<List<Users>> entities = new GenericEntity<List<Users>>(listUser) {};
            return Response.status(Response.Status.OK)
                    .entity(entities)
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user is not authorized to perform this action."))
                    .build();
        }
    }
    
    //Test
    @GET
    @Path("getTest")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAllTest(@HeaderParam("authorization") String token) {
            List<Users> listUser = super.findAll();
            GenericEntity<List<Users>> entities = new GenericEntity<List<Users>>(listUser) {};
            return Response.status(Response.Status.OK)
                    .entity(entities)
                    .build();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String testGet() {
        return "testing purpose";
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
