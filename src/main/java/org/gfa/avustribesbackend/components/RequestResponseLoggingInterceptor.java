package org.gfa.avustribesbackend.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
    Map<String, String[]> requestParametersMap = request.getParameterMap();
    String mapAsString = "";
    if (!requestParametersMap.isEmpty()) {
      mapAsString = requestParametersMap.keySet().stream()
          .map(key -> key + " = " + Arrays.toString(requestParametersMap.get(key)))
          .collect(Collectors.joining(", ", "", ""));
    }

    try {
      LOGGER.info("<=======================================================Request start=======================================================>");
      LOGGER.info("Method: {}", request.getMethod());
      LOGGER.info("Endpoint path(URI): {}", request.getRequestURI());
      if (!mapAsString.isEmpty()) {
        LOGGER.info("Request parameters: {}", mapAsString);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request,
                         HttpServletResponse response,
                         Object handler,
                         ModelAndView modelAndView) throws Exception {
    try {
      LOGGER.info("Status code: {}", response.getStatus());
      LOGGER.info("<=======================================================Response end========================================================>");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
