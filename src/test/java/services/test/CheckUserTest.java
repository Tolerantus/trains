package services.test;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.UserExist;
import com.dto.UserInfo;
import com.entities.Dao;
import com.entities.User;
import com.service.CheckUser;

/*@RunWith(MockitoJUnit4Runner.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/applicationContext.xml")*/
public class CheckUserTest {
	CheckUser checkuser;
	@Mock
	private Dao dao = Mockito.mock(Dao.class);
	
	@Before
	public void init() {
		checkuser = new CheckUser();
		checkuser.setDao(dao);
	}
	
	@Test
	public void test() throws Exception {
		UserInfo info = new UserInfo("login", "password");
		UserExist exist = new UserExist(true, false);
		User user = new User(0,"login", "password", false);
		
		Mockito.when(dao.getUserByName(info.getLogin())).thenReturn(user);
		Assert.assertEquals(exist.isAdmin(), checkuser.check(info).isAdmin());
		Assert.assertEquals(exist.isExist(), checkuser.check(info).isExist());
	}
	
	@Test(expected=NullPointerException.class)
	public void test2() throws Exception{
		checkuser.check(null);
	}
	
	public void test3() throws Exception {
		UserInfo info = new UserInfo("login", "password");
		Mockito.when(dao.getUserByName(info.getLogin())).thenReturn(null);
		Assert.assertTrue(!checkuser.check(info).isExist());
		Assert.assertTrue(!checkuser.check(info).isAdmin());
	}
	
	

}
