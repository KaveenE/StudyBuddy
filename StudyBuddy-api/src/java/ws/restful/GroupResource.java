/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejb.session.stateless.GroupEntitySessionBeanLocal;
import ejb.session.stateless.StudentSessionBeanLocal;
import entities.GroupEntity;

import entities.MessageEntity;

import entities.StudentEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.exception.AccessRightsException;
import util.exception.AlreadyExistsException;
import util.exception.DoesNotExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import ws.websocket.MessageSessionHandler;

/**
 *
 * @author SCXY
 */
@RequestScoped
@Path("Group")
public class GroupResource {

    @Inject
    private MessageSessionHandler messageSessionHandler;

    StudentSessionBeanLocal studentSessionBean;

    GroupEntitySessionBeanLocal groupEntitySessionBean;

    @Context
    private UriInfo context;

    public GroupResource() {
        groupEntitySessionBean = new SessionBeanLookup().lookupGroupEntitySessionBeanLocal();
        studentSessionBean = new SessionBeanLookup().lookupStudentSessionBeanLocal();
    }

    @Path("retrieveGroupsByStudentId/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGroupsByStudentId(@PathParam("studentId") Long studentId) {
        StudentEntity student;
        try {
            student = studentSessionBean.retrieveStudentById(studentId);
            List<GroupEntity> groups = student.getGroups();
            String result = new ObjectMapper().writeValueAsString(groups);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("groupById/{groupId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveGroupById(@PathParam("groupId") Long groupId) {
        try {
            GroupEntity group = groupEntitySessionBean.retrieveGroupEntityById(groupId);
            Map<String, List<Double>> map = new HashMap<>();
            group.getModuleEntity().getSchool().getModuleEntities().clear();
            String res = new ObjectMapper().writeValueAsString(group);
            return Response.ok(res, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllOpenGroups/{schoolId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOpenGroups(@PathParam("schoolId") Long schoolId, @QueryParam("studentId") Long studentId) {
        try {
            StudentEntity stu = new StudentEntity();
            stu.setAccountId(studentId);
            List<GroupEntity> groups = groupEntitySessionBean.retrieveAllOpenGroups(schoolId);
            groups = groups.stream().filter(g -> !g.getGroupMembers().contains(stu)).collect(Collectors.toList());
            String result = new ObjectMapper().writeValueAsString(groups);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllMyGroups/{studentId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMyGroups(@PathParam("studentId") Long studentId) {
        try {
            List<GroupEntity> groups = groupEntitySessionBean.retrieveAllMyGroups(studentId);
            String result = new ObjectMapper().writeValueAsString(groups);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllCandidates/{groupId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCandidates(@PathParam("groupId") Long groupId) {
        try {
            List<StudentEntity> students = studentSessionBean.retrieveAllCandidates(groupId);
            String result = new ObjectMapper().writeValueAsString(students);
            return Response.ok(result, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createGroup")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupEntity newGroupEntity, @QueryParam("moduleId") Long moduleId, @QueryParam("studentId") Long studentId) {
        try {
            Long groupId = groupEntitySessionBean.createNewGroupEntity(newGroupEntity, moduleId, studentId);
            return this.retrieveGroupById(groupId);
        } catch (InputDataValidationException | AlreadyExistsException | DoesNotExistException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("applyToGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyToGroup(@QueryParam("studentId") Long studentId, @QueryParam("groupId") Long groupId) {
        try {
            groupEntitySessionBean.applyToGroup(groupId, studentId);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("approveReq")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveReq(@QueryParam("studentId") Long studentId, @QueryParam("groupId") Long groupId) {
        try {
            groupEntitySessionBean.approveReq(groupId, studentId);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("disapproveReq")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response disapproveReq(@QueryParam("studentId") Long studentId, @QueryParam("groupId") Long groupId) {
        try {
            groupEntitySessionBean.disapproveReq(groupId, studentId);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("updateGroup/{studentId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGroup(GroupEntity groupEntityToUpdate, @PathParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.updateGroup(groupEntityToUpdate, studentId);
            return Response.status(Status.OK).build();
        } catch (AccessRightsException | DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("updateMapMarkers")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMapMarkers(GroupEntity groupEntityToUpdate) {
        try {
            groupEntitySessionBean.updateMapMarkers(groupEntityToUpdate);
            return Response.status(Status.OK).build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    private StudentSessionBeanLocal lookupStudentSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StudentSessionBeanLocal) c.lookup("java:global/StudyBuddy/StudyBuddy-ejb/StudentSessionBean!ejb.session.stateless.StudentSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Path("approveToGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveMemberToGroup(@QueryParam("groupId") Long groupId, @QueryParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.approveMemberToGroup(groupId, studentId);
            return Response.ok().build();
        } catch (DoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("rejectFromGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response rejectCandidateFromGroup(@QueryParam("groupId") Long groupId, @QueryParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.rejectCandidateFromGroup(groupId, studentId);
            return Response.ok().build();
        } catch (DoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("leaveGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response leaveGroup(@QueryParam("groupId") Long groupId, @QueryParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.leaveGroup(groupId, studentId);
            return Response.ok().build();
        } catch (DoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("closeGroup")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelApplicationToGroup(@QueryParam("groupId") Long groupId, @QueryParam("studentId") Long studentId) {
        try {
            groupEntitySessionBean.cancelApplicationToGroup(groupId, studentId);
            return Response.ok().build();
        } catch (DoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("messages")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessageText(MessageEntity messageEntity) {
        try {
            groupEntitySessionBean.addNewMessage(messageEntity);

            return Response.ok().build();
        } catch (DoesNotExistException | InputDataValidationException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("messages")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@QueryParam("groupId") Long groupId) {
        System.out.printf("Message GET called to group[%d]%n", groupId);
        try {
            List<MessageEntity> messages = groupEntitySessionBean.retrieveMessagesByGroupId(groupId);
            messages.forEach(m -> {
                GroupEntity group = new GroupEntity();
                StudentEntity student = new StudentEntity();
                group.setGroupId(m.getGroup().getGroupId());
                student.setAccountId(m.getSender().getAccountId());
                m.setGroup(group);
                m.setSender(student);
            });

            String res = new ObjectMapper().writeValueAsString(messages);
            return Response.ok(res, MediaType.APPLICATION_JSON).build();
        } catch (DoesNotExistException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (JsonProcessingException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("messages")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessageFile(@Context HttpHeaders headers, @FormDataParam("img") InputStream fileInputStream, @QueryParam("groupId") Long groupId, @QueryParam("studentId") Long studentId) {
        OutputStream out = null;

        try {
            GroupEntity group = new GroupEntity();
            StudentEntity sender = new StudentEntity();

            group.setGroupId(groupId);
            sender.setAccountId(studentId);

            MessageEntity message = new MessageEntity("", group, sender, util.enumeration.MediaType.PICTURE);

            Long id = groupEntitySessionBean.addNewMessage(message);

            System.out.println("New Message Id=" + id);

            MultivaluedMap<String, String> map = headers.getRequestHeaders();
            String fileType = getFileType(map);
            String fileName = id.toString();
            String filePath = "../docroot/StudyBuddy/messageImg/";
            String fullPath = filePath + fileName + "." + fileType;
            File outFile = new File(fullPath);
            out = new FileOutputStream(outFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            groupEntitySessionBean.changeMessageContent(id, fullPath);

            System.out.println("Content changed");

            message.setMessageId(id);
            message.setContent(fullPath);
            messageSessionHandler.broadCastFileMessage(message);

            return Response.ok().build();
        } catch (IOException | DoesNotExistException | InputDataValidationException ex) {
            Logger.getLogger(GroupResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            try {
                out.close();
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(GroupResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Path("messageFile")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMessageFile(@QueryParam("messageId") Long messageId) {
        try {
            MessageEntity messageEntity = groupEntitySessionBean.retrieveMessageEntityById(messageId);
            System.out.println("This is modified");
            System.out.println("Received new message File req: " + messageEntity.getMediaType());
            if (messageEntity.getMediaType().equals(util.enumeration.MediaType.PICTURE)) {
                File file = new File(messageEntity.getContent());
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                return Response.ok(encodedString, MediaType.TEXT_PLAIN).build();
                
//                InputStream imgIs = new FileInputStream(file);
//                return Response.ok(imgIs).build();
            } else {
                return Response.status(Status.BAD_REQUEST).build();
            }
        } catch (DoesNotExistException | FileNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex).build();
        } catch (IOException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }

    private String getFileType(MultivaluedMap<String, String> headers) {
        String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filetype"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "";
    }
}
