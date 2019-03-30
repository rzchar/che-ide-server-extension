package edu.tongji.sse.notifiiercenter.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.che.api.project.shared.Constants.CHE_DIR;

import com.google.inject.Inject;
import io.swagger.annotations.*;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.rest.Service;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.commons.env.EnvironmentContext;
import org.json.JSONObject;

@Path("intelliPluginAva/")
public class IntelligentAvailabilitiesService extends Service {

  private FsManager fsManager;

  private static final String INTELLIGENT_PLUGIN_AVAILABILITIES_FILE =
      "/" + CHE_DIR + "/user_{userName}/" + "intelligentAvailabilities.json";

  @Inject
  public IntelligentAvailabilitiesService(FsManager fsManager) {
    this.fsManager = fsManager;
  }

  @GET
  @Produces(APPLICATION_JSON)
  @ApiOperation(
      value = "get intelligent availabilities",
      notes = "return intelligent availabilities")
  @ApiResponses({
    @ApiResponse(code = 200, message = "intelligent availabilities got"),
    @ApiResponse(code = 404, message = "something not found"),
    @ApiResponse(code = 500, message = "intelligent availabilities fail")
  })
  public Map<String, String> getAvailabilities()
      throws ConflictException, NotFoundException, ServerException {
    Map<String, String> result = new HashMap<>();
    String userName = EnvironmentContext.getCurrent().getSubject().getUserId();
    String filePath = INTELLIGENT_PLUGIN_AVAILABILITIES_FILE.replace("{userName}", userName);
    if (fsManager.exists(filePath)) {
      String strExistingAvailabilities = fsManager.readAsString(filePath);
      JSONObject jsonExistAvailabilities = new JSONObject(strExistingAvailabilities);
      for (String key : jsonExistAvailabilities.keySet()) {
        result.put(key, jsonExistAvailabilities.getString(key));
      }
    }
    //    result.put("q", "true");
    return result;
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @ApiOperation(
      value = "set intelligent availabilities",
      notes = "all existing intelligent plugins will be reset by this method")
  @ApiResponses({
    @ApiResponse(code = 200, message = "intelligent availabilities set succeed"),
    @ApiResponse(code = 500, message = "intelligent availabilities fail")
  })
  public void setAvailabilities(Map<String, String> newAvailabilities)
      throws BadRequestException, ConflictException, ServerException, NotFoundException {
    if (newAvailabilities == null) {
      throw new BadRequestException("Require non-null new availabilities");
    }
    Map<String, String> mapToPut = new HashMap<>();
    String userName = EnvironmentContext.getCurrent().getSubject().getUserId();
    String filePath = INTELLIGENT_PLUGIN_AVAILABILITIES_FILE.replace("{userName}", userName);
    if (fsManager.exists(filePath)) {
      String strExistingAvailabilities = fsManager.readAsString(filePath);
      JSONObject jsonExistingAvailabilities = new JSONObject(strExistingAvailabilities);
      for (String key : jsonExistingAvailabilities.keySet()) {
        mapToPut.put(key, jsonExistingAvailabilities.get(key).toString());
      }
    } else {
      fsManager.createFile(filePath);
    }
    mapToPut.putAll(newAvailabilities);
    JSONObject jsonToPut = new JSONObject(mapToPut);
    fsManager.update(filePath, jsonToPut.toString());
  }
}
