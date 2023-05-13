package com.manjeet.admin;

import com.manjeet.admin.category.CategoryService;
import com.manjeet.admin.customer.CustomerService;
import com.manjeet.admin.product.ProductService;
import com.manjeet.admin.setting.GeneralSettingBag;
import com.manjeet.admin.setting.SettingService;
import com.manjeet.admin.setting.country.CountryRepository;
import com.manjeet.admin.setting.state.StateRepository;
import com.manjeet.admin.user.UserService;
import com.manjeet.common.entity.setting.Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final UserService userService;
	private final CustomerService customerService;
	private final CategoryService categoryService;
	private final ProductService productService;
	private final SettingService settingService;
	private final CountryRepository countryRepo;
	private final StateRepository stateRepo;

	@GetMapping("/")
	public String homepage(Model model) {
		List<Setting> settingList= settingService.getMailServerSettings();
		GeneralSettingBag settingBag = settingService.getGeneralSettings();
		settingList.addAll(settingBag.list());
		for(Setting setting:settingList){
			model.addAttribute(setting.getKey(),setting.getValue());
		}
		model.addAttribute("totalCountry",countryRepo.count());
		model.addAttribute("totalState",stateRepo.count());
		model.addAttribute("totalUsers",userService.countAll());
		model.addAttribute("totalEnabledUsers",userService.countEnabled());
		model.addAttribute("totalCustomers",customerService.countAll());
		model.addAttribute("totalEnabledCustomers",customerService.countEnabled());
		model.addAttribute("totalCategories",categoryService.countAll());
		model.addAttribute("totalEnabledCategories",categoryService.countEnabled());
		model.addAttribute("totalProducts",productService.countAll());
		model.addAttribute("totalEnabledProducts",productService.countEnabled());

		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
