package com.manjeet.admin.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.manjeet.admin.paging.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manjeet.admin.role.RoleRepository;
import com.manjeet.common.entity.Role;
import com.manjeet.common.entity.User;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	public static final int USERS_PER_PAGE = 5;

	private final UserRepository  userRepo;

	private final RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public List<User> getAllUsers(){
		return (List<User>)userRepo.findAll();
	}

	public List<Role> listRoles(){
		return (List<Role>)roleRepo.findAll();
	}

	public User save(User user) {
		boolean isUpdating = (user.getId() != null);

		if(isUpdating) {
			User existingUser = userRepo.findById(user.getId()).get();
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user);
			}
		}else {
			encodePassword(user);
		}
		return userRepo.save(user) ;
	}

	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();

		if( ! userInForm.getPassword().isEmpty()){
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}

		if(!userInForm.getPhotos().isEmpty())
			userInDB.setPhotos(userInForm.getPhotos());

		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());

		return userRepo.save(userInDB) ;
	}

	public boolean isEmailUnique(Integer id,String email) {
		User userByEmail = userRepo.getUserByEmail(email);

		boolean creatingNewUser = (id==null);

		if(creatingNewUser) {
			if(userByEmail != null)return false;
		}else {
			if(userByEmail != null)
				return id == userByEmail.getId();
		}

		return true;
	}

	public User getUserByEmail(String email){
		User user = userRepo.getUserByEmail(email);

		return user;
	}

	public User getUserById(Integer id) throws UserNotFoundException{
		Optional<User> optUser = userRepo.findById(id);
		try {
			return optUser.get();
		}catch(NoSuchElementException e) {
			throw new UserNotFoundException("No Such User Present With ID : "+id);
		}
	}

	public void deleteById(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if(countById == null | countById==0) {
			throw new UserNotFoundException("No Such User Present With ID : "+id);
		}

		userRepo.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id,boolean status) {
		userRepo.updateEnableStatus(id, status);
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		helper.listEntities(pageNum,USERS_PER_PAGE,userRepo);
	}

	public Long countAll(){
		return userRepo.count();
	}

	public Long countEnabled(){
		return userRepo.countEnabled();
	}

}
