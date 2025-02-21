package io.study.erd_example.emp.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Getter @Setter
@ToString(exclude = "employees", of = {"deptNo", "deptName"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEPARTMENT")
public class Department {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DEPT_NO")
	private Long deptNo;

	private String deptName;

	@OneToMany(mappedBy = "dept")
	private List<Employee> employees = new ArrayList<>();

//	public Department(){}

	public Department(String deptName){
		this.deptName = deptName;
	}
}
