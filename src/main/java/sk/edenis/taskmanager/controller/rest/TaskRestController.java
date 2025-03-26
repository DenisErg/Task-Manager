package sk.edenis.taskmanager.controller.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sk.edenis.taskmanager.dto.ApiResponse;
import sk.edenis.taskmanager.dto.ErrorResponse;
import sk.edenis.taskmanager.dto.TaskDTO;
import sk.edenis.taskmanager.service.TaskService;
import sk.edenis.taskmanager.util.ErrorUtil;


@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

	private final TaskService taskService;
	
	private static final Logger logger = LoggerFactory.getLogger(TaskRestController.class);

	public TaskRestController(TaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping
	public ResponseEntity<?> addTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponseDTO = ErrorUtil.formatErrorMessage("Invalid or missing fields", bindingResult);
			return ResponseEntity.badRequest().body(errorResponseDTO);
		}
		TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
		logger.info("Task added, task id: " + createdTaskDTO.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Task created."));		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTask(@PathVariable UUID id) { 
		return ResponseEntity.ok(taskService.getTask(id));	
	}

	@GetMapping
	public ResponseEntity<?> getAllTasks() {
		return ResponseEntity.ok().body(taskService.getAllTasks());	
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTask(@PathVariable UUID id, @RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorUtil.formatErrorMessage("Invalid or missing fields", bindingResult));
		} 
	
		taskService.updateTask(id, taskDTO);
		logger.info("Task updated, task id: " + taskDTO.getId());
		return ResponseEntity.ok().body(new ApiResponse("Task updated."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
			taskService.deleteTask(id);
			logger.info("Task deleted, task id: " + id);
			return ResponseEntity.ok(new ApiResponse("Task has been deleted."));
	}
}
