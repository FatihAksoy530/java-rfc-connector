package com.sap.cloud.sdk.tutorial;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.result.DefaultResultCollection;
import com.sap.cloud.sdk.result.GsonResultObject;
import com.sap.cloud.sdk.result.ResultElement;
import com.sap.cloud.sdk.result.ResultObject;
import com.sap.cloud.sdk.s4hana.connectivity.exception.RequestExecutionException;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequest;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequestResult;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RemoteFunctionCache;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.exception.RemoteFunctionException;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**

 * Sample application that uses the Connectivity
 service. In particular, it is

 * making use of the capability to invoke a function module in an ABAP system

 * via RFC

 *

 * Note: The JCo APIs are available under <code>com.sap.conn.jco</code>.

 */


@WebServlet("/profile_parameters_unsecured")
public class ConnectRFCLocal extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ConnectRFCCloud.class);
    private static final Destination destinationRfc =
            DestinationAccessor.getDestination("sapuretestrfc");

    public void uploadParameters(RfmRequestResult result, String company_subdomain) throws RemoteFunctionException {
        // get elements as a list from result
        ArrayList elements = result.getResultElements();
        DefaultResultCollection resultCollection = (DefaultResultCollection) elements.get(0);
        ArrayList resultElements = (ArrayList) resultCollection.getResultElements();
        // print resultCollection
        ProfileParametersQueries ppq = new ProfileParametersQueries();
        ppq.deleteParameters(company_subdomain);
        for (int i = 0; i < resultElements.size(); i++) {
            if (i == 10) {
                break;
            }
            GsonResultObject gsonResultObject = (GsonResultObject) resultElements.get(i);
            JsonObject jsonObject = gsonResultObject.getJsonObject();
            String param_name = jsonObject.get("PARAM_NAME").getAsString();
            String user_value = jsonObject.get("USER_VALUE").getAsString();
            String dfl_value = jsonObject.get("DFL_VALUE").getAsString();
            ppq.addParameter(param_name, user_value, dfl_value, "RFC", company_subdomain);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        logger.info("Start get method: " + request.getRequestURI());
        // get company_subdomain from request query string
        String company_subdomain = request.getParameter("company_subdomain");
        if (company_subdomain == null) {
            company_subdomain = "sapuretest";
        }

        try {
            // call RfmRequest with parameters
            RfmRequestResult result = new RfmRequest("PFL_GET_SERVER_PARAM_VALUES")
//                    .withExporting("QUERY_TABLE","DD02L-TABNAME","AGR_1252")
//                    .withTable("OPTIONS","RFC_DB_OPT").end()
//                    .withTable("FIELDS","RFC_DB_FLD").end()
//                    .withTable("DATA","TAB512").end()
//                    .withTableAsReturn("DATA","TAB512")
                    .execute(destinationRfc);
            logger.info("Request sent to ABAP system");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // write result
            this.uploadParameters(result, company_subdomain);
            // return json scucces message
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // send stack trace to client in response body as text
            response.setContentType("text/plain");
            response.getWriter().write(e.getMessage());
        }
    }
}