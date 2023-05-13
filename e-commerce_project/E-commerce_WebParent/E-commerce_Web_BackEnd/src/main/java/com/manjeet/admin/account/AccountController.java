package com.manjeet.admin.account;

import com.manjeet.admin.config.customDetailsSerive.CustomUserDetails;
import com.manjeet.admin.user.UserService;
import com.manjeet.admin.util.FileUploadUtil;
import com.manjeet.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserService service;

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal CustomUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = service.getUserByEmail(email);
        model.addAttribute("user",user);
        return "users/account-form";
    }

    @PostMapping("/account/update")
    public String updateAccount(User user,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("photo") MultipartFile multipartFile,
                                @AuthenticationPrincipal CustomUserDetails loggedUser){
        System.out.println(user);
        User savedUser;
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            savedUser = service.updateAccount(user);
            String uploadDir = "user-photos/"+savedUser.getId();
            FileUploadUtil.clean(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else {
            if(user.getPhotos().isEmpty())
                user.setPhotos(null);

            savedUser = service.updateAccount(user);
        }

        loggedUser.setFirstName(savedUser.getFirstName());
        loggedUser.setLastName(savedUser.getLastName());

        redirectAttributes.addFlashAttribute("message","Your account details have been updated successfully.");
        return "redirect:/account";
    }


}
