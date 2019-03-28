package edu.tongji.sse.notifiiercenter.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.swagger.annotations.*;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import org.eclipse.che.api.core.rest.Service;

@Path("intelligentPluginPreference/")
public class IntelligentPreferenceService extends Service {
  @GET
  @Produces(APPLICATION_JSON)
  @ApiOperation(value = "get intelligent preference", notes = "return intelligent preference")
  @ApiResponses({
    @ApiResponse(code = 200, message = "intelligent preference got"),
    @ApiResponse(code = 404, message = "workspace not found"),
    @ApiResponse(code = 500, message = "intelligent preference fail")
  })
  public Map<String, Boolean> getPreference() {
    Map<String, Boolean> result = new HashMap<String, Boolean>();
    result.put("pluginId", true);
    return result;
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @ApiOperation(
      value = "set intelligent preference",
      notes = "all existing intelligent plugins will be reset by this method")
  @ApiResponses({
    @ApiResponse(code = 200, message = "intelligent preference set succeed"),
    @ApiResponse(code = 404, message = "workspace not found"),
    @ApiResponse(code = 500, message = "intelligent preference fail")
  })
  public void setPreference(Map<String, String> preference) throws BadRequestException {
    if (preference == null) {
      throw new BadRequestException("Require non-null new preferences");
    }
  }
}
