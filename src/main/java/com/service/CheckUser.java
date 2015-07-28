package com.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.UserExist;
import com.dto.UserInfo;
import com.entities.Dao;
import com.entities.User;
@Service("checkUser")
public class CheckUser {
	@Autowired
	private Dao dao;

	@Transactional
	public UserExist check(UserInfo dto){
			String login = dto.getLogin();
			boolean isExist = false;
			boolean isAdmin = false;
			User user = dao.getUserByName(login);
			if (user!=null){
				isExist = true;
				isAdmin = user.getAccount_type();
			}
			return new UserExist(isExist, isAdmin);
	}
}
