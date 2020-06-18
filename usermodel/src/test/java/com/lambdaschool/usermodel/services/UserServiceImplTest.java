package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        List<User> userList = userService.findAll();
//        for (User user:userList) {
//            System.out.println(user.getUserid()+ " "+ user.getUsername());
//        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void afindUserById() {
        assertEquals("admin", userService.findUserById(4).getUsername());
    }

    @Test
    public void bfindByNameContaining() {
        assertEquals(1, userService.findByNameContaining("admin").size());
    }

    @Test
    public void cfindAll() {
        assertEquals(30, userService.findAll().size());
    }

    @Test
    public void fdelete() {
        userService.delete(7);
        assertEquals(29, userService.findAll().size());
//        assertEquals("cinnamon", userService.findUserById(7));
    }

    @Test
    public void dfindByName() {
        assertEquals(4, userService.findByName("admin").getUserid());
    }

    @Test
    public void hsave() {

        // new role
        Role newRole = new Role();
        newRole.setName("testRole");
        newRole = roleService.save(newRole);

        ArrayList<UserRoles> users = new ArrayList<>();
        // user roles from seed data
//        users.add(new UserRoles(new User(),
//                roleService.findByName("user")));
        users.add(new UserRoles(new User(), newRole));

//        User user = new User("test user",
//                "testpassword",
//                "test@lambdaschool.local",
//                users);
        User user = new User();
        user.setUsername("testtestuser");
        user.setPassword("testpassword");
        user.setPrimaryemail("test@lambdaschool.com");
        user.setRoles(users);
        user.getUseremails()
                .add(new Useremail(user,
                        "test@email.com"));
        user.setUserid(0);
        User newUser = userService.save(user);

        assertNotNull(newUser);
        User testUser = userService.findUserById(newUser.getUserid());
        assertEquals(newUser.getUsername(), testUser.getUsername());
    }

    @Test
    public void gupdate() {
        User oldUser = userService.findByName("admin");
        User user = new User();
        user.setUserid(oldUser.getUserid());
        user.setUsername("test");
        user.setPassword("testpassword");
        user.setPrimaryemail("test@lambdaschool.local");
        User updateUser = userService.update(user, oldUser.getUserid());

        assertNotNull(updateUser);
        User testUser = userService.findUserById(updateUser.getUserid());
        assertEquals(updateUser.getUsername(), testUser.getUsername());
    }

    @Test
    public void egetCountUserEmails() {
        assertEquals(28, userService.getCountUserEmails().size());
    }

    @Test
    public void deleteUserRole() {

    }

    @Test
    public void addUserRole() {
    }
}