package com.example.demo.user.adapter.in.web;

import am.ik.yavi.core.ConstraintViolation;
import com.example.demo.user.adapter.in.web.dto.AddUserRequest;
import com.example.demo.user.adapter.in.web.dto.AddUserResponse;
import com.example.demo.user.adapter.in.web.dto.GetUserResponse;
import com.example.demo.user.ports.in.AddUser;
import com.example.demo.user.ports.in.GetUser;
import io.vavr.control.Either;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

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
        return getUser.execute(new GetUser.Query(id))
                .map(value -> value.map(opt -> ResponseEntity.ok(GetUserResponse.fromDomain(opt)))
                        .getOrElse(() -> ResponseEntity.notFound().build()))
                .recover(ex -> ResponseEntity.internalServerError().build())
                .get();
    }

    @PostMapping("/")
    public ResponseEntity<AddUserResponse> addUserRoute(@RequestBody AddUserRequest request) {
        Either<List<ConstraintViolation>, AddUser.Command> maybeCommand = AddUser.Command.of(request.name());
        if (maybeCommand.isLeft()) {
            String message = maybeCommand.getLeft().stream().map(ConstraintViolation::message).collect(Collectors.joining("|"));
            return ResponseEntity.unprocessableEntity().body(new AddUserResponse(null, message));
        }
        return addUser.execute(maybeCommand.get())
                .map(res -> Match(res.status()).<ResponseEntity<AddUserResponse>>of(
                        Case($(AddUser.Status.OK), ResponseEntity.ok(AddUserResponse.fromDomain(res))),
                        Case($(AddUser.Status.DUPLICATE_NAME), ResponseEntity.status(409).build())
                ))
                .recover(ex -> ResponseEntity.internalServerError().build())
                .get();
    }
}
