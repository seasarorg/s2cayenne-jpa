package org.seasar.cayenne.jpa;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity(name = "Dept")
public class Department {

	@Id
	private int deptno;

	private String dname;

	private String loc;

	@SuppressWarnings("unused")
	@Version
	private int versionNo;

	private boolean active;

	@OneToMany(mappedBy = "department")
	private Set<Employee> employees;

	public Department() {
	}

	public int getDeptno() {
		return this.deptno;
	}

	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}

	public java.lang.String getDname() {
		return this.dname;
	}

	public void setDname(java.lang.String dname) {
		this.dname = dname;
	}

	public java.lang.String getLoc() {
		return this.loc;
	}

	public void setLoc(java.lang.String loc) {
		this.loc = loc;
	}

	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Department))
			return false;
		Department castOther = (Department) other;
		return this.getDeptno() == castOther.getDeptno();
	}

	@Override
	public int hashCode() {
		return this.getDeptno();
	}

}
