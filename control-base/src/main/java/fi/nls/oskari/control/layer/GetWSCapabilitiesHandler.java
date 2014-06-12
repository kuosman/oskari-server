package fi.nls.oskari.control.layer;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.*;
import fi.nls.oskari.domain.map.OskariLayer;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.*;
import fi.nls.oskari.wms.GetGtWMSCapabilities;
import fi.nls.oskari.wmts.WMTSCapabilitiesParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Get WMS capabilites and return JSON
 */
@OskariActionRoute("GetWSCapabilities")
public class GetWSCapabilitiesHandler extends ActionHandler {

    private static final Logger log = LogFactory.getLogger(GetWSCapabilitiesHandler.class);
    private static final String PARM_URL = "url";
    private static final String PARM_TYPE = "type";

    private String[] permittedRoles = new String[0];

    @Override
    public void init() {
        super.init();
        permittedRoles = PropertyUtil.getCommaSeparatedList("actionhandler.GetWSCapabilitiesHandler.roles");
    }

    public void handleAction(ActionParameters params) throws ActionException {

        if (!params.getUser().hasAnyRoleIn(permittedRoles)) {
            throw new ActionDeniedException("Unauthorized user tried to proxy via capabilities");
        }
        final String url = params.getRequiredParam(PARM_URL);
        final String layerType = params.getHttpParam(PARM_TYPE, OskariLayer.TYPE_WMS);

        log.debug("Trying to get capabilities for type:", layerType, "with url:", url);
        try {
            if(OskariLayer.TYPE_WMS.equals(layerType)) {
                // New method for parsing WMSCetGapabilites to Oskari layers structure
                final JSONObject capabilities = GetGtWMSCapabilities.getWMSCapabilities(url);
                ResponseHelper.writeResponse(params, capabilities);
            }
            else {
                if (OskariLayer.TYPE_WMTS.equals(layerType)) {
                    WMTSCapabilitiesParser parser = new WMTSCapabilitiesParser();

                    // setup capabilities URL
                    Map<String, String> capabilitiesParams = new HashMap<String, String>();
                    capabilitiesParams.put("service", "WMTS");
                    capabilitiesParams.put("request", "GetCapabilities");
                    final String capabilitiesUrl = IOHelper.constructUrl(url, capabilitiesParams);

                    final String capabilities = IOHelper.getURL(capabilitiesUrl);
                    JSONObject resultJSON = parser.parseCapabilitiesToJSON(capabilities, url);
                    JSONHelper.putValue(resultJSON, "xml", capabilities);
                    ResponseHelper.writeResponse(params, resultJSON);
                } else {
                    throw new ActionParamsException("Couldn't determine operation based on parameters");
                }
            }
        } catch (Exception ee) {
            throw new ActionException("WMS Capabilities parsing failed: ", ee);
        }
    }
}
