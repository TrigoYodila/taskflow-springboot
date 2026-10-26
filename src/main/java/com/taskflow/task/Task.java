package com.taskflow.task;

public record Task (Long id, String title, String description, TaskStatus status) { }
