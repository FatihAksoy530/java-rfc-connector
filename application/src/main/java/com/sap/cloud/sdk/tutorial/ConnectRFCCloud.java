package com.sap.cloud.sdk.tutorial;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.s4hana.connectivity.exception.RequestExecutionException;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequest;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequestResult;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RemoteFunctionCache;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.exception.RemoteFunctionException;
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


@WebServlet("/ConnectivityRFCCloud")
public class ConnectRFCCloud extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ConnectRFCCloud.class);
    private static final Destination destinationRfc =
            DestinationAccessor.getDestination("sapuretestrfc");

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        // print VCAP_SERVICES environment variable
//        logger.info("VCAP_SERVICES: {}", System.getenv("VCAP_SERVICES"));
        logger.info("Start get method: " + request.getRequestURI());
//        try {
//            RemoteFunctionCache.clearCache(destinationRfc);
//        } catch (RemoteFunctionException e) {
//            e.printStackTrace();
//        }
//        String parameter = request.getParameter("name");
//        logger.info("Get parameter 'name': " + parameter);
//        if (parameter == null) {
//            parameter = "USER_NAME_GET";
//        }
//        Iterable names = destinationRfc.getPropertyNames();
//        logger.info("DESTINATION PARAMETERS:",new Gson().toJson(names));
        try {
            // call RfmRequest with parameters
            RfmRequestResult result = new RfmRequest("RFC_READ_TABLE")
                    .withExporting("QUERY_TABLE","DD02L-TABNAME","AGR_1252")
                    .withTable("OPTIONS","RFC_DB_OPT").end()
                    .withTable("FIELDS","RFC_DB_FLD").end()
                    .withTable("DATA","TAB512").end()
                    .withTableAsReturn("DATA","TAB512")
                    .execute(destinationRfc);
            logger.info("Request sent to ABAP system");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.valueOf(result.getResultElements()));
        } catch (Exception e) {
            e.printStackTrace();
            // send stack trace to client in response body as text
            response.setContentType("text/plain");
            response.getWriter().write(e.getMessage());
        }
    }
}