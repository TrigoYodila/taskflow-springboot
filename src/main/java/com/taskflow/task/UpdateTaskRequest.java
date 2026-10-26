package com.taskflow.task;

public record UpdateTaskRequest(String title, String description, TaskStatus status) {
}
