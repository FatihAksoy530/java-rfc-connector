package com.sap.cloud.sdk.tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet("/hello")
public class HelloWorldServlet extends HttpServlet
{
    public static final long serialVersionUID = 1L;
    public static final Logger logger = LoggerFactory.getLogger(HelloWorldServlet.class);

    @Override
    public void doGet( final HttpServletRequest request, final HttpServletResponse response )
        throws IOException
    {
        logger.info("VCAP_SERVICES: {}", System.getenv("VCAP_SERVICES"));
        logger.info("I am running!");
        response.getWriter().write("Hello World extra!a");
    }
}
