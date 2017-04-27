package me.j360.dubbo.client;

import me.j360.dubbo.api.model.param.user.UserDTO;
import me.j360.dubbo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.concurrent.Callable;

@Controller
@RequestMapping(value = "/ping", method = RequestMethod.GET)
public class PingController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/sync")
    public ResponseEntity<Void> sync() throws IOException {
        userService.getUserInfo(new UserDTO());

        userService.getUserInfo(new UserDTO());

        userService.getUserInfo(new UserDTO());

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
}
