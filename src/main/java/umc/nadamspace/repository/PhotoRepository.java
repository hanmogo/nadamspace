package umc.nadamspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.nadamspace.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
