package com.vertrualNoriceBoard.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vertrualNoriceBoard.model.ContactUs;
import com.vertrualNoriceBoard.repository.ContactUsRepository;

@Controller
public class ContactUsController {

	private ContactUsRepository contactUsRepository;

	@Autowired
	public ContactUsController(ContactUsRepository contactUsRepository) {
		this.contactUsRepository = contactUsRepository;
	}

	@RequestMapping(value = "/contactUs", method = RequestMethod.GET)
	public String showPage(Model model) {
		model.addAttribute("contactUs", new ContactUs());
		return "contactUs";
	}

	@RequestMapping(value = "/contactUs", method = RequestMethod.POST)
	public String action(@Valid ContactUs contactUs, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "/contactUs";
		}

		contactUs.setRead(false);
		contactUs.setDate(LocalDate.now());

		contactUsRepository.save(contactUs);
		redirectAttributes.addFlashAttribute("message", "Message sent...");
		return "redirect:/contactUs";
	}
}
