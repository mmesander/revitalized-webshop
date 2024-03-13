package nu.revitalized.revitalizedwebshop.controllers;

// Imports

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SupplementControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create correct supplement")
    void shouldCreateCorrectSupplement() throws Exception {

        String requestJson = """
                {
                       "name" : "Energize Protein Powder",
                       "brand" : "Energize Gear",
                       "description" : "The best in town",
                       "price" : 30.99,
                       "stock" : 100,
                       "contains" : "2500 gram"
                }
                """;

        MvcResult result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/products/supplements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Energize Protein Powder")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", is("Energize Gear")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("The best in town")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(30.99)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stock", is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contains", is("2500 gram")))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String createdId = jsonNode.get("id").asText();

        assertThat(result.getResponse().getHeader("Location"),
                matchesPattern("^.*/products/supplements/" + createdId));
    }
}