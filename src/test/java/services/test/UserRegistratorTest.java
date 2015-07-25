package services.test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.NewUserInfo;
import com.dto.UserExist;
import com.entities.Dao;
import com.service.UserRegistrator;

public class UserRegistratorTest {
UserRegistrator registrator;
@Mock
Dao dao = Mockito.mock(Dao.class);
@Before
public void init(){
registrator = new UserRegistrator(dao);
}

	@Test
	public void test() throws Exception {
		NewUserInfo info = new NewUserInfo("user", "password");
		Mockito.when(dao.getUserByName("login")).thenReturn(null);
		Mockito.when(dao.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(null);
		Mockito.doNothing().when(dao).close();
		UserExist exist = new UserExist(false,false);
		Assert.assertEquals(exist.isExist(), registrator.register(info).isExist());
		Assert.assertEquals(exist.isAdmin(), registrator.register(info).isAdmin());

	}

	@Test(expected=NullPointerException.class)
	public void test2() throws Exception {
		
		registrator.register(null);
	}
}
