package services.test;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dto.NewTrainInfo;
import com.entities.Dao;
import com.service.TrainCreator;

public class TrainCreatorTest {

	TrainCreator creator;
	@Mock
	private Dao dao = Mockito.mock(Dao.class);
	@Before
	public void createChecker(){
		creator = new TrainCreator(dao);
	}
	
	@Test
	public void createTest() {
		NewTrainInfo info = new NewTrainInfo("100");
		Mockito.doNothing().when(dao).close();
		creator.create(info);
		
	}

}
