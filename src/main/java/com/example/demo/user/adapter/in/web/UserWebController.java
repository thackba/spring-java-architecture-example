package com.example.demo.user.adapter.in.web;

import com.example.demo.user.adapter.in.web.dto.AddUserRequest;
import com.example.demo.user.adapter.in.web.dto.AddUserResponse;
import com.example.demo.user.adapter.in.web.dto.GetUserResponse;
import com.example.demo.user.ports.in.AddUser;
import com.example.demo.user.ports.in.GetUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserWebController {

    final private AddUser addUser;
    final private GetUser getUser;

    public UserWebController(AddUser addUser, GetUser getUser) {
        this.addUser = addUser;
        this.getUser = getUser;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("id") String id) {
        return getUser.execute(id)
                .map(value -> ResponseEntity.ok(GetUserResponse.fromDomain(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<AddUserResponse> addUserRoute(@RequestBody AddUserRequest request) {
        var result = addUser.execute(request.toCommand());
        switch (result.status()) {
            case OK -> {
                return ResponseEntity.ok(AddUserResponse.fromDomain(result));
            }
            case INVALID_COMMAND -> {
                return ResponseEntity.unprocessableEntity().build();
            }
            case DUPLICATE_NAME -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.internalServerError().build();
    }
}
