package com.sap.cloud.sdk.tutorial;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.s4hana.connectivity.exception.RequestExecutionException;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequest;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
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


@WebServlet("/ConnectivityRFCLocal")
public class ConnectRFCLocal extends HttpServlet {
    public static final long serialVersionUID = 1L;
    public static final Logger logger = LoggerFactory.getLogger(ConnectRFCLocal.class);
    public static final Destination destinationRfc =
            DestinationAccessor.getDestination("ABAP_AS1");

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        logger.info("Start get method: " + request.getRequestURI());
        String parameter = request.getParameter("name");
        logger.info("Get parameter 'name': " + parameter);
        if (parameter == null) {
            parameter = "STFC_CONNECTION";
        }
        Iterable names = destinationRfc.getPropertyNames();
        logger.info(new Gson().toJson(names));

        try {
            final RfmRequestResult rfmTest = new RfmRequest(parameter, false).withChanging("REQUTEXT" ,"STRING", "JCO successful").execute(destinationRfc); //false is for non-commit
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(rfmTest));
        } catch (RequestExecutionException e) {
            e.printStackTrace();
        }
    }
}