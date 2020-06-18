package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {

        userList = new ArrayList<>();

        Role r1 = new Role();
        r1.setName("Role1");
        r1.setRoleid(1);
        Role r2 = new Role();
        r2.setName("Role2");
        r2.setRoleid(2);
        Role r3 = new Role();
        r3.setName("Role3");
        r3.setRoleid(3);

        ArrayList<UserRoles> roles1 = new ArrayList<>();
        roles1.add(new UserRoles(new User(), r1));

        User u1 = new User();
        u1.setUserid(1);
        u1.setUsername("user1");
        u1.setPassword("testpassword");
        u1.setPrimaryemail("test@lambdaschool.local");
        u1.setRoles(roles1);
        userList.add(u1);

        ArrayList<UserRoles> roles2 = new ArrayList<>();
        roles2.add(new UserRoles(new User(), r2));

        User u2 = new User();
        u2.setUserid(2);
        u2.setUsername("user2");
        u2.setPassword("testpassword");
        u2.setPrimaryemail("test@lambdaschool.local");
        u2.setRoles(roles2);
        userList.add(u2);

        ArrayList<UserRoles> roles3 = new ArrayList<>();
        roles3.add(new UserRoles(new User(), r3));

        User u3 = new User();
        u3.setUserid(3);
        u3.setUsername("user3");
        u3.setPassword("testpassword");
        u3.setPrimaryemail("test@lambdaschool.local");
        u3.setRoles(roles3);
        userList.add(u3);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String testResult = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList);

        System.out.println("Expect: " + expectedResult);
        System.out.println("Actual: " + testResult);

        assertEquals("Rest API Returns List", expectedResult, testResult);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/1";
        Mockito.when(userService.findUserById(1)).thenReturn(userList.get(1));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String testResult = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList.get(1));

        System.out.println("Expect: " + expectedResult);
        System.out.println("Actual: " + testResult);

        assertEquals("Rest API Returns List", expectedResult, testResult);
    }

    @Test
    public void getUserByName() throws Exception {
        String apiUrl = "/users/user/name/user1";
        Mockito.when(userService.findByName("user1")).thenReturn(userList.get(1));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String testResult = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(userList.get(1));

        System.out.println("Expect: " + expectedResult);
        System.out.println("Actual: " + testResult);

        assertEquals("Rest API Returns List", expectedResult, testResult);
    }

    @Test
    public void getUserLikeName() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(userList.get(1));
        String apiUrl = "/users/user/name/like/1";
        Mockito.when(userService.findByNameContaining("1")).thenReturn(users);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String testResult = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(users);

        System.out.println("Expect: " + expectedResult);
        System.out.println("Actual: " + testResult);

        assertEquals("Rest API Returns List", expectedResult, testResult);
    }

    @Test
    public void addNewUser() throws Exception {

        String apiUrl = "/users/user";

        Role r1 = new Role();

        r1.setName("Role1");
        r1.setRoleid(1);
        ArrayList<UserRoles> roles = new ArrayList<>();
        roles.add(new UserRoles(new User(), r1));

        User u = new User();
        u.setUserid(4);
        u.setUsername("user4");
        u.setPassword("testpassword");
        u.setPrimaryemail("test@lambdaschool.local");
        u.setRoles(roles);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        Mockito.when(userService.save(any(User.class))).thenReturn(u);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        ResultActions mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        String testResult = mvcResult.toString();
//
//        System.out.println("Expect: " + expectedResult);
        System.out.println("Actual: " + testResult);
//
//        assertEquals("Rest API Returns List", expectedResult, testResult);
    }

    @Test
    public void updateFullUser() throws Exception {
        String apiUrl = "/users/user/1";

        Role r1 = new Role();

        r1.setName("Role1");
        r1.setRoleid(1);
        ArrayList<UserRoles> roles = new ArrayList<>();
        roles.add(new UserRoles(new User(), r1));

        User u = new User();
        u.setUserid(1);
        u.setUsername("userupdate");
        u.setPassword("testpassword");
        u.setPrimaryemail("test@lambdaschool.local");
        u.setRoles(roles);

        Mockito.when(userService.save(u)).thenReturn(u);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(apiUrl, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        ResultActions mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        String testResult = mvcResult.toString();
        System.out.println("Actual: " + testResult);
    }

    @Test
    public void updateUser() throws Exception {
        String apiUrl = "/users/user/1";

        List<UserRoles> roles = new ArrayList<>();
        User u = new User();
        u.setUsername("userupdate");
        u.setPassword("");
        u.setPrimaryemail("");

        Mockito.when(userService.update(u, 1L)).thenReturn(u);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(apiUrl, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);

        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
//        String testResult = mvcResult.toString();
//        System.out.println("Actual: " + testResult);
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/1";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getNumUserEmails() {
    }

    @Test
    public void deleteUserRoleByIds() throws Exception {
        String apiUrl = "/users/user/1/role/1";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postUserRoleByIds() {
    }
}