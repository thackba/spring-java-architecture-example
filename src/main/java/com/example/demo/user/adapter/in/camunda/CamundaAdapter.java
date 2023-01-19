package com.example.demo.user.adapter.in.camunda;

import com.example.demo.user.ports.in.AddUser;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CamundaAdapter {

    private final AddUser addUser;

    public CamundaAdapter(AddUser addUser) {
        this.addUser = addUser;
    }

    @JobWorker(type = "add-user")
    public Map<String, Object> addUser(final ActivatedJob job) {
        Map<String, Object> input = job.getVariablesAsMap();

        if (input.containsKey("username")) {
            var maybeCommand = AddUser.Command.of(input.get("username").toString());
            if (maybeCommand.isRight()) {
                // TODO: Error handling
                this.addUser.execute(maybeCommand.get());
            }
        }

        return new HashMap<String, Object>();
    }
}
