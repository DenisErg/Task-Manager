package sk.edenis.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sk.edenis.taskmanager.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
	Optional<User> findByEmail(String email);
	
	List<User> findAll();
}
