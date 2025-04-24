package edu.pazyuk.projectfortests.model;

/*
  @author xsour
  @project projectfortests
  @class Patient
  @version 1.0.0
  @since 24.04.2025 - 14.16
*/

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Patient extends AuditMetadata {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;


    public Patient(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient p)) return false;
        return id != null && id.equals(p.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
