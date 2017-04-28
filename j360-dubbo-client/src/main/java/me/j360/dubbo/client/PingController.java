package me.j360.dubbo.client;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.service.UserService;
import me.j360.dubbo.modules.util.mapper.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
@Slf4j
@Controller
@RequestMapping(value = "/ping", method = RequestMethod.GET)
public class PingController {

    JsonMapper jsonMapper = JsonMapper.defaultMapper();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Void> exception(Exception e, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        log.error("程序猿正在被吊打中... {}",getExceptionHeadersMessage(request), e);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.addHeader("status","1");

        String message = e.getMessage();
        if (message == null) message = e.getClass().getSimpleName();
        httpServletResponse.addHeader("message",message);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/sync")
    public ResponseEntity<Void> sync(HttpServletRequest request) throws IOException {
        userService.getUserInfo(new UserDTO());

        userService.getUserInfo(new UserDTO());

        userService.getUserInfo(new UserDTO());

        if (Objects.equal("1",request.getParameter("error"))) {
            throw new ControllerException("参数异常");
        }

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/async")
    public Callable<ResponseEntity<Void>> async() throws IOException {
        return new Callable<ResponseEntity<Void>>() {
            public ResponseEntity<Void> call() throws Exception {
                return ResponseEntity.noContent().build();
            }
        };
    }


    private String getExceptionHeadersMessage(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<String, String>();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }

        //path
        String path = request.getRequestURI();
        headers.put("path",path);

        //method
        String method = request.getMethod();
        headers.put("method",method);

        return jsonMapper.toJson(headers);
    }
}
