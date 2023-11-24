package ar.edu.utn.frba.dds.controller.response;

import com.google.protobuf.Api;
import java.util.List;
import java.util.Map;

public class ApiResponse {
  private boolean success;
  private String message;
  private List<Map<String, Object>> content;

  public ApiResponse (boolean success, String message, List<Map<String, Object>> content) {
    this.success = success;
    this.message = message;
    this.content = content;
  }
}
