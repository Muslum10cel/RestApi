package com.hackengine_er;

import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws java.sql.SQLException
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws SQLException {
        return "error";
    }
}
