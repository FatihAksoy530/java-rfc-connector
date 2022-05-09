package com.sap.cloud.sdk.tutorial;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.security.token.SecurityContext;
import com.sap.cloud.security.token.Token;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**

 * Sample application that uses the Connectivity
 service. In particular, it is

 * making use of the capability to invoke a function module in an ABAP system

 * via RFC

 *

 * Note: The JCo APIs are available under <code>com.sap.conn.jco</code>.

 */


@WebServlet("/security_token")
public class SecurityToken extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ConnectRFCCloud.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        logger.info("Start get method: " + request.getRequestURI());
        // get company_subdomain from request query string

        Token token = SecurityContext.getToken();

//        String finalCompany_subdomain = company_subdomain;
        Thread runThread = new Thread(() -> {
            SecurityContext.setToken(token);
        });

        // print the token
        logger.info("Token: " + token);

        // send helloword to client
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("Hello World");
    }
}
