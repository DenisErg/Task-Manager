package sk.edenis.taskmanager.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@Column(updatable = false, nullable = false)
	private UUID taskId;

	private String description;
	private int priority = 5;
	private LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
	private LocalDateTime deadline;
	private Boolean isFinished = false;
	private boolean isDeleted = false;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	public UUID getTaskId() {
		return taskId;
	}

	public void setTaskId(UUID taskId) {
		this.taskId = taskId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String taskName) {
		this.description = taskName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime createdAt) {
		this.startTime = createdAt;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}

	public Boolean isFinished() {
		return isFinished;
	}

	public void setFinished(Boolean isFinished) {
		 this.isFinished = isFinished != null ? isFinished : false;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
