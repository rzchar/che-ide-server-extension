package edu.tongji.sse.qyd.recommendersample.server.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;

@Path("analyzeFile/{ws-id}")
public class GetFileDetailService {
  private FsManager fsManager;

  @Inject
  public GetFileDetailService(FsManager fsManager) {
    this.fsManager = fsManager;
  }

  private int countLines(String wsPath)
      throws ServerException, NotFoundException, ConflictException {
    String content = fsManager.readAsString(wsPath);
    String[] lines = content.split("\r\n|\r|\n");
    return lines.length;
  }

  /**
   * Count LOC for all JSON files within the given project.
   *
   * @param projectPath the path to the project that contains the JSON files for which to calculate
   *     the LOC
   * @return a Map mapping the file name to their respective LOC value
   * @throws ServerException in case the server encounters an error
   * @throws org.eclipse.che.api.core.NotFoundException in case the project couldn't be found
   * @throws ForbiddenException in case the operation is forbidden
   */
  @GET
  @Path("{projectPath}")
  public Map<String, String> countLinesPerFile(@PathParam("projectPath") String projectPath)
      throws ServerException, NotFoundException, ConflictException {
    String projectWsPath = WsPathUtils.absolutize(projectPath);

    Map<String, String> linesPerFile = new LinkedHashMap<>();
    Set<String> fileWsPaths = fsManager.getFileWsPaths(projectWsPath);
    for (String fileWsPath : fileWsPaths) {
      linesPerFile.put(WsPathUtils.nameOf(fileWsPath), Integer.toString(countLines(fileWsPath)));
    }

    return linesPerFile;
  }
}
