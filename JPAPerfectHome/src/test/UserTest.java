package test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.User;

public class UserTest {

	private EntityManagerFactory emf;
    private EntityManager em;
	
	@Before
	public void setUp() throws Exception {
		emf = Persistence.
	            createEntityManagerFactory("JPAPerfectHome");
	    em = emf.createEntityManager();
	}

	@Test
	public void test() {
		User user = em.find(User.class, 1);
		
		assertEquals("testUser", user.getUsername());
		assertEquals("John", user.getFirstName());
		
		assertEquals(3, user.getHomeUsers().size());
	}
	
	@After
	public void tearDown() throws Exception {
		em.close();
        emf.close();
	}

}
