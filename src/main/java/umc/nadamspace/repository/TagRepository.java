package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
