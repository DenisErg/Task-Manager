package sk.edenis.taskmanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskDTO {

	@NotBlank(message = "Description is missing!")
	private String description;

	@NotNull(message = "Deadline is missing!")
	private LocalDateTime deadline;

	private int priority = 5;

	private LocalDateTime startTime;

	private String id;

	private Boolean finished;

	public String getDescription() {
		return description;
	}

	public int getPriority() {
		return priority;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

}
