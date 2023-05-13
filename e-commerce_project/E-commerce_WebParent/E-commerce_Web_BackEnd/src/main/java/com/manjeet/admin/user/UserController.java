package com.manjeet.admin.user;

import com.manjeet.admin.paging.PagingAndSortingHelper;
import com.manjeet.admin.paging.PagingAndSortingParam;
import com.manjeet.admin.util.FileUploadUtil;
import com.manjeet.common.entity.*;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listUsersFirstPage(Model model) {
		return defaultRedirectURL;
	}
	@GetMapping("/users/page/{pageNum}")
	public String listUsersByPage(
			@PagingAndSortingParam(listName = "usersList", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable("pageNum")int pageNum) {
		service.listByPage(pageNum,helper);
		return "users/users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> rolesList= service.listRoles();
		User newUser = new User();
		newUser.setEnabled(true);
		model.addAttribute("user", newUser);
		model.addAttribute("rolesList", rolesList);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user-form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user,RedirectAttributes redirectAttributes,@RequestParam("photo") MultipartFile multipartFile){
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
			User savedUser = service.save(user);
			String uploadDir = "user-photos/"+savedUser.getId();
            FileUploadUtil.clean(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if(user.getPhotos().isEmpty())
				user.setPhotos(null);

			service.save(user);
		}
		redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
		return getRedirectUrlOfAffectedUser(user);
	}

	private static String getRedirectUrlOfAffectedUser(User user) {
		String firstPartEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name="id") Integer id,Model model,RedirectAttributes redirectAttributes) {
		try {
			User user = service.getUserById(id);
			model.addAttribute("user", user);
			List<Role> rolesList= service.listRoles();
			model.addAttribute("rolesList", rolesList);
			model.addAttribute("pageTitle", "Edit User ( ID : "+id+" )");
			return "users/user-form";
		}catch(UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name="id") Integer id,RedirectAttributes redirectAttributes) {
		try {
			service.deleteById(id);
			redirectAttributes.addFlashAttribute("message","The user with ID : "+id+" has been deleted successfully.");
			String uploadDir = "user-photos/"+id;
			FileUploadUtil.clean(uploadDir);
			return "redirect:/users";
		}catch(UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable(name="id") Integer id,@PathVariable(name="status")boolean enabled,RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled.":"disabled.";
		String message = "User with ID : "+id+" has been "+status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}


}
