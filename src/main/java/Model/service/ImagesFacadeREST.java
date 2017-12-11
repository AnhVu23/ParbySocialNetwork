/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.service;

import Model.Images;
import Model.Users;
import Response.ImagesResponse;
import Utils.HTTPErrorUtils;
import Utils.JWTUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author GamingPc
 */
@Stateless
@Path("images")
public class ImagesFacadeREST extends AbstractFacade<Images> {

    private static final String UPLOAD_FOLDER = "/var/www/html/storage/";
    @PersistenceContext(unitName = "com.mycompany_Parby_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ImagesFacadeREST() {
        super(Images.class);
    }

    @POST
    @Path("upload")
    @Consumes({MediaType.MULTIPART_FORM_DATA}) //, MediaType.APPLICATION_JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImage(
            @HeaderParam("authorization") String token,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("caption") String caption,
            @FormDataParam("album") String albumTitle) {
        System.err.println("upload: here we go.");
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            System.out.println("upload: user ok.");
            Users myUser = em.find(Users.class, userId);
            if (myUser != null) {
                if (uploadedInputStream == null || fileDetail.getFileName() == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Invalid form data")).build();
                }
                try {
                    System.out.println(myUser.getUsername());
                    createFolderIfNotExists(UPLOAD_FOLDER + myUser.getUsername() + "/");
                } catch (SecurityException se) {
                    System.out.println("upload: not able to create folder...");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(HTTPErrorUtils.internalServerError("Can not create destination folder on server"))
                            .build();
                }
                String uploadedFileLocation = UPLOAD_FOLDER + myUser.getUsername() + "/" + fileDetail.getFileName();
                String imgData = "http://10.114.34.134/storage/" + myUser.getUsername() + "/" + fileDetail.getFileName();

                try {
                    saveToFile(uploadedInputStream, uploadedFileLocation);
                    //After saving file, add image to database
                    Images img = new Images();
                    img.setCaption(caption);
                    img.setImgData(imgData);
                    img.setUploader(userId);
                    Date date = new Date();
                    img.setTime(date);
                    super.create(img);
                    return Response.status(Response.Status.CREATED)
                            .entity(new ImagesResponse(img, myUser)).build();
                } catch (IOException e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                            entity(HTTPErrorUtils.internalServerError("Can not save file")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        } else {
            System.out.println("upload: user not ok...");
            return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
        }

    }

    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(new File(target));
            while ((read = inStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdirs();
            System.out.println("Tbe dir should be created?");
        }
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Images entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}/{caption}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@HeaderParam("authorization") String token, @PathParam("id") Integer id, @PathParam("caption") String caption) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users myUser = em.find(Users.class, userId);
            if (myUser != null) {
                Images image = super.find(id);
                if (image != null) {
                    image.setCaption(caption);
                    super.edit(image);
                    return Response.status(Response.Status.OK).entity(new ImagesResponse(image, myUser)).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the image")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
    }

    //delete my image
    @DELETE
    @Path("{id}")
    public Response remove(@HeaderParam("authorization") String token, @PathParam("id") Integer id) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users myUser = em.find(Users.class, userId);
            if (myUser != null) {
                Images image = super.find(id);
                if (image != null) {
                    super.remove(image);
                    return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Successful")).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the image")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
    }
    
     @DELETE
    @Path("admin/{id}")
    public Response removeUserImage(@HeaderParam("authorization") String token, @PathParam("id") Integer id) {
        int userId = JWTUtils.parseJWT(token);
        if (userId != -1) {
            Users myUser = em.find(Users.class, userId);
            if (myUser != null) {
                Images image = super.find(id);
                if (image != null) {
                    super.remove(image);
                    return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Successful")).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).entity(HTTPErrorUtils.badRequest("Can not find the image")).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(HTTPErrorUtils.unauthorized("The user can not perform this action")).build();
    }
    //Get specify image by id 
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findMyImage(@PathParam("id") Integer id) {
        Images image = super.find(id);
        if (image != null) {
            Users user = em.find(Users.class, image.getUploader());
            return Response.status(Response.Status.OK).entity(new ImagesResponse(image, user)).build();
        } else {
            return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Not found")).build();
        }

    }

    @GET
    @Path("users/{userid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findUserImage(@PathParam("userid") Integer userid) {
        Users user = em.find(Users.class, userid);
        List<Images> imageList = em.createNamedQuery("Images.findByUploader", Images.class).setParameter("uploader", user).getResultList();
        if (!imageList.isEmpty()) {
            List<ImagesResponse> imagesResponsesList = new ArrayList<>();
            for (Images imageFound : imageList) {
                imagesResponsesList.add(new ImagesResponse(imageFound, user));
            }
            GenericEntity<List<ImagesResponse>> entities = new GenericEntity<List<ImagesResponse>>(imagesResponsesList) {
            };
            return Response.status(Response.Status.OK).entity(entities).build();
        } else {
            return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Empty")).build();
        }

    }

    @GET
    @Path("get/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAllImage() {
        List<Images> imageList = em.createNamedQuery("Images.findAll", Images.class).getResultList();
        System.out.println(imageList.isEmpty());
        if (!imageList.isEmpty()) {
            List<ImagesResponse> imagesResponsesList = new ArrayList<>();
            for (Images imageFound : imageList) {
                Users user = em.find(Users.class, imageFound.getUploader());
                imagesResponsesList.add(new ImagesResponse(imageFound, user));
            }
            GenericEntity<List<ImagesResponse>> entities = new GenericEntity<List<ImagesResponse>>(imagesResponsesList) {
            };
            return Response.status(Response.Status.OK).entity(entities).build();
        } else {
            return Response.status(Response.Status.OK).entity(HTTPErrorUtils.Ok("Empty")).build();

        }
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
