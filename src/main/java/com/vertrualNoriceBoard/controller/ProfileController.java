package com.vertrualNoriceBoard.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vertrualNoriceBoard.config.PicturesUploadProperties;
import com.vertrualNoriceBoard.model.Profile;
import com.vertrualNoriceBoard.repository.ProfileRepository;
import com.vertrualNoriceBoard.repository.UserRepository;
import com.vertrualNoriceBoard.user.User;

@Controller
public class ProfileController {

	private UserRepository userRepository;
	private ProfileRepository profileRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Resource picturesDir;// file upload helper class

	@Autowired
	public ProfileController(UserRepository userRepository, ProfileRepository profileRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder, PicturesUploadProperties uploadProperties) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.picturesDir = uploadProperties.getUploadPath();// file Upload
															// helper class
	}

	// showing profile page
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String showPage(Model model, Principal principal) {
		User user = userRepository.findByUserName(principal.getName());
		model.addAttribute("user", user);
		System.out.println(user.getProfile());
		return "profile";
	}

	@RequestMapping(value = "/profile/{userid}", method = RequestMethod.GET)
	public String showprofilePage(Model model, @PathVariable String userid) {
		User user = userRepository.findByUserId(userid);
		model.addAttribute("user", user);
		return "profile";
	}

	// showing update profile page
	@RequestMapping(value = "/updateProfile", method = RequestMethod.GET)
	public String showUpdateProfilePage(Principal principal, Model model) {
		User user = userRepository.findByUserName(principal.getName());
		Profile profile = profileRepository.findOne(user.getUserId());
		if (profile == null) {
			model.addAttribute("profile", new Profile());
		}
		if (profile != null) {
			model.addAttribute("profile", profile);
		}

		return "updateProfile";
	}

	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public String updateProfileAction(@Valid Profile profile, BindingResult result, Principal principal, Model model,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "updateProfile";
		}

		User user = userRepository.findByUserName(principal.getName());
		Profile check = profileRepository.findOne(user.getUserId());

		if (check != null) {
			profile.setUserId(user.getUserId());
			profile.setPhoto(check.getPhoto());
		}

		profile.setUser(user);
		profileRepository.save(profile);

		redirectAttributes.addFlashAttribute("message", "Profile updated.");
		return "redirect:/profile";
	}

	// showing change Password page;
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String showChangePasswrodPage() {
		return "changePassword";
	}

	// change password Action
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePasswrodAciton(@RequestParam Map<String, String> requestParams, Principal principal,
			Model model) {

		String oldPassword = requestParams.get("oldPassword");
		String newPasswrod = requestParams.get("newPassword");
		String confirmPassword = requestParams.get("confirmPassword");

		User user = userRepository.findByUserName(principal.getName());

		if (oldPassword.isEmpty() || newPasswrod.isEmpty() || confirmPassword.isEmpty()) {
			model.addAttribute("message", "Please fill all the fields.");
			return "changePassword";
		}
		if (newPasswrod.length() < 6) {
			model.addAttribute("message", "password is too weak. Password length must be 6-20");
			return "changePassword";
		}

		if (!(bCryptPasswordEncoder.matches(oldPassword, user.getPassword()))) {
			model.addAttribute("message", "Old passwrod didnt match..");
			return "changePassword";
		}

		if (!newPasswrod.equals(confirmPassword)) {
			model.addAttribute("message", "New passwrod didnt match..");
			return "changePassword";
		}

		user.setPassword(bCryptPasswordEncoder.encode(newPasswrod));
		userRepository.save(user);

		model.addAttribute("suecess", "Passwrod changed..");

		return "changePassword";
	}

	// ****************************Start of file Upload*********************

	// showing changePhot page
	@RequestMapping(value = "/changePhoto", method = RequestMethod.GET)
	public String showChangePhotoPage() {
		return "/changePhoto";
	}

	// file upload Action
	@RequestMapping(value = "/changePhoto", method = RequestMethod.POST)
	public String fileUploadAction(MultipartFile file, Principal principal, Model model) throws IOException {
		User user = userRepository.findByUserName(principal.getName());

		if (file.getSize() > 500000) {
			model.addAttribute("message", "Maximum file size is 500KB");
			return "/changePhoto";
		}

		if (file.isEmpty() || !isImage(file)) {
			model.addAttribute("message", "Incorrect file.Please upload a picture");
			return "/changePhoto";
		}

		Resource pic = copyFiletoPictures(file);

		if (user.getProfile().getPhoto() != null) {
			// deleting previous file
			deleteFile(user.getProfile().getPhoto());
		}

		user.getProfile().setPhoto(pic.getFilename());

		userRepository.save(user);

		model.addAttribute("suecess", "File Uploaded...");

		return "/changePhoto";
	}

	private static String getFileExtension(String name) {
		return name.substring(name.lastIndexOf("."));
	}

	private boolean isImage(MultipartFile file) {
		return file.getContentType().startsWith("image");
	}

	private Resource copyFiletoPictures(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		File tempFile = File.createTempFile("pic", getFileExtension(fileName), picturesDir.getFile());
		try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
			IOUtils.copy(in, out);
		}
		return new FileSystemResource(tempFile);
	}

	// IoException handler
	@ExceptionHandler(IOException.class)
	public ModelAndView handleIOException(IOException exception) {
		ModelAndView modelAndView = new ModelAndView("/changePhoto");
		modelAndView.addObject("message", exception.getMessage());
		return modelAndView;
	}

	// for deleting file
	public void deleteFile(String filename) throws IOException {
		String fileDetails = picturesDir.getFile() + "/" + filename;
		System.out.println(fileDetails);

		File file = new File(fileDetails);
		file.delete();
	}

	// ****************************End of file Upload*********************

}
