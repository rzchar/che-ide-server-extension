package edu.tongji.sse.qyd.recommendersample.server.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import edu.tongji.sse.qyd.recommendersample.server.astbuilder.AstTreeAnalyzer;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.server.FsManager;
import org.eclipse.che.api.fs.server.WsPathUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("getFileLines/{ws-id}/")
public class GetFileService {
  private FsManager fsManager;

  @Inject
  public GetFileService(FsManager fsManager) {
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

  @GET
  @Path("ast/{project}")
  @Produces(APPLICATION_JSON)
  public Map<String, String> getAstForProject(@PathParam("project") String projectPath) {
    Map<String, String> result = new HashMap<>();
    try {
      String projectWsPath = WsPathUtils.absolutize(projectPath);
      long serverStartTime = (new Date()).getTime();
      Map<String, JSONObject> astPerFile = new HashMap<>();
      this.getFileASTRecursivly(projectWsPath, astPerFile);
      JSONArray resultArray = new JSONArray();
      long serverEndTime = (new Date()).getTime();
      long totalExpression = 0;
      long totalMethod = 0;
      long fileNum = 0;
      for (String fileName : astPerFile.keySet()) {
        fileNum += 1;
        JSONObject jo = astPerFile.get(fileName);
        jo.put("fileName", fileName);
        totalMethod += jo.optInt("methodNum", 0);
        totalExpression += jo.optInt("expressionNum", 0);
        resultArray.put(jo);
      }
      fsManager.createFile(
          projectWsPath + "/astRecord/" + "rec" + serverStartTime + ".json",
          resultArray.toString());
      result.put("fileAmount", String.valueOf(fileNum));
      result.put("methodAmount", String.valueOf(totalMethod));
      result.put("expressionAmount", String.valueOf(totalExpression));
      result.put("serverAnalyzerCost", String.valueOf(serverEndTime - serverStartTime));
    } catch (Exception e) {
      StringBuilder errBuilder = new StringBuilder();
      errBuilder.append("\n");
      for (StackTraceElement s : e.getStackTrace()) {
        errBuilder.append(s.toString() + "\n");
      }
      result.put("errorMessage", e.getMessage());
      result.put("stacktrace", errBuilder.toString());
    }

    return result;
  }

  private boolean isFileWanted(String filePath) {
    if (filePath.endsWith(".java")) {
      return true;
    }
    return false;
  }

  private void getFileASTRecursivly(String path, Map<String, JSONObject> astPerFile)
      throws ConflictException, NotFoundException, ServerException {
    Set<String> fileWsPaths = fsManager.getFileWsPaths(path);
    for (String fileWsPath : fileWsPaths) {
      if (isFileWanted(fileWsPath)) {
        astPerFile.put(fileWsPath, getASTResult(fileWsPath));
      }
    }
    Set<String> dirWsPaths = fsManager.getDirWsPaths(path);
    for (String dirWsPath : dirWsPaths) {
      getFileASTRecursivly(dirWsPath, astPerFile);
    }
  }

  private JSONObject getASTResult(String fileWsPath)
      throws ConflictException, NotFoundException, ServerException {
    String fileContent = fsManager.readAsString(fileWsPath);
    AstTreeAnalyzer astTreeAnalyzer = new AstTreeAnalyzer(fileContent);
    astTreeAnalyzer.reslove();
    return astTreeAnalyzer.getRecord();
  }
}
