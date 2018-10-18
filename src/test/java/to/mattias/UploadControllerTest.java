package to.mattias;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = GifitApplication.class)
@TestPropertySource(properties = {
    "spring.http.multipart.location=/someDirectoryThatDontExists"
})
public class UploadControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void testThatTheControllerReturnsAcounterAndAnEmptyListOfGifs() throws Exception {
    this.mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("counter", "showableGifs"))
        .andExpect(model().attribute("counter", is(0)))
        .andExpect(model().attribute("showableGifs", hasSize(0)));
  }

}
