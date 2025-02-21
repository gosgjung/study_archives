package io.study.erd_example.emp.entity.base;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ErdBaseTimeEntity {

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}
