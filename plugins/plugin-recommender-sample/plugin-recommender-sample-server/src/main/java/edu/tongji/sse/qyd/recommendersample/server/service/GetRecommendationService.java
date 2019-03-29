package edu.tongji.sse.qyd.recommendersample.server.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;

@Path("getFileLines/{ws-id}")
public class GetRecommendationService {
  private FsManager fsManager;

  @Inject
  public GetRecommendationService(FsManager fsManager) {
    this.fsManager = fsManager;
  }

  private int countLines(String wsPath)
      throws ServerException, NotFoundException, ConflictException {
    String content = fsManager.readAsString(wsPath);
    String[] lines = content.split("\r\n|\r|\n");
    return lines.length;
  }

  private void getFileLinesRecursivly(String path, Map<String, String> linesPerFile)
      throws ServerException, NotFoundException, ConflictException {
    Set<String> fileWsPaths = fsManager.getFileWsPaths(path);
    for (String fileWsPath : fileWsPaths) {
      linesPerFile.put(fileWsPath, Integer.toString(countLines(fileWsPath)));
    }
    Set<String> dirWsPaths = fsManager.getDirWsPaths(path);
    for (String dirWsPath : dirWsPaths) {
      getFileLinesRecursivly(dirWsPath, linesPerFile);
    }
  }

  @GET
  @Path("{projectPath}")
  public Map<String, String> countLinesPerFile(@PathParam("projectPath") String projectPath)
      throws ServerException, NotFoundException, ConflictException {
    String projectWsPath = WsPathUtils.absolutize(projectPath);
    Map<String, String> linesPerFile = new LinkedHashMap<>();
    getFileLinesRecursivly(projectWsPath, linesPerFile);
    return linesPerFile;
  }
}
