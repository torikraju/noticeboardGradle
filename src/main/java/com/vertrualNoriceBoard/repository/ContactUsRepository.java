package com.vertrualNoriceBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vertrualNoriceBoard.model.ContactUs;

public interface ContactUsRepository extends CrudRepository<ContactUs, String> {

	@Query("SELECT count(*) from ContactUs a WHERE a.read= false")
	public int newMailcount();
	
	public List<ContactUs> findByOrderByReadAsc();

}
