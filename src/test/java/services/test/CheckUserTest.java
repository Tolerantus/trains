package services.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dto.UserExist;
import com.dto.UserInfo;
import com.entities.Dao;
import com.entities.User;
import com.service.CheckUser;

@RunWith(MockitoJUnitRunner.class)
public class CheckUserTest {
	CheckUser checkuser;
	@Mock
	private Dao dao = Mockito.mock(Dao.class);
	
	
	@Before
	public void createChecker(){
		checkuser = new CheckUser(dao);
	}
	
	@Test
	public void test() throws Exception {
		UserInfo info = new UserInfo("login", "password");
		UserExist exist = new UserExist(false, false);
		User user = new User(0,"login", "password", false);
		List<User> users = new ArrayList<User>();users.add(user);
		Mockito.when(dao.getAllUsers()).thenReturn(users);
		Assert.assertEquals(exist.isAdmin(), checkuser.check(info).isAdmin());
		Assert.assertEquals(exist.isExist(), checkuser.check(info).isAdmin());
	}
	
	@Test(expected=NullPointerException.class)
	public void test2() throws Exception{
		checkuser.check(null);
	}
	
	public void test3() throws Exception {
		UserInfo info = new UserInfo("login", "password");
		Mockito.when(dao.getAllUsers()).thenReturn(null);
		Mockito.doNothing().when(dao).close();
		Assert.assertTrue(!checkuser.check(info).isExist());
		Assert.assertTrue(!checkuser.check(info).isAdmin());
	}
	
	

}
