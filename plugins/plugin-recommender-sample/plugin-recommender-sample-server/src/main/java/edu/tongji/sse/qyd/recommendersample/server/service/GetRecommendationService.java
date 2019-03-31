package edu.tongji.sse.qyd.recommendersample.server.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import org.json.JSONObject;

@Path("sample2Recommendation/")
public class GetRecommendationService {

  private static String EXAMPLE_REQUEST = "example_request";

  private static String PRE_TEST = "pre_test";

  @GET
  @Path("{code_context}")
  @Produces(APPLICATION_JSON)
  public Map<String, String> getRecommendition(@PathParam("code_context") String request) {
    JSONObject jojo = new JSONObject();
    jojo.put("ora", "muda");
    Map<String, String> result = new HashMap<>();
    for (int i = 0; i < 3; i++) {
      result.put("result" + i, request);
    }
    result.put("jojo", jojo.toString());
    return result;
  }
}
