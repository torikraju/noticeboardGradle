package com.vertrualNoriceBoard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vertrualNoriceBoard.model.Notice;
import com.vertrualNoriceBoard.repository.NoticeRepository;

@Controller
public class HomeController {

	private NoticeRepository noticeRepository;

	@Autowired
	public HomeController(NoticeRepository noticeRepository) {
		this.noticeRepository = noticeRepository;
	}

	@RequestMapping(value = "/")
	public String showHome(Model model) {
		List<Notice> notices = noticeRepository.findFirst5ByOrderByIdDesc();
		model.addAttribute("notices", notices);
		return "home";
	}
	

	
}
