package com.vertrualNoriceBoard.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vertrualNoriceBoard.model.Notice;
import com.vertrualNoriceBoard.user.User;

public interface NoticeRepository extends CrudRepository<Notice, String> {

	public List<Notice> findByUser(User user);
	
	public Notice findById(String id);
	
	public List<Notice> findFirst5ByOrderByIdDesc();

}
