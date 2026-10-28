package com.taskflow.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public List<Task> findAll(){
        return tasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        return  tasks.stream().filter(t -> t.id().equals(id)).findFirst().map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody CreateTaskRequest request) {
        Task task = new Task(
                idGenerator.getAndIncrement(),
                request.title(),
                request.description(),
                TaskStatus.TODO
        );
        tasks.add(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + task.id())).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody UpdateTaskRequest request){
        for (int i = 0; i < tasks.size(); i++){
            if(tasks.get(i).id().equals(id)){
                Task updated = new Task(id, request.title(), request.description(), request.status());
                tasks.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean removed = tasks.removeIf(t -> t.id().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
