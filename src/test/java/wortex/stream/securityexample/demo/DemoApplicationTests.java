package wortex.stream.securityexample.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {


    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void unauthorizedCallerCanAccessPublicEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/public").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void unauthorizedCallerCantAccessTheEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/person/find/Erica").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @WithMockUser(username = "testUser", roles = { })
    @Test
    public void userWithoutRolesCantAccessPersonEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/person/find/Erica").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @WithMockUser(username = "testUser", roles = { })
    @Test
    public void userWithoutRolesCanAccessPublicEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/public").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "testAdminUser", roles = { "ADMIN" })
    @Test
    public void adminWithProperRoleCanAccessPersonEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/person/find/Erica").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser(username = "testAdminUser", roles = { "ADMIN" })
    @Test
    public void adminCanAccessPublicEndpoint() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/public").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
