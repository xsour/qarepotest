package edu.pazyuk.projectfortests.repository;

/*
  @author xsour
  @project projectfortests
  @class PatientRepository
  @version 1.0.0
  @since 24.04.2025 - 14.18
*/

import edu.pazyuk.projectfortests.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
}
